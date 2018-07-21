package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.TagAutoCompleteAdapter;
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
import com.faltenreich.diaguard.util.Vector2D;
import com.faltenreich.diaguard.util.ViewUtils;
import com.pchmn.materialchips.ChipView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EntryActivity extends BaseActivity implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    public static final String EXTRA_ENTRY_ID = "entryId";
    public static final String EXTRA_DATE = "date";

    private static Intent getIntent(Context context, @Nullable Vector2D source) {
        Intent intent = new Intent(context, EntryActivity.class);
        if (source != null) {
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_X, source.x);
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_Y, source.y);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        return intent;
    }

    private static Intent getIntent(Context context, @Nullable View view) {
        Vector2D position = view != null ? ViewUtils.getPositionOnScreen(view) : null;
        Vector2D source = position != null ? new Vector2D(position.x + (view.getWidth() / 2), position.y + (view.getHeight() / 2)) : null;
        return getIntent(context, source);
    }

    public static void show(Context context, @Nullable View source) {
        context.startActivity(getIntent(context, source));
    }

    public static void show(Context context, @Nullable View source, @Nullable Entry entry) {
        Intent intent = getIntent(context, source);
        if (entry != null) {
            intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @Nullable Vector2D source, @Nullable Entry entry) {
        Intent intent = getIntent(context, source);
        if (entry != null) {
            intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @Nullable View source, @Nullable Food food) {
        Intent intent = getIntent(context, source);
        if (food != null) {
            intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @Nullable View source, @NonNull DateTime dateTime) {
        Intent intent = getIntent(context, source);
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
    @BindView(R.id.entry_alarm_separator) View separatorAlarm;
    @BindView(R.id.entry_alarm_container) ViewGroup containerAlarm;
    @BindView(R.id.entry_tags_input) AutoCompleteTextView tagsInput;
    @BindView(R.id.entry_tags) ViewGroup tagsView;

    private long entryId;
    private long foodId;
    private DateTime time = DateTime.now();

    private Entry entry;
    private List<EntryTag> entryTags;
    private Measurement.Category[] activeCategories;
    private int alarmInMinutes;

    private TagAutoCompleteAdapter tagAdapter;

    public EntryActivity() {
        super(R.layout.activity_entry);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        layoutMeasurements.setOnCategoryEventListener(this);
        fab.init();
        fab.setOnFabSelectedListener(this);

        fetchData();
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
            case R.id.action_done:
                trySubmit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            foodId = arguments.getLong(BaseFoodFragment.EXTRA_FOOD_ID);
            if (arguments.get(EXTRA_DATE) != null) {
                time = (DateTime) arguments.getSerializable(EXTRA_DATE);
            }
        }

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            separatorAlarm.setVisibility(View.GONE);
            containerAlarm.setVisibility(View.GONE);
        } else {
            separatorAlarm.setVisibility(View.VISIBLE);
            containerAlarm.setVisibility(View.VISIBLE);
        }
        updateDateTime();
        updateAlarm();
    }

    private void fetchData() {
        activeCategories = PreferenceHelper.getInstance().getActiveCategories();

        if (entryId > 0) {
            new FetchEntryTask(entryId).execute();
        } else if (foodId > 0) {
            new FetchFoodTask(foodId).execute();
            new FetchTagsTask().execute();
        } else {
            new FetchTagsTask().execute();
            initPinnedCategories();
        }
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
        } else {
            initPinnedCategories();
        }
        new FetchTagsTask().execute();
        updateDateTime();
    }

    private void initFood(Food food) {
        if (food != null) {
            layoutMeasurements.addMeasurement(food);
            fab.ignore(Measurement.Category.MEAL);
            fab.restock();
        }
    }

    private void initTags(List<Tag> tags) {
        tagAdapter = new TagAutoCompleteAdapter(this, tags);
        tagsInput.setAdapter(tagAdapter);
        tagsInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    addTag(textView.getText().toString());
                    textView.setText(null);
                    return true;
                }
                return false;
            }
        });
        tagsInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    tagsInput.showDropDown();
                }
            }
        });
        tagsInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagsInput.showDropDown();
            }
        });
        tagsInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                tagsInput.setText(null);
                Tag tag = tagAdapter.getItem(position);
                addTag(tag);
            }
        });

        if (entryTags != null) {
            for (EntryTag entryTag : entryTags) {
                addTag(entryTag.getTag());
            }
        }
    }

    private void addTag(String name) {
        Tag tag = tagAdapter.findTag(name);
        if (tag != null) {
            addTag(tag);
        } else {
            tag = new Tag();
            tag.setId(-1);
            tag.setName(name);
            addTag(tag);
        }
    }

    private void addTag(final Tag tag) {
        int margin = (int) getResources().getDimension(R.dimen.margin_between);
        final ChipView chipView = new ChipView(this);
        chipView.setTag(tag);
        chipView.setLabel(tag.getName());
        chipView.setDeletable(true);
        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTag(tag, chipView);
            }
        });
        chipView.setPadding(0, 0, margin, margin);
        chipView.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTag(tag, chipView);
            }
        });
        tagsView.addView(chipView);

        tagAdapter.setTag(tag, false);
        dismissTagDropDown();
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.setTag(tag, true);
        tagsView.removeView(view);

        // Workaround: Force notifyDataSetChanged
        tagsInput.setText(tagsInput.getText().toString());
        dismissTagDropDown();
    }

    private void dismissTagDropDown() {
        // Workaround
        tagsInput.post(new Runnable() {
            @Override
            public void run() {
                tagsInput.dismissDropDown();
            }
        });
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

        // Check whether there are values to submit
        if (layoutMeasurements.getMeasurements().size() == 0) {
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

    private void trySubmit() {
        if (inputIsValid()) {
            //
            String missingTag = tagsInput.getText().toString();
            if (!TextUtils.isEmpty(missingTag)) {
                addTag(missingTag);
            }
            submit();
        }
    }

    private void submit() {
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
                if (tag.getId() < 0) {
                    tag = TagDao.getInstance().createOrUpdate(tag);
                }
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

    private void deleteEntry() {
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, entryTags));
            finish();
        }
    }

    @OnClick(R.id.button_date)
    public void showDatePicker() {
        DatePickerFragment.newInstance(time, new DatePickerFragment.DatePickerListener() {
            @Override
            public void onDatePicked(@Nullable DateTime dateTime) {
                time = dateTime;
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
            initTags(tags);
        }
    }
}