package com.faltenreich.diaguard.shared.view.recyclerview.drag;

public interface Draggable {
    boolean isDraggable();
    void onDrag(boolean isDragged);
}
