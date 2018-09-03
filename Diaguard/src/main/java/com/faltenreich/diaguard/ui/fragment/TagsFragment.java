package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.LinearDividerItemDecoration;
import com.faltenreich.diaguard.adapter.TagListAdapter;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.Tag;

import java.util.List;

import butterknife.BindView;

public class TagsFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.list_placeholder) View placeholder;

    private TagListAdapter listAdapter;

    public TagsFragment() {
        super(R.layout.fragment_list, R.string.tags, -1);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        invalidateLayout();
        loadTags();
    }

    private void initLayout() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.addItemDecoration(new LinearDividerItemDecoration(getContext()));
        listAdapter = new TagListAdapter(getContext());
        list.setAdapter(listAdapter);
    }

    private void invalidateLayout() {
        boolean isEmpty = listAdapter == null || listAdapter.getItemCount() == 0;
        placeholder.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void loadTags() {
        // TODO: Make asynchronous
        List<Tag> tags = TagDao.getInstance().getAll();
        setTags(tags);
    }

    private void setTags(List<Tag> tags) {
        listAdapter.clear();
        listAdapter.addItems(tags);
        listAdapter.notifyDataSetChanged();
        invalidateLayout();
    }
}
