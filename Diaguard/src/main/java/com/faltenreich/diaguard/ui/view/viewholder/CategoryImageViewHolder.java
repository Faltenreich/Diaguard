package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategory;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Faltenreich on 17.10.2015.
 */
public class CategoryImageViewHolder extends BaseViewHolder<ListItemCategory> {

    @BindView(R.id.category_image) ImageView imageView;

    public CategoryImageViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindData() {
        ListItemCategory listItem = getListItem();
        int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(listItem.getCategory());
        if (categoryImageResourceId > 0) {
            Picasso.with(getContext()).load(categoryImageResourceId).into(imageView);
        }
    }
}
