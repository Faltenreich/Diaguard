package com.faltenreich.diaguard.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.pdfjet.Box;
import com.pdfjet.Cell;
import com.pdfjet.Color;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.Letter;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Point;
import com.pdfjet.Table;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class Export {

    public static final String MIME_PDF = "application/pdf";

    private Context context;

    public Export(Context context) {
        this.context = context;
    }

    public void exportPDF(IFileListener listener, DateTime dateStart, DateTime dateEnd) {
        PDFExportTask pdfExportTask = new PDFExportTask(listener, dateStart, dateEnd);
        pdfExportTask.execute();
    }

    private class PDFExportTask extends AsyncTask<Void, String, File> {

        private final Measurement.Category[] selectedCategories =
                new Measurement.Category[] {
                        Measurement.Category.BLOODSUGAR,
                        Measurement.Category.INSULIN,
                        Measurement.Category.MEAL,
                        Measurement.Category.ACTIVITY};

        private ProgressDialog progressDialog;
        private IFileListener listener;
        private DateTime dateStart;
        private DateTime dateEnd;

        private Font fontNormal;
        private Font fontBold;

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
                FileOutputStream stream = new FileOutputStream(file);
                PDF pdf = new PDF(stream);
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

                // One day after last chosen day
                DateTime dateAfter = dateEnd.plusDays(1);

                // Total number of days to export
                int totalDays = Days.daysBetween(dateStart, dateEnd).getDays() + 1;

                String[] weekDays = context.getResources().getStringArray(R.array.weekdays);

                // Day by day
                int currentDay = 1;
                while(dateIteration.isBefore(dateAfter)) {
                    // title bar for new week

                    // Header
                    String weekDay = weekDays[dateIteration.getDayOfWeek() - 1];

                    addPageForDay(pdf, dateIteration);

                    publishProgress(String.format("%s %d/%d",
                            context.getString(R.string.day),
                            currentDay,
                            totalDays));

                    // Next day
                    dateIteration = dateIteration.plusDays(1);
                    currentDay++;
                }

                pdf.flush();
                stream.close();
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

        private void addPageForDay(PDF pdf, DateTime day) {
            try {
                Page page = new Page(pdf, Letter.PORTRAIT);
                Table table = new Table();
                table.setData(getData(day), Table.DATA_HAS_1_HEADER_ROWS);
                table.setPosition(0, 0);
                table.autoAdjustColumnWidths();

                boolean hasMoreData = true;
                while (hasMoreData) {
                    Point point = table.drawOn(page);
                    point.drawOn(page);
                    // TO DO: Draw "Page 1 of N" here
                    if (!table.hasMoreData()) {
                        // Allow the table to be drawn again later:
                        table.resetRenderedPagesCount();
                        hasMoreData = false;
                    }
                    page = new Page(pdf, Letter.PORTRAIT);
                }

                // TODO: Alternating row background
                table.drawOn(page);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<List<Cell>> getData(DateTime day) {
            List<List<Cell>> data = new ArrayList<>();
            HashMap<Measurement.Category, float[]> values = EntryDao.getInstance().getAverageDataTable(day, selectedCategories, 2);
            for (Measurement.Category category : values.keySet()) {
                List<Cell> cells = new ArrayList<>();
                for (float value : values.get(category)) {
                    float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
                    String text = customValue > 0 ?
                            PreferenceHelper.getInstance().getDecimalFormat(category).format(customValue) :
                            "";
                    Cell cell = new Cell(fontNormal, text);
                    cell.setFgColor(Color.black);
                    cells.add(cell);
                }
                data.add(cells);
            }
            return data;
        }

        /*
        private Paragraph getWeekBar(DateTime weekStart) {
            Paragraph paragraph = new Paragraph();

            // Week
            Chunk chunk = new Chunk(context.getString(R.string.calendarweek) + " " + weekStart.getWeekOfWeekyear());
            chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            paragraph.add(chunk);

            DateTime weekEnd = weekStart.withDayOfWeek(DateTimeConstants.SUNDAY);

            // Dates
            chunk = new Chunk("\n" +
                    Helper.getDateFormat().print(weekStart) + " - " +
                    Helper.getDateFormat().print(weekEnd));
            chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 9));
            paragraph.add(chunk);

            return paragraph;
        }
        */
    }
}
