package com.faltenreich.diaguard.export.pdf;

import android.content.Context;

import com.faltenreich.diaguard.R;
import com.pdfjet.Font;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.Text;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PdfWeekHeader implements PdfPrintable {

    private static final float FONT_SIZE_HEADER = 15f;
    private static final float PADDING_PARAGRAPH = 20;
    private static final float PADDING_LINE = 3;

    private WeakReference<Context> context;
    private DateTime dateTime;
    private Font fontNormal;
    private Font fontBold;

    PdfWeekHeader(WeakReference<Context> context, DateTime dateTime, Font fontNormal, Font fontBold) {
        this.context = context;
        this.dateTime = dateTime;
        this.fontNormal = fontNormal;
        this.fontBold = fontBold;
    }

    @Override
    public Point drawOn(PdfPage page) throws Exception {
        DateTime weekStart = dateTime.withDayOfWeek(1);

        TextLine week = new TextLine(fontBold);
        week.setFontSize(FONT_SIZE_HEADER);
        week.setText(String.format("%s %d",
            context.get().getString(R.string.calendarweek),
            weekStart.getWeekOfWeekyear())
        );
        Paragraph weekParagraph = new Paragraph(week);

        DateTime weekEnd = weekStart.withDayOfWeek(DateTimeConstants.SUNDAY);
        TextLine interval = new TextLine(fontNormal);
        interval.setText(String.format("%s - %s",
            DateTimeFormat.mediumDate().print(weekStart),
            DateTimeFormat.mediumDate().print(weekEnd))
        );
        Paragraph intervalParagraph = new Paragraph(interval);

        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(weekParagraph);
        paragraphs.add(intervalParagraph);

        Text text = new Text(paragraphs);
        text.setParagraphLeading(week.getFont().getBodyHeight() + PADDING_LINE);

        Point currentPosition = page.getStartPoint();
        text.setLocation(currentPosition.getX(), currentPosition.getY());
        text.setWidth(page.getWidth());

        float[] points = text.drawOn(page);
        currentPosition.setY(points[1] + PADDING_PARAGRAPH);
        return currentPosition;
    }
}
