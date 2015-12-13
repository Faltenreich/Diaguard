package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.fragment.DatePickerFragment;
import com.faltenreich.diaguard.ui.fragment.TimePickerFragment;
import com.faltenreich.diaguard.ui.view.entry.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.ui.view.entry.MeasurementListView;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewHelper;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Filip on 19.10.13.
 */
public class EntryActivity extends BaseActivity implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    public static final String EXTRA_ENTRY = "EXTRA_ENTRY";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    @Bind(R.id.activity_newevent_scrollview)
    protected ScrollView scrollView;

    @Bind(R.id.fab_menu)
    protected MeasurementFloatingActionMenu fab;

    @Bind(R.id.layout_measurements)
    protected MeasurementListView layoutMeasurements;

    @Bind(R.id.edittext_notes)
    protected EditText editTextNotes;

    @Bind(R.id.button_date)
    protected Button buttonDate;

    @Bind(R.id.button_time)
    protected Button buttonTime;

    @Bind(R.id.spinner_alarm)
    protected Spinner spinnerAlarm;

    private Entry entry;
    private DateTime time;

    public EntryActivity() {
        super(R.layout.activity_entry);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        checkIntents();
        setDateTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entry != null);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                deleteEntry();
                return true;
            case R.id.action_done:
                submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        time = new DateTime();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(adapter);

        fab.init();
        fab.setOnFabSelectedListener(this);

        layoutMeasurements.setOnCategoryEventListener(this);
    }

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getLong(EXTRA_ENTRY) != 0L) {
                setTitle(getString(R.string.entry_edit));
                entry = EntryDao.getInstance().get(extras.getLong(EXTRA_ENTRY));

                time = entry.getDate();
                editTextNotes.setText(entry.getNote());

                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                layoutMeasurements.addMeasurements(measurements);
                for (Measurement measurement : measurements) {
                    fab.ignore(measurement.getCategory());
                }
                fab.restock();
            } else if (extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void showDialogCategories() {
        final Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();

        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Measurement.Category category = activeCategories[position];
            categoryNames[position] = category.toLocalizedString();
            visibleCategoriesOld[position] = layoutMeasurements.hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
                .setMultiChoiceItems(
                        categoryNames,
                        visibleCategoriesOld,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                visibleCategories[which] = isChecked;
                            }
                        }
                )
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int position = activeCategories.length - 1; position >= 0; position--) {
                            Measurement.Category category = activeCategories[position];
                            if (visibleCategories[position]) {
                                addMeasurementView(category);
                            } else {
                                removeMeasurementView(category);
                            }
                        }
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

    private void setDateTime() {
        buttonDate.setText(Helper.getDateFormat().print(time));
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void addMeasurementView(Measurement.Category category) {
        scrollView.smoothScrollTo(0, 0);
        layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(Measurement.Category category) {
        layoutMeasurements.removeMeasurement(category);
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        // Validate date
        if (time.isAfter(DateTime.now())) {
            ViewHelper.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_infuture));
            inputIsValid = false;
        }

        // Check whether there are values to submit
        else if (layoutMeasurements.getMeasurements().size() == 0) {
            ViewHelper.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_none));
            inputIsValid = false;
        } else {
            for (Measurement measurement : layoutMeasurements.getMeasurements()) {
                if (measurement == null) {
                    inputIsValid = false;
                }
            }
        }

        return inputIsValid;
    }

    private void submit() {
        if (inputIsValid()) {
            boolean isNewEntry = entry == null;
            if (isNewEntry) {
                entry = new Entry();
            }

            entry.setDate(time);
            entry.setNote(editTextNotes.length() > 0 ? editTextNotes.getText().toString() : null);
            EntryDao.getInstance().createOrUpdate(entry);

            for (Measurement.Category category : Measurement.Category.values()) {
                if (layoutMeasurements.hasCategory(category)) {
                    Measurement measurement = layoutMeasurements.getMeasurement(category);
                    measurement.setEntry(entry);
                    MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
                } else {
                    MeasurementDao.getInstance(category.toClass()).deleteMeasurements(entry);
                }
            }

            if (spinnerAlarm.getSelectedItemPosition() > 0) {
                Helper.setAlarm(this, getResources().getIntArray(R.array.alarm_intervals_values)[spinnerAlarm.getSelectedItemPosition()]);
            }

            if (isNewEntry) {
                Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }

    private void deleteEntry() {
        if (entry != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.entry_delete);
            builder.setMessage(R.string.entry_delete_desc);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EntryDao.getInstance().delete(entry);
                    Toast.makeText(EntryActivity.this, getString(R.string.entry_deleted), Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_date)
    public void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                setDateTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.button_time)
    public void showTimePicker() {
        DialogFragment fragment = new TimePickerFragment() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                setDateTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TimePickerFragment.TIME, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @Override
    public void onCategorySelected(@Nullable Measurement.Category category) {
        addMeasurementView(category);
    }

    @Override
    public void onMiscellaneousSelected() {
        showDialogCategories();
    }

    @Override
    public void onCategoryAdded(Measurement.Category category) {
        fab.ignore(category);
        fab.restock();
    }

    @Override
    public void onCategoryRemoved(Measurement.Category category) {
        fab.removeIgnore(category);
        fab.restock();
    }
}