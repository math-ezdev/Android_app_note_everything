package ez_dev.app.note_everything.ui.state;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ez_dev.app.note_everything.data.local.NoteEntity;

public class NoteViewModel extends ViewModel {
    private final MutableLiveData<NoteEntity> _item;
    public final LiveData<NoteEntity> item;

    private NoteViewModel(){
        _item = new MutableLiveData<>(new NoteEntity("",""));
        item = _item;
    }

    public void setItem(NoteEntity note){
        _item.setValue(note);
    }
}
