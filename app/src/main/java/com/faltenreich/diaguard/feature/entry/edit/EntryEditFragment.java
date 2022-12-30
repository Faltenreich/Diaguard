package com.faltenreich.diaguard.feature.entry.edit;

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
import com.faltenreich.diaguard.feature.category.CategoryListFragment;
import com.faltenreich.diaguard.feature.datetime.DatePicker;
import com.faltenreich.diaguard.feature.datetime.TimePicker;
import com.faltenreich.diaguard.feature.entry.edit.measurement.MeasurementView;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.navigation.FabDescribing;
import com.faltenreich.diaguard.feature.navigation.FabDescription;
import com.faltenreich.diaguard.feature.navigation.FabProperties;
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
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.data.reflect.ObjectFactory;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.event.ui.FoodSearchEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.chip.ChipView;
import com.faltenreich.diaguard.shared.view.edittext.EditTextUtils;
import com.faltenreich.diaguard.shared.view.edittext.StickyHintInputView;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
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
    implements ToolbarDescribing, FabDescribing
{

    private static final String TAG = EntryEditFragment.class.getSimpleName();

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_ENTRY_ID = "entryId";
    public static final String EXTRA_FOOD_ID = "foodId";
    public static final String EXTRA_DATE = "date";

    public static EntryEditFragment newInstance(@Nullable Entry entry) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (entry != null) {
            Bundle arguments = new Bundle();
            arguments.putLong(EntryEditFragment.EXTRA_ENTRY_ID, entry.getId());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public static EntryEditFragment newInstance(@Nullable Food food) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (food != null) {
            Bundle arguments = new Bundle();
            arguments.putLong(EntryEditFragment.EXTRA_FOOD_ID, food.getId());
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    public static EntryEditFragment newInstance(@Nullable DateTime dateTime) {
        EntryEditFragment fragment = new EntryEditFragment();
        if (dateTime != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(EntryEditFragment.EXTRA_DATE, dateTime);
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    private final EntryEditViewModel viewModel = new EntryEditViewModel();
    private boolean isRecreated;

    private NestedScrollView scrollView;
    private Button dateButton;
    private Button timeButton;
    private AutoCompleteTextView tagInput;
    private ChipGroup tagListView;
    private TagAutoCompleteAdapter tagAdapter;
    private ImageView tagEditButton;
    private EditText noteInput;
    private ViewGroup alarmContainer;
    private StickyHintInputView alarmInput;
    private LinearLayout measurementContainer;

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
    public FabDescription getFabDescription() {
        return new FabDescription(FabProperties.confirmButton(view -> trySubmit()), false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setArguments(getArguments());
        tagAdapter = new TagAutoCompleteAdapter(requireContext());
        setArguments(null);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRecreated = scrollView != null;
        bindViews();
        initLayout();
        initData();
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
        scrollView = getBinding().scrollView;
        dateButton = getBinding().dateButton;
        timeButton = getBinding().timeButton;
        tagInput = getBinding().tagInput;
        tagListView = getBinding().tagListView;
        tagEditButton = getBinding().tagEditButton;
        noteInput = getBinding().noteInput;
        alarmContainer = getBinding().alarmContainer;
        alarmInput = getBinding().alarmInput;
        measurementContainer = getBinding().measurementContainer;
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
        tagEditButton.setOnClickListener(view -> openTagSettings());

        EditTextUtils.afterTextChanged(noteInput, () -> viewModel.getEntry().setNote(noteInput.getText().toString()));

        alarmContainer.setVisibility(viewModel.isEditing() ? View.GONE : View.VISIBLE);
        // FIXME: alarmInput.setOnClickListener(view -> requestPermissionToPostNotification());
        alarmInput.setEndIconOnClickListener(view -> alarmInput.setText(null));
        // Workaround: Display hint inside EditText instead of TextInputLayout
        alarmInput.getEditText().setHint(R.string.alarm_reminder);
        EditTextUtils.afterTextChanged(
            alarmInput.getEditText(),
            () -> viewModel.setAlarmInMinutes(alarmInput.getText())
        );
    }

    private void initData() {
        viewModel.observeEntry(requireContext(), this::setEntry);
    }

    private void setEntry(@NonNull Entry entry) {
        noteInput.setText(entry.getNote());

        for (Category category : viewModel.getActiveCategories()) {
            if (!hasCategory(category)) {
                Measurement persisted = null;
                List<Measurement> measurements = entry.getMeasurementCache();
                if (measurements != null && !measurements.isEmpty()) {
                    for (Measurement measurement : measurements) {
                        if (measurement.getCategory() == category) {
                            persisted = measurement;
                            break;
                        }
                    }
                }
                if (persisted != null) {
                    addMeasurement(persisted);
                } else {
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
    }

    private void setTags(List<Tag> tags) {
        tagAdapter.addAll(tags);
        tagAdapter.notifyDataSetChanged();
    }

    private void addMeasurement(Measurement measurement) {
        MeasurementView<?> view = new MeasurementView<>(getContext(), measurement);
        measurementContainer.addView(view, measurementContainer.getChildCount());
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

    private void invalidateDateTime() {
        DateTime dateTime = viewModel.getEntry() != null ? viewModel.getEntry().getDate() : DateTime.now();
        dateButton.setText(Helper.getDateFormat().print(dateTime));
        timeButton.setText(Helper.getTimeFormat().print(dateTime));
    }

    private boolean inputIsValid() {
        boolean inputIsValid = true;

        List<Measurement> measurements = viewModel.getMeasurements();
        if (measurements.isEmpty()) {
            // Allow entries with no measurements but with a note or tag
            if (StringUtils.isBlank(viewModel.getEntry().getNote()) && tagListView.getChildCount() == 0) {
                ViewUtils.showSnackbar(scrollView, getString(R.string.validator_value_none));
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

    protected void trySubmit() {
        // Convenience: Accept tag that hasn't been submitted by user
        String missingTag = tagInput.getText().toString();
        if (!StringUtils.isBlank(missingTag)) {
            addTag(missingTag);
            tagInput.setText(null);
        }

        if (inputIsValid()) {
            submit();
        }
    }

    private void submit() {
        Entry entry = viewModel.getEntry();
        List<Measurement> measurements = viewModel.getMeasurements();
        entry.setMeasurementCache(measurements);
        boolean isNewEntry = !entry.isPersisted();
        entry = EntryDao.getInstance().createOrUpdate(entry);

        for (Measurement measurement : EntryDao.getInstance().getMeasurements(entry)) {
            boolean isObsolete = !measurements.contains(measurement);
            if (isObsolete) {
                MeasurementDao.getInstance(measurement.getClass()).delete(measurement);
            }
        }
        for (Measurement measurement : measurements) {
            MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
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
        // TODO: Update instead of delete
        EntryTagDao.getInstance().deleteAll(entry);
        EntryTagDao.getInstance().bulkCreateOrUpdate(entryTags);

        List<FoodEaten> foodEatenList = getFoodEaten();

        // Force update in order to synchronize the measurement cache
        entry = EntryDao.getInstance().getById(entry.getId());
        entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));

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
            finish();
            Events.unregister(this);
            Events.post(new EntryDeletedEvent(entry, viewModel.getEntryTags(), getFoodEaten()));
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
        new DatePicker.Builder()
            .date(entry.getDate())
            .callback(dateTime -> {
                if (dateTime != null) {
                    LocalTime time = viewModel.getEntry().getDate() != null
                        ? viewModel.getEntry().getDate().toLocalTime()
                        : LocalTime.now();
                    dateTime = dateTime.withTime(time);
                    viewModel.getEntry().setDate(dateTime);
                    invalidateDateTime();
                }
            })
            .build()
            .show(getChildFragmentManager());
    }

    private void showTimePicker() {
        new TimePicker.Builder()
            .time(viewModel.getEntry().getDate())
            .callback(time -> {
                viewModel.getEntry().setDate(time);
                invalidateDateTime();
            })
            .build()
            .show(getChildFragmentManager());
    }

    private void requestPermissionToPostNotification() {
        Events.post(new PermissionRequestEvent(Permission.POST_NOTIFICATIONS, PermissionUseCase.REMINDER));
    }

    // TODO: Add option to open category settings (e.g. via button in section header)
    private void openCategorySettings() {
        openFragment(new CategoryListFragment(), true);
    }

    private void openTagSettings() {
        openFragment(new TagListFragment(), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoodSearchEvent event) {
        openFragment(FoodSearchFragment.newInstance(true), true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.POST_NOTIFICATIONS &&
            event.useCase == PermissionUseCase.REMINDER &&
            !event.isGranted
        ) {
            ViewUtils.showSnackbar(getView(), getString(R.string.error_missing_permission));
        }
    }
}