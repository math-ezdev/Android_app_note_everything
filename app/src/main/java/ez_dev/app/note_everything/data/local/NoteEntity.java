package ez_dev.app.note_everything.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note_table")
public class NoteEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    @Ignore
    public boolean isChecked;

    public NoteEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "NoteEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public boolean hasValid(){
        return title.length() != 0 && description.length() != 0;
    }
}
