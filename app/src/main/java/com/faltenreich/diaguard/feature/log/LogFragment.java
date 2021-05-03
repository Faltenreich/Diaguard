package com.faltenreich.diaguard.feature.log;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentLogBinding;
import com.faltenreich.diaguard.feature.datetime.DatePicking;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragmentFactory;
import com.faltenreich.diaguard.feature.entry.search.EntrySearchFragment;
import com.faltenreich.diaguard.feature.log.empty.LogEmptyListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
import com.faltenreich.diaguard.shared.event.file.BackupImportedEvent;
import com.faltenreich.diaguard.shared.event.preference.CategoryPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.StickyHeaderDecoration;
import com.faltenreich.diaguard.shared.view.recyclerview.layoutmanager.SafeLinearLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Filip on 05.07.2015.
 */
public class LogFragment
    extends BaseFragment<FragmentLogBinding>
    implements DatePicking, ToolbarDescribing, MainButton, LogListAdapter.Listener
{

    private DateTime day;

    private RecyclerView listView;
    private ProgressBar progressIndicator;

    private LogListAdapter listAdapter;
    private StickyHeaderDecoration listDecoration;
    private LinearLayoutManager listLayoutManager;

    @Override
    protected FragmentLogBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentLogBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        String title = null;
        if (day != null) {
            boolean isLargeTitle = ViewUtils.isLandscape(getActivity()) || ViewUtils.isLargeScreen(getActivity());
            String format = isLargeTitle ? "MMMM YYYY" : "MMM YYYY";
            title = day.toString(format);
        }
        return new ToolbarProperties.Builder()
            .setTitle(title)
            .setMenu(R.menu.log)
            .setOnClickListener((view) -> showDatePicker(day, getParentFragmentManager()))
            .build();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                // Date will not be passed through to compensate negative user feedback
                openFragment(new EntryEditFragment(), true);
            }
        }, true);
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
        goToDay(day);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLabels();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_today) {
            goToDay(DateTime.now());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        day = DateTimeUtils.atStartOfDay(DateTime.now());
        listAdapter = new LogListAdapter(getActivity(), this);
    }

    private void bindViews() {
        listView = getBinding().listView;
        progressIndicator = getBinding().progressIndicator;
    }

    private void initLayout() {
        listLayoutManager = new SafeLinearLayoutManager(getActivity());
        listView.setLayoutManager(listLayoutManager);
        listView.setAdapter(listAdapter);
        listDecoration = new StickyHeaderDecoration(listAdapter, true);
        listView.addItemDecoration(listDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new LogSwipeCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(listView);

        // Fragment updates
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                DateTime firstVisibleDay = getFirstVisibleDay();
                if (firstVisibleDay != null) {
                    day = firstVisibleDay;
                    // Update month in Toolbar when section is being crossed
                    boolean isScrollingUp = dy < 0;
                    boolean isCrossingMonth = isScrollingUp ?
                            day.dayOfMonth().get() == day.dayOfMonth().getMaximumValue() :
                        day.dayOfMonth().get() == day.dayOfMonth().getMinimumValue();
                    if (isCrossingMonth) {
                        updateLabels();
                    }
                }
            }
        });
    }

    private DateTime getFirstVisibleDay() {
        int firstVisibleItemPosition = listLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < listAdapter.getItemCount()) {
            LogListItem item = listAdapter.getItem(listLayoutManager.findFirstVisibleItemPosition());
            return item.getDateTime();
        } else {
            return null;
        }
    }

    @Override
    public void goToDay(DateTime day) {
        this.day = day;
        updateLabels();

        int position = listAdapter.getDayPosition(day);
        if (position >= 0) {
            listView.scrollToPosition(position);
        } else {
            progressIndicator.setVisibility(View.VISIBLE);
            listAdapter.setup(day);
        }
    }

    private void updateLabels() {
        setTitle(getToolbarProperties().getTitle());
    }

    @Override
    public void onOrderChanges() {
        invalidateSections();
    }

    @Override
    public void onSetupEnd() {
        if (isAdded()) {
            progressIndicator.setVisibility(View.GONE);
            goToDay(day);
        }
    }

    @Override
    public void onEntrySelected(Entry entry) {
        if (isAdded()) {
            openFragment(EntryEditFragmentFactory.newInstance(entry), true);
        }
    }

    @Override
    public void onTagSelected(Tag tag, View view) {
        if (isAdded()) {
            openFragment(EntrySearchFragment.newInstance(tag), true);
        }
    }

    @Override
    public void onDateSelected(DateTime dateTime) {
        if (isAdded()) {
            openFragment(EntryEditFragmentFactory.newInstance(dateTime), true);
        }
    }

    private void invalidateSections() {
        if (isAdded() && listDecoration != null) {
            listDecoration.clearHeaderCache();
        }
    }

    private void updateHeaderSection(DateTime dateTime) {
        int position = listAdapter.getFirstListItemEntryOfDayPosition(dateTime);
        if (position >= 0) {
            LogEntryListItem firstListItemEntry = (LogEntryListItem) listAdapter.getItem(position);
            while (listAdapter.getItem(position).getDateTime().withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay()) &&
                    listAdapter.getItem(position) instanceof LogEntryListItem) {
                LogEntryListItem listItem = (LogEntryListItem) listAdapter.getItem(position);
                listItem.setFirstListItemEntryOfDay(firstListItemEntry);
                position++;
            }
        }
        invalidateSections();
    }

    private void addEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        if (entry != null) {
            DateTime date = entry.getDate();
            int position = listAdapter.getNextDateTimePosition(date);
            if (position >= 0) {

                // Remove any existing empty view
                int previousPosition = position - 1;
                LogListItem previousListItem = listAdapter.getItem(previousPosition);
                if (previousListItem instanceof LogEmptyListItem && previousListItem.getDateTime().getDayOfYear() == date.getDayOfYear()) {
                    listAdapter.removeItem(previousPosition);
                    listAdapter.notifyItemRemoved(previousPosition);
                    position = previousPosition;
                }

                entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));

                LogEntryListItem listItemEntry = new LogEntryListItem(entry, entryTags, foodEatenList);
                listAdapter.addItem(position, listItemEntry);
                listAdapter.notifyItemInserted(position);

                updateHeaderSection(date);
            }
        }
    }

    private void removeEntry(Entry entry) {
        if (entry != null) {
            int position = listAdapter.getEntryPosition(entry);
            if (position >= 0) {
                removeEntry(position);
            }
        }
    }

    private void removeEntry(int position) {
        DateTime dateTime = listAdapter.getItem(position).getDateTime();
        listAdapter.removeItem(position);

        // Add empty view if there is no entry available anymore for this day
        boolean hasNoMoreEntries = listAdapter.getFirstListItemEntryOfDayPosition(dateTime) == -1;
        if (hasNoMoreEntries) {
            listAdapter.addItem(position, new LogEmptyListItem(dateTime));
            listAdapter.notifyItemChanged(position);
        } else {
            listAdapter.notifyItemRemoved(position);
        }

        updateHeaderSection(dateTime);
    }

    private void updateEntry(Entry entry, List<EntryTag> entryTags, List<FoodEaten> foodEatenList) {
        if (entry != null) {
            int originalPosition = listAdapter.getEntryPosition(entry);
            if (originalPosition >= 0) {
                int updatedPosition = listAdapter.getNextDateTimePosition(entry.getDate()) - 1;
                if (originalPosition == updatedPosition) {
                    Object listItem = listAdapter.getItem(originalPosition);
                    if (listItem instanceof LogEntryListItem) {
                        LogEntryListItem listItemEntry = (LogEntryListItem) listItem;
                        listItemEntry.setEntry(entry);
                        listItemEntry.setEntryTags(entryTags);
                        listItemEntry.setFoodEatenList(foodEatenList);
                        listAdapter.notifyItemChanged(originalPosition);
                    }
                } else {
                    removeEntry(originalPosition);
                    addEntry(entry, entryTags, foodEatenList);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        addEntry(event.context, event.entryTags, event.foodEatenList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        super.onEvent(event);
        removeEntry(event.context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        updateEntry(event.context, event.entryTags, event.foodEatenList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        progressIndicator.setVisibility(View.VISIBLE);
        listAdapter.setup(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BackupImportedEvent event) {
        progressIndicator.setVisibility(View.VISIBLE);
        listAdapter.setup(day);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CategoryPreferenceChangedEvent event) {
        progressIndicator.setVisibility(View.VISIBLE);
        listAdapter.setup(day);
    }
}
