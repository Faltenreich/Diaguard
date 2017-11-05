package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.faltenreich.diaguard.adapter.CategoryRecyclerAdapter;
import com.faltenreich.diaguard.adapter.SimpleDividerItemDecoration;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.TimelineTableTask;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryTable extends RecyclerView {

    private CategoryRecyclerAdapter adapter;
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

    private void update() {
        new TimelineTableTask(getContext(), day, categories, new BaseAsyncTask.OnAsyncProgressListener<List<ListItemCategoryValues>>() {
            @Override
            public void onPostExecute(List<ListItemCategoryValues> values) {
                // Other notify methods lead to rendering issues on view paging
                adapter.clear();
                adapter.addItems(values);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public void setDay(DateTime day) {
        this.day = day;
        update();
    }

    public void scrollTo(int yOffset) {
        // Other scroll methods do not work in this case
        scrollBy(0, yOffset - computeVerticalScrollOffset());
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
}
