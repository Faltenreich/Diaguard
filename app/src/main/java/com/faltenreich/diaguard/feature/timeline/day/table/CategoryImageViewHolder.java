package com.faltenreich.diaguard.feature.timeline.day.table;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

import butterknife.BindView;

class CategoryImageViewHolder extends BaseViewHolder<CategoryImageListItem> implements View.OnClickListener {

    @BindView(R.id.category_image) ImageView imageView;

    CategoryImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_table_category_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onBind(CategoryImageListItem item) {
        int categoryImageResourceId = getItem().getCategory().getIconImageResourceId();
        if (categoryImageResourceId > 0) {
            ImageLoader.getInstance().load(categoryImageResourceId, imageView);
        }
    }

    @Override
    public void onClick(View view) {
        Category category = getItem().getCategory();
        ViewUtils.showToast(getContext(), getContext().getString(category.getStringResId()));
    }
}
