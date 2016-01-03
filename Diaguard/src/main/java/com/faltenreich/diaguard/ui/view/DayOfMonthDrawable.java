package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.faltenreich.diaguard.R;

/**
 * Created by Filip on 11.07.2015.
 */
public class DayOfMonthDrawable extends Drawable {

    private static final int OFFSET = 8;

    private String mDayOfMonth = "1";
    private final Paint mPaint;
    private final Rect mTextBounds = new Rect();
    private static float mTextSize = 14;

    public DayOfMonthDrawable(Context c) {
        mTextSize = c.getResources().getDimension(R.dimen.today_icon_text_size);
        mPaint = new Paint();
        mPaint.setAlpha(255);
        mPaint.setColor(Color.WHITE);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.getTextBounds(mDayOfMonth, 0, mDayOfMonth.length(), mTextBounds);
        int textHeight = mTextBounds.bottom - mTextBounds.top;
        Rect bounds = getBounds();
        canvas.drawText(mDayOfMonth, bounds.right / 2, ((float) bounds.bottom + textHeight + 1 + OFFSET) / 2,
                mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // Ignore
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public void setDayOfMonth(int day) {
        mDayOfMonth = Integer.toString(day);
        invalidateSelf();
    }
}
