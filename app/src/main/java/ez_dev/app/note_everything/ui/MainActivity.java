package ez_dev.app.note_everything.ui;

import static ez_dev.app.note_everything.ui.NoteActivity.RESULT_CAN_BE_DELETE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import ez_dev.app.note_everything.R;
import ez_dev.app.note_everything.data.local.Note;
import ez_dev.app.note_everything.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_NOTE_ACTIVITY_REQUEST_CODE = 1;
    private static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final String DATA_CURRENT = "NOTE_CURRENT";

    private NoteViewModel viewModel;
    private NoteRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  binding library
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        //  request code add note
        binding.btnAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_ACTIVITY_REQUEST_CODE);
        });

        //  UI data
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.revNote.setLayoutManager(layoutManager);
        binding.revNote.setHasFixedSize(true);
        adapter = new NoteRecyclerAdapter();
        //  UI onclick
        adapter.setOnClickListener(new OnClickListener() {
            //  request code update note
            @Override
            public <E> void setOnClickListener(E element, int position) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(DATA_CURRENT, (Note) element);
                startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public <E> boolean setOnLongClickListener(E element, int position) {
                Note note = (Note) element;
                note.isChecked = !note.isChecked;
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //  view model: connect data to ui
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        viewModel.getAllNotes().observe(this, notes -> {
            adapter.setData(notes);
            binding.revNote.setAdapter(adapter);
        });


        //  search bar + search view
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
        binding.search.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            String title = binding.search.getText().toString().trim();
            binding.searchbar.setText(title);
            viewModel.findByTitle(title).observe(this, notes -> {
                adapter.setData(notes);
                binding.revNote.setAdapter(adapter);

            });
            binding.search.hide();
            return false;
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Note resultData = (Note) data.getSerializableExtra(NoteActivity.DATA_RESULT);
            switch (resultCode) {
                case RESULT_OK:
                    switch (requestCode) {
                        case ADD_NOTE_ACTIVITY_REQUEST_CODE:
                            viewModel.insert(resultData);
                            break;
                        case UPDATE_NOTE_ACTIVITY_REQUEST_CODE:
                            viewModel.update(resultData);
                            break;
                        default:
                    }
                    break;
                case RESULT_CAN_BE_DELETE:
                    viewModel.delete(resultData);
                    break;
                default:
            }
        }

    }

    private void toggleSelectAllItem(boolean reverseCheck) {
        adapter.getData().forEach(it -> it.isChecked = !reverseCheck);
        adapter.notifyDataSetChanged();
    }

}