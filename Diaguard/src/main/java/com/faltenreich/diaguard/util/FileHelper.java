package com.faltenreich.diaguard.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
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
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Filip on 05.06.2014.
 */
public class FileHelper {

    public static final String PATH_EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String PATH_EXTERNAL_PARENT = Environment.getExternalStorageDirectory().getParent();
    public static final String PATH_STORAGE =  File.separator  + "Diaguard";

    public static final char DELIMITER = ';';
    public static final String KEY_META = "meta";

    public static final String MIME_MAIL = "message/rfc822";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_CSV = "text/csv";

    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getExternalStorageDirectory() {
        String path = null;

        if(new File(FileHelper.PATH_EXTERNAL).exists()) {
            path = PATH_EXTERNAL;
        }
        else if(new File(FileHelper.PATH_EXTERNAL_PARENT).exists()) {
            path = PATH_EXTERNAL_PARENT;
        }

        if(path == null) {
            return null;
        }

        return path + PATH_STORAGE;
    }

    public static File getExternalStorage() {
        if(!isExternalStorageWritable())
            return null;

        String path = getExternalStorageDirectory();

        if(path == null) {
            return null;
        }

        File file = new File(path);
        boolean fileCouldBeCreated = true;

        if(!file.exists())
            fileCouldBeCreated = file.mkdirs();

        if(!fileCouldBeCreated)
            return null;

        return file;
    }

    public void exportCSV(IFileListener listener) {
        CSVExportTask csvExportTask = new CSVExportTask(listener);
        csvExportTask.execute();
    }

    public void importCSV(String fileName) {
        CSVImportTask csvImportTask = new CSVImportTask();
        csvImportTask.execute(fileName);
    }

    public void exportPDF(IFileListener listener, DateTime dateStart, DateTime dateEnd) {
        PDFExportTask pdfExportTask = new PDFExportTask(listener, dateStart, dateEnd);
        pdfExportTask.execute();
    }

    private class CSVExportTask extends AsyncTask<Void, Void, File> {
        IFileListener listener;

        public CSVExportTask(IFileListener listener) {
            this.listener = listener;
        }

        @Override
        protected File doInBackground(Void... params) {
            File directory = FileHelper.getExternalStorage();
            if(directory == null)
                return null;

            File file = new File(directory.getAbsolutePath() + "/backup" + DateTimeFormat.forPattern("yyyyMMddHHmmss").
                    print(new DateTime()) + ".csv");

            try {
                FileWriter fileWriter = new FileWriter(file);
                CSVWriter writer = new CSVWriter(fileWriter, DELIMITER);

                // TODO
                        /*
                // Meta information to detect the data schema in future iterations
                String[] meta = new String[]{
                        KEY_META,
                        Integer.toString(dataSource.getVersion()) };
                writer.writeNext(meta);

                List<BaseEntity> entries = dataSource.get(DatabaseHelper.ENTRY);
                for(BaseEntity entryModel : entries) {
                    Entry entry = (Entry)entryModel;
                    String[] entryValues = {
                            DatabaseHelper.ENTRY,
                            Helper.getDateDatabaseFormat().print(entry.getDate()),
                            entry.getNote() };
                    writer.writeNext(entryValues);

                    List<BaseEntity> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                            DatabaseHelper.ENTRY + "=?",
                            new String[]{Long.toString(entry.getId())},
                            null, null, null, null);
                    for(BaseEntity measurementModel : measurements) {
                        Measurement measurement = (Measurement)measurementModel;
                        String[] measurementValues = {
                                DatabaseHelper.MEASUREMENT,
                                Float.toString(measurement.getValue()),
                                measurement.getCategory().name()
                        };
                        writer.writeNext(measurementValues);
                    }
                }
                        */

                writer.close();
            }
            catch (IOException ex) {
                //Log.e("DiaguardError", ex.getEntry());
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if(listener != null)
                listener.handleFile(file, MIME_CSV);
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
                String filePath = getExternalStorage().getAbsolutePath() + File.separator + params[0];
                CSVReader reader = new CSVReader(new FileReader(filePath), DELIMITER);

                // Read first line and check data version
                String[] nextLine = reader.readNext();

                // TODO
                        /*
                if(!nextLine[0].equals(KEY_META)) {
                    // First data version without meta information (17)
                    while (nextLine != null) {
                        // Entry
                        Entry entry = new Entry();
                        entry.setDate(nextLine[1]);
                        entry.setNote(nextLine[2]);
                        long entryId = dataSource.insert(entry);

                        // Measurement
                        Measurement measurement = new Measurement();
                        measurement.setValue(Float.parseFloat(nextLine[0]));
                        measurement.setCategory(Measurement.Category.valueOf(nextLine[3]));
                        measurement.setEntry(entryId);
                        dataSource.insert(measurement);

                        nextLine = reader.readNext();
                    }
                }

                // Database version > 17
                else {
                    int databaseVersion = Integer.parseInt(nextLine[1]);

                    // Migrate from old data version
                    if(databaseVersion < dataSource.getVersion()) {
                        // For future releases
                    }

                    long parentId = -1;
                    while ((nextLine = reader.readNext()) != null) {
                        String key = nextLine[0];
                        if (key.equals(DatabaseHelper.ENTRY)) {
                            Entry entry = new Entry();
                            entry.setDate(nextLine[1]);
                            entry.setNote(nextLine[2]);
                            parentId = dataSource.insert(entry);
                        }
                        else if(key.equals(DatabaseHelper.MEASUREMENT) && parentId != -1) {
                            // Measurement
                            // TODO
                            /*
                            Measurement measurement = new Measurement();
                            measurement.setValue(Float.parseFloat(nextLine[1]));
                            measurement.setCategory(Measurement.Category.valueOf(nextLine[2]));
                            measurement.setEntry(parentId);
                            dataSource.insert(measurement);
                        }
                    }
                }
                */

                reader.close();

            } catch (IOException ex) {
                //Log.e("DiaguardError", ex.getEntry());
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

    private class PDFExportTask extends AsyncTask<Void, String, File> {
        IFileListener listener;

        ProgressDialog progressDialog;
        private final int TEXT_SIZE = 9;

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

                DateTime dateIteration = dateStart;

                // One day after last chosen day
                DateTime dateAfter = dateEnd.plusDays(1);

                // Total number of days to export
                int totalDays = Days.daysBetween(dateStart, dateEnd).getDays() + 1;

                String[] weekDays = context.getResources().getStringArray(R.array.weekdays);

                document.add(getWeekBar(dateIteration));
                document.add(Chunk.NEWLINE);

                // Day by day
                int currentDay = 1;
                while(dateIteration.isBefore(dateAfter)) {

                    // title bar for new week
                    if(currentDay > 1 && dateIteration.getDayOfWeek() == 1) {
                        document.newPage();
                        document.add(getWeekBar(dateIteration));
                        document.add(Chunk.NEWLINE);
                    }

                    PdfPTable table = new PdfPTable(13);
                    table.setWidths(new float[]{3,1,1,1,1,1,1,1,1,1,1,1,1});
                    table.setWidthPercentage(100);

                    PdfPCell cell;

                    // Header
                    String weekDay = weekDays[dateIteration.getDayOfWeek()-1];
                    cell = new PdfPCell(new Phrase(weekDay.substring(0, 2) + " " +
                            DateTimeFormat.forPattern("dd.MM.").print(dateIteration),
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
                    /*TODO
                    float[][] values = dataSource.getAverageDataTable(dateIteration, selectedCategories, 12);

                    // Insert values into table
                    for(int categoryPosition = 0; categoryPosition < selectedCategories.length; categoryPosition++) {
                        Measurement.Category category = selectedCategories[categoryPosition];

                        cell = new PdfPCell(new Paragraph(PreferenceHelper.getInstance().getCategoryName(category), fontGray));
                        cell.setBorder(0);
                        if(categoryPosition == selectedCategories.length-1)
                            cell.setBorder(Rectangle.BOTTOM);
                        table.addCell(cell);

                        for(int hour = 0; hour < 12; hour++) {
                            float value = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category,
                                    values[categoryPosition][hour]);

                            Paragraph paragraph = new Paragraph();
                            if(value > 0) {
                                String valueString = PreferenceHelper.getInstance().
                                        getDecimalFormat(category).format(value);

                                paragraph = new Paragraph(valueString, fontBasis);
                                if(category == Measurement.Category.BLOODSUGAR) {
                                    if (values[categoryPosition][hour] <
                                            PreferenceHelper.getInstance().getLimitHypoglycemia())
                                        paragraph = new Paragraph(valueString, fontBlue);
                                    else if (values[categoryPosition][hour] >
                                            PreferenceHelper.getInstance().getLimitHyperglycemia())
                                        paragraph = new Paragraph(valueString, fontRed);
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
                        */

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
                    dateIteration = dateIteration.plusDays(1);
                    currentDay++;
                }

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
            if(listener != null)
                listener.handleFile(file, MIME_PDF);
        }

        private void iTextGMetaData(Document document) {
            String subject = context.getString(R.string.app_name) + " " +
                    context.getString(R.string.export) + ": " +
                    Helper.getDateFormat().print(dateStart) + " - " +
                    Helper.getDateFormat().print(dateEnd);
            document.addTitle(subject);
            document.addAuthor(context.getString(R.string.app_name));
            document.addCreator(context.getString(R.string.app_name));
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

        private Paragraph getInformationPage() {
            Paragraph paragraph = new Paragraph();

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

            DateTime today = new DateTime();
            String stamp = context.getString(R.string.export_stamp) + " " +
                    Helper.getDateFormat().print(today);
            Chunk chunk = new Chunk(stamp,
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(chunk),
                    rect.getLeft(), rect.getBottom() - 18, 0);

            chunk = new Chunk(context.getString(R.string.app_homepage_short),
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(chunk),
                    rect.getRight(), rect.getBottom() - 18, 0);
        }
    }
}
