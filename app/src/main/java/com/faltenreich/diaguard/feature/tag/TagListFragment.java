package com.faltenreich.diaguard.feature.tag;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentTagListBinding;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TagListFragment extends BaseFragment<FragmentTagListBinding> implements ToolbarDescribing, TagListAdapter.TagListener {

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.list_placeholder)
    View placeholder;

    private TagListAdapter listAdapter;

    public TagListFragment() {
        super(R.layout.fragment_tag_list);
    }

    @Override
    protected FragmentTagListBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentTagListBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.tags)
            .build();
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
        list.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        listAdapter = new TagListAdapter(getContext());
        listAdapter.setTagListener(this);
        list.setAdapter(listAdapter);
    }

    private void invalidateLayout() {
        boolean isEmpty = listAdapter == null || listAdapter.getItemCount() == 0;
        placeholder.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void setTags(List<Tag> tags) {
        listAdapter.clear();
        listAdapter.addItems(tags);
        listAdapter.notifyDataSetChanged();
        invalidateLayout();
    }

    private void loadTags() {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<List<Tag>>() {

            @Override
            public List<Tag> onShouldLoad() {
                return TagDao.getInstance().getAll();
            }

            @Override
            public void onDidLoad(List<Tag> data) {
                setTags(data);
            }
        });
    }

    private void addTag(Tag tag) {
        int position = 0;
        list.scrollToPosition(position);
        listAdapter.addItem(position, tag);
        listAdapter.notifyItemInserted(position);
    }

    private void removeTag(Tag tag) {
        int position = listAdapter.getItemPosition(tag);
        if (position >= 0) {
            listAdapter.removeItem(position);
            listAdapter.notifyItemRemoved(position);
        }
    }

    private void confirmTagDeletion(final Tag tag) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Long>() {

            @Override
            public Long onShouldLoad() {
                return EntryTagDao.getInstance().count(tag);
            }

            @Override
            public void onDidLoad(Long data) {
                new AlertDialog.Builder(getContext())
                    .setTitle(R.string.tag_delete)
                    .setMessage(String.format(getString(R.string.tag_delete_confirmation), data))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    })
                    .setPositiveButton(R.string.ok, (dialog, which) -> deleteTag(tag))
                    .create()
                    .show();
            }
        });
    }

    private void deleteTag(final Tag tag) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Tag>() {

            @Override
            public Tag onShouldLoad() {
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(tag);
                EntryTagDao.getInstance().delete(entryTags);
                int result = TagDao.getInstance().delete(tag);
                return result > 0 ? tag : null;
            }

            @Override
            public void onDidLoad(@Nullable Tag result) {
                if (result != null) {
                    removeTag(result);
                }
            }
        });
    }

    private void createTag() {
        TagEditFragment fragment = new TagEditFragment();
        fragment.setListener(result -> {
            if (result != null) {
                addTag(result);
            }
        });
        fragment.show(getParentFragmentManager(), null);
    }

    @Override
    public void onTagDeleted(Tag tag, View view) {
        confirmTagDeletion(tag);
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        createTag();
    }
}
