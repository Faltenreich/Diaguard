package com.faltenreich.diaguard.feature.entry.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityEntryEditBinding;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementListView;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementView;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.tag.TagAutoCompleteAdapter;
import com.faltenreich.diaguard.feature.tag.TagListActivity;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.chip.ChipView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class EntryEditActivity extends BaseActivity<ActivityEntryEditBinding> implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    private static final String TAG = EntryEditActivity.class.getSimpleName();

    public static final String EXTRA_ENTRY_ID = "entryId";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_CATEGORY = "category";
    private static final String EXTRA_FOOD_ID = "EXTRA_FOOD_ID";

    private static Intent getIntent(Context context) {
        return new Intent(context, EntryEditActivity.class);
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
            intent.putExtra(EXTRA_FOOD_ID, food.getId());
        }
        context.startActivity(intent);
    }

    public static void show(Context context, @NonNull DateTime dateTime) {
        Intent intent = getIntent(context);
        intent.putExtra(EXTRA_DATE, dateTime);
        context.startActivity(intent);
    }

    public static void show(Context context, @NonNull Category category) {
        Intent intent = getIntent(context);
        intent.putExtra(EXTRA_CATEGORY, category);
        context.startActivity(intent);
    }

    private long entryId;
    private long foodId;
    private DateTime time = DateTime.now();

    private Entry entry;
    private List<EntryTag> entryTags;
    private Category[] activeCategories;
    private int alarmInMinutes;

    private TagAutoCompleteAdapter tagAdapter;

    public EntryEditActivity() {
        super(R.layout.activity_entry_edit);
    }

    @Override
    protected ActivityEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initLayout();
        if (entryId > 0) {
            fetchEntry(entryId);
        } else if (foodId > 0) {
            fetchFood(foodId);
        }
        addMeasurementForGivenCategory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entryId > 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteEntry();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            foodId = arguments.getLong(EXTRA_FOOD_ID);
            if (arguments.get(EXTRA_DATE) != null) {
                time = (DateTime) arguments.getSerializable(EXTRA_DATE);
            }
        }
        activeCategories = PreferenceStore.getInstance().getActiveCategories();
    }

    private void initLayout() {
        updateDateTime();
        updateAlarm();

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            binding.entryAlarmContainer.setVisibility(View.GONE);
        } else {
            binding.entryAlarmContainer.setVisibility(View.VISIBLE);
        }

        binding.layoutMeasurements.setOnCategoryEventListener(this);

        binding.fabMenu.setOnFabSelectedListener(this);

        if (entryId <= 0 && foodId <= 0) {
            addPinnedCategories();
        }
        binding.fabMenu.restock();

        binding.entryTags.setVisibility(View.GONE);
        tagAdapter = new TagAutoCompleteAdapter(this);
        binding.entryTagsInput.setAdapter(tagAdapter);
        binding.entryTagsInput.setOnEditorActionListener((textView, action, keyEvent) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                String name = textView.getText().toString().trim();
                if (!StringUtils.isBlank(name)) {
                    addTag(textView.getText().toString());
                    textView.setText(null);
                }
                return true;
            }
            return false;
        });
        binding.entryTagsInput.setOnFocusChangeListener((view, hasFocus) -> {
            // Attempt to fix android.view.WindowManager$BadTokenException
            new Handler().post(() -> {
                try {
                    if (hasFocus) binding.entryTagsInput.showDropDown();
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to show dropdown");
                }
            });
        });
        binding.entryTagsInput.setOnClickListener(view -> binding.entryTagsInput.showDropDown());
        binding.entryTagsInput.setOnItemClickListener((adapterView, view, position, l) -> {
            binding.entryTagsInput.setText(null);
            Tag tag = tagAdapter.getItem(position);
            addTag(tag);
        });

        binding.entryTagsEditButton.setOnClickListener(view -> startActivity(new Intent(this, TagListActivity.class)));

        binding.fab.setOnClickListener(view -> trySubmit());
        binding.buttonDate.setOnClickListener(view -> showDatePicker());
        binding.buttonTime.setOnClickListener(view -> showTimePicker());
        binding.entryButtonAlarm.setOnClickListener(view -> showAlarmPicker());
    }

    private void fetchEntry(final long id) {
        DataLoader.getInstance().load(this, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                EntryDao dao = EntryDao.getInstance();
                entry = dao.getById(id);
                if (entry != null) {
                    entry.setMeasurementCache(dao.getMeasurements(entry));
                    entryTags = EntryTagDao.getInstance().getAll(entry);
                }
                return null;
            }

            @Override
            public void onDidLoad(List<Tag> data) {
                initEntry(entry, entryTags);
            }
        });
    }

    private void fetchFood(final long id) {
        DataLoader.getInstance().load(this, new DataLoaderListener<Food>() {
            @Override
            public Food onShouldLoad() {
                return FoodDao.getInstance().getById(id);
            }

            @Override
            public void onDidLoad(Food food) {
                if (food != null) {
                    binding.layoutMeasurements.addMeasurement(food);
                    binding.fabMenu.ignore(Category.MEAL);
                    binding.fabMenu.restock();
                }
            }
        });
    }

    private void fetchTags() {
        DataLoader.getInstance().load(this, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                return TagDao.getInstance().getRecent();
            }

            @Override
            public void onDidLoad(List<Tag> tags) {
                tagAdapter.clear();
                tagAdapter.addAll(tags);
                tagAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addMeasurementForGivenCategory() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            Category category = (Category) arguments.getSerializable(EXTRA_CATEGORY);
            if (category != null) {
                addMeasurementView(category);
            }
        }
    }

    private void addPinnedCategories() {
        for (Category category : activeCategories) {
            if (PreferenceStore.getInstance().isCategoryPinned(category) && !binding.layoutMeasurements.hasCategory(category)) {
                binding.layoutMeasurements.addMeasurementAtEnd(category);
            }
        }
    }

    private void initEntry(Entry entry, List<EntryTag> entryTags) {
        if (entry != null) {
            this.entry = entry;
            this.entryTags = entryTags;
            this.time = entry.getDate();

            binding.edittextNotes.setText(entry.getNote());
            binding.layoutMeasurements.addMeasurements(entry.getMeasurementCache());

            if (entryTags != null) {
                for (EntryTag entryTag : entryTags) {
                    Tag tag = entryTag.getTag();
                    if (tag != null) {
                        addTag(entryTag.getTag());
                    }
                }
            }
        } else {
            addPinnedCategories();
        }
        updateDateTime();
    }

    private void toggleSubmitButton(boolean isEnabled) {
        binding.fab.setEnabled(isEnabled);
    }

    private void addTag(String name) {
        Tag tag = tagAdapter.find(name);
        if (tag == null) {
            tag = new Tag();
            tag.setId(-1);
            tag.setName(name);
        }
        addTag(tag);
    }

    private void addTag(final Tag tag) {
        final ChipView chipView = new ChipView(this);
        chipView.setTag(tag);
        chipView.setText(tag.getName());
        chipView.setCloseIconVisible(true);
        chipView.setOnCloseIconClickListener(view -> removeTag(tag, chipView));
        chipView.setOnClickListener(view -> removeTag(tag, chipView));
        binding.entryTags.addView(chipView);

        tagAdapter.set(tag, false);
        dismissTagDropDown();

        binding.entryTags.setVisibility(View.VISIBLE);
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.set(tag, true);
        binding.entryTags.removeView(view);

        // Workaround: Force notifyDataSetChanged
        binding.entryTagsInput.setText(binding.entryTagsInput.getText().toString());
        dismissTagDropDown();

        if (binding.entryTags.getChildCount() == 0) {
            binding.entryTags.setVisibility(View.GONE);
        }
    }

    private void dismissTagDropDown() {
        // Workaround
        binding.entryTagsInput.post(() -> binding.entryTagsInput.dismissDropDown());
    }

    private void showDialogCategories() {
        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Category category = activeCategories[position];
            categoryNames[position] = getString(category.getStringResId());
            visibleCategoriesOld[position] = binding.layoutMeasurements.hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.categories)
            .setMultiChoiceItems(categoryNames, visibleCategoriesOld, (dialog, which, isChecked) -> visibleCategories[which] = isChecked)
            .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                for (int position = activeCategories.length - 1; position >= 0; position--) {
                    Category category = activeCategories[position];
                    if (visibleCategories[position]) {
                        addMeasurementView(category);
                    } else {
                        removeMeasurementView(category);
                    }
                }
            })
            .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDateTime() {
        binding.buttonDate.setText(Helper.getDateFormat().print(time));
        binding.buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void updateAlarm() {
        binding.entryButtonAlarm.setText(alarmInMinutes > 0 ?
            String.format("%s %s",
                getString(R.string.alarm_reminder_in),
                DateTimeUtils.parseInterval(this, alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)
            ) : getString(R.string.alarm_reminder_none));
    }

    private void addMeasurementView(Category category) {
        binding.activityNeweventScrollview.smoothScrollTo(0, 0);
        binding.layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(Category category) {
        binding.layoutMeasurements.removeMeasurement(category);
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        if (binding.layoutMeasurements.getMeasurements().size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(binding.edittextNotes.getText().toString()) && binding.entryTags.getChildCount() == 0) {
                ViewUtils.showSnackbar(binding.root, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (Measurement measurement : binding.layoutMeasurements.getMeasurements()) {
                if (measurement == null) {
                    inputIsValid = false;
                    break;
                }
            }
        }

        return inputIsValid;
    }

    private void trySubmit() {
        toggleSubmitButton(false);

        // Convenience: Accept tag that hasn't been submitted by user
        String missingTag = binding.entryTagsInput.getText().toString();
        if (!StringUtils.isBlank(missingTag)) {
            addTag(missingTag);
            binding.entryTagsInput.setText(null);
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
        entry.setNote(binding.edittextNotes.length() > 0 ? binding.edittextNotes.getText().toString() : null);
        entry = EntryDao.getInstance().createOrUpdate(entry);

        entry.getMeasurementCache().clear();
        for (Category category : Category.values()) {
            if (binding.layoutMeasurements.hasCategory(category)) {
                Measurement measurement = binding.layoutMeasurements.getMeasurement(category);
                measurement.setEntry(entry);
                //noinspection unchecked
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
                entry.getMeasurementCache().add(measurement);
            } else {
                MeasurementDao.getInstance(category.toClass()).deleteMeasurements(entry);
            }
        }

        // TODO: Delete distinct
        if (entryTags != null && entryTags.size() > 0) {
            EntryTagDao.getInstance().delete(entryTags);
        }

        List<Tag> tags = new ArrayList<>();
        List<EntryTag> entryTags = new ArrayList<>();
        for (int index = 0; index < binding.entryTags.getChildCount(); index++) {
            View view = binding.entryTags.getChildAt(index);
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

        List<FoodEaten> foodEatenList = getFoodEaten();
        if (isNewEntry) {
            Toast.makeText(this, getString(R.string.entry_added), Toast.LENGTH_LONG).show();
            Events.post(new EntryAddedEvent(entry, entryTags, foodEatenList));
        } else {
            Events.post(new EntryUpdatedEvent(entry, entryTags, originalDate, foodEatenList));
        }

        if (alarmInMinutes > 0) {
            AlarmUtils.setAlarm(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE);
        }

        finish();
    }

    private void deleteEntry() {
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, entryTags, getFoodEaten()));
            finish();
        }
    }

    private List<FoodEaten> getFoodEaten() {
        for (int index = 0; index < binding.layoutMeasurements.getChildCount(); index++) {
            View view = binding.layoutMeasurements.getChildAt(index);
            if (view instanceof MeasurementView) {
                MeasurementView measurementView = ((MeasurementView) view);
                Measurement measurement = measurementView.getMeasurement();
                if (measurement instanceof Meal) {
                    return ((Meal) measurement).getFoodEatenCache();
                }
            }
        }
        return new ArrayList<>();
    }

    private void showDatePicker() {
        DatePickerFragment.newInstance(time, dateTime -> {
            if (dateTime != null) {
                time = dateTime;
                updateDateTime();
            }
        }).show(getSupportFragmentManager());
    }

    private void showTimePicker() {
        TimePickerFragment.newInstance(time, (view, hourOfDay, minute) -> {
            time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
            updateDateTime();
        }).show(getSupportFragmentManager());
    }

    private void showAlarmPicker() {
        ViewUtils.showNumberPicker(this, R.string.minutes, alarmInMinutes, 0, 10000, (reference, number, decimal, isNegative, fullNumber) -> {
            alarmInMinutes = number.intValue();
            updateAlarm();
        });
    }

    @Override
    public void onCategorySelected(@Nullable Category category) {
        addMeasurementView(category);
    }

    @Override
    public void onMiscellaneousSelected() {
        showDialogCategories();
    }

    @Override
    public void onCategoryAdded(Category category) {
        binding.fabMenu.ignore(category);
        binding.fabMenu.restock();
    }

    @Override
    public void onCategoryRemoved(Category category) {
        binding.fabMenu.removeIgnore(category);
        binding.fabMenu.restock();
    }
}