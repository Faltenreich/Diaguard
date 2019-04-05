package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.entity.Tag;

import butterknife.BindView;

public class TagViewHolder extends BaseViewHolder<Tag> {

    @BindView(R.id.tag_name) TextView nameView;
    @BindView(R.id.tag_description) TextView descriptionView;
    @BindView(R.id.tag_button_delete) public View deleteButton;

    public TagViewHolder(View view) {
        super(view);
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
