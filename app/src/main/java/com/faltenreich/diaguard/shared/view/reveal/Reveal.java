package com.faltenreich.diaguard.shared.view.reveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

public class Reveal {

    public static void reveal(View view, int revealX, int revealY, @Nullable Callback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && revealX >= 0 && revealY >= 0) {
            view.setVisibility(View.INVISIBLE);
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        view.setVisibility(View.VISIBLE);
                        RevealUtils.reveal(view, revealX, revealY, true, new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (callback != null) {
                                    callback.onEnd();
                                }
                            }
                        });
                    }
                });
            }
        } else if (callback != null) {
            callback.onEnd();
        }
    }

    public static void unreveal(View view, int revealX, int revealY, @Nullable Callback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && revealX >= 0 && revealY >= 0) {
            RevealUtils.reveal(view, revealX, revealY, false, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                    if (callback != null) {
                        callback.onEnd();
                    }
                }
            });
        } else if (callback != null) {
            callback.onEnd();
        }
    }

    public interface Callback {
        void onEnd();
    }
}
