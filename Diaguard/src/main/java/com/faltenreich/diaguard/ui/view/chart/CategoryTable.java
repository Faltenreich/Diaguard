package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.faltenreich.diaguard.adapter.CategoryRecyclerAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryTable extends RecyclerView {

    private static final int SKIP_EVERY_X_HOUR = 2;

    CategoryRecyclerAdapter adapter;

    private DateTime day;
    private Measurement.Category[] categories;

    public CategoryTable(Context context) {
        super(context);
        setup();
    }

    public CategoryTable(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup();
    }

    public CategoryTable(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);
        setup();
    }

    public void setDay(DateTime day) {
        this.day = day;
        new UpdateDataTask().execute();
    }

    private void setup() {
        if (!isInEditMode()) {
            Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
            categories = Arrays.copyOfRange(activeCategories, 1, activeCategories.length);
            addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CategoryRecyclerAdapter(getContext());
            setAdapter(adapter);
        }
    }

    private class UpdateDataTask extends AsyncTask<Void, Void, List<ListItemCategoryValues>> {

        protected List<ListItemCategoryValues> doInBackground(Void... params) {
            LinkedHashMap<Measurement.Category, float[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, SKIP_EVERY_X_HOUR);
            List<ListItemCategoryValues> rowList = new ArrayList<>();
            for (Map.Entry<Measurement.Category, float[]> mapEntry : values.entrySet()) {
                rowList.add(new ListItemCategoryValues(mapEntry.getKey(), mapEntry.getValue()));
            }
            return rowList;
        }

        protected void onPostExecute(List<ListItemCategoryValues> measurements) {
            adapter.clear();
            adapter.addItems(measurements);
            adapter.notifyDataSetChanged();
        }
    }
}
