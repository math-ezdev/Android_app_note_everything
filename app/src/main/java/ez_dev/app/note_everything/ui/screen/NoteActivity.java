package ez_dev.app.note_everything.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ez_dev.app.note_everything.data.local.NoteEntity;
import ez_dev.app.note_everything.databinding.ActivityNoteBinding;
import ez_dev.app.note_everything.ui.state.NoteViewModel;

public class NoteActivity extends AppCompatActivity {
    public static final String DATA_RESULT = "NOTE_INPUT";
    public static final int RESULT_CODE_DELETE = 2;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  binding library
        ez_dev.app.note_everything.databinding.ActivityNoteBinding binding = ActivityNoteBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        //  get note from request activity
        resultIntent = getIntent();
        NoteEntity currentNote = (NoteEntity) resultIntent.getSerializableExtra(MainActivity.DATA_CURRENT);

        //  view model
        NoteViewModel viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        if (currentNote != null) {
            viewModel.setItem(currentNote);
            binding.btnDelete.setOnClickListener(v -> {
                resultIntent.putExtra(DATA_RESULT, viewModel.item.getValue());
                setResult(RESULT_CODE_DELETE, resultIntent);
                finish();
            });
        }

        //  data binding
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);


        //  handle user event
        binding.btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        binding.btnSave.setOnClickListener(v -> {
            NoteEntity note = viewModel.item.getValue();
            if (note != null && note.hasValid()) {
                resultIntent.putExtra(DATA_RESULT, note);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


}