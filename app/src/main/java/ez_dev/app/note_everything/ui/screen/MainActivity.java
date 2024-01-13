package ez_dev.app.note_everything.ui.screen;

import static ez_dev.app.note_everything.ui.screen.NoteActivity.RESULT_CODE_DELETE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ez_dev.app.note_everything.R;
import ez_dev.app.note_everything.data.local.NoteEntity;
import ez_dev.app.note_everything.databinding.ActivityMainBinding;
import ez_dev.app.note_everything.ui.NoteRecyclerAdapter;
import ez_dev.app.note_everything.ui.state.MainViewModel;
import ez_dev.app.note_everything.util.OnClickListener;

public class MainActivity extends AppCompatActivity {
    public static final String DATA_CURRENT = "NOTE_CURRENT";
    private ActivityMainBinding binding;

    private MainViewModel viewModel;
    private NoteRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  binding library
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        binding.setLifecycleOwner(this);


        //  ui
        binding.revNote.setHasFixedSize(true);
        adapter = new NoteRecyclerAdapter();
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public <E> void setOnClickListener(E element, int position) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(DATA_CURRENT, (NoteEntity) element);
                updatingNoteActivityResultLauncher.launch(intent);
            }

            @Override
            public <E> boolean setOnLongClickListener(E element, int position) {
                NoteEntity note = (NoteEntity) element;
                note.isChecked = !note.isChecked;
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        binding.revNote.setAdapter(adapter);


        //  view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getAllNotes().observe(this, notes -> adapter.setData(notes));
        viewModel.title.observe(this, title -> {
            viewModel.findByTitle(title).observe(this, notes -> adapter.setData(notes));
            binding.searchbar.setText(title);
        });


        //  handle user event
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.setTitle("");
            binding.swipeRefreshLayout.setRefreshing(false);
        });
        binding.btnAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            addingNoteActivityResultLauncher.launch(intent);
        });
        binding.searchbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_chooseAll) {
                boolean hasCheckedItem = adapter.getCheckedData().size() > 0;
                toggleSelectAllItem(hasCheckedItem);
            } else if (item.getItemId() == R.id.menu_delete) {
                adapter.getData().forEach(it -> {
                    if (it.isChecked) {
                        viewModel.delete(it);
                    }
                });
            } else {
                return false;
            }

            return true;
        });
        binding.search.getEditText().setOnEditorActionListener((view, actionId, event) -> {
            String title = binding.search.getText().toString().trim();
            viewModel.setTitle(title);
            binding.search.hide();
            return true;
        });


        //  onBackPressed
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.search.isShown()) {
                    binding.search.hide();
                }
            }
        });

    }

    ActivityResultLauncher<Intent> addingNoteActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();
            Intent data = result.getData();

            if (data != null) {
                NoteEntity resultData = (NoteEntity) data.getSerializableExtra(NoteActivity.DATA_RESULT);
                if (resultCode == RESULT_OK) {
                    viewModel.insert(resultData);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> updatingNoteActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();
            Intent data = result.getData();

            if (data != null) {
                NoteEntity resultData = (NoteEntity) data.getSerializableExtra(NoteActivity.DATA_RESULT);
                switch (resultCode) {
                    case RESULT_OK:
                        viewModel.update(resultData);
                        break;
                    case RESULT_CODE_DELETE:
                        viewModel.delete(resultData);
                        break;
                    default:
                }
            }
        }
    });


    private void toggleSelectAllItem(boolean reverseCheck) {
        adapter.getData().forEach(it -> it.isChecked = !reverseCheck);
        adapter.notifyDataSetChanged();
    }

}