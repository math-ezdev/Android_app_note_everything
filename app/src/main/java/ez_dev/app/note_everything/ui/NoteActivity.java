package ez_dev.app.note_everything.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ez_dev.app.note_everything.data.local.Note;
import ez_dev.app.note_everything.databinding.ActivityNoteBinding;

public class NoteActivity extends AppCompatActivity {
    public static final String DATA_RESULT = "NOTE_INPUT";
    public static final int RESULT_CAN_BE_DELETE = 2;
    private ActivityNoteBinding binding;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  binding library
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        //  get note from request activity
        Intent intent = getIntent();
        currentNote = (Note) intent.getSerializableExtra(MainActivity.DATA_CURRENT);

        //  button action
        binding.btnDelete.setVisibility(currentNote != null ? View.VISIBLE : View.GONE);
        if (currentNote != null) {
            binding.inputTitle.setText(currentNote.title);
            binding.inputDescription.setText(currentNote.description);
            binding.btnDelete.setOnClickListener(v -> {
                intent.putExtra(DATA_RESULT, getNoteInput());
                setResult(RESULT_CAN_BE_DELETE, intent);
                finish();
            });
        }
        binding.btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        binding.btnSave.setOnClickListener(v -> {
            if (getNoteInput().isValidated()) {
                intent.putExtra(DATA_RESULT, getNoteInput());
                setResult(RESULT_OK, intent);
                finish();
            }

        });


    }

    private Note getNoteInput() {
        String title = binding.inputTitle.getText().toString().trim();
        String description = binding.inputDescription.getText().toString().trim();
        Note note = new Note(title, description);
        if (currentNote != null) {
            note.id = currentNote.id;
        }
        return note;
    }


}