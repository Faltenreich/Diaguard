package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.export.pdf.view.SizedText;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class PdfHeader implements PdfPrintable {

    private static final String TAG = PdfHeader.class.getSimpleName();

    private static final float MARGIN_BOTTOM = 20;

    private SizedText text;

    PdfHeader(PdfExportCache cache) {
        // FIXME: Start and end of week are not localized, since calendar weeks cannot be localized via JodaTIme
        DateTime weekStart = cache.getDateTime().withDayOfWeek(1);
        TextLine week = new TextLine(cache.getFontHeader());
        week.setText(String.format("%s %d",
            cache.getContext().getString(R.string.calendarweek),
            weekStart.getWeekOfWeekyear())
        );
        Paragraph weekParagraph = new Paragraph(week);

        DateTime weekEnd = weekStart.withDayOfWeek(DateTimeConstants.SUNDAY);
        TextLine interval = new TextLine(cache.getFontNormal());
        interval.setText(String.format("%s - %s",
            DateTimeFormat.mediumDate().print(weekStart),
            DateTimeFormat.mediumDate().print(weekEnd))
        );
        Paragraph intervalParagraph = new Paragraph(interval);

        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(weekParagraph);
        paragraphs.add(intervalParagraph);

        try {
            text = new SizedText(paragraphs);
            text.setParagraphLeading(week.getFont().getBodyHeight());
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public float getHeight() {
        if (text != null) {
            return text.getHeight() + MARGIN_BOTTOM;
        }
        return 0f;
    }

    @Override
    public void drawOn(PdfPage page, Point position) throws Exception {
        text.setLocation(position.getX(), position.getY());
        text.setWidth(page.getWidth());
        text.drawOn(page);
    }
}
