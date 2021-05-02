package com.faltenreich.diaguard.feature.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentCategoryListBinding;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.CategoryPreferenceChangedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.drag.DragDropItemTouchHelperCallback;

import java.util.List;

public class CategoryListFragment extends BaseFragment<FragmentCategoryListBinding> implements ToolbarDescribing, CategoryListAdapter.Listener {

    private CategoryListAdapter listAdapter;
    private ItemTouchHelper itemTouchHelper;
    private boolean hasChanged;

    @Override
    protected FragmentCategoryListBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentCategoryListBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.categories)
            .setMenu(R.menu.categories)
            .build();
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
        if (item.getItemId() == R.id.action_help) {
            showHelp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (hasChanged) {
            Events.post(new CategoryPreferenceChangedEvent());
        }
        super.onDestroy();
    }

    private void initLayout() {
        RecyclerView listView = getBinding().listView;
        listAdapter = new CategoryListAdapter(getContext(), this);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(listAdapter);
        itemTouchHelper = new ItemTouchHelper(new DragDropItemTouchHelperCallback(listAdapter));
        itemTouchHelper.attachToRecyclerView(listView);
    }

    private void setCategories() {
        listAdapter.clear();
        listAdapter.addItems(PreferenceStore.getInstance().getSortedCategories());
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
            PreferenceStore.getInstance().setCategorySortIndex(categories.get(sortIndex), sortIndex);
        }
        CategoryComparatorFactory.getInstance().invalidate();
        hasChanged = true;
    }

    @Override
    public void onCheckedChange() {
        hasChanged = true;
    }
}
