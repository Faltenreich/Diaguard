package com.faltenreich.diaguard.feature.export.job.pdf.view;

import com.pdfjet.Box;

public class SizedBox extends Box {

    private final float width;
    private final float height;

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
