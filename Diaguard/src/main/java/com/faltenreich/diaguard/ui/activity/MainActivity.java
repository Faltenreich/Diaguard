package com.faltenreich.diaguard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.BuildConfig;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.SlideOutBehavior;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.ui.fragment.BaseFragment;
import com.faltenreich.diaguard.ui.fragment.CalculatorFragment;
import com.faltenreich.diaguard.ui.fragment.CalculatorMissingFragment;
import com.faltenreich.diaguard.ui.fragment.ChangelogFragment;
import com.faltenreich.diaguard.ui.fragment.ChartFragment;
import com.faltenreich.diaguard.ui.fragment.ExportFragment;
import com.faltenreich.diaguard.ui.fragment.LogFragment;
import com.faltenreich.diaguard.ui.fragment.MainFragment;
import com.faltenreich.diaguard.ui.fragment.OnFragmentChangeListener;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;
import com.faltenreich.diaguard.ui.view.MainFragmentType;
import com.faltenreich.diaguard.ui.view.ToolbarBehavior;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.github.clans.fab.FloatingActionButton;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements OnFragmentChangeListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer) NavigationView drawer;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ActionBarDrawerToggle drawerToggle;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initialize();
        showChangelog();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                EntrySearchActivity.show(MainActivity.this, findViewById(R.id.action_search));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentChanged(Fragment fragment) {
        invalidateLayout();
    }

    private void initialize() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawer.getMenu().findItem(R.id.nav_calculator).setVisible(BuildConfig.isCalculatorEnabled);
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Delay as workaround to smooth transition
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawers();
                    }
                }, 150);
                return showFragment(menuItem);
            }
        });
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
                invalidateLayout();
            }
        });

        // Setup start fragment
        int startScreen = PreferenceHelper.getInstance().getStartScreen();
        MenuItem menuItem = drawer.getMenu().getItem(startScreen);
        showFragment(menuItem);
    }

    private void invalidateLayout() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            invalidateToolbar(fragment instanceof ToolbarBehavior ? (ToolbarBehavior) fragment : null);
            invalidateMainButton(fragment instanceof MainButton ? (MainButton) fragment : null);
            invalidateNavigationDrawer(fragment);
        }
    }

    private void invalidateToolbar(@Nullable ToolbarBehavior toolbarBehavior) {
        if (toolbarBehavior != null) {
            setTitle(toolbarBehavior.getTitle());
        }
    }

    private void invalidateMainButton(@Nullable MainButton mainButton) {
        MainButtonProperties properties = mainButton != null ? mainButton.getMainButtonProperties() : null;
        fab.setVisibility(properties != null ? View.VISIBLE : View.GONE);
        fab.setImageResource(properties != null ? properties.getIconDrawableResId() : android.R.color.transparent);
        fab.setOnClickListener(properties != null ? properties.getOnClickListener() : null);
        if (properties != null) {
            CoordinatorLayout.Behavior behavior = ViewUtils.getBehavior(fab);
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
        MainFragmentType mainFragmentType = MainFragmentType.valueOf(fragment.getClass());
        if (mainFragmentType != null) {
            int position = mainFragmentType.position;
            if (position < drawer.getMenu().size()) {
                MenuItem menuItem = drawer.getMenu().getItem(position);
                selectNavigationDrawerMenuItem(menuItem);
            }
        }
    }

    public void showFragment(@IdRes int itemId) {
        MenuItem menuItem = drawer.getMenu().findItem(itemId);
        showFragment(menuItem);
    }

    private boolean showFragment(MenuItem menuItem) {
        if (menuItem != null) {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showFragment(new MainFragment(), menuItem, false);
                    break;
                case R.id.nav_timeline:
                    showFragment(new ChartFragment(), menuItem, false);
                    break;
                case R.id.nav_log:
                    showFragment(new LogFragment(), menuItem, false);
                    break;
                case R.id.nav_calculator:
                    if (BuildConfig.isCalculatorEnabled) {
                        showFragment(new CalculatorFragment(), menuItem, true);
                    } else {
                        explainMissingCalculator();
                    }
                    break;
                case R.id.nav_food_database:
                    startActivity(new Intent(this, FoodSearchActivity.class));
                    break;
                case R.id.nav_statistics:
                    showFragment(new StatisticsFragment(), menuItem, true);
                    break;
                case R.id.nav_export:
                    showFragment(new ExportFragment(), menuItem, true);
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(this, PreferenceActivity.class));
                    break;
                default:
                    showFragment(new MainFragment(), menuItem, false);
                    break;
            }
        }
        return true;
    }

    public void showFragment(BaseFragment fragment, MenuItem menuItem, boolean addToBackStack) {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        boolean isActive = activeFragment != null && activeFragment.getClass() == fragment.getClass();
        if (!isActive) {
            ViewUtils.hideKeyboard(this);
            resetMainButton();
            selectNavigationDrawerMenuItem(menuItem);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            String tag = fragment.getClass().getSimpleName();
            getSupportFragmentManager().popBackStackImmediate();
            if (addToBackStack) {
                transaction.add(R.id.container, fragment, tag);
                transaction.addToBackStack(tag);
            } else {
                transaction.replace(R.id.container, fragment, tag);
            }
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        }
    }

    private void explainMissingCalculator() {
        new CalculatorMissingFragment().show(getSupportFragmentManager(), null);
    }

    private void selectNavigationDrawerMenuItem(MenuItem menuItem) {
        if (menuItem != null) {
            // First uncheck all, then check current Fragment
            for (int index = 0; index < drawer.getMenu().size(); index++) {
                drawer.getMenu().getItem(index).setChecked(false);
            }
            menuItem.setChecked(true);
        }
    }

    // Show changelog only for updated versions
    private void showChangelog() {
        int oldVersionCode = PreferenceHelper.getInstance().getVersionCode();
        int currentVersionCode = SystemUtils.getVersionCode(this);
        boolean isUpdate = oldVersionCode > 0 && oldVersionCode < currentVersionCode;

        if (isUpdate) {
            PreferenceHelper.getInstance().setVersionCode(currentVersionCode);

            String[] changelog = PreferenceHelper.getInstance().getChangelog(this);
            boolean hasChangelog = changelog != null && changelog.length > 0;

            if (hasChangelog) {
                ChangelogFragment fragment = new ChangelogFragment();
                String tag = fragment.getClass().getSimpleName();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(tag);
                fragment.show(fragmentTransaction, tag);
            }

            if (currentVersionCode == 25) {
                explainMissingCalculator();
            }

        } else if (oldVersionCode == 0) {
            PreferenceHelper.getInstance().setVersionCode(currentVersionCode);
        }
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
