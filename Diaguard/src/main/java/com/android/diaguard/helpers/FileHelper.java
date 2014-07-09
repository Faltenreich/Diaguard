package com.android.diaguard.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileHelper {

    public static final String PATH_STORAGE = Environment.getExternalStorageDirectory() + "/Diaguard";
    public static final char DELIMITER = ';';

    public static final String MIME_MAIL = "message/rfc822";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_CSV = "text/csv";

    Context context;
    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;

    public FileHelper(Context context) {
        this.context = context;
        this.dataSource = new DatabaseDataSource(context);
        this.preferenceHelper = new PreferenceHelper(context);
    }

    public File exportCSV() {
        CSVExportTask csvExportTask = new CSVExportTask();
        csvExportTask.execute();

        File createdFile = null;
        try {
            createdFile = csvExportTask.get();
        }
        catch(InterruptedException ex) {
            ex.printStackTrace();
        }
        catch(ExecutionException ex) {
            ex.printStackTrace();
        }
        return createdFile;
    }

    public void importCSV(String fileName) {
        CSVImportTask csvImportTask = new CSVImportTask();
        csvImportTask.execute(fileName);
    }

    public File exportPDF(Calendar dateStart, Calendar dateEnd) {
        PDFExportTask pdfExportTask = new PDFExportTask();
        pdfExportTask.execute(dateStart, dateEnd);

        File createdFile = null;
        try {
            createdFile = pdfExportTask.get();
        }
        catch(InterruptedException ex) {
            ex.printStackTrace();
        }
        catch(ExecutionException ex) {
            ex.printStackTrace();
        }

        return createdFile;
    }

    private class CSVExportTask extends AsyncTask<Void, Void, File> {

        @Override
        protected File doInBackground(Void... params) {

            dataSource.open();
            List<Event> events = dataSource.getEvents();
            dataSource.close();

            File directory = new File(PATH_STORAGE);

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            File file = new File(directory + "/backup" +
                    format.format(Calendar.getInstance().getTime()) + ".csv");

            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter writer = new CSVWriter(fileWriter, DELIMITER);

                for(Event event : events) {
                    String[] array = {
                            Float.toString(event.getValue()),
                            Helper.getDateDatabaseFormat().format(event.getDate().getTime()),
                            event.getNotes(),
                            event.getCategory().name() };
                    writer.writeNext(array);
                }

                writer.close();
            }
            catch (IOException ex) {
                //Log.e("DiaguardError", ex.getMessage());
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class CSVImportTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                String filePath = PATH_STORAGE + "/" + params[0];
                CSVReader reader = new CSVReader(new FileReader(filePath), DELIMITER);

                Event event = new Event();
                String[] nextLine;
                dataSource.open();
                while ((nextLine = reader.readNext()) != null) {

                    event.setValue(Float.parseFloat(nextLine[0]));
                    event.setDate(nextLine[1]);
                    event.setNotes(nextLine[2]);
                    event.setCategory(Event.Category.valueOf(nextLine[3]));
                    dataSource.insertEvent(event);
                }
                dataSource.close();
                reader.close();
            } catch (IOException ex) {
                //Log.e("DiaguardError", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PDFExportTask extends AsyncTask<Calendar, String, File> {
        ProgressDialog progressDialog;
        private final int TEXT_SIZE = 9;

        Event.Category[] selectedCategories =
                new Event.Category[] {
                        Event.Category.BloodSugar,
                        Event.Category.Bolus,
                        Event.Category.Meal,
                        Event.Category.Activity};

        Calendar dateStart;
        Calendar dateEnd;

        @Override
        protected File doInBackground(Calendar... params) {

            File directory = new File(PATH_STORAGE);
            File file = new File(directory + "/export" + new SimpleDateFormat("yyyyMMddHHmmss").
                    format(Calendar.getInstance().getTime()) + ".pdf");

            dateStart = params[0];
            dateEnd = params[1];

            // iTextG
            try {
                Font fontBasis = FontFactory.getFont(FontFactory.HELVETICA, TEXT_SIZE);
                Font fontBold = FontFactory.getFont(FontFactory.HELVETICA, TEXT_SIZE, Font.BOLD);
                Font fontGray = FontFactory.getFont(FontFactory.HELVETICA, TEXT_SIZE, BaseColor.GRAY);
                Font fontRed = FontFactory.getFont(FontFactory.HELVETICA, TEXT_SIZE, BaseColor.RED);
                Font fontBlue = FontFactory.getFont(FontFactory.HELVETICA, TEXT_SIZE, BaseColor.BLUE);

                Document document = new Document();

                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                HeaderFooter event = new HeaderFooter();
                writer.setBoxSize("Header", new Rectangle(36, 54, 559, 788));
                writer.setPageEvent(event);

                document.open();
                iTextGMetaData(document);

                final Calendar dateIteration = Calendar.getInstance();
                dateIteration.setTime(dateStart.getTime());

                // One day after last chosen day
                Calendar dateAfter = Calendar.getInstance();
                dateAfter.setTime(dateEnd.getTime());
                dateAfter.set(Calendar.DAY_OF_MONTH, dateAfter.get(Calendar.DAY_OF_MONTH) + 1);

                // Total number of days to export
                int totalDays = Helper.getDifferenceInDays(dateStart, dateEnd) + 1;

                String[] weekDays = context.getResources().getStringArray(R.array.weekdays);

                document.add(getWeekBar(dateIteration));
                document.add(Chunk.NEWLINE);

                // Day by day
                int currentDay = 1;
                while(dateIteration.before(dateAfter)) {

                    // title bar for new week
                    if(currentDay > 1 && dateIteration.get(Calendar.DAY_OF_WEEK) == 2) {
                        document.newPage();
                        document.add(getWeekBar(dateIteration));
                        document.add(Chunk.NEWLINE);
                    }

                    PdfPTable table = new PdfPTable(13);
                    table.setWidths(new float[]{3,1,1,1,1,1,1,1,1,1,1,1,1});
                    table.setWidthPercentage(100);

                    PdfPCell cell;

                    // Header
                    cell = new PdfPCell(new Phrase(weekDays[dateIteration.get(Calendar.DAY_OF_WEEK)-1].substring(0, 2) + " " +
                            new SimpleDateFormat("dd.MM.").format(dateIteration.getTime()),
                            new Font(fontBold)));
                    cell.setBorder(0);
                    cell.setBorder(Rectangle.BOTTOM);
                    table.addCell(cell);
                    for(int i = 0; i < 12; i++) {
                        cell = new PdfPCell(new Paragraph(Integer.toString(i * 2), fontGray));
                        cell.setBorder(0);
                        cell.setBorder(Rectangle.BOTTOM);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    }

                    // Content
                    dataSource.open();
                    float[][] values = dataSource.getAverageDataTable(dateIteration, selectedCategories, 12);
                    dataSource.close();

                    // Insert values into table
                    for(int categoryPosition = 0; categoryPosition < selectedCategories.length; categoryPosition++) {
                        Event.Category category = selectedCategories[categoryPosition];

                        cell = new PdfPCell(new Paragraph(preferenceHelper.getCategoryName(category), fontGray));
                        cell.setBorder(0);
                        if(categoryPosition == selectedCategories.length-1)
                            cell.setBorder(Rectangle.BOTTOM);
                        table.addCell(cell);

                        for(int hour = 0; hour < 12; hour++) {
                            float value = preferenceHelper.formatDefaultToCustomUnit(category,
                                    values[categoryPosition][hour]);

                            Paragraph paragraph = new Paragraph();
                            if(value > 0) {
                                String valueString = preferenceHelper.
                                        getDecimalFormat(category).format(value);

                                if(category == Event.Category.BloodSugar) {
                                    if (values[categoryPosition][hour] <
                                            preferenceHelper.getLimitHypoglycemia())
                                        paragraph = new Paragraph(valueString, fontBlue);
                                    else if (values[categoryPosition][hour] >
                                            preferenceHelper.getLimitHyperglycemia())
                                        paragraph = new Paragraph(valueString, fontRed);
                                    else
                                        paragraph = new Paragraph(valueString, fontBasis);
                                }
                            }

                            cell = new PdfPCell(paragraph);
                            cell.setBorder(0);
                            if(categoryPosition == selectedCategories.length-1)
                                cell.setBorder(Rectangle.BOTTOM);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell);
                        }
                    }

                    // Alternating row background
                    boolean b = true;
                    for(PdfPRow r: table.getRows()) {
                        for(PdfPCell c: r.getCells())
                            c.setBackgroundColor(b ? BaseColor.WHITE : new BaseColor(230,230,230));
                        b = !b;
                    }

                    document.add(table);
                    document.add(new Paragraph(" ", fontBasis));

                    publishProgress(context.getString(R.string.day) + " " + currentDay + "/" + totalDays);

                    // Next day
                    dateIteration.set(Calendar.DAY_OF_MONTH, dateIteration.get(Calendar.DAY_OF_MONTH) + 1);
                    currentDay++;
                }

                document.close();
            }
            catch (Exception ex) {
                //Log.e("DiaguardError", ex.getMessage());
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            progressDialog.dismiss();
            super.onPostExecute(file);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.export_progress));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... message) {
            progressDialog.setMessage(message[0]);
        }

        private void iTextGMetaData(Document document) {
            String subject = context.getResources().getString(R.string.app_name) + " " +
                    context.getResources().getString(R.string.export) + ": " +
                    preferenceHelper.getDateFormat().format(dateStart.getTime()) + " - " +
                    preferenceHelper.getDateFormat().format(dateEnd.getTime());
            document.addTitle(subject);
            document.addAuthor(context.getResources().getString(R.string.app_name));
            document.addCreator(context.getResources().getString(R.string.app_name));
        }

        private Paragraph getWeekBar(Calendar weekStart) {

            Paragraph paragraph = new Paragraph();

            // Week
            Chunk chunk = new Chunk(context.getString(R.string.calendarweek) + " " + weekStart.get(Calendar.WEEK_OF_YEAR));
            chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            paragraph.add(chunk);

            Calendar weekEnd = Calendar.getInstance();
            weekEnd.setTime(weekStart.getTime());
            weekEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            // Dates
            chunk = new Chunk("\n" + preferenceHelper.getDateFormat().format(weekStart.getTime()) + " - " +
                    preferenceHelper.getDateFormat().format(weekEnd.getTime()));
            chunk.setFont(FontFactory.getFont(FontFactory.HELVETICA, 9));
            paragraph.add(chunk);

            return paragraph;
        }
    }

    class HeaderFooter extends PdfPageEventHelper {
        int pageNumber;

        public void onOpenDocument(PdfWriter writer, Document document) {
        }

        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {
            pageNumber = 1;
        }

        public void onStartPage(PdfWriter writer, Document document) {
            pageNumber++;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("Header");

            Calendar today = Calendar.getInstance();
            String stamp = context.getString(R.string.export_stamp) + " " +
                    preferenceHelper.getDateFormat().format(today.getTime());
            Chunk chunk = new Chunk(stamp,
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(chunk),
                    rect.getLeft(), rect.getBottom() - 18, 0);

            chunk = new Chunk(context.getString(R.string.app_facebook),
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(chunk),
                    rect.getRight(), rect.getBottom() - 18, 0);
        }
    }
}
