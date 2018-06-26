package com.faltenreich.diaguard.ui.view.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryImage;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class CategoryImageViewHolder extends BaseViewHolder<ListItemCategoryImage> implements View.OnClickListener {

    @BindView(R.id.category_image) ImageView imageView;

    public CategoryImageViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    @Override
    public void bindData() {
        int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(getListItem().getCategory());
        if (categoryImageResourceId > 0) {
            Picasso picasso = Picasso.with(getContext());
            if (picasso != null) {
                picasso.load(categoryImageResourceId).into(imageView);
            }
        }
    }

    @Override
    public void onClick(View view) {
        ViewUtils.showToast(getContext(), getListItem().getCategory().toLocalizedString());
    }
}
