package com.faltenreich.diaguard.feature.export.job.pdf.view;

import android.content.Context;

import androidx.annotation.RawRes;

import com.faltenreich.diaguard.feature.export.job.pdf.print.Pdf;
import com.pdfjet.Image;
import com.pdfjet.ImageType;

public class SizedImage extends Image {

    public SizedImage(Pdf pdf, Context context, @RawRes int imageRes) throws Exception {
        super(pdf, context.getResources().openRawResource(imageRes), ImageType.PNG);
    }

    public void setSize(float size) {
        this.w = size;
        this.h = size;
    }
}
