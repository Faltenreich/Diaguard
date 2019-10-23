package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.pdfjet.Color;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class PdfPage extends Page {

    private static final String TAG = PdfPage.class.getSimpleName();

    private static final int PADDING_TOP = 80;
    private static final int PADDING_BOTTOM = 100;
    private static final int PADDING_HORIZONTAL = 60;

    private Point position;

    public PdfPage(PDF pdf) throws Exception {
        super(pdf, Letter.PORTRAIT);
        appendFooter();
        this.position = getStartPoint();
    }

    private Point getStartPoint() {
        return new Point(PADDING_HORIZONTAL, PADDING_TOP);
    }

    public Point getEndPoint() {
        return new Point(PADDING_HORIZONTAL, super.getHeight() - PADDING_BOTTOM);
    }

    @Override
    public float getWidth() {
        return super.getWidth() - (PADDING_HORIZONTAL * 2);
    }

    @Override
    public float getHeight() {
        return super.getHeight() - PADDING_TOP - PADDING_BOTTOM;
    }

    public Point getPosition() {
        return position;
    }

    private void appendFooter() {
        try {
            int textColor = Color.gray;
            float positionY = super.getHeight() - (PADDING_BOTTOM / 2);

            // Created by
            Font font = new Font(pdf, CoreFont.HELVETICA);
            TextLine generatedBy = new TextLine(font);
            generatedBy.setPosition(PADDING_HORIZONTAL, positionY);
            generatedBy.setColor(textColor);
            generatedBy.setText(String.format("%s %s",
                DiaguardApplication.getContext().getString(R.string.export_stamp),
                DateTimeFormat.mediumDate().print(DateTime.now())));
            generatedBy.drawOn(this);

            // Url
            TextLine url = new TextLine(font);
            url.setText(DiaguardApplication.getContext().getString(R.string.app_homepage_short));
            url.setPosition(super.getWidth() - PADDING_HORIZONTAL - url.getWidth(), positionY);
            url.setColor(textColor);
            url.drawOn(this);
        } catch (Exception e) {
            Log.e(TAG, "Failed to append footer");
        }
    }

    public void draw(PdfPrintable printable) {
        try {
            position = printable.drawOn(this);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }
}
