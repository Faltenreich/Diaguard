package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Box;

public class SizedBox extends Box {

    private float width;
    private float height;

    public SizedBox(float width, float height) {
        super(0, 0, width, height);
        this.width = width;
        this.height = height;
        setSize(width, height);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
