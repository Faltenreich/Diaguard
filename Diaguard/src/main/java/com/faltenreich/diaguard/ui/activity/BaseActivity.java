package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.Vector2D;
import com.faltenreich.diaguard.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public static void show(Context context, Class<? extends BaseActivity> clazz, View from) {
        Intent intent = new Intent(context, clazz);
        Vector2D position = ViewUtils.getPositionOnScreen(from);
        intent.putExtra(BaseActivity.ARGUMENT_REVEAL_X, position.x);
        intent.putExtra(BaseActivity.ARGUMENT_REVEAL_Y, position.y);
        context.startActivity(intent);
    }

    private static final String ARGUMENT_REVEAL_X = "revealX";
    private static final String ARGUMENT_REVEAL_Y = "revealY";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.action) TextView actionView;
    @Nullable @BindView(R.id.root) ViewGroup rootLayout;

    private int layoutResourceId;
    private int revealX;
    private int revealY;

    private BaseActivity() {
        // Forbidden
    }

    public BaseActivity(@LayoutRes int layoutResourceId) {
        this();
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResourceId);
        ButterKnife.bind(this);

        init();

        if (savedInstanceState == null) {
            reveal();
        }
    }

    @Override
    public void finish() {
        unreveal();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public @Nullable TextView getActionView() {
        return actionView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SystemUtils.PERMISSION_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Events.post(new PermissionGrantedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                } else {
                    Events.post(new PermissionDeniedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                }
            }
        }
    }

    private void init() {
        OpenFoodFactsManager.getInstance().start();

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    private void reveal() {
        if (rootLayout != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                revealX = getIntent().getIntExtra(ARGUMENT_REVEAL_X, -1);
                revealY = getIntent().getIntExtra(ARGUMENT_REVEAL_Y, -1);
                if (revealX >= 0 && revealY >= 0) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
                                Animator animation = ViewAnimationUtils.createCircularReveal(rootLayout, revealX, revealY, 0, finalRadius);
                                animation.setDuration(400);
                                animation.setInterpolator(new AccelerateInterpolator());
                                rootLayout.setVisibility(View.VISIBLE);
                                animation.start();
                            }
                        });
                    }
                } else {
                    rootLayout.setVisibility(View.VISIBLE);
                }
            } else {
                rootLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void unreveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rootLayout != null && revealX >= 0 && revealY >= 0) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator animation = ViewAnimationUtils.createCircularReveal(rootLayout, revealX, revealY, finalRadius, 0);
            animation.setDuration(400);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    BaseActivity.super.finish();
                }
            });
            animation.start();
        } else {
            super.finish();
        }
    }
}
