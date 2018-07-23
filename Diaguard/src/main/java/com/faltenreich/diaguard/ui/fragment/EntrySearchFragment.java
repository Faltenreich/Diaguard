package com.faltenreich.diaguard.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SafeLinearLayoutManager;
import com.faltenreich.diaguard.adapter.SearchAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.util.Helper;
import com.lapism.searchview.SearchView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EntrySearchFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    public static final String EXTRA_TAG_ID = "tagId";

    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.search_list) RecyclerView list;
    @BindView(R.id.button_datestart) Button buttonDateStart;
    @BindView(R.id.button_dateend) Button buttonDateEnd;
    @BindView(R.id.dateSeparator) TextView dateSeparatorView;
    @BindView(R.id.button_categories) Button buttonCategories;

    private SearchAdapter listAdapter;
    private long tagId = -1;
    private DateTime dateStart;
    private DateTime dateEnd;
    private Measurement.Category[] categories;

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

        searchView.open(true);

        invalidateLayout();
    }

    private void invalidateLayout() {
        if (getContext() != null) {
            @ColorInt int colorActive = ContextCompat.getColor(getContext(), android.R.color.black);
            @ColorInt int colorInactive = ContextCompat.getColor(getContext(), R.color.gray);

            buttonDateStart.setTextColor(dateStart != null ? colorActive : colorInactive);
            buttonDateStart.setText(dateStart != null ? Helper.getDateFormat().print(dateStart) : getString(R.string.datestart));
            buttonDateEnd.setTextColor(dateEnd != null ? colorActive : colorInactive);
            buttonDateEnd.setText(dateEnd != null ? Helper.getDateFormat().print(dateEnd) : getString(R.string.dateend));
            dateSeparatorView.setTextColor(dateStart != null && dateEnd != null ? colorActive : colorInactive);

            String buttonCategoryText;
            if (categories != null && categories.length > 0) {
                String[] categoryNames = new String[categories.length];
                for (int index = 0; index < categories.length; index ++) {
                    Measurement.Category category = categories[index];
                    categoryNames[index] = category.toLocalizedString();
                }
                buttonCategoryText = TextUtils.join(", ", categoryNames);
            } else {
                buttonCategoryText = getString(R.string.categories);
            }
            buttonCategories.setTextColor(categories != null ? colorActive : colorInactive);
            buttonCategories.setText(buttonCategoryText);
        }
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
        if (StringUtils.isNotBlank(query) || dateStart != null || dateEnd != null || categories != null) {
            new SearchTask(searchView.getQuery().toString(), dateStart, dateEnd, categories, new SearchListener() {
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

    @OnClick(R.id.button_datestart)
    public void showStartDatePicker() {
        DatePickerFragment.newInstance(dateStart, null, dateEnd, new DatePickerFragment.DatePickerListener() {
            @Override
            public void onDatePicked(@Nullable DateTime dateTime) {
                dateStart = dateTime;
                invalidateLayout();
                search();
            }
        }).show(getFragmentManager());
    }

    @OnClick(R.id.button_dateend)
    public void showEndDatePicker() {
        DatePickerFragment.newInstance(dateEnd, dateStart, null, new DatePickerFragment.DatePickerListener() {
            @Override
            public void onDatePicked(@Nullable DateTime dateTime) {
                dateEnd = dateTime;
                invalidateLayout();
                search();
            }
        }).show(getFragmentManager());
    }

    @OnClick(R.id.button_categories)
    public void showCategoryPicker() {
        CategoryPickerFragment.newInstance(categories, new CategoryPickerFragment.OnCategorySelectedListener() {
            @Override
            public void onCategoriesSelected(@Nullable Measurement.Category[] categories) {
                EntrySearchFragment.this.categories = categories;
                invalidateLayout();
                search();
            }
        }).show(getFragmentManager());
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
        private DateTime dateStart;
        private DateTime dateEnd;
        private Measurement.Category[] categories;
        private SearchListener listener;

        private SearchTask(String query, DateTime dateStart, DateTime dateEnd, Measurement.Category[] categories, SearchListener listener) {
            this.query = query;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.categories = categories;
            this.listener = listener;
        }

        @Override
        protected List<ListItemEntry> doInBackground(Void... voids) {
            List<ListItemEntry> listItems = new ArrayList<>();
            List<Entry> entries = EntryDao.getInstance().search(query, dateStart, dateEnd);
            for (Entry entry : entries) {
                List<Measurement> measurements = categories != null ? EntryDao.getInstance().getMeasurements(entry, categories) : EntryDao.getInstance().getMeasurements(entry);
                if (measurements.size() > 0) {
                    entry.setMeasurementCache(measurements);
                    List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(entry);
                    listItems.add(new ListItemEntry(entry, entryTags));
                }
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
