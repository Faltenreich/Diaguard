package com.faltenreich.diaguard.feature.timeline.table;

import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ListItemTableCategoryImageBinding;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.image.ImageLoader;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

class CategoryImageViewHolder extends BaseViewHolder<ListItemTableCategoryImageBinding, CategoryImageListItem> {

    CategoryImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_table_category_image);
        itemView.setOnClickListener((view) -> showCategory());
    }

    @Override
    protected ListItemTableCategoryImageBinding createBinding(View view) {
        return ListItemTableCategoryImageBinding.bind(view);
    }

    @Override
    public void onBind(CategoryImageListItem item) {
        int categoryImageResourceId = item.getCategory().getIconImageResourceId();
        if (categoryImageResourceId > 0) {
            ImageLoader.getInstance().load(categoryImageResourceId, getBinding().imageView);
        }
    }

    private void showCategory() {
        ViewUtils.showToast(getContext(), getContext().getString(getItem().getCategory().getStringResId()));
    }
}
