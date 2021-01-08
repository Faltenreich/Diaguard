package com.faltenreich.diaguard.feature.entry.search;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentEntrySearchBinding;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragmentFactory;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryViewHolder;
import com.faltenreich.diaguard.feature.navigation.Navigation;
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
import com.faltenreich.diaguard.shared.view.reveal.Reveal;
import com.faltenreich.diaguard.shared.view.reveal.Revealable;
import com.faltenreich.diaguard.shared.view.search.SearchViewListener;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class EntrySearchFragment
    extends BaseFragment<FragmentEntrySearchBinding>
    implements ToolbarDescribing, Revealable, SearchViewListener, LogEntryViewHolder.Listener {

    private static final String TAG = EntrySearchFragment.class.getSimpleName();
    private static final String ARGUMENT_TAG_ID = "tagId";
    private static final String ARGUMENT_REVEAL_X = "revealX";
    private static final String ARGUMENT_REVEAL_Y = "revealY";
    private static final int PAGE_SIZE = 25;

    static EntrySearchFragment newInstance(int revealX, int revealY, long tagId) {
        EntrySearchFragment fragment = new EntrySearchFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_REVEAL_X, revealX);
        arguments.putInt(ARGUMENT_REVEAL_Y, revealY);
        arguments.putLong(ARGUMENT_TAG_ID, tagId);
        fragment.setArguments(arguments);
        return fragment;
    }

    private ViewGroup rootView;
    private RecyclerView listView;
    private EntrySearchListAdapter listAdapter;
    private int currentPage = 0;
    private int revealX;
    private int revealY;
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
        if (savedInstanceState == null) {
            reveal(null);
        }
    }

    private void init() {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            revealX = arguments.getInt(ARGUMENT_REVEAL_X, -1);
            revealY = arguments.getInt(ARGUMENT_REVEAL_Y, -1);
            tagId = arguments.getLong(ARGUMENT_TAG_ID, -1);
        }
    }

    private void bindView() {
        rootView = getBinding().root;
        listView = getBinding().listView;
    }

    private void initLayout() {
        listView.setLayoutManager(new SafeLinearLayoutManager(getActivity()));
        listAdapter = new EntrySearchListAdapter(getContext(), this);
        listAdapter.setOnEndlessListener(scrollingDown -> { if (scrollingDown) continueSearch(); });
        listView.setAdapter(listAdapter);

        getBinding().searchView.setSearchListener(this);

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
                        getBinding().searchView.setQuery(tag.getName(), false);
                        newSearch();
                    }
                }
            });
        } else {
            // Workaround to focus EditText onViewCreated
            new Handler().postDelayed(() -> getBinding().searchView.focusSearchField(), 500);
        }
    }

    private void newSearch() {
        int oldCount = listAdapter.getItemCount();
        if (oldCount > 0) {
            listAdapter.clear();
            listAdapter.notifyItemRangeRemoved(0, oldCount);
        }
        currentPage = 0;

        if (StringUtils.isNotBlank(getBinding().searchView.getQuery())) {
            getBinding().progressIndicator.setVisibility(View.VISIBLE);
            continueSearch();
        }
        invalidateEmptyView();
    }

    private void continueSearch() {
        final String query = getBinding().searchView.getQuery();
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
                String currentQuery = getBinding().searchView.getQuery();
                if (query.equals(currentQuery)) {
                    currentPage++;
                    int oldCount = listAdapter.getItemCount();
                    listAdapter.addItems(items);
                    listAdapter.notifyItemRangeInserted(oldCount, items.size());
                    getBinding().progressIndicator.setVisibility(View.GONE);
                    invalidateEmptyView();
                } else {
                    Log.d(TAG, "Dropping obsolete result for " + query + " (is now: " + currentQuery);
                }
            }
        });
    }

    private void invalidateEmptyView() {
        TextView emptyLabel = getBinding().emptyLabel;
        emptyLabel.setVisibility(getBinding().progressIndicator.getVisibility() != View.VISIBLE && listAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        emptyLabel.setText(StringUtils.isBlank(getBinding().searchView.getQuery()) ? R.string.search_prompt : R.string.no_results_found);
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
    public void reveal(Reveal.Callback callback) {
        Reveal.reveal(rootView, revealX, revealY, callback);
    }

    @Override
    public void unreveal(Reveal.Callback callback) {
        Reveal.unreveal(rootView, revealX, revealY, callback);
    }

    @Override
    public void onEntrySelected(Entry entry) {
        openFragment(EntryEditFragmentFactory.newInstance(entry), Navigation.Operation.REPLACE, true);
    }

    @Override
    public void onTagSelected(Tag tag, View view) {
        if (isAdded()) {
            getBinding().searchView.setQuery(tag.getName(), true);
        }
    }

    @Override
    public void onDateSelected(DateTime dateTime) {
        openFragment(EntryEditFragmentFactory.newInstance(dateTime), Navigation.Operation.REPLACE, true);
    }
}
