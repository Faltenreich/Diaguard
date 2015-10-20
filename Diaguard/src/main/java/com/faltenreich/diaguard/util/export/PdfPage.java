package com.faltenreich.diaguard.util.export;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.pdfjet.Align;
import com.pdfjet.Color;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.Text;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 20.10.2015.
 */
public class PdfPage extends Page {

    private static final String TAG = PdfPage.class.getSimpleName();

    private static final int PADDING_TOP = 80;
    private static final int PADDING_BOTTOM = 100;
    private static final int PADDING_HORIZONTAL = 60;

    public PdfPage(PDF pdf) throws Exception {
        super(pdf, Letter.PORTRAIT);
        appendFooter();
    }

    public Point getStartPoint() {
        return new Point(PADDING_HORIZONTAL, PADDING_TOP);
    }

    @Override
    public float getWidth() {
        return super.getWidth() - (PADDING_HORIZONTAL * 2);
    }

    @Override
    public float getHeight() {
        return super.getHeight() - PADDING_TOP - PADDING_BOTTOM;
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
}
