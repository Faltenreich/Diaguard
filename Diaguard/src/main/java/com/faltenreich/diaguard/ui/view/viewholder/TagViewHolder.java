package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
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
        Tag tag = getListItem();
        nameView.setText(tag.getName());
        descriptionView.setText(String.format("%d %s", EntryTagDao.getInstance().count(tag), getContext().getString(R.string.entries)));
    }
}
