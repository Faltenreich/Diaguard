package com.faltenreich.diaguard.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.SearchAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Entry;
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
        super(R.layout.fragment_entry_search, R.string.search, -1);
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
            new SetupTask(tagId, new SetupListener() {
                @Override
                public void onSetupFinished(Tag tag) {
                    if (tag != null) {
                        searchView.setQuery(tag.getName(), true);
                    }
                }
            }).execute();
        }
    }

    private void search() {
        String query = searchView.getQuery().toString();
        if (StringUtils.isNotBlank(query)) {
            new SearchTask(searchView.getQuery().toString(), new SearchListener() {
                @Override
                public void onSearchFinished(List<ListItemEntry> listItems) {
                    listAdapter.clear();
                    listAdapter.addItems(listItems);
                    listAdapter.notifyDataSetChanged();
                    listEmptyView.setVisibility(listItems.size() > 0 ? View.GONE : View.VISIBLE);
                }
            }).execute();
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

    private static class SetupTask extends AsyncTask<Void, Void, Tag> {

        private long tagId;
        private SetupListener listener;

        private SetupTask(long tagId, SetupListener listener) {
            this.tagId = tagId;
            this.listener = listener;
        }

        @Override
        protected Tag doInBackground(Void... params) {
            return TagDao.getInstance().get(tagId);
        }

        @Override
        protected void onPostExecute(Tag tag) {
            super.onPostExecute(tag);
            listener.onSetupFinished(tag);
        }
    }

    private static class SearchTask extends AsyncTask<Void, Void, List<ListItemEntry>> {

        private String query;
        private SearchListener listener;

        private SearchTask(String query, SearchListener listener) {
            this.query = query;
            this.listener = listener;
        }

        @Override
        protected List<ListItemEntry> doInBackground(Void... voids) {
            List<ListItemEntry> listItems = new ArrayList<>();
            List<Entry> entries = EntryDao.getInstance().search(query);
            for (Entry entry : entries) {
                entry.setMeasurementCache(EntryDao.getInstance().getMeasurements(entry));
                listItems.add(new ListItemEntry(entry, EntryTagDao.getInstance().getAll(entry)));
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItemEntry> listItems) {
            super.onPostExecute(listItems);
            listener.onSearchFinished(listItems);
        }
    }

    private interface SetupListener {
        void onSetupFinished(Tag tag);
    }

    private interface SearchListener {
        void onSearchFinished(List<ListItemEntry> listItems);
    }
}
