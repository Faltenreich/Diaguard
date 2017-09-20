package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;
import com.faltenreich.diaguard.util.SystemUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Filip on 27.05.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.action) TextView actionView;

    private int layoutResourceId;
    private DatabaseHelper databaseHelper;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
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

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public @Nullable TextView getActionView() {
        return actionView;
    }

    public void startActivity(Intent intent, ActivityOptionsCompat options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
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
}
