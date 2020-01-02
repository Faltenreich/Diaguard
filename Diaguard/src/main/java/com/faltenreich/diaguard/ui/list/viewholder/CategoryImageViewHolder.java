package com.faltenreich.diaguard.ui.list.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryImage;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.image.ImageLoader;

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
            ImageLoader.getInstance().load(categoryImageResourceId, imageView);
        }
    }

    @Override
    public void onClick(View view) {
        Measurement.Category category = getListItem().getCategory();
        ViewUtils.showToast(getContext(), getContext().getString(category.getStringResId()));
    }
}
