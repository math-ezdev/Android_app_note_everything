package ez_dev.app.note_everything.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ez_dev.app.note_everything.data.local.NoteEntity;
import ez_dev.app.note_everything.databinding.ItemNoteBinding;
import ez_dev.app.note_everything.util.OnClickListener;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {
    private List<NoteEntity> dataSet = new ArrayList<>();
    private OnClickListener onClickListener;

    public List<NoteEntity> getData() {
        return dataSet;
    }

    public List<NoteEntity> getCheckedData() {
        List<NoteEntity> selectedDataSet = new ArrayList<>();
        for (NoteEntity item : dataSet) {
            if (item.isChecked) {
                selectedDataSet.add(item);
            }
        }
        return selectedDataSet;
    }

    public void setData(List<NoteEntity> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
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
        NoteEntity note = dataSet.get(position);

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
