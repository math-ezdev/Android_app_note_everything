package ez_dev.app.note_everything.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ez_dev.app.note_everything.data.local.Note;
import ez_dev.app.note_everything.databinding.ItemNoteBinding;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {
    private List<Note> dataSet;
    private OnClickListener onClickListener;

    public List<Note> getData() {
        return dataSet;
    }

    public List<Note> getCheckedData() {
        List<Note> selectedDataSet = new ArrayList<>();
        for (Note item : dataSet) {
            if (item.isChecked) {
                selectedDataSet.add(item);
            }
        }
        return selectedDataSet;
    }

    public void setData(List<Note> dataSet) {
        this.dataSet = dataSet;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNoteBinding binding = ItemNoteBinding.inflate(layoutInflater, parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = dataSet.get(position);

        //  ui
        holder.binding.tvNoteTitle.setText(note.title);
        holder.binding.cardNote.setCardElevation(note.isChecked ? 40f : 0.1f);

        //  ui listener
        if (getCheckedData().size() > 0) {
            holder.binding.cardNote.setOnClickListener(v -> onClickListener.setOnLongClickListener(note, position));
            holder.binding.cardNote.setOnLongClickListener(v -> {
                onClickListener.setOnClickListener(note, position);
                return true;
            });
        } else {
            holder.binding.cardNote.setOnClickListener(v -> onClickListener.setOnClickListener(note, position));
            holder.binding.cardNote.setOnLongClickListener(v -> onClickListener.setOnLongClickListener(note, position));
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoteBinding binding;

        public NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
