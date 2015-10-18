package com.faltenreich.diaguard.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Faltenreich on 18.10.2015.
 */
public class Export {

    /*

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
        IFileListener listener;

        ProgressDialog progressDialog;
        private final int TEXT_SIZE = 9;

        private final

        Measurement.Category[] selectedCategories =
                new Measurement.Category[] {
                        Measurement.Category.BLOODSUGAR,
                        Measurement.Category.INSULIN,
                        Measurement.Category.MEAL,
                        Measurement.Category.ACTIVITY};

        DateTime dateStart;
        DateTime dateEnd;

        public PDFExportTask(IFileListener listener, DateTime dateStart, DateTime dateEnd) {
            this.listener = listener;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
        }

        @Override
        protected File doInBackground(Void... params) {
            File directory = FileHelper.getExternalStorage();
            if(directory == null)
                return null;

            File file = new File(directory.getAbsolutePath() + "/export" + DateTimeFormat.forPattern("yyyyMMddHHmmss").
                    print(new DateTime()) + ".pdf");

            // iTextG
            try {
                PDFont fontBasis = PDType1Font.HELVETICA;
                PDFont fontBold = PDType1Font.HELVETICA_BOLD;
                PDFont fontGray = PDType1Font.HELVETICA;
                PDFont fontRed = PDType1Font.HELVETICA;
                PDFont fontBlue = PDType1Font.HELVETICA;

                PDDocument document = new PDDocument();

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
                    String weekDay = weekDays[dateIteration.getDayOfWeek()-1];

                    PDPage page = new PDPage();
                    document.addPage(page);
                    pageForDay(document, page, dateIteration);

                    // Alternating row background


                    publishProgress(context.getString(R.string.day) + " " + currentDay + "/" + totalDays);

                    // Next day
                    dateIteration = dateIteration.plusDays(1);
                    currentDay++;
                }

                document.save(file);
                document.close();
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

        private void pageForDay(PDDocument document, PDPage page, DateTime day) {
            try {
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                HashMap<Measurement.Category, Float[]> values = EntryDao.getInstance().getAverageDataTable(day, selectedCategories, 2);
                String[][] content = new String[selectedCategories.length][values.get(selectedCategories[0]).length];
                int categoryPosition = 0;
                for (Measurement.Category category : values.keySet()) {
                    int valuePosition = 0;
                    for (float value : values.get(category)) {
                        content[categoryPosition][valuePosition] = PreferenceHelper.getInstance().getDecimalFormat(category).format(value);
                        valuePosition++;
                    }
                    categoryPosition++;
                }
                drawTable(page, contentStream, 0, 0, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        private void drawTable(PDPage page, PDPageContentStream contentStream, float y, float margin, String[][] content) throws IOException {
            final int rows = content.length;
            final int cols = content[0].length;
            final float rowHeight = 20f;
            final float tableWidth = page.getMediaBox().getWidth() - margin - margin;
            final float tableHeight = rowHeight * rows;
            final float colWidth = tableWidth/(float)cols;
            final float cellMargin=5f;

            //draw the rows
            float nexty = y ;
            for (int i = 0; i <= rows; i++) {
                contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);
                nexty-= rowHeight;
            }

            //draw the columns
            float nextx = margin;
            for (int i = 0; i <= cols; i++) {
                contentStream.drawLine(nextx, y, nextx, y-tableHeight);
                nextx += colWidth;
            }

            //now add the text
            contentStream.setFont( PDType1Font.HELVETICA_BOLD , 12 );

            float textx = margin+cellMargin;
            float texty = y-15;
            for(int i = 0; i < content.length; i++){
                for(int j = 0 ; j < content[i].length; j++){
                    String text = content[i][j];
                    contentStream.beginText();
                    contentStream.moveTextPositionByAmount(textx,texty);
                    contentStream.drawString(text);
                    contentStream.endText();
                    textx += colWidth;
                }
                texty-=rowHeight;
                textx = margin+cellMargin;
            }
        }
    }
        */
}
