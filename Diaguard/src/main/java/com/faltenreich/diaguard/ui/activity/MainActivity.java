package com.faltenreich.diaguard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.ui.fragment.BaseFragment;
import com.faltenreich.diaguard.ui.fragment.CalculatorFragment;
import com.faltenreich.diaguard.ui.fragment.ChartFragment;
import com.faltenreich.diaguard.ui.fragment.ExportFragment;
import com.faltenreich.diaguard.ui.fragment.LogFragment;
import com.faltenreich.diaguard.ui.fragment.MainFragment;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer) NavigationView drawer;

    private ActionBarDrawerToggle drawerToggle;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initialize();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                replaceFragment(menuItem);
                return true;
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
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if (fragment != null && fragment instanceof BaseFragment) {
                    setTitle(((BaseFragment)fragment).getTitle());
                }
            }
        });

        // Setup start fragment
        int startScreen = PreferenceHelper.getInstance().getStartScreen();
        MenuItem menuItem = drawer.getMenu().getItem(startScreen);
        replaceFragment(menuItem);
    }

    public void replaceFragment(@IdRes int itemId) {
        MenuItem menuItem = drawer.getMenu().findItem(itemId);
        replaceFragment(menuItem);
    }

    private void replaceFragment(MenuItem menuItem) {
        if (menuItem != null) {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new MainFragment(), menuItem, false);
                    break;
                case R.id.nav_timeline:
                    replaceFragment(new ChartFragment(), menuItem, false);
                    break;
                case R.id.nav_log:
                    replaceFragment(new LogFragment(), menuItem, false);
                    break;
                case R.id.nav_calculator:
                    replaceFragment(new CalculatorFragment(), menuItem, false);
                    break;
                case R.id.nav_food_database:
                    startActivity(new Intent(MainActivity.this, FoodSearchActivity.class));
                    break;
                case R.id.nav_statistics:
                    replaceFragment(new StatisticsFragment(), menuItem, true);
                    break;
                case R.id.nav_export:
                    replaceFragment(new ExportFragment(), menuItem, true);
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                    break;
                default:
                    replaceFragment(new MainFragment(), menuItem, false);
                    break;
            }
        }
    }

    public void replaceFragment(BaseFragment fragment, MenuItem menuItem, boolean addToBackStack) {
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        boolean isActive = activeFragment != null && activeFragment.getClass() == fragment.getClass();
        if (!isActive) {
            // First uncheck all, then check current Fragment
            for (int index = 0; index < drawer.getMenu().size(); index++) {
                drawer.getMenu().getItem(index).setChecked(false);
            }
            menuItem.setChecked(true);

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
            setTitle(fragment.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
