package com.faltenreich.diaguard.feature.entry.search;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
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
import com.faltenreich.diaguard.shared.view.search.SearchView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EntrySearchFragment extends BaseFragment implements SearchViewListener {

    private static final String TAG = EntrySearchFragment.class.getSimpleName();
    private static final int SEARCH_INPUT_DELAY_IN_MILLIS = 1000;
    private static final int PAGE_SIZE = 25;

    static final String EXTRA_TAG_ID = "tagId";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.search_list) RecyclerView list;
    @BindView(R.id.search_list_empty) TextView listEmptyView;
    @BindView(R.id.search_list_progress) View progressView;

    private EntrySearchListAdapter listAdapter;
    private long tagId = -1;
    private int currentPage = 0;

    public EntrySearchFragment() {
        super(R.layout.fragment_entry_search, R.string.search);
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
        preFillQuery();
    }

    private void init() {
        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            Bundle arguments = getActivity().getIntent().getExtras();
            tagId = arguments.getLong(EXTRA_TAG_ID, -1);
        }
    }

    private void initLayout() {
        list.setLayoutManager(new SafeLinearLayoutManager(getActivity()));
        listAdapter = new EntrySearchListAdapter(getContext(), (tag, view) -> { if (isAdded()) searchView.setQuery(tag.getName(), true); });
        listAdapter.setOnEndlessListener(scrollingDown -> { if (scrollingDown) continueSearch(); });
        list.setAdapter(listAdapter);

        searchView.setSearchListener(this);

        invalidateEmptyView();
    }

    private void preFillQuery() {
        if (tagId >= 0) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {
                @Override
                public Tag onShouldLoad() {
                    return TagDao.getInstance().getById(tagId);
                }
                @Override
                public void onDidLoad(Tag tag) {
                    if (tag != null) {
                        searchView.setQuery(tag.getName(), false);
                        newSearch();
                    }
                }
            });
        } else {
            // Workaround to focus EditText onViewCreated
            new Handler().postDelayed(() -> searchView.focusSearchField(), 500);
        }
    }

    private void newSearch() {
        int oldCount = listAdapter.getItemCount();
        if (oldCount > 0) {
            listAdapter.clear();
            listAdapter.notifyItemRangeRemoved(0, oldCount);
        }
        currentPage = 0;

        if (StringUtils.isNotBlank(searchView.getQuery())) {
            progressView.setVisibility(View.VISIBLE);
            continueSearch();
        }
        invalidateEmptyView();
    }

    private void continueSearch() {
        final String query = searchView.getQuery();
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<LogEntryListItem>>() {
            @Override
            public List<LogEntryListItem> onShouldLoad() {
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
                String currentQuery = searchView.getQuery();
                if (query.equals(currentQuery)) {
                    currentPage++;
                    int oldCount = listAdapter.getItemCount();
                    listAdapter.addItems(items);
                    listAdapter.notifyItemRangeInserted(oldCount, items.size());
                    progressView.setVisibility(View.GONE);
                    invalidateEmptyView();
                } else {
                    Log.d(TAG, "Dropping obsolete result for \'" + query + "\' (is now: \'" + currentQuery + "\'");
                }
            }
        });
    }

    private void invalidateEmptyView() {
        listEmptyView.setVisibility(progressView.getVisibility() != View.VISIBLE && listAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        listEmptyView.setText(StringUtils.isBlank(searchView.getQuery()) ? R.string.search_prompt : R.string.no_results_found);
    }

    @Override
    public void onQueryChanged(String query) {
        // Delay search in order to reduce obsolete searches
        new Handler().postDelayed(() -> {
            if (query.equals(searchView.getQuery())) {
                newSearch();
            }
        }, SEARCH_INPUT_DELAY_IN_MILLIS);
    }

    @Override
    public void onQueryClosed() {
        finish();
    }
}
