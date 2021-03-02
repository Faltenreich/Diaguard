package com.faltenreich.diaguard.feature.entry.edit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentEntryEditBinding;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementView;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.tag.TagAutoCompleteAdapter;
import com.faltenreich.diaguard.feature.tag.TagListFragment;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
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
import com.faltenreich.diaguard.shared.data.reflect.ObjectFactory;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSearchEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.chip.ChipView;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.chip.ChipGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class EntryEditFragment
    extends BaseFragment<FragmentEntryEditBinding>
    implements ToolbarDescribing
{

    private static final String TAG = EntryEditFragment.class.getSimpleName();

    static final String EXTRA_ENTRY_ID = "entryId";
    static final String EXTRA_FOOD_ID = "foodId";
    static final String EXTRA_DATE = "date";

    private ViewGroup root;
    private NestedScrollView scrollView;
    private Button dateButton;
    private Button timeButton;
    private AutoCompleteTextView tagInput;
    private ChipGroup tagListView;
    private ImageView tagEditButton;
    private EditText noteInput;
    private ViewGroup alarmContainer;
    private Button alarmButton;
    private LinearLayout measurementContainer;
    private MeasurementFloatingActionMenu fabMenu;
    private FloatingActionButton fab;

    private TagAutoCompleteAdapter tagAdapter;

    private long entryId;
    private long foodId;
    private DateTime dateTime;

    private Entry entry;
    private List<EntryTag> entryTags;
    private int alarmInMinutes;

    @Override
    protected FragmentEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), entryId > 0 ? R.string.entry_edit : R.string.entry_new)
            .setMenu(R.menu.form_edit)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initLayout();
        fetchData();
        updateDateTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchTags();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            foodId = arguments.getLong(EXTRA_FOOD_ID);
            if (arguments.get(EXTRA_DATE) != null) {
                dateTime = (DateTime) arguments.getSerializable(EXTRA_DATE);
            }
        }
        tagAdapter = new TagAutoCompleteAdapter(requireContext());
    }

    private void bindViews() {
        root = getBinding().root;
        scrollView = getBinding().scrollView;
        dateButton = getBinding().dateButton;
        timeButton = getBinding().timeButton;
        tagInput = getBinding().tagInput;
        tagListView = getBinding().tagListView;
        tagEditButton = getBinding().tagEditButton;
        noteInput = getBinding().noteInput;
        alarmContainer = getBinding().alarmContainer;
        alarmButton = getBinding().alarmButton;
        measurementContainer = getBinding().measurementContainer;
        fabMenu = getBinding().fabMenu;
        fab = getBinding().fab;
    }

    private void initLayout() {
        dateButton.setOnClickListener(view -> showDatePicker());
        timeButton.setOnClickListener(view -> showTimePicker());

        fabMenu.setOnFabSelectedListener(new MeasurementFloatingActionMenu.OnFabSelectedListener() {
            @Override
            public void onCategorySelected(Category category) {
                addCategory(category, 0);
            }
            @Override
            public void onMiscellaneousSelected() {
                showDialogCategories();
            }
        });

        tagListView.setVisibility(View.GONE);
        tagInput.setAdapter(tagAdapter);
        tagInput.setOnEditorActionListener((textView, action, keyEvent) -> {
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
        tagInput.setOnFocusChangeListener((view, hasFocus) -> {
            // Attempt to fix android.view.WindowManager$BadTokenException
            new Handler().post(() -> {
                try {
                    if (hasFocus) tagInput.showDropDown();
                } catch (Exception exception) {
                    Log.e(TAG, exception.getMessage() != null ? exception.getMessage() : "Failed to show dropdown");
                }
            });
        });
        tagInput.setOnClickListener(view -> tagInput.showDropDown());
        tagInput.setOnItemClickListener((adapterView, view, position, l) -> {
            tagInput.setText(null);
            Tag tag = tagAdapter.getItem(position);
            addTag(tag);
        });

        tagEditButton.setOnClickListener(view -> openTags());

        alarmContainer.setVisibility(entryId > 0 ? View.GONE : View.VISIBLE);
        alarmButton.setOnClickListener(view -> showAlarmPicker());
        updateAlarm();

        fab.setOnClickListener(view -> trySubmit());
    }

    private void fetchData() {
        if (entry != null) {
            initEntry(entry, entryTags);
        } else {
            if (entryId > 0) {
                fetchEntry(entryId);
            } else if (foodId > 0) {
                entry = new Entry();
                entry.setDate(dateTime);
                fetchFood(foodId);
                updateDateTime();
            } else {
                entry = new Entry();
                entry.setDate(dateTime);
                addPinnedCategories();
            }
        }
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
                    addFood(food);
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

    private Category[] getActiveCategories() {
        return PreferenceStore.getInstance().getActiveCategories();
    }

    private void addPinnedCategories() {
        for (Category category : getActiveCategories()) {
            if (PreferenceStore.getInstance().isCategoryPinned(category) && !hasCategory(category)) {
                addCategory(category, 0);
            }
        }
    }


    private void addMeasurement(Measurement measurement, int index) {
        MeasurementView<?> view = new MeasurementView<>(getContext(), measurement);
        view.setOnCategoryRemovedListener(this::removeCategory);
        measurementContainer.addView(view, index);
        fabMenu.ignore(measurement.getCategory());
        fabMenu.restock();
    }

    private void addCategory(Category category, int index) {
        Measurement measurement = ObjectFactory.createFromClass(category.toClass());
        addMeasurement(measurement, index);
        int indexInCache = entry.indexInMeasurementCache(category);
        if (indexInCache != -1) {
            entry.getMeasurementCache().set(indexInCache, measurement);
        } else {
            entry.getMeasurementCache().add(measurement);
        }
    }

    private void addFood(Food food) {
        // TODO
    }

    private void removeCategory(Category category) {
        int index = indexOf(category);
        if (index != -1) {
            measurementContainer.removeViewAt(index);

            int indexInCache = entry.indexInMeasurementCache(category);
            if (indexInCache != -1) {
                entry.getMeasurementCache().remove(indexInCache);
            }

            fabMenu.removeIgnore(category);
            fabMenu.restock();
        }
    }

    private int indexOf(Category category) {
        for (int index = 0; index < measurementContainer.getChildCount(); index++) {
            View view = measurementContainer.getChildAt(index);
            if (view instanceof MeasurementView<?>) {
                MeasurementView<?> measurementView = (MeasurementView<?>) view;
                if (measurementView.getMeasurement().getCategory() == category) {
                    return index;
                }
            }
        }
        return -1;
    }

    private boolean hasCategory(Category category) {
        return indexOf(category) != -1;
    }

    private void initEntry(Entry entry, List<EntryTag> entryTags) {
        if (entry != null) {
            this.entry = entry;
            this.entryTags = entryTags;

            noteInput.setText(entry.getNote());

            if (entry.getMeasurementCache() != null) {
                for (Measurement measurement : entry.getMeasurementCache()) {
                    addMeasurement(measurement, 0);
                }
            }

            if (entryTags != null) {
                for (EntryTag entryTag : entryTags) {
                    Tag tag = entryTag.getTag();
                    if (tag != null) {
                        addTag(entryTag.getTag());
                    }
                }
            }
            updateDateTime();
        }
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
        tagListView.addView(chipView);

        tagAdapter.set(tag, false);
        dismissTagDropDown();

        tagListView.setVisibility(View.VISIBLE);
    }

    private void removeTag(Tag tag, View view) {
        tagAdapter.set(tag, true);
        tagListView.removeView(view);

        // Workaround: Force notifyDataSetChanged
        tagInput.setText(tagInput.getText().toString());
        dismissTagDropDown();

        if (tagListView.getChildCount() == 0) {
            tagListView.setVisibility(View.GONE);
        }
    }

    private void dismissTagDropDown() {
        // Workaround
        tagInput.post(() -> tagInput.dismissDropDown());
    }

    private void showDialogCategories() {
        Category[] activeCategories = getActiveCategories();
        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Category category = activeCategories[position];
            categoryNames[position] = getString(category.getStringResId());
            visibleCategoriesOld[position] = hasCategory(category);
        }

        final boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.categories)
            .setMultiChoiceItems(categoryNames, visibleCategoriesOld, (dialog, which, isChecked) -> visibleCategories[which] = isChecked)
            .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                for (int position = activeCategories.length - 1; position >= 0; position--) {
                    Category category = activeCategories[position];
                    if (visibleCategories[position]) {
                        scrollView.smoothScrollTo(0, 0);
                        addCategory(category, 0);
                    } else {
                        removeCategory(category);
                    }
                }
            })
            .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDateTime() {
        if (entry != null) {
            DateTime dateTime = entry.getDate();
            dateButton.setText(Helper.getDateFormat().print(dateTime));
            timeButton.setText(Helper.getTimeFormat().print(dateTime));
        }
    }

    private void updateAlarm() {
        alarmButton.setText(alarmInMinutes > 0 ?
            String.format("%s %s",
                getString(R.string.alarm_reminder_in),
                DateTimeUtils.parseInterval(getContext(), alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)
            ) : getString(R.string.alarm_reminder_none));
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        List<Measurement> measurements = entry.getMeasurementCache();
        if (measurements.size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(noteInput.getText().toString()) && tagListView.getChildCount() == 0) {
                ViewUtils.showSnackbar(root, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (Measurement measurement : measurements) {
                if (measurement == null) {
                    inputIsValid = false;
                    break;
                }
            }
        }

        return inputIsValid;
    }

    private void trySubmit() {
        fab.setEnabled(false);

        // Convenience: Accept tag that hasn't been submitted by user
        String missingTag = tagInput.getText().toString();
        if (!StringUtils.isBlank(missingTag)) {
            addTag(missingTag);
            tagInput.setText(null);
        }

        if (inputIsValid()) {
            submit();
        }

        fab.setEnabled(true);
    }

    private void submit() {
        boolean isNewEntry = entry == null;
        DateTime originalDate = isNewEntry ? null : entry.getDate();
        if (isNewEntry) {
            entry = new Entry();
        }

        entry.setNote(noteInput.length() > 0 ? noteInput.getText().toString() : null);
        entry = EntryDao.getInstance().createOrUpdate(entry);
        // TODO: Check measurements

        // TODO: Delete distinct
        if (entryTags != null && entryTags.size() > 0) {
            EntryTagDao.getInstance().delete(entryTags);
        }

        List<Tag> tags = new ArrayList<>();
        List<EntryTag> entryTags = new ArrayList<>();
        for (int index = 0; index < tagListView.getChildCount(); index++) {
            View view = tagListView.getChildAt(index);
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
        for (int index = 0; index < measurementContainer.getChildCount(); index++) {
            View view = measurementContainer.getChildAt(index);
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
        DatePickerFragment.newInstance(entry.getDate(), dateTime -> {
            if (dateTime != null) {
                entry.setDate(dateTime);
                updateDateTime();
            }
        }).show(getChildFragmentManager());
    }

    private void showTimePicker() {
        TimePickerFragment.newInstance(entry.getDate(), (view, hourOfDay, minute) -> {
            entry.setDate(entry.getDate().withHourOfDay(hourOfDay).withMinuteOfHour(minute));
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

    private void openTags() {
        openFragment(new TagListFragment(), Navigation.Operation.REPLACE, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSearchEvent event) {
        if (isAdded()) {
            openFragment(FoodSearchFragment.newInstance(true), Navigation.Operation.REPLACE, true);
        }
    }
}