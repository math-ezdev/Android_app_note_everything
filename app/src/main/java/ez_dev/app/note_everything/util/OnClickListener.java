package ez_dev.app.note_everything.util;

public interface OnClickListener {
    <E> void setOnClickListener(E element, int position);

    <E> boolean setOnLongClickListener(E element, int position);
}
