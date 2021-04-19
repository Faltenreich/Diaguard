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
import androidx.core.widget.NestedScrollView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentEntryEditBinding;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.feature.datetime.TimePickerFragment;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementFloatingActionMenu;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementView;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.tag.TagAutoCompleteAdapter;
import com.faltenreich.diaguard.feature.tag.TagListFragment;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
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
import com.faltenreich.diaguard.shared.view.edittext.EditTextUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.picker.NumberPickerDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.chip.ChipGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class EntryEditFragment
    extends BaseFragment<FragmentEntryEditBinding>
    implements ToolbarDescribing
{

    private static final String TAG = EntryEditFragment.class.getSimpleName();

    private final EntryEditViewModel viewModel = new EntryEditViewModel();

    private ViewGroup root;
    private NestedScrollView scrollView;
    private Button dateButton;
    private Button timeButton;
    private AutoCompleteTextView tagInput;
    private ChipGroup tagListView;
    private TagAutoCompleteAdapter tagAdapter;
    private ImageView tagEditButton;
    private EditText noteInput;
    private ViewGroup alarmContainer;
    private Button alarmButton;
    private LinearLayout measurementContainer;
    private MeasurementFloatingActionMenu fabMenu;
    private FloatingActionButton fab;

    @Override
    protected FragmentEntryEditBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentEntryEditBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), viewModel.isEditing() ? R.string.entry_edit : R.string.entry_new)
            .setMenu(R.menu.form_edit)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setArguments(getArguments());
        tagAdapter = new TagAutoCompleteAdapter(requireContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initLayout();
        invalidateAlarm();
        viewModel.observeEntry(requireContext(), this::setEntry);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.observeTags(requireContext(), this::setTags);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_delete).setVisible(viewModel.isEditing());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteEntry();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        tagInput.setAdapter(tagAdapter);
        tagInput.setOnEditorActionListener((textView, action, keyEvent) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                String name = textView.getText().toString().trim();
                if (!StringUtils.isBlank(name)) {
                    addTag(name);
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
                    Log.e(TAG, exception.toString());
                }
            });
        });
        tagInput.setOnClickListener(view -> tagInput.showDropDown());
        tagInput.setOnItemClickListener((adapterView, view, position, l) -> {
            tagInput.setText(null);
            addTag(tagAdapter.getItem(position));
        });
        tagListView.setVisibility(View.GONE);
        tagEditButton.setOnClickListener(view -> openTags());

        EditTextUtils.afterTextChanged(noteInput, () -> viewModel.getEntry().setNote(noteInput.getText().toString()));

        alarmContainer.setVisibility(viewModel.isEditing() ? View.GONE : View.VISIBLE);
        alarmButton.setOnClickListener(view -> showAlarmPicker());

        fabMenu.setOnCategorySelectedListener(this::addCategory);
        fabMenu.setOnMiscellaneousSelectedListener(this::showDialogCategories);
        fab.setOnClickListener(view -> trySubmit());
    }

    private void setEntry(Entry entry) {
        noteInput.setText(entry.getNote());

        if (entry.getMeasurementCache() != null && !entry.getMeasurementCache().isEmpty()) {
            for (Measurement measurement : entry.getMeasurementCache()) {
                addMeasurement(measurement);
            }
        } else if (!entry.isPersisted()) {
            for (Category category : viewModel.getPinnedCategory()) {
                if (!hasCategory(category)) {
                    addCategory(category);
                }
            }
        }

        if (viewModel.getEntryTags() != null) {
            for (EntryTag entryTag : viewModel.getEntryTags()) {
                Tag tag = entryTag.getTag();
                if (tag != null) {
                    addTag(entryTag.getTag());
                }
            }
        }

        invalidateDateTime();
        fabMenu.restock();
    }

    private void setTags(List<Tag> tags) {
        tagAdapter.clear();
        tagAdapter.addAll(tags);
        tagAdapter.notifyDataSetChanged();
    }

    private void addMeasurement(Measurement measurement) {
        MeasurementView<?> view = new MeasurementView<>(getContext(), measurement);
        view.setOnCategoryRemovedListener(this::removeCategory);
        measurementContainer.addView(view, 0);
        fabMenu.ignore(measurement.getCategory());
        fabMenu.restock();
    }

    private void addCategory(Category category) {
        Measurement measurement = ObjectFactory.createFromClass(category.toClass());
        addMeasurement(measurement);

        Entry entry = viewModel.getEntry();
        int indexInCache = entry.indexInMeasurementCache(category);
        if (indexInCache != -1) {
            entry.getMeasurementCache().set(indexInCache, measurement);
        } else {
            measurement.setEntry(entry);
            entry.getMeasurementCache().add(measurement);
        }
    }

    private void removeCategory(Category category) {
        int index = indexOf(category);
        if (index != -1) {
            Entry entry = viewModel.getEntry();
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

    private void addTag(String name) {
        Tag tag = tagAdapter.find(name);
        if (tag == null) {
            tag = new Tag();
            tag.setId(-1);
            tag.setName(name);
        }
        addTag(tag);
    }

    private void addTag(Tag tag) {
        int index = viewModel.getIndexOfTag(tag);
        if (index == -1) {
            EntryTag entryTag = new EntryTag();
            entryTag.setEntry(viewModel.getEntry());
            entryTag.setTag(tag);
            viewModel.getEntryTags().add(entryTag);
        }

        ChipView chip = new ChipView(getContext());
        chip.setTag(tag);
        chip.setText(tag.getName());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(view -> removeTag(tag, chip));
        chip.setOnClickListener(view -> removeTag(tag, chip));
        tagListView.addView(chip);

        tagAdapter.set(tag, false);
        dismissTagDropDown();

        tagListView.setVisibility(View.VISIBLE);
    }

    private void removeTag(Tag tag, View view) {
        int index = viewModel.getIndexOfTag(tag);
        if (index != -1) {
            viewModel.getEntryTags().remove(index);
        }

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
        Category[] activeCategories = viewModel.getActiveCategories();
        String[] categoryNames = new String[activeCategories.length];
        boolean[] visibleCategoriesOld = new boolean[activeCategories.length];
        for (int position = 0; position < activeCategories.length; position++) {
            Category category = activeCategories[position];
            categoryNames[position] = getString(category.getStringResId());
            visibleCategoriesOld[position] = hasCategory(category);
        }

        boolean[] visibleCategories = visibleCategoriesOld.clone();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.categories)
            .setMultiChoiceItems(categoryNames, visibleCategoriesOld, (dialog, which, isChecked) -> visibleCategories[which] = isChecked)
            .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                for (int position = activeCategories.length - 1; position >= 0; position--) {
                    Category category = activeCategories[position];
                    if (visibleCategories[position]) {
                        scrollView.smoothScrollTo(0, 0);
                        addCategory(category);
                    } else {
                        removeCategory(category);
                    }
                }
            })
            .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void invalidateDateTime() {
        DateTime dateTime = viewModel.getEntry() != null ? viewModel.getEntry().getDate() : DateTime.now();
        dateButton.setText(Helper.getDateFormat().print(dateTime));
        timeButton.setText(Helper.getTimeFormat().print(dateTime));
    }

    private void invalidateAlarm() {
        alarmButton.setText(viewModel.getAlarmInMinutesAsText(getContext()));
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        List<Measurement> measurements = viewModel.getEntry().getMeasurementCache();
        if (measurements.size() == 0) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(viewModel.getEntry().getNote()) && tagListView.getChildCount() == 0) {
                ViewUtils.showSnackbar(root, getString(R.string.validator_value_none));
                inputIsValid = false;
            }
        } else {
            for (int index = 0; index < measurementContainer.getChildCount(); index++) {
                View view = measurementContainer.getChildAt(index);
                if (view instanceof MeasurementView<?>) {
                    MeasurementView<?> measurementView = (MeasurementView<?>) view;
                    if (!measurementView.getInputView().isValid()) {
                        inputIsValid = false;
                    }
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

    // TODO: Extract into view model
    private void submit() {
        Entry entry = viewModel.getEntry();
        boolean isNewEntry = !entry.isPersisted();
        entry = EntryDao.getInstance().createOrUpdate(entry);

        // TODO: Delete distinct
        List<EntryTag> entryTags = viewModel.getEntryTags();
        if (entryTags != null && entryTags.size() > 0) {
            EntryTagDao.getInstance().delete(entryTags);
        }

        for (Measurement measurement : EntryDao.getInstance().getMeasurements(entry)) {
            boolean isObsolete = !entry.getMeasurementCache().contains(measurement);
            if (isObsolete) {
                MeasurementDao.getInstance(measurement.getClass()).delete(measurement);
            }
        }
        for (Measurement measurement : entry.getMeasurementCache()) {
            MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
        }

        List<Tag> tags = new ArrayList<>();
        entryTags = new ArrayList<>();
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
            Events.post(new EntryUpdatedEvent(entry, entryTags, foodEatenList));
        }

        int alarmInMinutes = viewModel.getAlarmInMinutes();
        if (alarmInMinutes > 0) {
            AlarmUtils.setAlarm(alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE);
        }

        finish();
    }

    private void deleteEntry() {
        Entry entry = viewModel.getEntry();
        if (entry != null) {
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, viewModel.getEntryTags(), getFoodEaten()));
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
                    List<FoodEaten> foodEatenList = new ArrayList<>();
                    for (FoodEaten foodEaten : ((Meal) measurement).getFoodEatenCache()) {
                        if (foodEaten.isValid()) {
                            foodEatenList.add(foodEaten);
                        }
                    }
                    return foodEatenList;
                }
            }
        }
        return new ArrayList<>();
    }

    private void showDatePicker() {
        Entry entry = viewModel.getEntry();
        DatePickerFragment.newInstance(entry.getDate(), dateTime -> {
            if (dateTime != null) {
                LocalTime time = viewModel.getEntry().getDate() != null
                    ? viewModel.getEntry().getDate().toLocalTime()
                    : LocalTime.now();
                dateTime = dateTime.withTime(time);
                viewModel.getEntry().setDate(dateTime);
                invalidateDateTime();
            }
        }).show(getChildFragmentManager());
    }

    private void showTimePicker() {
        Entry entry = viewModel.getEntry();
        TimePickerFragment.newInstance(entry.getDate(), (view, hourOfDay, minute) -> {
            DateTime dateTime = viewModel.getEntry().getDate().withHourOfDay(hourOfDay).withMinuteOfHour(minute);
            viewModel.getEntry().setDate(dateTime);
            invalidateDateTime();
        }).show(getChildFragmentManager());
    }

    private void showAlarmPicker() {
        new NumberPickerDialog(requireContext(), R.string.minutes, viewModel.getAlarmInMinutes(), 0, 10_000, (number) -> {
            viewModel.setAlarmInMinutes(number.intValue());
            invalidateAlarm();
        }).show(getChildFragmentManager());
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