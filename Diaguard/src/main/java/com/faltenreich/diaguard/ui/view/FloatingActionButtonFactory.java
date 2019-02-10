package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.util.Helper;
import com.github.clans.fab.FloatingActionButton;

public class FloatingActionButtonFactory {

    public static FloatingActionButton createFloatingActionButton(Context context, String text, @DrawableRes int imageResourceId, @ColorInt int color) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab.setLabelText(text);
        fab.setImageResource(imageResourceId);
        fab.setColorNormal(color);
        float brighteningPercentage = color == Color.WHITE ? .9f : 1.2f;
        int colorHighlight = Helper.colorBrighten(color, brighteningPercentage);
        fab.setColorPressed(colorHighlight);
        fab.setColorRipple(colorHighlight);
        return fab;
    }
}
