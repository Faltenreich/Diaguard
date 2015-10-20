package com.faltenreich.diaguard.util.export;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.FileHelper;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.pdfjet.Align;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Paragraph;
import com.pdfjet.Point;
import com.pdfjet.Table;
import com.pdfjet.Text;
import com.pdfjet.TextLine;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class PdfExport {

    private static final String TAG = PdfExport.class.getSimpleName();

    public static final String MIME_PDF = "application/pdf";

    private Context context;

    public PdfExport(Context context) {
        this.context = context;
    }

    public void exportPDF(IFileListener listener, DateTime dateStart, DateTime dateEnd) {
        PDFExportTask pdfExportTask = new PDFExportTask(listener, dateStart, dateEnd);
        pdfExportTask.execute();
    }

    private class PDFExportTask extends AsyncTask<Void, String, File> {

        private static final float PADDING = 20;

        private ProgressDialog progressDialog;
        private IFileListener listener;
        private DateTime dateStart;
        private DateTime dateEnd;

        private Font fontNormal;
        private Font fontBold;
        private PdfPage page;

        public PDFExportTask(IFileListener listener, DateTime dateStart, DateTime dateEnd) {
            this.listener = listener;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
        }

        @Override
        protected File doInBackground(Void... params) {

            File directory = FileHelper.getExternalStorage();
            if(directory == null) {
                return null;
            }

            // TODO: Store in Documents folder
            File file = new File(String.format("%s/export%s.pdf",
                    directory.getAbsolutePath(),
                    DateTimeFormat.forPattern("yyyyMMddHHmmss").print(new DateTime())));

            try {
                Point currentPosition = new Point();

                PDF pdf = new PDF(new FileOutputStream(file));
                pdf.setTitle(String.format("%s %s", context.getString(R.string.app_name), context.getString(R.string.export)));
                pdf.setSubject(String.format("%s %s: %s - %s",
                        context.getString(R.string.app_name),
                        context.getString(R.string.export),
                        Helper.getDateFormat().print(dateStart),
                        Helper.getDateFormat().print(dateEnd)));
                pdf.setAuthor(context.getString(R.string.app_name));

                fontNormal = new Font(pdf, CoreFont.HELVETICA);
                fontBold = new Font(pdf, CoreFont.HELVETICA_BOLD);

                DateTime dateIteration = dateStart;

                page = createPage(pdf);

                Text weekBar = getWeekBar(dateIteration);
                weekBar.setLocation(page.getStartPoint().getX(), page.getStartPoint().getY());

                float[] nextPosition = weekBar.drawOn(page);
                currentPosition.setX(nextPosition[0]);
                currentPosition.setY(nextPosition[1]);

                // One day after last chosen day
                DateTime dateAfter = dateEnd.plusDays(1);

                String[] weekDays = context.getResources().getStringArray(R.array.weekdays);

                // Day by day
                while(dateIteration.isBefore(dateAfter)) {
                    // title bar for new week
                    if(dateIteration.isAfter(dateStart) && dateIteration.getDayOfWeek() == 1) {
                        page = createPage(pdf);

                        weekBar = getWeekBar(dateIteration);
                        weekBar.setLocation(page.getStartPoint().getX(), page.getStartPoint().getY());

                        nextPosition = weekBar.drawOn(page);
                        currentPosition.setX(nextPosition[0]);
                        currentPosition.setY(nextPosition[1]);
                    }

                    PdfTable table = new PdfTable(pdf, page, dateIteration);

                    // Page break
                    if (currentPosition.getY() + table.getHeight() > page.getHeight()) {
                        page = new PdfPage(pdf);
                        currentPosition = page.getStartPoint();
                    }

                    table.setPosition(currentPosition.getX(), currentPosition.getY());
                    currentPosition = table.drawOn(page);
                    currentPosition.setY(currentPosition.getY() + PADDING);

                    publishProgress(String.format("%s %d/%d",
                            context.getString(R.string.day),
                            Days.daysBetween(dateStart, dateIteration).getDays() + 1,
                            Days.daysBetween(dateStart, dateEnd).getDays() + 1));

                    dateIteration = dateIteration.plusDays(1);
                }

                pdf.flush();
            }
            catch (Exception ex) {
                Log.e("DiaguardError", ex.getMessage());
            }

            return file;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.export_progress));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... message) {
            progressDialog.setMessage(message[0]);
        }

        @Override
        protected void onPostExecute(File file) {
            progressDialog.dismiss();
            super.onPostExecute(file);
            if(listener != null) {
                listener.handleFile(file, MIME_PDF);
            }
        }

        private PdfPage createPage(PDF pdf) {
            try {
                return new PdfPage(pdf);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create new page");
                return null;
            }
        }

        private Text getWeekBar(DateTime weekStart) {
            Paragraph paragraph = new Paragraph();

            TextLine week = new TextLine(fontBold);
            week.setText(String.format("%s %d",
                    context.getString(R.string.calendarweek),
                    weekStart.getWeekOfWeekyear()));
            paragraph.add(week);

            DateTime weekEnd = weekStart.withDayOfWeek(DateTimeConstants.SUNDAY);
            TextLine interval = new TextLine(fontNormal);
            interval.setText(String.format("%s - %s",
                    Helper.getDateFormat().print(weekStart),
                    Helper.getDateFormat().print(weekEnd)));
            paragraph.add(interval);

            List<Paragraph> paragraphs = new ArrayList<>();
            paragraphs.add(paragraph);
            Text text = null;
            try {
                text = new Text(paragraphs);
                text.setSpaceBetweenTextLines(50);
                text.setWidth(page.getWidth());
            } catch (Exception e) {
                Log.e(TAG, "Failed to instantiate Text");
            }

            return text;
        }
    }
}
