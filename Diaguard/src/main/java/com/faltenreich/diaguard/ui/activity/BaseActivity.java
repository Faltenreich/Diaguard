package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.FileProvidedEvent;
import com.faltenreich.diaguard.event.FileProvidedFailedEvent;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewUtils;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final int REQUEST_CODE_BACKUP_IMPORT = 25151;

    static final String ARGUMENT_REVEAL_X = "revealX";
    static final String ARGUMENT_REVEAL_Y = "revealY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.action)
    TextView actionView;
    @Nullable
    @BindView(R.id.root)
    ViewGroup rootLayout;

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
        } else {
            onViewShown();
        }
    }

    /**
     * Called after the activity is created and its view revealed
     */
    @CallSuper
    protected void onViewShown() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && revealX >= 0 && revealY >= 0) {
            overridePendingTransition(0, 0);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BACKUP_IMPORT:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null && data.getData() != null) {
                            try {
                                Uri uri = data.getData();
                                getContentResolver().openFileDescriptor(uri, "r");
                                Events.post(new FileProvidedEvent(uri));
                            } catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                                Events.post(new FileProvidedFailedEvent());
                            }
                        } else {
                            Events.post(new FileProvidedFailedEvent());
                        }
                        break;
                    default:
                        Events.post(new FileProvidedFailedEvent());
                        break;
                }
                break;
            default:
                Log.d(TAG, "Ignoring unknown result with request code" + requestCode);
                break;
        }
    }

    @Override
    public void finish() {
        unreveal();
    }

    public @Nullable
    TextView getActionView() {
        return actionView;
    }

    private void init() {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealX = getIntent().getIntExtra(ARGUMENT_REVEAL_X, -1);
            revealY = getIntent().getIntExtra(ARGUMENT_REVEAL_Y, -1);
            if (revealX >= 0 && revealY >= 0) {
                rootLayout.setVisibility(View.INVISIBLE); // Fail fast and early
                ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onGlobalLayout() {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            rootLayout.setVisibility(View.VISIBLE);
                            ViewUtils.reveal(rootLayout, revealX, revealY, true, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    onViewShown();
                                }
                            });
                        }
                    });
                }
            } else {
                onViewShown();
            }
        } else {
            onViewShown();
        }
    }

    protected void unreveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rootLayout != null && revealX >= 0 && revealY >= 0) {
            ViewUtils.reveal(rootLayout, revealX, revealY, false, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    BaseActivity.super.finish();
                }
            });
        } else {
            super.finish();
        }
    }
}