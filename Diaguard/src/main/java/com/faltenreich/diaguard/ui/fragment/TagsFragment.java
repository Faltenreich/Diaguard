package com.faltenreich.diaguard.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.LinearDividerItemDecoration;
import com.faltenreich.diaguard.adapter.TagListAdapter;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.TagDao;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Tag;
import com.faltenreich.diaguard.ui.activity.EntrySearchActivity;
import com.faltenreich.diaguard.util.SystemUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TagsFragment extends BaseFragment implements TagListAdapter.TagListener {

    @BindView(R.id.list) RecyclerView list;
    @BindView(R.id.list_placeholder) View placeholder;

    private TagListAdapter listAdapter;

    public TagsFragment() {
        super(R.layout.fragment_tags, R.string.tags, -1);
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
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTag(tag);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deleteTag(final Tag tag) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Void>() {
            @Override
            public Void onShouldLoad() {
                List<EntryTag> entryTags = EntryTagDao.getInstance().getAll(tag);
                EntryTagDao.getInstance().delete(entryTags);
                TagDao.getInstance().delete(tag);
                return null;
            }
            @Override
            public void onDidLoad(Void data) {
                loadTags();
            }
        });
    }

    private void showDialogForNewTag() {
        final EditText editText = new EditText(getContext());
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.tag_new)
                .setView(editText)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createTag(editText.getText().toString());
                    }
                })
                .create()
                .show();
    }

    private void createTag(final String name) {
        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Boolean>() {
            @Override
            public Boolean onShouldLoad() {
                // TODO: Check for empty and duplicate tags
                boolean isValid = true;
                if (isValid) {
                    Tag tag = new Tag();
                    tag.setName(name);
                    TagDao.getInstance().createOrUpdate(tag);
                }
                return isValid;
            }
            @Override
            public void onDidLoad(Boolean isValid) {
                if (isValid) {
                    loadTags();
                    SystemUtils.hideKeyboard(getActivity());
                } else {
                    // TODO: Do not dismiss dialog
                }
            }
        });
    }

    @Override
    public void onTagSelected(Tag tag, View view) {
        EntrySearchActivity.show(getContext(), tag, null);
    }

    @Override
    public void onTagDeleted(Tag tag, View view) {
        confirmTagDeletion(tag);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        showDialogForNewTag();
    }
}
