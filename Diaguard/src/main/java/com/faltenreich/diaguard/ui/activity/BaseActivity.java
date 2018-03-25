package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.SearchManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.event.PermissionGrantedEvent;
import com.faltenreich.diaguard.networking.openfoodfacts.OpenFoodFactsManager;
import com.faltenreich.diaguard.ui.view.SearchViewListener;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Filip on 27.05.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int SEARCH_REVEAL_DURATION = 400;

    @Nullable @BindView(R.id.toolbar_container) ViewGroup toolbarContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @Nullable @BindView(R.id.toolbar_search) Toolbar toolbarSearch;

    private int layoutResourceId;
    private DatabaseHelper databaseHelper;
    private MenuItem searchMenuItem;
    private SearchView searchView;

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
            case R.id.action_search_open:
                toggleSearchBar(true);
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
        return toolbarTitle;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(null);
        }
        initToolbar();
        initSearchBar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initSearchBar() {
        if (toolbarSearch != null) {
            toolbarSearch.inflateMenu(R.menu.search);
            Menu menu = toolbarSearch.getMenu();
            searchMenuItem = menu.findItem(R.id.action_search_view);

            toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleSearchBar(false);
                }
            });

            if (searchMenuItem != null) {
                searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
                SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (BaseActivity.this instanceof SearchViewListener) {
                            ((SearchViewListener) BaseActivity.this).onSearchViewQueryChange(newText);
                        }
                        return true;
                    }
                });
                MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        if (BaseActivity.this instanceof SearchViewListener) {
                            ((SearchViewListener) BaseActivity.this).onSearchViewExpanded();
                        }
                        return true;
                    }
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        toggleSearchBar(false);
                        if (BaseActivity.this instanceof SearchViewListener) {
                            ((SearchViewListener) BaseActivity.this).onSearchViewCollapsed();
                        }
                        return true;
                    }
                });
            }
        }
    }

    private void toggleSearchBar(final boolean show) {
        boolean changes = toolbarSearch != null && (show && toolbarSearch.getVisibility() == View.INVISIBLE || !show && toolbarSearch.getVisibility() == View.VISIBLE);
        if (changes) {
            toolbarSearch.setVisibility(View.VISIBLE);
            if (show) {
                searchMenuItem.expandActionView();
                searchView.clearFocus();
            }
            ViewUtils.reveal(toolbarSearch, toolbarSearch.getWidth(), toolbarSearch.getHeight() / 2, show, SEARCH_REVEAL_DURATION, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!show) {
                        toolbarSearch.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
    }
}
