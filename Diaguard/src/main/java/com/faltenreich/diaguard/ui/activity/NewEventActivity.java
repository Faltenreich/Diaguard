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
import com.faltenreich.diaguard.ui.fragments.DatePickerFragment;
import com.faltenreich.diaguard.ui.fragments.TimePickerFragment;
import com.faltenreich.diaguard.ui.view.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.ui.view.MeasurementListView;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewHelper;
import com.github.clans.fab.FloatingActionButton;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends BaseActivity implements MeasurementFloatingActionMenu.MeasurementFloatingActionMenuCallback {

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

    private HashMap<Measurement.Category, FloatingActionButton> menuButtons;

    public NewEventActivity() {
        super(R.layout.activity_newevent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

        checkIntents();
        setDateTime();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(adapter);

        fab.init();
        fab.setMeasurementFloatingActionMenuCallback(this);
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
            categoryNames[position] = category.toString();
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
        buttonDate.setText(PreferenceHelper.getInstance().getDateFormat().print(time));
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void addMeasurementView(Measurement.Category category) {
        scrollView.smoothScrollTo(0, 0);
        layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(Measurement.Category category) {
        layoutMeasurements.removeMeasurement(category);
    }

    private void submit() {
        boolean inputIsValid = true;

        // Validate date
        DateTime now = DateTime.now();
        if (time.isAfter(now)) {
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

        if (inputIsValid) {
            if (entry == null) {
                entry = new Entry();
                entry.setCreatedAt(now);
            }

            /*
            // Step through measurements and compare
            List<Measurement> measurementsToDelete = new ArrayList<>(entry.getMeasurements());
            for(Measurement measurement : measurements) {
                // Case 1: Measurement is new and old --> Update
                boolean updatedExistingMeasurement = false;
                for (Measurement oldMeasurement : entry.getMeasurements()) {
                    if (measurement.getCategory() == oldMeasurement.getCategory()) {
                        oldMeasurement.setValue(measurement.getValue());
                        updatedExistingMeasurement = true;
                        measurementsToDelete.remove(oldMeasurement);
                        dataSource.update(oldMeasurement);
                    }
                }
                // Case 2: Measurement is new but not old --> Insert
                if(!updatedExistingMeasurement) {
                    measurement.setEntry(entry);
                    dataSource.insert(measurement);
                }
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
            }
            // Case 3: Measurement is old but not new --> Delete
            for(Measurement measurement : measurementsToDelete) {
                MeasurementDao.getInstance(measurement.getClass()).delete(measurement);
            }
            */

            entry.setUpdatedAt(now);
            entry.setDate(time);
            entry.setNote(editTextNotes.length() > 0 ? editTextNotes.getText().toString() : null);
            EntryDao.getInstance().createOrUpdate(entry);

            for (Measurement measurement : layoutMeasurements.getMeasurements()) {
                measurement.setCreatedAt(now);
                measurement.setUpdatedAt(now);
                measurement.setEntry(entry);
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
            }

            if (spinnerAlarm.getSelectedItemPosition() > 0) {
                Helper.setAlarm(this, getResources().getIntArray(R.array.alarm_intervals_values)[spinnerAlarm.getSelectedItemPosition()]);
            }

            Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
            finish();
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
}