package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 29.10.2016.
 */

public class FoodLabelView extends LinearLayout {

    public enum Type {

        DEFAULT(R.drawable.bg_round_green),
        WARNING(R.drawable.bg_round_yellow),
        ERROR(R.drawable.bg_round_red);

        public @DrawableRes int background;

        Type(@DrawableRes int background) {
            this.background = background;
        }
    }

    @BindView(R.id.food_label) TextView label;

    private String text;
    private Type type;

    public FoodLabelView(Context context) {
        super(context);
        this.type = Type.DEFAULT;
        init();
    }

    public FoodLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.type = Type.DEFAULT;
        init();
    }

    public FoodLabelView(Context context, String text) {
        super(context);
        this.text = text;
        this.type = Type.DEFAULT;
        init();
    }

    public FoodLabelView(Context context, String text, Type type) {
        super(context);
        this.text = text;
        this.type = type;
        init();
    }

    public FoodLabelView(Context context, @StringRes int textResId) {
        super(context);
        this.text = getContext().getString(textResId);
        this.type = Type.DEFAULT;
        init();
    }

    public FoodLabelView(Context context, @StringRes int textResId, Type type) {
        super(context);
        this.text = getContext().getString(textResId);
        this.type = type;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_food_label, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        label.setText(text);
        label.setBackgroundResource(type.background);
    }
}
