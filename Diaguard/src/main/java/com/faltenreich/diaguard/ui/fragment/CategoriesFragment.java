package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.CategoryListAdapter;
import com.faltenreich.diaguard.adapter.DragDropItemTouchHelperCallback;
import com.faltenreich.diaguard.data.PreferenceHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.listView) RecyclerView list;

    private CategoryListAdapter listAdapter;

    public CategoriesFragment() {
        super(R.layout.fragment_categories, R.string.categories_select);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        setCategories();
    }

    private void initLayout() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new CategoryListAdapter(getContext());
        list.setAdapter(listAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new DragDropItemTouchHelperCallback(listAdapter));
        touchHelper.attachToRecyclerView(list);
    }

    private void setCategories() {
        listAdapter.addItems(PreferenceHelper.getInstance().getSortedCategories());
        listAdapter.notifyDataSetChanged();
    }
}
