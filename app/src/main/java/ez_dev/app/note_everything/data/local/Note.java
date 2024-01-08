package ez_dev.app.note_everything.data.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_note")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    @Ignore
    public boolean isChecked;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public boolean isValidated(){
        return title.length() != 0 && description.length() != 0;
    }
}
