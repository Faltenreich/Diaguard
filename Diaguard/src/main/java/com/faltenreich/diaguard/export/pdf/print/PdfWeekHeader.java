package com.faltenreich.diaguard.export.pdf.print;

import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.export.pdf.PdfExportCache;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class PdfWeekHeader implements PdfPrintable {

    private static final String TAG = PdfWeekHeader.class.getSimpleName();

    private static final float FONT_SIZE_HEADER = 15f;
    private static final float PADDING_PARAGRAPH = 20;
    private static final float PADDING_LINE = 3;

    private PdfText text;

    public PdfWeekHeader(PdfExportCache cache) {
        DateTime weekStart = cache.getDateTime().withDayOfWeek(1);
        TextLine week = new TextLine(cache.getFontBold());
        week.setFontSize(FONT_SIZE_HEADER);
        week.setText(String.format("%s %d",
            cache.getConfig().getContextReference().get().getString(R.string.calendarweek),
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
            text = new PdfText(paragraphs);
            text.setParagraphLeading(week.getFont().getBodyHeight() + PADDING_LINE);
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
    }

    @Override
    public float getHeight() {
        if (text != null) {
            return text.getHeight() + PADDING_PARAGRAPH;
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
