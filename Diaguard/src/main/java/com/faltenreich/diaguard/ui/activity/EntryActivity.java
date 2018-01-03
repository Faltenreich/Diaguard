package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Food;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.ui.fragment.BaseFoodFragment;
import com.faltenreich.diaguard.ui.fragment.DatePickerFragment;
import com.faltenreich.diaguard.ui.fragment.TimePickerFragment;
import com.faltenreich.diaguard.ui.view.entry.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.ui.view.entry.MeasurementListView;
import com.faltenreich.diaguard.util.AlarmUtils;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.ViewUtils;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.model.Chip;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Filip on 19.10.13.
 */
public class EntryActivity extends BaseActivity implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    public static final String EXTRA_ENTRY_ID = "entryId";
    public static final String EXTRA_DATE = "date";

    public static void show(Context context) {
        Intent intent = new Intent(context, EntryActivity.class);
        context.startActivity(intent);
    }

    public static void show(Context context, Entry entry) {
        Intent intent = new Intent(context, EntryActivity.class);
        if (entry != null) {
            intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, Food food) {
        Intent intent = new Intent(context, EntryActivity.class);
        if (food != null) {
            intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, DateTime dateTime) {
        Intent intent = new Intent(context, EntryActivity.class);
        intent.putExtra(EXTRA_DATE, dateTime);
        context.startActivity(intent);
    }

    @BindView(android.R.id.content) View contentView;
    @BindView(R.id.activity_newevent_scrollview) NestedScrollView scrollView;
    @BindView(R.id.fab_menu) MeasurementFloatingActionMenu fab;
    @BindView(R.id.layout_measurements) MeasurementListView layoutMeasurements;
    @BindView(R.id.edittext_notes) EditText editTextNotes;
    @BindView(R.id.button_date) Button buttonDate;
    @BindView(R.id.button_time) Button buttonTime;
    @BindView(R.id.entry_button_alarm) Button buttonAlarm;
    @BindView(R.id.entry_tags_input) AutoCompleteTextView tagsInput;
    @BindView(R.id.entry_tags) ViewGroup tagsView;

    private long entryId;
    private long foodId;
    private DateTime time;

    private Entry entry;
    private List<EntryTag> entryTags;
    private Measurement.Category[] activeCategories;
    private int alarmInMinutes;

    public EntryActivity() {
        super(R.layout.activity_entry);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entryId > 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteEntry();
                return true;
            case R.id.action_done:submit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        time = new DateTime();
        activeCategories = PreferenceHelper.getInstance().getActiveCategories();

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            foodId = arguments.getLong(BaseFoodFragment.EXTRA_FOOD_ID);
            if (arguments.get(EXTRA_DATE) != null) {
                time = (DateTime) arguments.getSerializable(EXTRA_DATE);
            }
        }
    }

    private void initLayout() {
        layoutMeasurements.setOnCategoryEventListener(this);

        fab.init();
        fab.setOnFabSelectedListener(this);

        // TODO: Set ArrayAdapter for AutoCompleteTextView
        tagsInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    Tag tag = new Tag();
                    tag.setName(textView.getText().toString());
                    addTag(tag);
                    textView.setText(null);
                    return true;
                }
                return false;
            }
        });

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            new FetchEntryTask(entryId).execute();
        } else if (foodId > 0) {
            new FetchFoodTask(foodId).execute();
            updateDateTime();
        } else {
            initPinnedCategories();
            updateDateTime();
        }

        new FetchTagsTask().execute();

        updateAlarm();
    }

    private void initPinnedCategories() {
        for (Measurement.Category category : activeCategories) {
            if (PreferenceHelper.getInstance().isCategoryPinned(category) && !layoutMeasurements.hasCategory(category)) {
                layoutMeasurements.addMeasurementAtEnd(category);
            }
        }
    }

    private void initEntry(Entry entry, List<EntryTag> entryTags) {
        if (entry != null) {
            this.entry = entry;
            this.entryTags = entryTags;
            this.time = entry.getDate();

            editTextNotes.setText(entry.getNote());

            List<Measurement> measurements = entry.getMeasurementCache();
            layoutMeasurements.addMeasurements(measurements);

            for (Measurement measurement : measurements) {
                layoutMeasurements.addMeasurement(measurement);
                fab.ignore(measurement.getCategory());
            }
            fab.restock();

            for (EntryTag entryTag : entryTags) {
                addTag(entryTag.getTag());
            }

        } else {
            initPinnedCategories();
        }
        updateDateTime();
    }

    private void initFood(Food food) {
        if (food != null) {
            layoutMeasurements.addMeasurement(food);
            fab.ignore(Measurement.Category.MEAL);
            fab.restock();
        }
    }

    private void initTagSuggestions(List<Tag> tags) {
        List<Chip> chips = new ArrayList<>();
        for (Tag tag : tags) {
            chips.add(new Chip(tag, tag.getName(), null));
        }
        // TODO
    }

    private void addTag(Tag tag) {
        int margin = (int) getResources().getDimension(R.dimen.margin_between);
        final ChipView chipView = new ChipView(this);
        chipView.setTag(tag);
        chipView.setLabel(tag.getName());
        chipView.setDeletable(true);
        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagsView.removeView(chipView);
            }
        });
        chipView.setPadding(0, 0, margin, margin);
        chipView.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagsView.removeView(chipView);
            }
        });
        tagsView.addView(chipView);
    }

    private void showDialogCategories() {
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

    private void updateDateTime() {
        buttonDate.setText(Helper.getDateFormat().print(time));
        buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void updateAlarm() {
        buttonAlarm.setText(alarmInMinutes > 0 ?
                String.format("%s %s", getString(R.string.alarm_reminder_in), DateTimeUtils.parseInterval(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)) :
                getString(R.string.alarm_reminder_none));
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
            ViewUtils.showSnackbar(contentView, getString(R.string.validator_value_infuture));
            inputIsValid = false;
        }

        // Check whether there are values to submit
        else if (layoutMeasurements.getMeasurements().size() == 0) {
            ViewUtils.showSnackbar(contentView, getString(R.string.validator_value_none));
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
            DateTime originalDate = isNewEntry ? null : entry.getDate();
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

            if (alarmInMinutes > 0) {
                AlarmUtils.setAlarm(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE);
            }

            // TODO: Delete distinct
            if (entryTags != null && entryTags.size() > 0) {
                EntryTagDao.getInstance().delete(entryTags);
            }

            List<EntryTag> entryTags = new ArrayList<>();
            for (int index = 0; index < tagsView.getChildCount(); index++) {
                View view = tagsView.getChildAt(index);
                if (view.getTag() instanceof Tag) {
                    Tag tag = (Tag) view.getTag();
                    EntryTag entryTag = new EntryTag();
                    entryTag.setEntry(entry);
                    entryTag.setTag(tag);
                    entryTags.add(entryTag);
                }
            }
            EntryTagDao.getInstance().bulkCreateOrUpdate(entryTags);

            if (isNewEntry) {
                Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
                Events.post(new EntryAddedEvent(entry, entryTags));
            } else {
                Events.post(new EntryUpdatedEvent(entry, entryTags, originalDate));
            }

            finish();
        }
    }

    private void deleteEntry() {
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, entryTags));
            finish();
        }
    }

    @OnClick(R.id.button_date)
    public void showDatePicker() {
        DatePickerFragment.newInstance(time, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day);
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.button_time)
    public void showTimePicker() {
        TimePickerFragment.newInstance(time, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    @OnClick(R.id.entry_button_alarm)
    public void showAlarmPicker() {
        ViewUtils.showNumberPicker(this, R.string.minutes, alarmInMinutes, 0, 10000, new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
            @Override
            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                alarmInMinutes = number.intValue();
                updateAlarm();
            }
        });
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

    private class FetchEntryTask extends AsyncTask<Void, Void, Void> {

        private long entryId;
        private Entry entry;
        private List<EntryTag> entryTags;

        private FetchEntryTask(long entryId) {
            this.entryId = entryId;
        }

        @Override
        protected Void doInBackground(Void... params) {
            EntryDao dao = EntryDao.getInstance();
            entry = dao.get(entryId);
            if (entry != null) {
                entry.setMeasurementCache(dao.getMeasurements(entry));
                entryTags = EntryTagDao.getInstance().getAll(entry);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            initEntry(entry, entryTags);
        }
    }

    private class FetchFoodTask extends AsyncTask<Void, Void, Food> {

        private long foodId;

        private FetchFoodTask(long foodId) {
            this.foodId = foodId;
        }

        @Override
        protected Food doInBackground(Void... params) {
            return FoodDao.getInstance().get(foodId);
        }

        @Override
        protected void onPostExecute(Food food) {
            super.onPostExecute(food);
            initFood(food);
        }
    }

    private class FetchTagsTask extends AsyncTask<Void, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(Void... params) {
            return TagDao.getInstance().getAll();
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            initTagSuggestions(tags);
        }
    }
}