package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.export.FileListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements FileListener {

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

    private ProgressDialog progressDialog;

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
                            importBackup(data.getData());
                        } else {
                            // TODO
                        }
                        break;
                    default:
                        // TODO
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

    public void searchForBackup() {
        FileUtils.searchFiles(this, Export.CSV_MIME_TYPE, BaseActivity.REQUEST_CODE_BACKUP_IMPORT);
        // TODO: Fallback if searchFiles() fails
    }

    private void importBackup(Uri uri) {
        showProgressDialog();
        File file = new File(uri.getPath());
        Export.importCsv(this, file);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.backup_import));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onProgress(String message) {
        progressDialog.setMessage(message);
    }

    @Override
    public void onComplete(File file, String mimeType) {
        progressDialog.dismiss();
        Toast.makeText(this, getString(R.string.backup_complete), Toast.LENGTH_SHORT).show();
    }
}