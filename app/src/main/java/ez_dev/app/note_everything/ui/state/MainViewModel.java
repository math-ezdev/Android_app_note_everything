package ez_dev.app.note_everything.ui.state;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ez_dev.app.note_everything.data.local.NoteEntity;
import ez_dev.app.note_everything.data.repository.NoteRepository;

public class MainViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<NoteEntity>> allNotes;
    private final MutableLiveData<String> _title;
    public final LiveData<String> title;

    public MainViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        _title = new MutableLiveData<>("");
        title = _title;
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public void setTitle(String title){
        _title.setValue(title);
    }

    public void insert(NoteEntity... notes) {
        repository.insert(notes);
    }

    public void update(NoteEntity... notes) {
        repository.update(notes);
    }

    public void delete(NoteEntity... notes) {
        repository.delete(notes);
    }


}
