package com.faltenreich.diaguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.faltenreich.diaguard.adapters.SwipeDismissTouchListener;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.HbA1c;
import com.faltenreich.diaguard.database.measurements.Insulin;
import com.faltenreich.diaguard.database.measurements.Meal;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.database.measurements.Pressure;
import com.faltenreich.diaguard.database.measurements.Pulse;
import com.faltenreich.diaguard.database.measurements.Weight;
import com.faltenreich.diaguard.fragments.DatePickerFragment;
import com.faltenreich.diaguard.fragments.TimePickerFragment;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.Validator;
import com.faltenreich.diaguard.helpers.ViewHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends BaseActivity {

    public static final String EXTRA_ENTRY = "Entry";
    public static final String EXTRA_MEASUREMENT = "Measurement";
    public static final String EXTRA_DATE = "Date";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;

    private Entry entry;
    private int alarmIntervalInMinutes;

    private DateTime time;

    private FloatingActionMenu fab;
    private LinearLayout layoutValues;
    private EditText editTextNotes;
    private Button buttonDate;
    private Button buttonTime;
    private Spinner spinnerAlarm;

    private LinkedHashMap<Measurement.Category, Boolean> categories;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formular, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(this);
        preferenceHelper = new PreferenceHelper(this);
        time = new DateTime();

        categories = new LinkedHashMap<>();
        Measurement.Category[] activeCategories = preferenceHelper.getActiveCategories();
        for(Measurement.Category category : activeCategories) {
            categories.put(category, false);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(getString(R.string.entry_new));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setSupportActionBar(toolbar);
        }

        getComponents();
        checkIntents();
        setDate();
        setTime();
        setFloatingActionMenu();

        alarmIntervalInMinutes = 0;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(adapter);
        spinnerAlarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmIntervalInMinutes = getResources().getIntArray(R.array.alarm_intervals_values)[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getComponents() {
        fab = (FloatingActionMenu) findViewById(R.id.fab_menu);
        layoutValues = (LinearLayout) findViewById(R.id.layout_measurements);
        editTextNotes = (EditText) findViewById(R.id.edittext_notes);
        buttonDate = (Button) findViewById(R.id.button_date);
        buttonTime = (Button) findViewById(R.id.button_time);
        spinnerAlarm = (Spinner) findViewById(R.id.spinner_alarm);
    }

    private void checkIntents() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getLong(EXTRA_ENTRY) != 0L || extras.getLong(EXTRA_MEASUREMENT) != 0L) {

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setTitle(getString(R.string.entry_edit));
                }

                dataSource.open();

                // Get entry
                if(extras.getLong(EXTRA_ENTRY) != 0L) {
                    entry = (Entry) dataSource.get(DatabaseHelper.ENTRY, extras.getLong(EXTRA_ENTRY));
                }
                else {
                    Measurement measurement = (Measurement)dataSource.get(DatabaseHelper.MEASUREMENT, extras.getLong("ID"));
                    entry = (Entry)dataSource.get(DatabaseHelper.ENTRY, measurement.getEntryId());
                }

                // and all of its measurements
                List<Model> measurements = dataSource.get(DatabaseHelper.MEASUREMENT, null,
                        DatabaseHelper.ENTRY_ID + "=?", new String[]{ Long.toString(entry.getId()) },
                        null, null, null, null);
                dataSource.close();

                time = entry.getDate();
                editTextNotes.setText(entry.getNote());

                for(Model model : measurements) {
                    Measurement measurement = (Measurement) model;
                    // TODO entry.getMeasurements().add(measurement);
                    for(int position = 0; position < layoutValues.getChildCount(); position++) {
                        View view = layoutValues.getChildAt(position);
                        Measurement.Category category = (Measurement.Category)view.getTag();
                        /*
                        if(category == measurement.getCategory()) {
                            EditText editTextValue = (EditText) view.findViewById(R.id.value);
                            float customValue = preferenceHelper.formatDefaultToCustomUnit(category, measurement.getValue());
                            editTextValue.setText(Helper.getDecimalFormat().format(customValue));
                        }
                        */
                    }
                }
            }
            else if(extras.getSerializable(EXTRA_DATE) != null) {
                time = (DateTime) extras.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void setFloatingActionMenu() {
        // Show categories as FAB
        int numberOfVisibleButtons = 0;
        for(final Measurement.Category category : categories.keySet()) {
            if(!categories.get(category)) {
                final FloatingActionButton fabCategory = getFloatingActionButton(preferenceHelper.getCategoryName(category),
                        getResources().getIdentifier(category.name().toLowerCase() + "_white", "drawable", getPackageName()),
                        preferenceHelper.getCategoryColorResourceId(category));
                fabCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fab.close(true);
                        addViewForCategory(category);
                    }
                });
                fab.addMenuButton(fabCategory);
            }
            numberOfVisibleButtons++;

            // Show at most three buttons
            if(numberOfVisibleButtons == 3) {
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
        floatingActionButton.setColorPressed(Helper.colorDarken(getResources().getColor(colorResId)));
        floatingActionButton.setColorRipple(Helper.colorDarken(getResources().getColor(colorResId)));
        return floatingActionButton;
    }

    private void showDialogCategories() {
        final Measurement.Category[] activeCategories = preferenceHelper.getActiveCategories();
        String[] categoryNames = new String[activeCategories.length];
        for(int position = 0; position < activeCategories.length; position++) {
            categoryNames[position] = preferenceHelper.getCategoryName(activeCategories[position]);
        }

        // Store old values
        final Boolean[] visibleCategories = categories.values().toArray(new Boolean[categories.size()]);
        // TODO: Avoid parsing to array of primitives
        boolean[] visibleCategoriesAsPrimitiveArray = new boolean[visibleCategories.length];
        for(int position = 0; position < visibleCategories.length; position++) {
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
                        int position = 0;
                        for (Measurement.Category category : categories.keySet()) {
                            // Value was false and is now true -> Add new measurement
                            if (!categories.get(category) && visibleCategories[position]) {
                                addViewForCategory(activeCategories[position]);
                            }
                            // Value was true and is now false -> Remove old measurement
                            else if (categories.get(category) && !visibleCategories[position]) {
                                removeViewForCategory(activeCategories[position]);
                            }
                            position++;
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

    private void setDate() {
        buttonDate.setText(preferenceHelper.getDateFormat().print(time));
    }

    private void setTime() {
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void addViewForCategory(final Measurement.Category category) {
        // Return if category is already active
        if(categories.get(category) || viewForCategoryIsVisible(category)) {
            return;
        }

        categories.put(category, true);

        // Add view
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.cardview_entry, layoutValues, false);
        view.setTag(category);

        // Category image
        ImageView imageViewCategory = (ImageView) view.findViewById(R.id.image_category);
        imageViewCategory.setImageResource(preferenceHelper.getCategoryImageWhiteResourceId(category));

        // Category name
        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        textViewCategory.setText(preferenceHelper.getCategoryName(category));

        // Delete button
        View buttonDelete = view.findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeViewForCategory(category);
            }
        });

        // Measurement
        ViewGroup layoutMeasurement = (ViewGroup) view.findViewById(R.id.layout_content);
        View viewContent;
        switch(category) {
            // TODO: Get rid of switch-case by making it more generic
            case Insulin:
                viewContent = inflater.inflate(R.layout.cardview_entry_insulin, layoutValues, false);
                EditText editTextBolus = (EditText) viewContent.findViewById(R.id.value);
                editTextBolus.setHint(preferenceHelper.getUnitAcronym(category));
                editTextBolus.requestFocus();
                EditText editTextCorrection = (EditText) viewContent.findViewById(R.id.value_correction);
                editTextCorrection.setHint(preferenceHelper.getUnitAcronym(category));
                EditText editTextBasal = (EditText) viewContent.findViewById(R.id.value_basal);
                editTextBasal.setHint(preferenceHelper.getUnitAcronym(category));
                break;
            case Meal:
                viewContent = inflater.inflate(R.layout.cardview_entry_meal, layoutValues, false);
                EditText editTextMeal = (EditText) viewContent.findViewById(R.id.value);
                editTextMeal.setHint(preferenceHelper.getUnitAcronym(category));
                editTextMeal.requestFocus();
                break;
            case Pressure:
                viewContent = inflater.inflate(R.layout.cardview_entry_pressure, layoutValues, false);
                EditText editTextSystolic = (EditText) viewContent.findViewById(R.id.value);
                editTextSystolic.setHint(preferenceHelper.getUnitAcronym(category));
                editTextSystolic.requestFocus();
                EditText editTextDiastolic = (EditText) viewContent.findViewById(R.id.value_diastolic);
                editTextDiastolic.setHint(preferenceHelper.getUnitAcronym(category));
                break;
            default:
                viewContent = inflater.inflate(R.layout.cardview_entry_generic, layoutValues, false);
                EditText editTextValue = (EditText) viewContent.findViewById(R.id.value);
                editTextValue.setHint(preferenceHelper.getUnitAcronym(category));
                editTextValue.requestFocus();
                break;
        }
        layoutMeasurement.addView(viewContent);

        // Swipe to dismiss
        view.setOnTouchListener(new SwipeDismissTouchListener(
                view,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }
                    @Override
                    public void onDismiss(View view, Object token) {
                        removeViewForCategory(category);
                    }
                }));

        categories.put(category, true);
        layoutValues.addView(view, 0);
    }

    private void removeViewForCategory(Measurement.Category category) {
        // Return if category is not yet active
        if(!categories.get(category) && !viewForCategoryIsVisible(category)) {
            return;
        }

        categories.put(category, false);

        for(int position = 0; position < layoutValues.getChildCount(); position++) {
            View childView = layoutValues.getChildAt(position);
            if(childView.getTag() == category) {
                layoutValues.removeView(childView);
            }
        }
    }

    private boolean viewForCategoryIsVisible(Measurement.Category category) {
        for(int position = 0; position < layoutValues.getChildCount(); position++) {
            View childView = layoutValues.getChildAt(position);
            if(childView.getTag() == category) {
                return true;
            }
        }
        return false;
    }

    private void submit() {
        boolean inputIsValid = true;

        // Validate date
        DateTime now = new DateTime();
        if (time.isAfter(now)) {
            ViewHelper.showAlert(this, getString(R.string.validator_value_infuture));
            return;
        }

        List<Measurement> measurements = new ArrayList<>();
        // Iterate through all views and validate
        for (int position = 0; position < layoutValues.getChildCount(); position++) {
            View view = layoutValues.getChildAt(position);
            if(view != null && view.getTag() != null) {
                if(view.getTag() instanceof Measurement.Category) {
                    Measurement.Category category = (Measurement.Category) view.getTag();
                    EditText editTextValue = (EditText) view.findViewById(R.id.value);
                    if(validateValue(category, editTextValue)) {
                        editTextValue.setError(null);
                        float value = preferenceHelper.formatCustomToDefaultUnit(
                                category,
                                Float.parseFloat(editTextValue.getText().toString()));
                        switch (category) {
                            case BloodSugar:
                                BloodSugar bloodSugar = new BloodSugar();
                                bloodSugar.setMgDl(value);
                                measurements.add(bloodSugar);
                                break;
                            case Insulin:
                                Insulin insulin = new Insulin();
                                insulin.setBolus(value);
                                measurements.add(insulin);
                                break;
                            case Meal:
                                Meal meal = new Meal();
                                meal.setCarbohydrates(value);
                                measurements.add(meal);
                                break;
                            case Activity:
                                com.faltenreich.diaguard.database.measurements.Activity activity =
                                        new com.faltenreich.diaguard.database.measurements.Activity();
                                activity.setMinutes((int) value);
                                measurements.add(activity);
                                break;
                            case HbA1c:
                                HbA1c hbA1c = new HbA1c();
                                hbA1c.setPercent(value);
                                measurements.add(hbA1c);
                                break;
                            case Weight:
                                Weight weight = new Weight();
                                weight.setKilogram(value);
                                measurements.add(weight);
                                break;
                            case Pulse:
                                Pulse pulse = new Pulse();
                                pulse.setFrequency(value);
                                measurements.add(pulse);
                                break;
                            case Pressure:
                                Pressure pressure = new Pressure();
                                pressure.setSystolic(value);
                                measurements.add(pressure);
                                break;
                            default:
                                inputIsValid = false;
                                // TODO: Throw Exception
                                break;
                        }
                    }
                    else {
                        inputIsValid = false;
                    }
                }
            }
        }

        // Check whether there are values to submit
        if(measurements.size() == 0) {
            // Show alert only if everything else was valid to reduce clutter
            if(inputIsValid)
                ViewHelper.showAlert(this, getString(R.string.validator_value_none));
            inputIsValid = false;
        }

        if(inputIsValid) {
            dataSource.open();

            // Update existing entry
            if(entry != null) {
                entry.setDate(time);
                entry.setNote(editTextNotes.getText().toString());
                dataSource.update(entry);

                // TODO
                /*
                // Step through measurements and compare
                List<Measurement> measurementsToDelete = new ArrayList<Measurement>(entry.getMeasurements());
                for(Measurement measurement : measurements) {
                    // Case 1: Measurement is new and old --> Update
                    boolean updatedExistingMeasurement = false;
                    for(Measurement oldMeasurement : entry.getMeasurements()) {
                        if (measurement.getCategory() == oldMeasurement.getCategory()) {
                            oldMeasurement.setValue(measurement.getValue());
                            updatedExistingMeasurement = true;
                            measurementsToDelete.remove(oldMeasurement);
                            dataSource.update(oldMeasurement);
                        }
                    }
                    // Case 2: Measurement is new but not old --> Insert
                    if(!updatedExistingMeasurement) {
                        measurement.setEntryId(entry.getId());
                        dataSource.insert(measurement);
                    }
                }
                // Case 3: Measurement is old but not new --> Delete
                for(Measurement measurement : measurementsToDelete) {
                    dataSource.delete(measurement);
                }
                    */
            }

            // Insert new entry
            else {
                entry = new Entry();
                entry.setDate(time);
                if(editTextNotes.length() > 0)
                    entry.setNote(editTextNotes.getText().toString());
                long entryId = dataSource.insert(entry);

                // Connect measurements with entry
                for(Measurement measurement : measurements) {
                    measurement.setEntryId(entryId);
                    dataSource.insert(measurement);
                }
            }
            dataSource.close();

            if(alarmIntervalInMinutes > 0) {
                Helper.setAlarm(this, alarmIntervalInMinutes);
            }

            // Tell MainActivity that Events have been created
            Intent intent = new Intent();
            intent.putExtra(MainActivity.ENTRY_CREATED, measurements.size());
            setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    private boolean validateValue(Measurement.Category category, EditText editText) {
        String value = editText.getText().toString();
        if (value.length() == 0 || !Validator.containsNumber(value)) {
            editText.setError(getString(R.string.validator_value_empty));
            return false;
        }
        else if (!preferenceHelper.validateEventValue(
                category, preferenceHelper.formatCustomToDefaultUnit(category,
                        Float.parseFloat(value)))) {
            editText.setError(getString(R.string.validator_value_unrealistic));
            return false;
        }
        return true;
    }


    // LISTENERS

    public void onClickShowDatePicker (View view) {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                setDate();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void onClickShowTimePicker (View view) {
        DialogFragment fragment = new TimePickerFragment() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                setTime();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TimePickerFragment.TIME, time);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "TimePicker");
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
}