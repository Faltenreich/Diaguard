package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;

import com.faltenreich.diaguard.data.entity.Tag;

public class TagViewHolder extends BaseViewHolder<Tag> {

    public TagViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        Tag tag = getListItem();
        // TODO
    }
}
