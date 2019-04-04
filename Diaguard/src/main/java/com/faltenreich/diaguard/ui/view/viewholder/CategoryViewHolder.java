package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.async.DataLoader;
import com.faltenreich.diaguard.data.async.DataLoaderListener;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Tag;

import butterknife.BindView;

public class CategoryViewHolder extends BaseViewHolder<Measurement.Category> {

    @BindView(R.id.checkbox) CheckBox checkBox;

    public CategoryViewHolder(View view) {
        super(view);
    }

    @Override
    protected void bindData() {
        Measurement.Category category = getListItem();
        checkBox.setText(category.toLocalizedString(getContext()));
    }
}
