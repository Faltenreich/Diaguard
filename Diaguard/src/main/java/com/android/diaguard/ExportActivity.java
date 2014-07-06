package com.android.diaguard;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.android.diaguard.fragments.DatePickerFragment;
import com.android.diaguard.helpers.FileHelper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.ViewHelper;

import java.io.File;
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
            FileHelper fileHelper = new FileHelper(this);
            File createdFile = null;
            String mimeType = null;

            if(spinnerFormat.getSelectedItemPosition() == 0) {
                createdFile = fileHelper.exportPDF(dateStart, dateEnd);
                mimeType = FileHelper.MIME_PDF;
            }

            else if(spinnerFormat.getSelectedItemPosition() == 1) {
                createdFile = fileHelper.exportCSV();
                mimeType = FileHelper.MIME_CSV;
            }

            if(checkBoxMail.isChecked())
                sendAttachment(createdFile);
            else
                openFile(createdFile, mimeType);
        }
    }

    private void sendAttachment(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType(FileHelper.MIME_MAIL);

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

    private void openFile(File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ViewHelper.showToastError(this, getString(R.string.error_no_app));
            Log.e("Open " + mimeType, e.getMessage());
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
}