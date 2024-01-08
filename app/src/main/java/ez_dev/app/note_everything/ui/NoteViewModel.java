package ez_dev.app.note_everything.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ez_dev.app.note_everything.data.NoteRepository;
import ez_dev.app.note_everything.data.local.Note;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> findByTitle(String title) {
        return repository.findByTitle(title);
    }


    long[] insert(Note... notes){
        return repository.insert(notes);
    }

    int update(Note... notes){
        return repository.update(notes);
    }

    int delete(Note... notes){
        return repository.delete(notes);
    }


}
