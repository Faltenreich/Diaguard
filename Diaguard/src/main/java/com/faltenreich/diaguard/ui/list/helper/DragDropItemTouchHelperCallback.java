package com.faltenreich.diaguard.ui.list.helper;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragDropItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private DragDropListener listener;

    public DragDropItemTouchHelperCallback(DragDropListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        boolean isDraggable = !(viewHolder instanceof Draggable) || ((Draggable) viewHolder).isDraggable();
        return isDraggable ? makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) : 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        boolean isDraggable = !(target instanceof Draggable) || ((Draggable) target).isDraggable();
        if (isDraggable) {
            listener.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (viewHolder instanceof Draggable) {
            ((Draggable) viewHolder).onDrag(isCurrentlyActive);
        }
    }

    public interface DragDropListener {
        void onItemMoved(int oldPosition, int newPosition);
    }
}
