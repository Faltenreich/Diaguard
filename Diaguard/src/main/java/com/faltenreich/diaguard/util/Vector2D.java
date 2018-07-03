package com.faltenreich.diaguard.util;

public class Vector2D {

    public int x;
    public int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%d, %d", x, y);
    }
}
