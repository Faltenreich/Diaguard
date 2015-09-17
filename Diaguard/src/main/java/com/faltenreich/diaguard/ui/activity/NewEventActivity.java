package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SwipeDismissTouchListener;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Food;
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
import com.faltenreich.diaguard.util.FileHelper;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.Validator;
import com.faltenreich.diaguard.util.ViewHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Filip on 19.10.13.
 */
public class NewEventActivity extends BaseActivity {

    private static final int ACTION_REQUEST_GALLERY = 98;
    private static final int ACTION_REQUEST_CAMERA = 99;

    public static final String EXTRA_ENTRY = "EXTRA_ENTRY";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    @Bind(R.id.fab_menu)
    protected FloatingActionMenu fab;

    @Bind(R.id.layout_measurements)
    protected LinearLayout layoutMeasurements;

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

    public void initialize() {
        time = new DateTime();

        categories = new LinkedHashMap<>();
        Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        for (Measurement.Category category : activeCategories) {
            categories.put(category, false);
        }

        checkIntents();
        setFloatingActionMenu();

        setDate();
        setTime();

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
                        addViewForCategory(category);
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
        int colorDarken = Helper.colorDarken(getResources().getColor(colorResId));
        floatingActionButton.setColorPressed(colorDarken);
        floatingActionButton.setColorRipple(colorDarken);
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
                                addViewForCategory(activeCategories[position]);
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

    private void setDate() {
        buttonDate.setText(PreferenceHelper.getInstance().getDateFormat().print(time));
    }

    private void setTime() {
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void addViewForCategory(final Measurement.Category category) {
        // Return if category is already active
        if (categories.get(category) || viewForCategoryIsVisible(category)) {
            return;
        }

        categories.put(category, true);

        // Add view
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_measurement, layoutMeasurements, false);
        view.setTag(category);

        // Showcase images and colors
        ImageView imageViewShowcase = (ImageView) view.findViewById(R.id.image_showcase);
        imageViewShowcase.setImageResource(PreferenceHelper.getInstance().getShowcaseImageResourceId(category));
        View layerShowcase = view.findViewById(R.id.layer_showcase);
        layerShowcase.setBackgroundColor(getResources().getColor(PreferenceHelper.getInstance().getCategoryColorResourceId(category)));

        // Category image
        ImageView imageViewCategory = (ImageView) view.findViewById(R.id.image_category);
        imageViewCategory.setImageResource(PreferenceHelper.getInstance().getCategoryImageResourceId(category));

        // Category name
        TextView textViewCategory = (TextView) view.findViewById(R.id.category);
        textViewCategory.setText(PreferenceHelper.getInstance().getCategoryName(category));

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
        switch (category) {
            // TODO: Get rid of switch-case by making it more generic
            case Insulin:
                viewContent = inflater.inflate(R.layout.list_item_measurement_insulin, layoutMeasurements, false);
                EditText editTextBolus = (EditText) viewContent.findViewById(R.id.edittext_bolus);
                editTextBolus.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                editTextBolus.requestFocus();
                EditText editTextCorrection = (EditText) viewContent.findViewById(R.id.edittext_correction);
                editTextCorrection.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                EditText editTextBasal = (EditText) viewContent.findViewById(R.id.edittext_basal);
                editTextBasal.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                break;
            case Meal:
                viewContent = inflater.inflate(R.layout.list_item_measurement_meal, layoutMeasurements, false);
                EditText editTextMeal = (EditText) viewContent.findViewById(R.id.edittext_value);
                editTextMeal.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                editTextMeal.requestFocus();
                Button buttonImage = (Button) viewContent.findViewById(R.id.button_photo);
                buttonImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickShowImageDialog();
                    }
                });
                break;
            case Pressure:
                viewContent = inflater.inflate(R.layout.list_item_measurement_pressure, layoutMeasurements, false);
                EditText editTextSystolic = (EditText) viewContent.findViewById(R.id.edittext_value);
                editTextSystolic.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                editTextSystolic.requestFocus();
                EditText editTextDiastolic = (EditText) viewContent.findViewById(R.id.edittext_diastolic);
                editTextDiastolic.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
                break;
            default:
                viewContent = inflater.inflate(R.layout.list_item_measurement_generic, layoutMeasurements, false);
                EditText editTextValue = (EditText) viewContent.findViewById(R.id.edittext_value);
                editTextValue.setHint(PreferenceHelper.getInstance().getUnitAcronym(category));
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
        layoutMeasurements.addView(view, 0);

        // TODO: Refresh FAB to prevent showing already visible categories
    }

    private void removeViewForCategory(Measurement.Category category) {
        // Return if category is not yet active
        if (!categories.get(category) && !viewForCategoryIsVisible(category)) {
            return;
        }

        categories.put(category, false);

        for (int position = 0; position < layoutMeasurements.getChildCount(); position++) {
            View childView = layoutMeasurements.getChildAt(position);
            if (childView.getTag() == category) {
                layoutMeasurements.removeView(childView);
            }
        }
    }

    private boolean viewForCategoryIsVisible(Measurement.Category category) {
        for (int position = 0; position < layoutMeasurements.getChildCount(); position++) {
            View childView = layoutMeasurements.getChildAt(position);
            if (childView.getTag() == category) {
                return true;
            }
        }
        return false;
    }

    private void submit() {
        boolean inputIsValid = true;

        // Validate date
        DateTime now = DateTime.now();
        if (time.isAfter(now)) {
            ViewHelper.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_infuture));
            return;
        }

        Food food;

        List<Measurement> measurements = new ArrayList<>();
        // Iterate through all views and validate
        for (int position = 0; position < layoutMeasurements.getChildCount(); position++) {
            Measurement measurement = getMeasurementFromView(layoutMeasurements.getChildAt(position));
            if (measurement != null) {
                measurements.add(measurement);
            } else {
                inputIsValid = false;
            }
        }

        // Check whether there are values to submit
        if (measurements.size() == 0) {
            // Show alert only if everything else was valid to reduce clutter
            if (inputIsValid)
                ViewHelper.showSnackbar(findViewById(android.R.id.content), getString(R.string.validator_value_none));
            inputIsValid = false;
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

            for (Measurement measurement : measurements) {
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

    private Measurement getMeasurementFromView(View view) {
        Measurement measurement = null;

        if (view != null && view.getTag() != null) {
            if (view.getTag() instanceof Measurement.Category) {
                Measurement.Category category = (Measurement.Category) view.getTag();
                switch (category) {
                    case Insulin:
                        measurement = getInsulinFromView(view);
                        break;
                    case Meal:
                        measurement = getMealFromView(view);
                        break;
                    case Activity:
                        measurement = getActivityFromView(view);
                        break;
                    case Pressure:
                        measurement = getPressureFromView(view);
                        break;
                    default:
                        measurement = getGenericFromView(category, view);
                        break;
                }
            }
        }
        return measurement;
    }

    private Measurement getGenericFromView(Measurement.Category category, View view) {
        Measurement measurement = null;

        EditText editText = (EditText) view.findViewById(R.id.edittext_value);
        editText.setError(null);
        if (validateEditText(editText)) {
            float value = PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    category,
                    Float.parseFloat(editText.getText().toString()));
            if (validateValue(category, value)) {
                switch (category) {
                    case BloodSugar:
                        BloodSugar bloodSugar = new BloodSugar();
                        bloodSugar.setMgDl(value);
                        measurement = bloodSugar;
                        break;
                    case Meal:
                        Meal meal = new Meal();
                        meal.setCarbohydrates(value);
                        measurement = meal;
                        break;
                    case HbA1c:
                        HbA1c hbA1c = new HbA1c();
                        hbA1c.setPercent(value);
                        measurement = hbA1c;
                        break;
                    case Weight:
                        Weight weight = new Weight();
                        weight.setKilogram(value);
                        measurement = weight;
                        break;
                    case Pulse:
                        Pulse pulse = new Pulse();
                        pulse.setFrequency(value);
                        measurement = pulse;
                        break;
                    case Pressure:
                        Pressure pressure = new Pressure();
                        pressure.setSystolic(value);
                        measurement = pressure;
                    default:
                        // TODO: Throw Exception
                        break;
                }
            } else {
                editText.setError(getString(R.string.validator_value_unrealistic));
            }
        } else {
            editText.setError(getString(R.string.validator_value_empty));
        }

        return measurement;
    }

    private Insulin getInsulinFromView(View view) {
        Insulin insulin = new Insulin();

        EditText editTextBolus = (EditText) view.findViewById(R.id.edittext_bolus);
        editTextBolus.setError(null);
        if (validateEditText(editTextBolus)) {
            float bolus = PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    Measurement.Category.Insulin,
                    Float.parseFloat(editTextBolus.getText().toString()));
            if (validateValue(Measurement.Category.Insulin, bolus)) {
                insulin.setBolus(bolus);
            } else {
                editTextBolus.setError(getString(R.string.validator_value_unrealistic));
            }
        }

        EditText editTextCorrection = (EditText) view.findViewById(R.id.edittext_correction);
        editTextCorrection.setError(null);
        if (validateEditText(editTextCorrection)) {
            float correction = PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    Measurement.Category.Insulin,
                    Float.parseFloat(editTextCorrection.getText().toString()));
            if (validateValue(Measurement.Category.Insulin, correction)) {
                insulin.setCorrection(correction);
            } else {
                editTextCorrection.setError(getString(R.string.validator_value_unrealistic));
            }
        }

        EditText editTextBasal = (EditText) view.findViewById(R.id.edittext_basal);
        editTextBasal.setError(null);
        if (validateEditText(editTextBasal)) {
            float basal = PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    Measurement.Category.Insulin,
                    Float.parseFloat(editTextBasal.getText().toString()));
            if (validateValue(Measurement.Category.Insulin, basal)) {
                insulin.setBasal(basal);
            } else {
                editTextBasal.setError(getString(R.string.validator_value_unrealistic));
            }
        }

        if (insulin.getBolus() == 0 && insulin.getCorrection() == 0 && insulin.getBasal() == 0) {
            editTextBolus.setError(getString(R.string.validator_value_empty));
            return null;
        } else {
            return insulin;
        }
    }

    private Meal getMealFromView(View view) {
        Meal meal = (Meal) getGenericFromView(Measurement.Category.Meal, view);

        String foodName = ((EditText) view.findViewById(R.id.food)).getText().toString();
        if (foodName.length() > 0) {
            Food food = new Food();
            food.setName(foodName);
            ImageView imageViewFood = (ImageView) view.findViewById(R.id.image);
            // TODO: Check for image, convert to Base64 and add to Food
        }

        return meal;
    }

    private com.faltenreich.diaguard.data.entity.Activity getActivityFromView(View view) {
        com.faltenreich.diaguard.data.entity.Activity activity =
                (com.faltenreich.diaguard.data.entity.Activity)
                        getGenericFromView(Measurement.Category.Activity, view);

        // TODO: Type

        return activity;
    }

    private Pressure getPressureFromView(View view) {
        Pressure pressure = (Pressure) getGenericFromView(Measurement.Category.Pressure, view);

        EditText editTextDiastolic = (EditText) view.findViewById(R.id.edittext_diastolic);
        editTextDiastolic.setError(null);
        if (validateEditText(editTextDiastolic)) {
            float diastolic = PreferenceHelper.getInstance().formatCustomToDefaultUnit(
                    Measurement.Category.Pressure,
                    Float.parseFloat(editTextDiastolic.getText().toString()));
            if (validateValue(Measurement.Category.Pressure, diastolic)) {
                pressure.setDiastolic(diastolic);
                return pressure;
            } else {
                editTextDiastolic.setError(getString(R.string.validator_value_unrealistic));
                return null;
            }
        } else {
            editTextDiastolic.setError(getString(R.string.validator_value_empty));
            return null;
        }
    }

    private boolean validateEditText(EditText editText) {
        String value = editText.getText().toString();
        return value.length() >= 0 && Validator.containsNumber(value);
    }

    private boolean validateValue(Measurement.Category category, Float value) {
        return PreferenceHelper.getInstance().validateEventValue(category, value);
    }

    // LISTENERS

    public void onClickShowDatePicker(View view) {
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

    public void onClickShowTimePicker(View view) {
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

    public void onClickShowImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEventActivity.this);
        builder.setTitle(R.string.photo_desc);
        builder.setItems(R.array.photo_dialog,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
                                break;
                            case 1:

                                File directory = FileHelper.getExternalStorage();
                                if(directory == null) {
                                    return;
                                }
                                File file = new File(directory.getAbsolutePath() +
                                        "/photo" + DateTimeFormat.forPattern("yyyyMMddHHmmss").
                                        print(new DateTime()) + ".jpg");

                                Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
                                getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(getCameraImage, ACTION_REQUEST_CAMERA);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
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