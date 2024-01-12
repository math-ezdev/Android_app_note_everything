package ez_dev.app.note_everything.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ez_dev.app.note_everything.data.local.AppDatabase;
import ez_dev.app.note_everything.data.local.NoteDAO;
import ez_dev.app.note_everything.data.local.NoteEntity;

public class NoteRepository {
    private final NoteDAO noteDAO;
    private final LiveData<List<NoteEntity>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        noteDAO = database.noteDAO();
        allNotes = noteDAO.getAll();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> findByTitle(String title) {
        return noteDAO.findByTitle(title);
    }

    public void insert(NoteEntity... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.insert(notes));
    }

    public void update(NoteEntity... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.update(notes));
    }

    public void delete(NoteEntity... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.delete(notes));
    }
}
