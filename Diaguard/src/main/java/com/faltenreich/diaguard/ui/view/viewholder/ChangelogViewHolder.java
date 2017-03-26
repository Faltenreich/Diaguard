package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemChangelog;

import butterknife.BindView;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogViewHolder extends BaseViewHolder<ListItemChangelog> {

    @BindView(R.id.changelog_item_text) TextView textView;

    public ChangelogViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        ListItemChangelog listItem = getListItem();
        textView.setText(listItem.getText());
    }
}
