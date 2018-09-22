package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.SearchAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.FoodEatenDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.FoodEaten;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.lapism.searchview.SearchView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EntrySearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    public static final String EXTRA_TAG_ID = "tagId";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.search_list) RecyclerView list;
    @BindView(R.id.search_list_empty) View listEmptyView;

    private SearchAdapter listAdapter;
    private long tagId = -1;

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
        listEmptyView.setVisibility(View.VISIBLE);

        list.setLayoutManager(new SafeLinearLayoutManager(getActivity()));
        listAdapter = new SearchAdapter(getContext(), new SearchAdapter.OnSearchItemClickListener() {
            @Override
            public void onTagClicked(Tag tag, View view) {
                if (isAdded()) {
                    searchView.setQuery(tag.getName(), true);
                }
            }
        });
        list.setAdapter(listAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.setOnMenuClickListener(this);
        searchView.setArrowOnly(false);
    }

    private void preFillQuery() {
        if (tagId >= 0) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {
                @Override
                public Tag onShouldLoad() {
                    return TagDao.getInstance().get(tagId);
                }
                @Override
                public void onDidLoad(Tag tag) {
                    if (tag != null) {
                        searchView.setQuery(tag.getName(), true);
                    }
                }
            });
        }
    }

    private void search() {
        final String query = searchView.getQuery().toString();
        if (StringUtils.isNotBlank(query)) {
            DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<ListItemEntry>>() {
                @Override
                public List<ListItemEntry> onShouldLoad() {
                    List<ListItemEntry> listItems = new ArrayList<>();
                    List<Entry> entries = EntryDao.getInstance().search(query);
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
                        listItems.add(new ListItemEntry(entry, entryTags, foodEatenList));
                    }
                    return listItems;
                }
                @Override
                public void onDidLoad(List<ListItemEntry> listItems) {
                    listAdapter.clear();
                    listAdapter.addItems(listItems);
                    listAdapter.notifyDataSetChanged();
                    listEmptyView.setVisibility(listItems.size() > 0 ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
            listEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.close(true);
        search();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search();
        return false;
    }

    @Override
    public void onMenuClick() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);
        } else {
            finish();
        }
    }

}
