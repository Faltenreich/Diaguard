package com.faltenreich.diaguard.feature.tag;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class TagViewHolder extends BaseViewHolder<Tag> {

    @BindView(R.id.tag_name) TextView nameView;
    @BindView(R.id.tag_description) TextView descriptionView;
    @BindView(R.id.tag_button_delete) public View deleteButton;

    TagViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_tag);
    }

    @Override
    protected void bindData() {
        final Tag tag = getListItem();
        nameView.setText(tag.getName());

        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Long>() {
            @Override
            public Long onShouldLoad() {
                return EntryTagDao.getInstance().count(tag);
            }
            @Override
            public void onDidLoad(Long count) {
                descriptionView.setText(String.format("%d %s", count, getContext().getString(R.string.entries)));
            }
        });
    }
}
