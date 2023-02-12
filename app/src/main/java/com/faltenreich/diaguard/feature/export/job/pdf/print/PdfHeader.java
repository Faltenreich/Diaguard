package com.faltenreich.diaguard.feature.export.job.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.feature.export.job.pdf.view.SizedText;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class PdfHeader {

    private static final String TAG = PdfHeader.class.getSimpleName();

    private static final float MARGIN_BOTTOM = 20;

    private final PdfExportCache cache;
    private SizedText text;

    PdfHeader(PdfExportCache cache) {
        this.cache = cache;

        if (cache.getConfig().includeCalendarWeek()) {
            DateTime weekStart = DateTimeUtils.atStartOfWeek(cache.getDateTime());
            TextLine week = new TextLine(cache.getFontHeader());
            week.setText(String.format("%s %d",
                cache.getContext().getString(R.string.calendarweek),
                weekStart.getWeekOfWeekyear())
            );
            Paragraph weekParagraph = new Paragraph(week);

            DateTime weekEnd = DateTimeUtils.atEndOfWeek(weekStart);
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
                Log.e(TAG, exception.toString());
            }
        }
    }

    public float getHeight() {
        if (cache.getConfig().includeCalendarWeek() && text != null) {
            return text.getHeight() + MARGIN_BOTTOM;
        }
        return 0f;
    }

    public void drawOn(PdfPage page, Point position) throws Exception {
        if (cache.getConfig().includeCalendarWeek()) {
            text.setLocation(position.getX(), position.getY());
            text.setWidth(page.getWidth());
            text.drawOn(page);
        }
    }
}
