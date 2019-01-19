package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.util.Helper;
import com.github.clans.fab.FloatingActionButton;

public class FloatingActionButtonFactory {

    public static FloatingActionButton createFloatingActionButton(Context context, String text, @DrawableRes int imageResourceId, @ColorRes int colorResId) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab.setLabelText(text);
        fab.setImageResource(imageResourceId);
        fab.setColorNormalResId(colorResId);
        float brighteningPercentage = colorResId == android.R.color.white ? .9f : 1.2f;
        int colorHighlight = Helper.colorBrighten(ContextCompat.getColor(context, colorResId), brighteningPercentage);
        fab.setColorPressed(colorHighlight);
        fab.setColorRipple(colorHighlight);
        return fab;
    }
}
