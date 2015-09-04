package com.faltenreich.diaguard.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.fragments.DatePickerFragment;
import com.faltenreich.diaguard.util.FileHelper;
import com.faltenreich.diaguard.util.IFileListener;
import com.faltenreich.diaguard.data.PreferenceHelper;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

import butterknife.Bind;

/**
 * Created by Filip on 30.11.13.
 */
public class ExportActivity extends BaseActivity implements IFileListener {

    private DateTime dateStart;
    private DateTime dateEnd;
    private DateTimeFormatter dateFormat;

    @Bind(R.id.spinner_format)
    protected Spinner spinnerFormat;

    @Bind(R.id.button_datestart)
    protected Button buttonDateStart;

    @Bind(R.id.button_dateend)
    protected Button buttonDateEnd;

    @Bind(R.id.checkbox_mail)
    protected CheckBox checkBoxMail;

    public ExportActivity() {
        super(R.layout.activity_export);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        dateEnd = new DateTime();
        dateStart = dateEnd.withDayOfMonth(1);
        dateFormat = PreferenceHelper.getInstance().getDateFormat();

        initializeGUI();
    }

    public void initializeGUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.export));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }

        buttonDateStart.setText(dateFormat.print(dateStart));
        buttonDateEnd.setText(dateFormat.print(dateEnd));
    }

    private boolean validate() {
        boolean isValid = true;

        if(dateStart.isAfter(dateEnd)) {
            // TODO ViewHelper.showAlert(this, getString(R.string.validator_value_enddate));
            isValid = false;
        }

        return isValid;
    }

    private void export() {
        if(validate()) {
            FileHelper fileHelper = new FileHelper(this);
            if(spinnerFormat.getSelectedItemPosition() == 0) {
                fileHelper.exportPDF(this, dateStart, dateEnd);
            }
            else if(spinnerFormat.getSelectedItemPosition() == 1) {
                fileHelper.exportCSV(this);
            }
        }
    }

    @Override
    // Callback method from IFileListener
    public void handleFile(File file, String mimeType) {
        if(file == null) {
            // TODO ViewHelper.showAlert(this, getString(R.string.error_sd_card));
        }
        else {
            if (checkBoxMail.isChecked()) {
                sendAttachment(file);
            } else {
                openFile(file, mimeType);
            }
        }
    }

    private void openFile(File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO ViewHelper.showAlert(this, getString(R.string.error_no_app));
            Log.e("Open " + mimeType, e.getMessage());
        }
    }

    private void sendAttachment(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType(FileHelper.MIME_MAIL);

        // Diaguard Export: DateStart - DateEnd
        DateTimeFormatter format = PreferenceHelper.getInstance().getDateFormat();
        String subject = getString(R.string.app_name) + " " + getString(R.string.export) + ": " +
                format.print(dateStart) + " - " + format.print(dateEnd);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.pref_data_export_mail_message));

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO ViewHelper.showAlert(this, getString(R.string.error_no_mail));
            Log.e("Send Mail", e.getMessage());
        }
    }

    // LISTENERS

    public void onClickShowStartDatePicker (View view) {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateStart = dateStart.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                buttonDateStart.setText(dateFormat.print(dateStart));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, dateStart);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "StartDatePicker");
    }

    public void onClickShowEndDatePicker (View view) {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateEnd = dateEnd.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                buttonDateEnd.setText(dateFormat.print(dateEnd));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, dateEnd);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "EndDatePicker");
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