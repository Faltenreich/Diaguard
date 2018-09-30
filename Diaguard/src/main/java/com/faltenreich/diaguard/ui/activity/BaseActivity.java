package com.faltenreich.diaguard.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
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
import com.faltenreich.diaguard.event.PermissionRequestEvent;
import com.faltenreich.diaguard.event.PermissionResponseEvent;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.Vector2D;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionManager;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final int REQUEST_CODE_BACKUP_IMPORT = 25151;

    static final String ARGUMENT_REVEAL_X = "revealX";
    static final String ARGUMENT_REVEAL_Y = "revealY";

    protected static <T extends BaseActivity> Intent getIntent(Class<T> clazz, Context context, @Nullable View source) {
        Intent intent = new Intent(context, clazz);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && source != null) {
            Vector2D position = ViewUtils.getPositionOnScreen(source);
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_X, position.x + (source.getWidth() / 2));
            intent.putExtra(BaseActivity.ARGUMENT_REVEAL_Y, position.y + (source.getHeight() / 2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        return intent;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitleView;
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
        } else {
            onViewShown();
        }
    }

    /**
     * Called after the activity is created and its view fully revealed
     */
    @CallSuper
    protected void onViewShown() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    protected void onPause() {
        Events.unregister(this);
        super.onPause();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String codes[], @NonNull int[] grantResults) {
        PermissionUseCase useCase = PermissionUseCase.fromRequestCode(requestCode);
        if (useCase != null) {
            for (String code : codes) {
                Permission permission = Permission.fromCode(code);
                if (permission != null) {
                    boolean isGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    Events.post(new PermissionResponseEvent(permission, useCase, isGranted));
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
                            Events.post(new FileProvidedEvent(data.getData()));
                        } else {
                            Events.post(new FileProvidedFailedEvent());
                        }
                        break;
                    default:
                        // Ignore
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

    @Nullable
    public TextView getTitleView() {
        return toolbarTitleView;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbarTitleView != null) {
            toolbarTitleView.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    public void setToolbarBackgroundColor(@ColorRes int colorResId) {
        if (toolbar != null) {
            toolbar.setBackgroundResource(colorResId);
        }
    }

    private void init() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setTitle(SystemUtils.getLabelForActivity(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    private void reveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealX = getIntent().getIntExtra(ARGUMENT_REVEAL_X, -1);
            revealY = getIntent().getIntExtra(ARGUMENT_REVEAL_Y, -1);
            if (rootLayout != null && revealX >= 0 && revealY >= 0) {
                rootLayout.setVisibility(View.INVISIBLE);
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

    private void unreveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && rootLayout != null && revealX >= 0 && revealY >= 0) {
            ViewUtils.reveal(rootLayout, revealX, revealY, false, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    BaseActivity.super.finish();
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            super.finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionRequestEvent event) {
        if (PermissionManager.getInstance().hasPermission(this, event.context)) {
            Events.post(new PermissionResponseEvent(event.context, event.useCase, true));
        } else {
            PermissionManager.getInstance().requestPermission(this, event.context, event.useCase);
        }
    }
}