package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Activity;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.HbA1c;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.data.entity.Pulse;
import com.faltenreich.diaguard.data.entity.Weight;
import com.faltenreich.diaguard.ui.fragments.DatePickerFragment;
import com.faltenreich.diaguard.ui.fragments.TimePickerFragment;
import com.faltenreich.diaguard.ui.view.MeasurementListView;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Validator;
import com.faltenreich.diaguard.util.ViewHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends BaseActivity {

    public static final String EXTRA_ENTRY = "EXTRA_ENTRY";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    @Bind(R.id.fab_menu)
    protected FloatingActionMenu fab;

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

    private boolean isNewEntry = true;

    private LinkedHashMap<Measurement.Category, Boolean> categories;

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

        categories = new LinkedHashMap<>();
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        for (Measurement.Category category : activeCategories) {
            categories.put(category, false);
        }

        checkIntents();
        setFloatingActionMenu();

        setDateTime();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(adapter);
    }

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getLong(EXTRA_ENTRY) != 0L) {
                getSupportActionBar().setTitle(getString(R.string.entry_edit));
                isNewEntry = false;

                /*
                // Get entry
                if (extras.getLong(EXTRA_ENTRY) != 0L) {
                    entry = (Entry) DatabaseFacade.getInstance().get(DatabaseHelper.ENTRY, extras.getLong(EXTRA_ENTRY));
                } else {
                    Measurement measurement = (Measurement) dataSource.get(DatabaseHelper.MEASUREMENT, extras.getLong("ID"));
                    entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, measurement.getEntry());
                }

                // and all of its measurements
                List<BaseEntity> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                        DatabaseHelper.ENTRY_ID + "=?", new String[]{Long.toString(entry.getId())},
                        null, null, null, null);

                time = entry.getDate();
                editTextNotes.setText(entry.getNote());

                for (BaseEntity baseEntity : measurements) {
                    Measurement measurement = (Measurement) baseEntity;
                    // TODO entry.getMeasurements().add(measurement);
                    for (int position = 0; position < layoutMeasurements.getChildCount(); position++) {
                        View view = layoutMeasurements.getChildAt(position);
                        Measurement.Category category = (Measurement.Category) view.getTag();
                        if(category == measurement.getCategory()) {
                            EditText editTextValue = (EditText) view.findViewById(R.id.value);
                            float customValue = preferenceHelper.formatDefaultToCustomUnit(category, measurement.getValue());
                            editTextValue.setText(Helper.getDecimalFormat().format(customValue));
                        }
                    }
                }
                */
            } else if (extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void setFloatingActionMenu() {
        // Show categories as FAB
        int numberOfVisibleButtons = 0;
        for (final Measurement.Category category : categories.keySet()) {
            if (!categories.get(category)) {
                final FloatingActionButton fabCategory = getFloatingActionButton(
                        PreferenceHelper.getInstance().getCategoryName(category),
                        PreferenceHelper.getInstance().getCategoryImageResourceId(category),
                        PreferenceHelper.getInstance().getCategoryColorResourceId(category));
                fabCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fab.close(true);
                        addMeasurementView(category);
                    }
                });
                fab.addMenuButton(fabCategory);
                numberOfVisibleButtons++;
            }

            // Show at most three buttons
            if (numberOfVisibleButtons == 3) {
                break;
            }
        }

        // FAB for all categories
        FloatingActionButton fabAll = getFloatingActionButton(getString(R.string.all),
                R.drawable.ic_other, android.R.color.white);
        fabAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.close(true);
                showDialogCategories();
            }
        });
        fab.addMenuButton(fabAll);

        // Close FAB on click outside
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fab.isOpened()) {
                    if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        fab.close(true);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private FloatingActionButton getFloatingActionButton(String text, int imageResourceId, int colorResId) {
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setButtonSize(FloatingActionButton.SIZE_MINI);
        floatingActionButton.setLabelText(text);
        floatingActionButton.setImageResource(imageResourceId);
        floatingActionButton.setColorNormalResId(colorResId);
        float brighteningPercentage = colorResId == android.R.color.white ? .9f : 1.2f;
        int colorHighlight = Helper.colorBrighten(ContextCompat.getColor(this, colorResId), brighteningPercentage);
        floatingActionButton.setColorPressed(colorHighlight);
        floatingActionButton.setColorRipple(colorHighlight);
        return floatingActionButton;
    }

    private void showDialogCategories() {
        final Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        String[] categoryNames = new String[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            categoryNames[position] = PreferenceHelper.getInstance().getCategoryName(activeCategories[position]);
        }

        // Store old values
        final Boolean[] visibleCategories = categories.values().toArray(new Boolean[categories.size()]);
        // TODO: Avoid parsing to array of primitives
        boolean[] visibleCategoriesAsPrimitiveArray = new boolean[visibleCategories.length];
        for (int position = 0; position < visibleCategories.length; position++) {
            visibleCategoriesAsPrimitiveArray[position] = visibleCategories[position];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
                .setMultiChoiceItems(
                        categoryNames,
                        visibleCategoriesAsPrimitiveArray,
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
                        Measurement.Category[] categoriesArray = categories.keySet().toArray(new Measurement.Category[categories.size()]);
                        for (int position = categoriesArray.length - 1; position >= 0; position--) {
                            Measurement.Category category = categoriesArray[position];
                            // Value was false and is now true -> Add new measurement
                            if (!categories.get(category) && visibleCategories[position]) {
                                addMeasurementView(activeCategories[position]);
                            }
                            // Value was true and is now false -> Remove old measurement
                            else if (categories.get(category) && !visibleCategories[position]) {
                                removeViewForCategory(activeCategories[position]);
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
        categories.put(category, true);
        layoutMeasurements.addMeasurement(category);
    }

    private void removeViewForCategory(Measurement.Category category) {
        categories.put(category, false);
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
            if (isNewEntry) {
                entry = new Entry();
                entry.setCreatedAt(now);
            } else {
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
            }

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
}