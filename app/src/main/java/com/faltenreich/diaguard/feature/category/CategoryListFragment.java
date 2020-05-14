package com.faltenreich.diaguard.feature.category;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.CategoryPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.drag.DragDropItemTouchHelperCallback;

import java.util.List;

import butterknife.BindView;

public class CategoryListFragment extends BaseFragment implements CategoryListAdapter.Listener {

    @BindView(R.id.listView) RecyclerView list;

    private CategoryListAdapter listAdapter;
    private ItemTouchHelper itemTouchHelper;
    private boolean hasChanged;

    public CategoryListFragment() {
        super(R.layout.fragment_categories, R.string.categories, R.menu.categories);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        setCategories();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        if (hasChanged) {
            Events.post(new CategoryPreferenceChangedEvent());
        }
        super.onDestroy();
    }

    private void initLayout() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new CategoryListAdapter(getContext(), this);
        list.setAdapter(listAdapter);
        itemTouchHelper = new ItemTouchHelper(new DragDropItemTouchHelperCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(list);
    }

    private void setCategories() {
        listAdapter.addItems(PreferenceHelper.getInstance().getSortedCategories());
        listAdapter.notifyDataSetChanged();
    }

    private void showHelp() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.categories)
                .setMessage(R.string.category_preference_desc)
                .setPositiveButton(R.string.ok, (dlg, which) -> { })
                .create()
                .show();
    }

    @Override
    public void onReorderStart(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onReorderEnd() {
        List<Category> categories = listAdapter.getItems();
        for (int sortIndex = 0; sortIndex < categories.size(); sortIndex++) {
            PreferenceHelper.getInstance().setCategorySortIndex(categories.get(sortIndex), sortIndex);
        }
        CategoryComparatorFactory.getInstance().invalidate();
        hasChanged = true;
    }

    @Override
    public void onCheckedChange() {
        hasChanged = true;
    }
}
