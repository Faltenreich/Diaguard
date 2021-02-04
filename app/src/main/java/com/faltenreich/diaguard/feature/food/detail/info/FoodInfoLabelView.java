package com.faltenreich.diaguard.feature.food.detail.info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ViewFoodLabelBinding;
import com.faltenreich.diaguard.shared.view.ViewBindable;

/**
 * Created by Faltenreich on 29.10.2016.
 */

public class FoodInfoLabelView extends LinearLayout implements ViewBindable<ViewFoodLabelBinding> {

    private static final @DrawableRes int DEFAULT_ICON_RES_ID = R.drawable.ic_info;

    public enum Type {
        DEFAULT(R.color.gray_darker),
        WARNING(R.color.yellow),
        ERROR(R.color.red_dark);

        public @ColorRes
        final int color;

        Type(@ColorRes int color) {
            this.color = color;
        }
    }

    private ViewFoodLabelBinding binding;

    private String text;
    private final Type type;
    @DrawableRes private final int iconResId;

    public FoodInfoLabelView(Context context) {
        super(context);
        this.type = Type.DEFAULT;
        this.iconResId = DEFAULT_ICON_RES_ID;
        bindView();
    }

    public FoodInfoLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.type = Type.DEFAULT;
        this.iconResId = DEFAULT_ICON_RES_ID;
        bindView();
    }

    public FoodInfoLabelView(Context context, String text) {
        super(context);
        this.text = text;
        this.type = Type.DEFAULT;
        this.iconResId = DEFAULT_ICON_RES_ID;
        bindView();
    }

    @Override
    public ViewFoodLabelBinding getBinding() {
        return binding;
    }

    private void bindView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_label, this);
        binding = ViewFoodLabelBinding.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int color = ContextCompat.getColor(getContext(), type.color);

        TextView label = getBinding().label;
        label.setText(text);
        label.setTextColor(color);

        ImageView imageView = getBinding().imageView;
        imageView.setImageResource(iconResId);
        imageView.setColorFilter(color);
    }
}
