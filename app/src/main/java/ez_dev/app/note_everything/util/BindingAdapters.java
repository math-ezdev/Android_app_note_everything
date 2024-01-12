package ez_dev.app.note_everything.util;

import android.view.View;

import androidx.databinding.BindingAdapter;

import ez_dev.app.note_everything.data.local.NoteEntity;

public class BindingAdapters {
    @BindingAdapter("app:show_if_data_valid")
    public static void showIfDataValid(View view, NoteEntity data) {
        boolean isVisibility = data != null && data.hasValid();
        view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }
}
