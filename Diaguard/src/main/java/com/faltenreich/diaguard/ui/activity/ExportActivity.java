package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.fragments.DatePickerFragment;
import com.faltenreich.diaguard.util.export.PdfExport;
import com.faltenreich.diaguard.util.FileHelper;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.IFileListener;
import com.faltenreich.diaguard.util.ViewHelper;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Bind(R.id.button_categories)
    protected Button buttonCategories;

    @Bind(R.id.checkbox_mail)
    protected CheckBox checkBoxMail;

    private static final Measurement.Category[] defaultCategories = new Measurement.Category[]{
            Measurement.Category.BLOODSUGAR,
            Measurement.Category.INSULIN,
            Measurement.Category.MEAL,
            Measurement.Category.ACTIVITY};

    private DateTime dateStart;
    private DateTime dateEnd;
    private LinkedHashMap<Measurement.Category, Boolean> categories;

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
        inflater.inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        dateEnd = DateTime.now();
        dateStart = dateEnd.withDayOfMonth(1);

        // TODO: Get user settings
        categories = new LinkedHashMap<>();
        List<Measurement.Category> preselectedCategories = Arrays.asList(defaultCategories);
        for (Measurement.Category category : Measurement.Category.values()) {
            categories.put(category, preselectedCategories.contains(category));
        }
    }

    public void initializeGUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.export));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }

        buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
        buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
        updateCategoryButton();
    }

    private boolean validate() {
        boolean isValid = true;

        if (dateStart.isAfter(dateEnd)) {
            // TODO ViewHelper.showAlert(this, getString(R.string.validator_value_enddate));
            isValid = false;
        }

        return isValid;
    }

    private Measurement.Category[] getSelectedCategories() {
        List<Measurement.Category> selectedCategories = new ArrayList<>();
        for (Map.Entry<Measurement.Category, Boolean> mapEntry : categories.entrySet()) {
            if (mapEntry.getValue()) {
                selectedCategories.add(mapEntry.getKey());
            }
        }
        return selectedCategories.toArray(new Measurement.Category[selectedCategories.size()]);
    }

    private void export() {
        if (validate()) {
            if (spinnerFormat.getSelectedItemPosition() == 0) {
                PdfExport pdfExport = new PdfExport(this);
                pdfExport.exportPDF(this, dateStart, dateEnd, getSelectedCategories());
            } else if (spinnerFormat.getSelectedItemPosition() == 1) {
                FileHelper fileHelper = new FileHelper(this);
                fileHelper.exportCSV(this);
            }
        }
    }

    @Override
    public void handleFile(File file, String mimeType) {
        if (file == null) {
            ViewHelper.showSnackbar(rootView, getString(R.string.error_sd_card));
        } else {
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
        String subject = getString(R.string.app_name) + " " + getString(R.string.export) + ": " +
                Helper.getDateFormat().print(dateStart) + " - " + Helper.getDateFormat().print(dateEnd);
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

    private void updateCategoryButton() {
        int selectedCategoriesCount = 0;
        for (Map.Entry<Measurement.Category, Boolean> mapEntry : categories.entrySet()) {
            if (mapEntry.getValue()) {
                selectedCategoriesCount++;
            }
        }
        buttonCategories.setText(String.format("%d", selectedCategoriesCount));
    }

    // LISTENERS

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

    @SuppressWarnings("unused")
    @OnClick(R.id.button_categories)
    public void showCategoryDialog() {

        String[] categoryNames = new String[categories.size()];
        final boolean[] selectedCategories = new boolean[categories.size()];

        int position = 0;
        for (Map.Entry<Measurement.Category, Boolean> mapEntry : categories.entrySet()) {
            categoryNames[position] = mapEntry.getKey().toString();
            selectedCategories[position] = mapEntry.getValue();
            position++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
                .setMultiChoiceItems(
                        categoryNames,
                        selectedCategories,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selectedCategories[which] = isChecked;
                            }
                        }
                )
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = 0;
                        for (Measurement.Category category : categories.keySet()) {
                            categories.put(category, selectedCategories[position]);
                        }
                        updateCategoryButton();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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