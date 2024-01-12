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
    @Query("SELECT * FROM note_table")
    LiveData<List<NoteEntity>> getAll();

    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :title || '%'")
    LiveData<List<NoteEntity>> findByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(NoteEntity... notes);

    @Update
    int update(NoteEntity... notes);

    @Delete
    int delete(NoteEntity... notes);
}
