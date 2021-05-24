package com.faltenreich.diaguard.feature.tag;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemTagBinding;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

class TagViewHolder extends BaseViewHolder<ListItemTagBinding, Tag> {

    private final TagListener listener;

    TagViewHolder(ViewGroup parent, TagListener listener) {
        super(parent, R.layout.list_item_tag);
        this.listener = listener;
        initLayout();
    }

    @Override
    protected ListItemTagBinding createBinding(View view) {
        return ListItemTagBinding.bind(view);
    }

    private void initLayout() {
        getBinding().deleteButton.setOnClickListener((view) -> {
            if (listener != null) {
                listener.onTagDeleted(getItem(), view);
            }
        });
    }

    @Override
    protected void onBind(Tag item) {
        getBinding().nameLabel.setText(item.getName());

        DataLoader.getInstance().load(getContext(), new DataLoaderListener<Long>() {
            @Override
            public Long onShouldLoad(Context context) {
                return EntryTagDao.getInstance().count(item);
            }
            @Override
            public void onDidLoad(Long count) {
                getBinding().descriptionLabel.setText(String.format("%d %s", count, getContext().getString(R.string.entries)));
            }
        });
    }
}
