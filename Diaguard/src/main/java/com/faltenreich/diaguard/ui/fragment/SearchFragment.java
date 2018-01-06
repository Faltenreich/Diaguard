package com.faltenreich.diaguard.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.SearchAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Measurement;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 06.01.2018
 */

public class SearchFragment extends BaseFragment {

    @BindView(R.id.search_input) EditText input;
    @BindView(R.id.search_list) RecyclerView list;

    private SearchAdapter listAdapter;

    public SearchFragment() {
        super(R.layout.fragment_search, R.string.search, -1);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        list.setLayoutManager(new SafeLinearLayoutManager(getActivity()));
        listAdapter = new SearchAdapter(getContext());
        list.setAdapter(listAdapter);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });
    }

    private void search() {
        String query = input.getText().toString();
        if (!TextUtils.isEmpty(query)) {
            new SearchTask(query, new SearchListener() {
                @Override
                public void onSearchFinished(List<ListItemEntry> listItems) {
                    listAdapter.clear();
                    listAdapter.addItems(listItems);
                    listAdapter.notifyDataSetChanged();
                }
            }).execute();
        } else {
            listAdapter.clear();
            listAdapter.notifyDataSetChanged();
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
            List<Entry> entries = EntryDao.getInstance().getAllForQuery(query);
            for (Entry entry : entries) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                entry.setMeasurementCache(measurements);
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                listItems.add(new ListItemEntry(entry, entryTags));
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(List<ListItemEntry> listItems) {
            super.onPostExecute(listItems);
            listener.onSearchFinished(listItems);
        }
    }

    private interface SearchListener {
        void onSearchFinished(List<ListItemEntry> listItems);
    }
}
