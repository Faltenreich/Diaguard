package com.faltenreich.diaguard.ui.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.fragment.DatePickerFragment;
import com.faltenreich.diaguard.ui.view.CategoryCheckBoxList;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.faltenreich.diaguard.util.ViewHelper;

import org.joda.time.DateTime;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Filip on 30.11.13.
 */
public class ExportActivity extends BaseActivity implements IFileListener {

    @Bind(R.id.root)
    protected ViewGroup rootView;

    @Bind(R.id.spinner_format)
    protected Spinner spinnerFormat;

    @Bind(R.id.button_datestart)
    protected Button buttonDateStart;

    @Bind(R.id.button_dateend)
    protected Button buttonDateEnd;

    @Bind(R.id.export_list_categories)
    protected CategoryCheckBoxList categoryCheckBoxList;

    private ProgressDialog progressDialog;

    private DateTime dateStart;
    private DateTime dateEnd;

    public ExportActivity() {
        super(R.layout.activity_export);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeGUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
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

    public void initialize() {
        dateEnd = DateTime.now();
        dateStart = dateEnd.withDayOfMonth(1);
    }

    public void initializeGUI() {
        buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
        buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
        progressDialog = new ProgressDialog(this);
    }

    private boolean validate() {
        boolean isValid = true;

        if (dateStart.isAfter(dateEnd)) {
            ViewHelper.showSnackbar(rootView, getString(R.string.validator_value_enddate));
            isValid = false;
        } else if (categoryCheckBoxList.getSelectedCategories().length == 0) {
            ViewHelper.showSnackbar(rootView, getString(R.string.validator_value_empty_list));
            isValid = false;
        }

        return isValid;
    }

    private void export() {
        if (validate()) {
            progressDialog.setMessage(getString(R.string.export_progress));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (spinnerFormat.getSelectedItemPosition() == 0) {
                Export.exportPdf(this, dateStart, dateEnd, categoryCheckBoxList.getSelectedCategories());
            } else if (spinnerFormat.getSelectedItemPosition() == 1) {
                Export.exportCsv(this, false, dateStart, dateEnd, categoryCheckBoxList.getSelectedCategories());
            }
        }
    }

    @Override
    public void onProgress(String message) {
        progressDialog.setMessage(message);
    }

    @Override
    public void onComplete(File file, String mimeType) {
        progressDialog.dismiss();
        String confirmationText = String.format(getString(R.string.export_complete), file.getAbsolutePath());
        Toast.makeText(this, confirmationText, Toast.LENGTH_LONG).show();
        openFile(file, mimeType);
    }

    private void openFile(File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mimeType);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ViewHelper.showSnackbar(rootView, getString(R.string.error_no_app));
            Log.e("Open " + mimeType, e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_datestart)
    public void showStartDatePicker() {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateStart = dateStart.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, dateStart);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "StartDatePicker");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_dateend)
    public void showEndDatePicker() {
        DatePickerFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateEnd = dateEnd.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, dateEnd);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "EndDatePicker");
    }
}