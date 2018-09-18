package com.faltenreich.diaguard.ui.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
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
import com.faltenreich.diaguard.util.StringUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.github.clans.fab.FloatingActionButton;
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

    private static Intent getIntent(Context context) {
        return new Intent(context, EntryActivity.class);
    }

    public static void show(Context context) {
        context.startActivity(getIntent(context));
    }

    public static void show(Context context, @Nullable Entry entry) {
        Intent intent = getIntent(context);
        if (entry != null) {
            intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @Nullable Food food) {
        Intent intent = getIntent(context);
        if (food != null) {
            intent.putExtra(BaseFoodFragment.EXTRA_FOOD_ID, food.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @NonNull DateTime dateTime) {
        Intent intent = getIntent(context);
        intent.putExtra(EXTRA_DATE, dateTime);
        context.startActivity(intent);
    }

    @BindView(R.id.root) View rootView;
    @BindView(R.id.activity_newevent_scrollview) NestedScrollView scrollView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_menu) MeasurementFloatingActionMenu fabMenu;
    @BindView(R.id.layout_measurements) MeasurementListView layoutMeasurements;
    @BindView(R.id.edittext_notes) EditText editTextNotes;
    @BindView(R.id.button_date) Button buttonDate;
    @BindView(R.id.button_time) Button buttonTime;
    @BindView(R.id.entry_button_alarm) Button buttonAlarm;
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
        initLayout();
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
        activeCategories = PreferenceHelper.getInstance().getActiveCategories();
    }

    private void initLayout() {
        updateDateTime();
        updateAlarm();

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            containerAlarm.setVisibility(View.GONE);
        } else {
            containerAlarm.setVisibility(View.VISIBLE);
        }

        layoutMeasurements.setOnCategoryEventListener(this);

        fabMenu.setOnFabSelectedListener(this);

        if (entryId <= 0 && foodId <= 0) {
            addPinnedCategories();
            fabMenu.restock();
        }

        tagAdapter = new TagAutoCompleteAdapter(this);
        tagsInput.setAdapter(tagAdapter);
        tagsInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    String name = textView.getText().toString().trim();
                    if (!StringUtils.isBlank(name)) {
                        addTag(textView.getText().toString());
                        textView.setText(null);
                    }
                    return true;
                }
                return false;
            }
        });
        tagsInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) tagsInput.showDropDown();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tagsInput.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewUtils.hideKeyboard(tagsInput);
                }
            });
        }
    }

    private void fetchData() {
        if (entryId > 0) {
            new FetchEntryTask(entryId).execute();
        } else if (foodId > 0) {
            new FetchFoodTask(foodId).execute();
        }
        new FetchTagsTask().execute();
    }

    private void addPinnedCategories() {
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
            layoutMeasurements.addMeasurements(entry.getMeasurementCache());

            if (entryTags != null) {
                for (EntryTag entryTag : entryTags) {
                    addTag(entryTag.getTag());
                }
            }
        } else {
            addPinnedCategories();
        }
        new FetchTagsTask().execute();
        updateDateTime();
    }

    private void toggleSubmitButton(boolean isEnabled) {
        fab.setEnabled(isEnabled);
    }

    private void addTag(String name) {
        Tag tag = tagAdapter.find(name);
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

        tagAdapter.set(tag, false);
        dismissTagDropDown();
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.set(tag, true);
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

        if (layoutMeasurements.getMeasurements().size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(editTextNotes.getText().toString()) && tagsView.getChildCount() == 0) {
                ViewUtils.showSnackbar(rootView, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (Measurement measurement : layoutMeasurements.getMeasurements()) {
                if (measurement == null) {
                    inputIsValid = false;
                }
            }
        }

        return inputIsValid;
    }

    @OnClick(R.id.fab)
    public void trySubmit() {
        toggleSubmitButton(false);

        // Convenience: Accept tag that hasn't been submitted by user
        String missingTag = tagsInput.getText().toString();
        if (!StringUtils.isBlank(missingTag)) {
            addTag(missingTag);
        }

        if (inputIsValid()) {
            submit();
        }

        toggleSubmitButton(true);
    }

    private void submit() {
        boolean isNewEntry = entry == null;
        DateTime originalDate = isNewEntry ? null : entry.getDate();
        if (isNewEntry) {
            entry = new Entry();
        }

        entry.setDate(time);
        entry.setNote(editTextNotes.length() > 0 ? editTextNotes.getText().toString() : null);
        entry = EntryDao.getInstance().createOrUpdate(entry);

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

        List<Tag> tags = new ArrayList<>();
        List<EntryTag> entryTags = new ArrayList<>();
        for (int index = 0; index < tagsView.getChildCount(); index++) {
            View view = tagsView.getChildAt(index);
            if (view.getTag() instanceof Tag) {
                Tag tag = (Tag) view.getTag();
                if (tag.getId() < 0) {
                    tag = TagDao.getInstance().createOrUpdate(tag);
                    Tag legacy = TagDao.getInstance().getByName(tag.getName());
                    if (legacy != null) {
                        tag.setId(legacy.getId());
                    }
                }
                tag.setUpdatedAt(DateTime.now());
                tags.add(tag);

                EntryTag entryTag = new EntryTag();
                entryTag.setEntry(entry);
                entryTag.setTag(tag);
                entryTags.add(entryTag);
            }
        }
        TagDao.getInstance().bulkCreateOrUpdate(tags);
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
                if (dateTime != null) {
                    time = dateTime;
                    updateDateTime();
                }
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

    @OnClick(R.id.entry_tags_button)
    public void openTags() {
        startActivity(new Intent(this, TagsActivity.class));
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
        fabMenu.ignore(category);
        fabMenu.restock();
    }

    @Override
    public void onCategoryRemoved(Measurement.Category category) {
        fabMenu.removeIgnore(category);
        fabMenu.restock();
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
            if (food != null) {
                layoutMeasurements.addMeasurement(food);
                fabMenu.ignore(Measurement.Category.MEAL);
                fabMenu.restock();
            }
        }
    }

    private class FetchTagsTask extends AsyncTask<Void, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(Void... params) {
            return TagDao.getInstance().getRecent();
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            tagAdapter.addAll(tags);
            tagAdapter.notifyDataSetChanged();
        }
    }
}