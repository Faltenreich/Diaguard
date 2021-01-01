package com.faltenreich.diaguard.feature.entry.edit;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentEntryEditBinding;
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
import com.faltenreich.diaguard.shared.view.chip.ChipView;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class EntryEditFragment
    extends BaseFragment<FragmentEntryEditBinding>
    implements MeasurementFloatingActionMenu.OnFabSelectedListener, MeasurementListView.OnCategoryEventListener {

    private static final String TAG = EntryEditFragment.class.getSimpleName();

    private long entryId;
    private long foodId;
    private DateTime time = DateTime.now();

    private Entry entry;
    private List<EntryTag> entryTags;
    private Category[] activeCategories;
    private int alarmInMinutes;

    private TagAutoCompleteAdapter tagAdapter;

    public EntryEditFragment() {
        super(R.layout.fragment_entry_edit);
    }

    @Override
    protected FragmentEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        if (entryId > 0) {
            fetchEntry(entryId);
        } else if (foodId > 0) {
            fetchFood(foodId);
        }
        addMeasurementForGivenCategory();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchTags();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.form_edit, menu);
        menu.findItem(R.id.action_delete).setVisible(entryId > 0);
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            entryId = arguments.getLong(EntryEditIntentFactory.EXTRA_ENTRY_ID);
            foodId = arguments.getLong(EntryEditIntentFactory.EXTRA_FOOD_ID);
            if (arguments.get(EntryEditIntentFactory.EXTRA_DATE) != null) {
                time = (DateTime) arguments.getSerializable(EntryEditIntentFactory.EXTRA_DATE);
            }
        }
        activeCategories = PreferenceStore.getInstance().getActiveCategories();
    }

    private void initLayout() {
        updateDateTime();
        updateAlarm();

        if (entryId > 0) {
            setTitle(getString(R.string.entry_edit));
            getBinding().entryAlarmContainer.setVisibility(View.GONE);
        } else {
            getBinding().entryAlarmContainer.setVisibility(View.VISIBLE);
        }

        getBinding().layoutMeasurements.setOnCategoryEventListener(this);

        getBinding().fabMenu.setOnFabSelectedListener(this);

        if (entryId <= 0 && foodId <= 0) {
            addPinnedCategories();
        }
        getBinding().fabMenu.restock();

        getBinding().entryTags.setVisibility(View.GONE);
        tagAdapter = new TagAutoCompleteAdapter(requireContext());
        getBinding().entryTagsInput.setAdapter(tagAdapter);
        getBinding().entryTagsInput.setOnEditorActionListener((textView, action, keyEvent) -> {
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
        getBinding().entryTagsInput.setOnFocusChangeListener((view, hasFocus) -> {
            // Attempt to fix android.view.WindowManager$BadTokenException
            new Handler().post(() -> {
                try {
                    if (hasFocus) getBinding().entryTagsInput.showDropDown();
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to show dropdown");
                }
            });
        });
        getBinding().entryTagsInput.setOnClickListener(view -> getBinding().entryTagsInput.showDropDown());
        getBinding().entryTagsInput.setOnItemClickListener((adapterView, view, position, l) -> {
            getBinding().entryTagsInput.setText(null);
            Tag tag = tagAdapter.getItem(position);
            addTag(tag);
        });

        getBinding().entryTagsEditButton.setOnClickListener(view -> startActivity(new Intent(getContext(), TagListActivity.class)));

        getBinding().fab.setOnClickListener(view -> trySubmit());
        getBinding().buttonDate.setOnClickListener(view -> showDatePicker());
        getBinding().buttonTime.setOnClickListener(view -> showTimePicker());
        getBinding().entryButtonAlarm.setOnClickListener(view -> showAlarmPicker());
    }

    private void fetchEntry(final long id) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<Tag>>() {
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
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Food>() {
            @Override
            public Food onShouldLoad() {
                return FoodDao.getInstance().getById(id);
            }

            @Override
            public void onDidLoad(Food food) {
                if (food != null) {
                    getBinding().layoutMeasurements.addMeasurement(food);
                    getBinding().fabMenu.ignore(Category.MEAL);
                    getBinding().fabMenu.restock();
                }
            }
        });
    }

    private void fetchTags() {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<Tag>>() {
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            Category category = (Category) arguments.getSerializable(EntryEditIntentFactory.EXTRA_CATEGORY);
            if (category != null) {
                addMeasurementView(category);
            }
        }
    }

    private void addPinnedCategories() {
        for (Category category : activeCategories) {
            if (PreferenceStore.getInstance().isCategoryPinned(category) && !getBinding().layoutMeasurements.hasCategory(category)) {
                getBinding().layoutMeasurements.addMeasurementAtEnd(category);
            }
        }
    }

    private void initEntry(Entry entry, List<EntryTag> entryTags) {
        if (entry != null) {
            this.entry = entry;
            this.entryTags = entryTags;
            this.time = entry.getDate();

            getBinding().edittextNotes.setText(entry.getNote());
            getBinding().layoutMeasurements.addMeasurements(entry.getMeasurementCache());

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
        getBinding().fab.setEnabled(isEnabled);
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
        final ChipView chipView = new ChipView(getContext());
        chipView.setTag(tag);
        chipView.setText(tag.getName());
        chipView.setCloseIconVisible(true);
        chipView.setOnCloseIconClickListener(view -> removeTag(tag, chipView));
        chipView.setOnClickListener(view -> removeTag(tag, chipView));
        getBinding().entryTags.addView(chipView);

        tagAdapter.set(tag, false);
        dismissTagDropDown();

        getBinding().entryTags.setVisibility(View.VISIBLE);
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.set(tag, true);
        getBinding().entryTags.removeView(view);

        // Workaround: Force notifyDataSetChanged
        getBinding().entryTagsInput.setText(getBinding().entryTagsInput.getText().toString());
        dismissTagDropDown();

        if (getBinding().entryTags.getChildCount() == 0) {
            getBinding().entryTags.setVisibility(View.GONE);
        }
    }

    private void dismissTagDropDown() {
        // Workaround
        getBinding().entryTagsInput.post(() -> getBinding().entryTagsInput.dismissDropDown());
    }

    private void showDialogCategories() {
        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Category category = activeCategories[position];
            categoryNames[position] = getString(category.getStringResId());
            visibleCategoriesOld[position] = getBinding().layoutMeasurements.hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        getBinding().buttonDate.setText(Helper.getDateFormat().print(time));
        getBinding().buttonTime.setText(Helper.getTimeFormat().print(time));
    }

    private void updateAlarm() {
        getBinding().entryButtonAlarm.setText(alarmInMinutes > 0 ?
            String.format("%s %s",
                getString(R.string.alarm_reminder_in),
                DateTimeUtils.parseInterval(getContext(), alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)
            ) : getString(R.string.alarm_reminder_none));
    }

    private void addMeasurementView(Category category) {
        getBinding().activityNeweventScrollview.smoothScrollTo(0, 0);
        getBinding().layoutMeasurements.addMeasurement(category);
    }

    private void removeMeasurementView(Category category) {
        getBinding().layoutMeasurements.removeMeasurement(category);
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        if (getBinding().layoutMeasurements.getMeasurements().size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(getBinding().edittextNotes.getText().toString()) && getBinding().entryTags.getChildCount() == 0) {
                ViewUtils.showSnackbar(getBinding().root, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (Measurement measurement : getBinding().layoutMeasurements.getMeasurements()) {
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
        String missingTag = getBinding().entryTagsInput.getText().toString();
        if (!StringUtils.isBlank(missingTag)) {
            addTag(missingTag);
            getBinding().entryTagsInput.setText(null);
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
        entry.setNote(getBinding().edittextNotes.length() > 0 ? getBinding().edittextNotes.getText().toString() : null);
        entry = EntryDao.getInstance().createOrUpdate(entry);

        entry.getMeasurementCache().clear();
        for (Category category : Category.values()) {
            if (getBinding().layoutMeasurements.hasCategory(category)) {
                Measurement measurement = getBinding().layoutMeasurements.getMeasurement(category);
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
        for (int index = 0; index < getBinding().entryTags.getChildCount(); index++) {
            View view = getBinding().entryTags.getChildAt(index);
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
            Toast.makeText(getContext(), getString(R.string.entry_added), Toast.LENGTH_LONG).show();
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
        for (int index = 0; index < getBinding().layoutMeasurements.getChildCount(); index++) {
            View view = getBinding().layoutMeasurements.getChildAt(index);
            if (view instanceof MeasurementView) {
                MeasurementView<?> measurementView = ((MeasurementView<?>) view);
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
        }).show(getChildFragmentManager());
    }

    private void showTimePicker() {
        TimePickerFragment.newInstance(time, (view, hourOfDay, minute) -> {
            time = time.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
            updateDateTime();
        }).show(getChildFragmentManager());
    }

    private void showAlarmPicker() {
        if (getActivity() instanceof AppCompatActivity) {
            ViewUtils.showNumberPicker((AppCompatActivity) getActivity(), R.string.minutes, alarmInMinutes, 0, 10_000, (reference, number, decimal, isNegative, fullNumber) -> {
                alarmInMinutes = number.intValue();
                updateAlarm();
            });
        }
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
        getBinding().fabMenu.ignore(category);
        getBinding().fabMenu.restock();
    }

    @Override
    public void onCategoryRemoved(Category category) {
        getBinding().fabMenu.removeIgnore(category);
        getBinding().fabMenu.restock();
    }
}