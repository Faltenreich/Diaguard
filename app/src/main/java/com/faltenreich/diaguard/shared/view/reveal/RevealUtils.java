package com.faltenreich.diaguard.shared.view.reveal;

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.RequiresApi;

class RevealUtils {

    private static final int REVEAL_DURATION = 400;
    private static final int UNREVEAL_DURATION = 300;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void reveal(final View view, int positionX, int positionY, final boolean reveal, int duration, Animator.AnimatorListener listener) {
        int radius = (int) Math.hypot(view.getWidth(), view.getHeight());
        int startRadius = reveal ? 0 : radius;
        int endRadius = reveal ? radius : 0;

        Animator animator = ViewAnimationUtils.createCircularReveal(view, positionX, positionY, startRadius, endRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(duration);

        if (listener != null) {
            animator.addListener(listener);
        }

        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void reveal(View view, int positionX, int positionY, boolean reveal, Animator.AnimatorListener listener) {
        reveal(view, positionX, positionY, reveal, reveal ? REVEAL_DURATION : UNREVEAL_DURATION, listener);
    }
}
