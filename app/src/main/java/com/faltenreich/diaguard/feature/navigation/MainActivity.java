package com.faltenreich.diaguard.feature.navigation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

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
import com.faltenreich.diaguard.feature.preference.data.PreferenceCache;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.preference.overview.PreferenceOverviewFragment;
import com.faltenreich.diaguard.feature.shortcut.Shortcuts;
import com.faltenreich.diaguard.feature.statistic.StatisticFragment;
import com.faltenreich.diaguard.feature.timeline.TimelineFragment;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.coordinatorlayout.SlideOutBehavior;
import com.faltenreich.diaguard.shared.view.search.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity
    extends BaseActivity<ActivityMainBinding>
    implements Navigating, ToolbarOwner, SearchOwner, OnFragmentChangeListener, PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private SearchView searchView;
    private ViewGroup fabGroup;
    private FloatingActionButton fabPrimary;
    private FloatingActionButton fabSecondary;

    private float fabPrimaryOffset;
    private float fabSecondaryOffset;

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

        if (Shortcuts.handleShortcut(this, getIntent())) {
            return;
        }

        checkChangelog();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        // Must be executed before Fragment.onViewCreated
        PreferenceCache.getInstance().setLocale(Helper.getLocale(this));
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            openFragment(new EntrySearchFragment(), true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        Navigation.openFragment(fragment, getSupportFragmentManager(), R.id.container, addToBackStack);
    }

    @Override
    public void onFragmentChanged(Fragment fragment) {
        Fragment visibleFragment = Navigation.getCurrentFragment(getSupportFragmentManager(), R.id.container);
        if (fragment != null && fragment == visibleFragment) {
            invalidateLayout(fragment);
        }
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference preference) {
        Fragment fragment = Navigation.instantiateFragment(preference, getSupportFragmentManager(), getClassLoader(), caller);
        openFragment(fragment, true);
        return true;
    }

    private void bindView() {
        drawerLayout = getBinding().drawerLayout;
        navigationView = getBinding().navigationView;
        toolbar = getBinding().toolbarContainer.toolbar;
        toolbarTitle = getBinding().toolbarContainer.toolbarTitle;
        searchView = getBinding().searchView;
        fabGroup = getBinding().fabGroup;
        fabPrimary = getBinding().fabPrimary;
        fabSecondary = getBinding().fabSecondary;
    }
    
    private void initLayout() {
        fabPrimary.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float target = drawerLayout.getHeight();
                fabPrimaryOffset = target - ViewUtils.getPositionInParent(fabPrimary, drawerLayout).y;
                fabSecondaryOffset = target - ViewUtils.getPositionInParent(fabSecondary, drawerLayout).y;
                fabPrimary.getViewTreeObserver().removeOnPreDrawListener(this);

                Fragment fragment = Navigation.getCurrentFragment(getSupportFragmentManager(), R.id.container);
                FabDescribing describing = fragment instanceof ToolbarDescribing ? (FabDescribing) fragment : null;
                invalidateFab(describing);

                return false;
            }
        });

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

        getSupportFragmentManager().addOnBackStackChangedListener(() ->
            drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0)
        );

        // Setup start fragment
        int startScreen = PreferenceStore.getInstance().getStartScreen();
        MenuItem menuItem = navigationView.getMenu().getItem(startScreen);
        selectMenuItem(menuItem);
    }

    private void invalidateLayout(@NonNull Fragment fragment) {
        Log.d(getClass().getSimpleName(), "Invalidating layout for " + fragment.getClass().getSimpleName());
        invalidateToolbar(fragment instanceof ToolbarDescribing ? (ToolbarDescribing) fragment : null);
        invalidateSearch(fragment instanceof Searching ? (Searching) fragment : null);
        invalidateFab(fragment instanceof FabDescribing ? (FabDescribing) fragment : null);
        invalidateNavigationDrawer(fragment);
    }

    private void invalidateToolbar(@Nullable ToolbarDescribing toolbarDescribing) {
        if (toolbarDescribing != null) {
            ToolbarProperties properties = toolbarDescribing.getToolbarProperties();
            setTitle(properties.getTitle());
            View titleView = getTitleView();
            titleView.setOnClickListener(properties.getOnClickListener());
            titleView.setClickable(properties.getOnClickListener() != null);
            titleView.setFocusable(properties.getOnClickListener() != null);
            titleView.setEnabled(properties.getOnClickListener() != null);
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

    private void invalidateFab(@Nullable FabDescribing fabDescribing) {
        FabDescription description = fabDescribing != null ? fabDescribing.getFabDescription() : null;

        invalidateFab(description != null ? description.getPrimaryProperties() : null, fabPrimary, fabPrimaryOffset);
        invalidateFab(description != null ? description.getSecondaryProperties() : null, fabSecondary, fabSecondaryOffset);

        CoordinatorLayout.Behavior<?> behavior = ViewUtils.getBehavior(fabGroup);
        if (behavior instanceof SlideOutBehavior) {
            boolean slideOut = description != null && description.slideOutOnScroll();
            ((SlideOutBehavior) behavior).setSlideOut(slideOut);
        }
    }

    @SuppressLint("RestrictedApi")
    private void invalidateFab(@Nullable FabProperties properties, FloatingActionButton fab, float offset) {
        int icon = properties != null ? properties.getIconDrawableResId() : android.R.color.transparent;
        // Workaround: ContextCompat.getDrawable() throws exception on Android 4
        fab.setImageDrawable(AppCompatDrawableManager.get().getDrawable(this, icon));
        fab.setOnClickListener(view -> {
            // Prevent redundant clicks
            fab.setEnabled(false);
            if (properties != null && properties.getOnClickListener() != null) {
                properties.getOnClickListener().onClick(view);
            }
            fab.setEnabled(true);
        });

        boolean isShown = fab.getTranslationY() == 0;
        boolean shouldShow = properties != null;
        boolean changes = isShown != shouldShow;
        // FIXME: FAB is wrongly visible when slid-out and navigating to fragment without fab_primary
        if (changes) {
            float from = fab.getTranslationY();
            float to = shouldShow ? 0 : offset;
            ObjectAnimator animation = ObjectAnimator.ofFloat(fab, "translationY", from, to);
            animation.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();
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
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_home) {
                Navigation.clearBackStack(getSupportFragmentManager());
                openFragment(new DashboardFragment(), false);
            } else if (itemId == R.id.nav_timeline) {
                Navigation.clearBackStack(getSupportFragmentManager());
                openFragment(new TimelineFragment(), false);
            } else if (itemId == R.id.nav_log) {
                Navigation.clearBackStack(getSupportFragmentManager());
                openFragment(new LogFragment(), false);
            } else if (itemId == R.id.nav_calculator) {
                openFragment(new CalculatorFragment(), true);
            } else if (itemId == R.id.nav_food_database) {
                openFragment(FoodSearchFragment.newInstance(), true);
            } else if (itemId == R.id.nav_statistics) {
                openFragment(new StatisticFragment(), true);
            } else if (itemId == R.id.nav_export) {
                openFragment(new ExportFragment(), true);
            } else if (itemId == R.id.nav_settings) {
                openFragment(new PreferenceOverviewFragment(), true);
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

    private void checkChangelog() {
        int oldVersionCode = PreferenceStore.getInstance().getVersionCode();
        int currentVersionCode = SystemUtils.getVersionCode(this);
        if (oldVersionCode == 55) {
            boolean isUpdate = oldVersionCode < currentVersionCode;
            if (isUpdate) {
                PreferenceStore.getInstance().setVersionCode(currentVersionCode);
                openChangelog();
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
