package com.faltenreich.diaguard.feature.entry.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentEntrySearchBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.feature.navigation.SearchOwner;
import com.faltenreich.diaguard.feature.navigation.SearchProperties;
import com.faltenreich.diaguard.feature.navigation.Searching;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.layoutmanager.SafeLinearLayoutManager;
import com.faltenreich.diaguard.shared.view.search.SearchViewListener;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class EntrySearchFragment
    extends BaseFragment<FragmentEntrySearchBinding>
    implements ToolbarDescribing, Searching, SearchViewListener, LogEntryViewHolder.Listener {

    private static final String TAG = EntrySearchFragment.class.getSimpleName();
    private static final String ARGUMENT_TAG_ID = "tagId";
    private static final int PAGE_SIZE = 25;

    public static EntrySearchFragment newInstance(Tag tag) {
        EntrySearchFragment fragment = new EntrySearchFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_TAG_ID, tag.getId());
        fragment.setArguments(arguments);
        return fragment;
    }

    private RecyclerView listView;
    private ProgressBar progressIndicator;
    private TextView emptyLabel;

    private EntrySearchListAdapter listAdapter;
    private int currentPage = 0;
    private long tagId = -1;

    @Override
    protected FragmentEntrySearchBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentEntrySearchBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.search)
            .build();
    }

    @Override
    public SearchProperties getSearchProperties() {
        return new SearchProperties.Builder(this)
            .setHint(getString(R.string.search_hint))
            .build();
    }

    @Override
    @Nullable
    public SearchOwner getSearchOwner() {
        return (SearchOwner) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
        initLayout();
        preFillQuery();
    }

    private void init() {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            tagId = arguments.getLong(ARGUMENT_TAG_ID, -1);
        }
    }

    private void bindView() {
        listView = getBinding().listView;
        progressIndicator = getBinding().progressIndicator;
        emptyLabel = getBinding().emptyLabel;
    }

    private void initLayout() {
        listView.setLayoutManager(new SafeLinearLayoutManager(getActivity()));
        listAdapter = new EntrySearchListAdapter(getContext(), this);
        listAdapter.setOnEndlessListener(scrollingDown -> { if (scrollingDown) continueSearch(); });
        listView.setAdapter(listAdapter);

        invalidateEmptyView();
    }

    private void preFillQuery() {
        if (tagId >= 0) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {
                @Override
                public Tag onShouldLoad(Context context) {
                    return TagDao.getInstance().getById(tagId);
                }
                @Override
                public void onDidLoad(Tag tag) {
                    if (tag != null && getSearchOwner() != null) {
                        getSearchOwner().setSearchQuery(tag.getName(), false);
                        newSearch();
                    }
                }
            });
        } else {
            // Workaround to focus EditText onViewCreated
            new Handler().postDelayed(() -> {
                if (getSearchOwner() != null) {
                    getSearchOwner().getSearchView().focusSearchField();
                    newSearch();
                }
            }, 500);
        }
    }

    private void newSearch() {
        int oldCount = listAdapter.getItemCount();
        if (oldCount > 0) {
            listAdapter.clear();
            listAdapter.notifyItemRangeRemoved(0, oldCount);
        }
        currentPage = 0;

        String query = getSearchOwner() != null ? getSearchOwner().getSearchQuery() : null;
        if (StringUtils.isNotBlank(query)) {
            progressIndicator.setVisibility(View.VISIBLE);
            continueSearch();
        }
        invalidateEmptyView();
    }

    private void continueSearch() {
        final String query = getSearchOwner() != null ? getSearchOwner().getSearchQuery() : "";
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<LogEntryListItem>>() {
            @Override
            public List<LogEntryListItem> onShouldLoad(Context context) {
                List<LogEntryListItem> listItems = new ArrayList<>();
                List<Entry> entries = EntryDao.getInstance().search(query, currentPage, PAGE_SIZE);
                for (Entry entry : entries) {
                    List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                    entry.setMeasurementCache(measurements);
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    List<FoodEaten> foodEatenList = new ArrayList<>();
                    for (Measurement measurement : measurements) {
                        if (measurement instanceof Meal) {
                            foodEatenList.addAll(FoodEatenDao.getInstance().getAll((Meal) measurement));
                        }
                    }
                    listItems.add(new LogEntryListItem(entry, entryTags, foodEatenList));
                }
                return listItems;
            }
            @Override
            public void onDidLoad(List<LogEntryListItem> items) {
                String currentQuery = getSearchOwner().getSearchQuery();
                if (query.equals(currentQuery)) {
                    currentPage++;
                    int oldCount = listAdapter.getItemCount();
                    listAdapter.addItems(items);
                    listAdapter.notifyItemRangeInserted(oldCount, items.size());
                    progressIndicator.setVisibility(View.GONE);
                    invalidateEmptyView();
                } else {
                    Log.d(TAG, "Dropping obsolete result for " + query + " (is now: " + currentQuery);
                }
            }
        });
    }

    private void invalidateEmptyView() {
        String query = getSearchOwner() != null ? getSearchOwner().getSearchQuery() : null;
        emptyLabel.setVisibility(progressIndicator.getVisibility() != View.VISIBLE && listAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        emptyLabel.setText(StringUtils.isBlank(query) ? R.string.search_prompt : R.string.no_results_found);
    }

    @Override
    public void onQueryChanged(String query) {
        if (isAdded()) {
            newSearch();
        }
    }

    @Override
    public void onQueryClosed() {
        finish();
    }

    @Override
    public void onEntrySelected(Entry entry) {
        openFragment(EntryEditFragment.newInstance(entry), true);
    }

    @Override
    public void onTagSelected(Tag tag, View view) {
        if (isAdded() && getSearchOwner() != null) {
            getSearchOwner().setSearchQuery(tag.getName(), true);
        }
    }

    @Override
    public void onDateSelected(DateTime dateTime) {
        openFragment(EntryEditFragment.newInstance(dateTime), true);
    }
}
