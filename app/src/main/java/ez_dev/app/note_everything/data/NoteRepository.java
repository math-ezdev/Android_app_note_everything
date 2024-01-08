package ez_dev.app.note_everything.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ez_dev.app.note_everything.data.local.AppDatabase;
import ez_dev.app.note_everything.data.local.Note;
import ez_dev.app.note_everything.data.local.NoteDAO;

public class NoteRepository {
    private final NoteDAO noteDAO;
    private final LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        noteDAO = database.noteDAO();
        allNotes = noteDAO.getAll();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> findByTitle(String title) {
        return noteDAO.findByTitle(title);
    }

    public long[] insert(Note... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.insert(notes));
        return new long[]{0};
    }

    public int update(Note... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.update(notes));
        return 0;
    }

    public int delete(Note... notes) {
        AppDatabase.databaseWriteExecutor.execute(() -> noteDAO.delete(notes));
        return 0;
    }
}
