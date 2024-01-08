package ez_dev.app.note_everything.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM table_note")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM table_note WHERE title LIKE '%' || :title || '%'")
    LiveData<List<Note>> findByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(Note... notes);

    @Update
    int update(Note... notes);

    @Delete
    int delete(Note... notes);
}
