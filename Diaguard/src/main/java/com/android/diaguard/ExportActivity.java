package com.android.diaguard;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.fragments.DatePickerFragment;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.ViewHelper;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Filip on 30.11.13.
 */
public class ExportActivity extends ActionBarActivity {

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;

    private Calendar dateStart;
    private Calendar dateEnd;
    SimpleDateFormat dateFormat;

    Spinner spinnerFormat;
    private Button buttonDateStart;
    private Button buttonDateEnd;
    private CheckBox checkBoxMail;

    private Event.Category[] selectedCategories;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.export));
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {

        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);

        dateEnd = Calendar.getInstance();
        dateStart = Calendar.getInstance();
        dateStart.set(Calendar.DAY_OF_MONTH, 1);
        dateFormat = preferenceHelper.getDateFormat();

        selectedCategories = new Event.Category[] {Event.Category.BloodSugar, Event.Category.Bolus, Event.Category.Meal, Event.Category.Activity};

        getComponents();
        initializeGUI();
    }

    public void getComponents() {
        spinnerFormat = (Spinner) findViewById(R.id.spinner_format);
        buttonDateStart = (Button) findViewById(R.id.button_datestart);
        buttonDateEnd = (Button) findViewById(R.id.button_dateend);
        checkBoxMail = (CheckBox) findViewById(R.id.checkbox_mail);
    }

    public void initializeGUI() {
        buttonDateStart.setText(dateFormat.format(dateStart.getTime()));
        buttonDateEnd.setText(dateFormat.format(dateEnd.getTime()));
    }

    private boolean validate() {
        boolean isValid = true;

        if(dateStart.after(dateEnd)) {
            ViewHelper.showToastError(this, getString(R.string.validator_value_enddate));
            isValid = false;
        }

        return isValid;
    }

    private void export() {
        if(validate()) {
            if(spinnerFormat.getSelectedItem().toString().toLowerCase().equals("pdf")) {
                PDFExportTask pdfExportTask = new PDFExportTask();
                pdfExportTask.execute();
            }
        }
    }

    private void sendAttachment(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType(Helper.MIME_PDF);
        // Diaguard Export: DateStart - DateEnd
        SimpleDateFormat format = preferenceHelper.getDateFormat();
        String subject = getString(R.string.app_name) + " " + getString(R.string.export) + ": " +
                format.format(dateStart.getTime()) + " - " + format.format(dateEnd.getTime());
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.pref_data_export_mail_message));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ViewHelper.showToastError(this, getString(R.string.error_no_mail));
            Log.e("Send Mail", e.getMessage());
        }
    }

    private void openPDF(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), Helper.MIME_PDF);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ViewHelper.showToastError(this, getString(R.string.error_no_pdf));
            Log.e("Open PDF", e.getMessage());
        }
    }

    // LISTENERS

    public void onClickShowStartDatePicker (View view) {
        new DatePickerFragment(dateStart) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateStart.set(Calendar.YEAR, year);
                dateStart.set(Calendar.MONTH, month);
                dateStart.set(Calendar.DAY_OF_MONTH, day);
                buttonDateStart.setText(dateFormat.format(dateStart.getTime()));
            }
        }.show(getSupportFragmentManager(), "StartDatePicker");
    }

    public void onClickShowEndDatePicker (View view) {
        new DatePickerFragment(dateEnd) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateEnd.set(Calendar.YEAR, year);
                dateEnd.set(Calendar.MONTH, month);
                dateEnd.set(Calendar.DAY_OF_MONTH, day);
                buttonDateEnd.setText(dateFormat.format(dateEnd.getTime()));
            }
        }.show(getSupportFragmentManager(), "EndDatePicker");
    }

    // ASYNCTASKS

    private class PDFExportTask extends AsyncTask<Void, String, File> {

        ProgressDialog progressDialog;
        private final int TEXT_SIZE = 9;

        @Override
        protected File doInBackground(Void... params) {

            File directory = new File(Helper.PATH_STORAGE);
            if (directory != null)
                directory.mkdirs();

            File file = new File(directory + "/export" + new SimpleDateFormat("yyyyMMddHHmmss").
                    format(Calendar.getInstance().getTime()) + ".pdf");

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

                String[] weekDays = getResources().getStringArray(R.array.weekdays);

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
                                    if (value < preferenceHelper.getLimitHypoglycemia())
                                        paragraph = new Paragraph(valueString, fontBlue);
                                    else if (value > preferenceHelper.getLimitHyperglycemia())
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

                    publishProgress(getString(R.string.day) + " " + currentDay + "/" + totalDays);

                    // Next day
                    dateIteration.set(Calendar.DAY_OF_MONTH, dateIteration.get(Calendar.DAY_OF_MONTH) + 1);
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
        protected void onPostExecute(File file) {

            progressDialog.dismiss();

            if(checkBoxMail.isChecked())
                sendAttachment(file);
            else
                openPDF(file);

            finish();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ExportActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.export_progress));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... message) {
            progressDialog.setMessage(message[0]);
        }
    }

    private void iTextGMetaData(Document document) {
        String subject = getResources().getString(R.string.app_name) + " " +
                getResources().getString(R.string.export) + ": " +
                preferenceHelper.getDateFormat().format(dateStart.getTime()) + " - " +
                preferenceHelper.getDateFormat().format(dateEnd.getTime());
        document.addTitle(subject);
        document.addAuthor(getResources().getString(R.string.app_name));
        document.addCreator(getResources().getString(R.string.app_name));
    }

    private Paragraph getWeekBar(Calendar weekStart) {

        Paragraph paragraph = new Paragraph();

        // Week
        Chunk chunk = new Chunk(getString(R.string.calendarweek) + " " + weekStart.get(Calendar.WEEK_OF_YEAR));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                export();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            String stamp = getString(R.string.app_stamp) + " " +
                    preferenceHelper.getDateFormat().format(today.getTime());
            Chunk chunk = new Chunk(stamp,
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(chunk),
                    rect.getLeft(), rect.getBottom() - 18, 0);

            chunk = new Chunk(getString(R.string.app_website),
                    FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY));
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(chunk),
                    rect.getRight(), rect.getBottom() - 18, 0);
        }
    }
}