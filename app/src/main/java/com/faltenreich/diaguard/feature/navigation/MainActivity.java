package com.faltenreich.diaguard.feature.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityMainBinding;
import com.faltenreich.diaguard.feature.calculator.CalculatorFragment;
import com.faltenreich.diaguard.feature.changelog.ChangelogFragment;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;
import com.faltenreich.diaguard.feature.dashboard.DashboardFragment;
import com.faltenreich.diaguard.feature.entry.search.EntrySearchFragment;
import com.faltenreich.diaguard.feature.export.ExportFragment;
import com.faltenreich.diaguard.feature.food.search.FoodSearchFragment;
import com.faltenreich.diaguard.feature.log.LogFragment;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.overview.PreferenceOverviewFragment;
import com.faltenreich.diaguard.feature.statistic.StatisticFragment;
import com.faltenreich.diaguard.feature.timeline.TimelineFragment;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.coordinatorlayout.SlideOutBehavior;
import com.faltenreich.diaguard.shared.view.search.SearchView;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity
    extends BaseActivity<ActivityMainBinding>
    implements Navigating, ToolbarOwner, SearchOwner, OnFragmentChangeListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private SearchView searchView;
    private FloatingActionButton fab;

    @Override
    protected ActivityMainBinding createBinding(LayoutInflater layoutInflater) {
        return ActivityMainBinding.inflate(layoutInflater);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public TextView getTitleView() {
        return toolbarTitle;
    }

    @Override
    public SearchView getSearchView() {
        return searchView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceStore.getInstance().setDefaultValues(this);
        bindView();
        initLayout();
        checkChangelog();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            openFragment(new EntrySearchFragment(), Navigation.Operation.REPLACE, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentChanged(Fragment fragment) {
        Fragment visibleFragment = Navigation.getCurrentFragment(getSupportFragmentManager(), R.id.container);
        if (fragment != null && fragment == visibleFragment) {
            invalidateLayout(fragment);
        }
    }

    private void bindView() {
        drawerLayout = getBinding().drawerLayout;
        navigationView = getBinding().navigationView;
        toolbar = getBinding().toolbarContainer.toolbar;
        toolbarTitle = getBinding().toolbarContainer.toolbarTitle;
        searchView = getBinding().searchView;
        fab = getBinding().fab;
    }
    
    private void initLayout() {
        ToolbarManager.applyToolbar(this, getToolbar());

        drawerToggle = new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.getMenu().findItem(R.id.nav_calculator).setVisible(ApplicationConfig.isCalculatorEnabled());
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            selectMenuItem(menuItem);
            return true;
        });
        drawerToggle.setToolbarNavigationClickListener(v -> {
            if (drawerToggle.isDrawerIndicatorEnabled()) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().popBackStackImmediate();
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
        });

        // Setup start fragment
        int startScreen = PreferenceStore.getInstance().getStartScreen();
        MenuItem menuItem = navigationView.getMenu().getItem(startScreen);
        selectMenuItem(menuItem);
    }

    private void invalidateLayout(@NonNull Fragment fragment) {
        Log.d(getClass().getSimpleName(), "Invalidating layout for " + fragment.getClass().getSimpleName());
        invalidateToolbar(fragment instanceof ToolbarDescribing ? (ToolbarDescribing) fragment : null);
        invalidateSearch(fragment instanceof Searching ? (Searching) fragment : null);
        invalidateMainButton(fragment instanceof MainButton ? (MainButton) fragment : null);
        invalidateNavigationDrawer(fragment);
    }

    private void invalidateToolbar(@Nullable ToolbarDescribing toolbarDescribing) {
        if (toolbarDescribing != null) {
            setTitle(toolbarDescribing.getToolbarProperties().getTitle());
        }
    }

    private void invalidateSearch(@Nullable Searching searching) {
        if (searching != null) {
            SearchProperties properties = searching.getSearchProperties();
            searchView.setVisibility(View.VISIBLE);
            searchView.setHint(properties.getHint());
            searchView.setSearchListener(properties.getListener());
            searchView.setAction(properties.getAction());
            searchView.setSuggestions(properties.getSuggestions());
        } else {
            searchView.setVisibility(View.GONE);
        }
    }

    private void invalidateMainButton(@Nullable MainButton mainButton) {
        MainButtonProperties properties = mainButton != null ? mainButton.getMainButtonProperties() : null;
        fab.setVisibility(properties != null ? View.VISIBLE : View.GONE);
        fab.setImageResource(properties != null ? properties.getIconDrawableResId() : android.R.color.transparent);
        fab.setOnClickListener(properties != null ? properties.getOnClickListener() : null);
        if (properties != null) {
            CoordinatorLayout.Behavior<?> behavior = ViewUtils.getBehavior(fab);
            if (behavior instanceof SlideOutBehavior) {
                ((SlideOutBehavior) behavior).setSlideOut(properties.slideOut());
            }
        }
    }

    private void resetMainButton() {
        if (fab.getTranslationY() != 0) {
            fab.animate().cancel();
            fab.animate().translationY(0).start();
        }
    }

    private void invalidateNavigationDrawer(Fragment fragment) {
        drawerLayout.closeDrawer(GravityCompat.START);

        MainFragmentType mainFragmentType = MainFragmentType.valueOf(fragment.getClass());
        if (mainFragmentType != null) {
            int position = mainFragmentType.position;
            if (position < navigationView.getMenu().size()) {
                MenuItem menuItem = navigationView.getMenu().getItem(position);
                selectMenuItemInNavigationView(menuItem);
            }
        }
    }

    private void selectMenuItem(MenuItem menuItem) {
        if (menuItem != null) {
            Navigation.clearBackStack(getSupportFragmentManager());
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_home) {
                openFragment(new DashboardFragment(), Navigation.Operation.REPLACE, false);
            } else if (itemId == R.id.nav_timeline) {
                openFragment(new TimelineFragment(), Navigation.Operation.REPLACE, false);
            } else if (itemId == R.id.nav_log) {
                openFragment(new LogFragment(), Navigation.Operation.REPLACE, false);
            } else if (itemId == R.id.nav_calculator) {
                openFragment(new CalculatorFragment(), Navigation.Operation.REPLACE, true);
            } else if (itemId == R.id.nav_food_database) {
                openFragment(FoodSearchFragment.newInstance(), Navigation.Operation.REPLACE, true);
            } else if (itemId == R.id.nav_statistics) {
                openFragment(new StatisticFragment(), Navigation.Operation.REPLACE, true);
            } else if (itemId == R.id.nav_export) {
                openFragment(new ExportFragment(), Navigation.Operation.REPLACE, true);
            } else if (itemId == R.id.nav_settings) {
                openFragment(new PreferenceOverviewFragment(), Navigation.Operation.REPLACE, true);
            }
            selectMenuItemInNavigationView(menuItem);
        }
    }

    private void selectMenuItemInNavigationView(MenuItem menuItem) {
        if (menuItem != null) {
            // First uncheck all, then check current Fragment
            for (int index = 0; index < navigationView.getMenu().size(); index++) {
                navigationView.getMenu().getItem(index).setChecked(false);
            }
            menuItem.setChecked(true);
        }
    }

    @Deprecated
    public void openFragment(@IdRes int itemId) {
        MenuItem menuItem = navigationView.getMenu().findItem(itemId);
        selectMenuItem(menuItem);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, @NonNull Navigation.Operation operation, boolean addToBackStack) {
        resetMainButton();
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.container, operation, addToBackStack);
    }

    private void checkChangelog() {
        int oldVersionCode = PreferenceStore.getInstance().getVersionCode();
        int currentVersionCode = SystemUtils.getVersionCode(this);
        if (oldVersionCode > 0) {
            boolean isUpdate = oldVersionCode < currentVersionCode;
            if (isUpdate) {
                PreferenceStore.getInstance().setVersionCode(currentVersionCode);
                // TODO: openChangelog() if needed
            }
        } else {
            // Skip changelog for fresh installs
            PreferenceStore.getInstance().setVersionCode(currentVersionCode);
        }
    }

    private void openChangelog() {
        ChangelogFragment fragment = new ChangelogFragment();
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragment.show(fragmentTransaction, tag);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
