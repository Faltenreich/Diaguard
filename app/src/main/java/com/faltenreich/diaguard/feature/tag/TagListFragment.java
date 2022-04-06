package com.faltenreich.diaguard.feature.tag;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentTagListBinding;
import com.faltenreich.diaguard.feature.navigation.FabDescribing;
import com.faltenreich.diaguard.feature.navigation.FabDescription;
import com.faltenreich.diaguard.feature.navigation.FabProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.event.data.TagSavedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class TagListFragment
    extends BaseFragment<FragmentTagListBinding>
    implements ToolbarDescribing, FabDescribing, TagListener {

    private RecyclerView listView;
    private TextView listPlaceholder;

    private TagListAdapter listAdapter;

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
    public FabDescription getFabDescription() {
        return new FabDescription(FabProperties.addButton((view) -> createTag()), false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        initLayout();
        invalidateLayout();
        loadTags();
    }

    private void bindViews() {
        listView = getBinding().listView;
        listPlaceholder = getBinding().listPlaceholder;
    }

    private void initLayout() {
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
        listAdapter = new TagListAdapter(getContext(), this);
        listView.setAdapter(listAdapter);
    }

    private void invalidateLayout() {
        boolean isEmpty = listAdapter == null || listAdapter.getItemCount() == 0;
        listPlaceholder.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
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
            public List<Tag> onShouldLoad(Context context) {
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
        listView.scrollToPosition(position);
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
            public Long onShouldLoad(Context context) {
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
            public Tag onShouldLoad(Context context) {
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
        new TagEditFragment().show(getParentFragmentManager(), null);
    }

    @Override
    public void onTagDeleted(Tag tag, View view) {
        confirmTagDeletion(tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TagSavedEvent event) {
        addTag(event.context);
    }
}
