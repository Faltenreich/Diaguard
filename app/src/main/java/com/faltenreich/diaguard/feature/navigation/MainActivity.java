package com.faltenreich.diaguard.feature.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ActivityMainBinding;
import com.faltenreich.diaguard.feature.calculator.CalculatorFragment;
import com.faltenreich.diaguard.feature.changelog.ChangelogFragment;
import com.faltenreich.diaguard.feature.config.ApplicationConfig;
import com.faltenreich.diaguard.feature.dashboard.DashboardFragment;
import com.faltenreich.diaguard.feature.entry.search.EntrySearchActivity;
import com.faltenreich.diaguard.feature.export.ExportFragment;
import com.faltenreich.diaguard.feature.food.search.FoodSearchActivity;
import com.faltenreich.diaguard.feature.log.LogFragment;
import com.faltenreich.diaguard.feature.preference.PreferenceActivity;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.statistic.StatisticFragment;
import com.faltenreich.diaguard.feature.timeline.TimelineFragment;
import com.faltenreich.diaguard.shared.SystemUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.activity.BaseActivity;
import com.faltenreich.diaguard.shared.view.coordinatorlayout.SlideOutBehavior;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

public class MainActivity extends BaseActivity implements OnFragmentChangeListener {

    private ActivityMainBinding binding;

    private ActionBarDrawerToggle drawerToggle;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceStore.getInstance().setDefaultValues(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        if (item.getItemId() == R.id.action_search) {
            EntrySearchActivity.show(MainActivity.this, findViewById(R.id.action_search));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentChanged(Fragment fragment) {
        invalidateLayout();
    }

    private void initLayout() {
        drawerToggle = new ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            getToolbar(),
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
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        binding.navigationView.getMenu().findItem(R.id.nav_calculator).setVisible(ApplicationConfig.isCalculatorEnabled());
        binding.navigationView.setNavigationItemSelectedListener(menuItem -> {
            binding. drawerLayout.closeDrawers();
            selectMenuItem(menuItem);
            return true;
        });
        drawerToggle.setToolbarNavigationClickListener(v -> {
            if (drawerToggle.isDrawerIndicatorEnabled()) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                }
            } else {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().popBackStackImmediate();
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            drawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
            invalidateLayout();
        });

        // Setup start fragment
        int startScreen = PreferenceStore.getInstance().getStartScreen();
        MenuItem menuItem = binding.navigationView.getMenu().getItem(startScreen);
        selectMenuItem(menuItem);
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
        binding.fab.setVisibility(properties != null ? View.VISIBLE : View.GONE);
        binding.fab.setImageResource(properties != null ? properties.getIconDrawableResId() : android.R.color.transparent);
        binding.fab.setOnClickListener(properties != null ? properties.getOnClickListener() : null);
        if (properties != null) {
            CoordinatorLayout.Behavior behavior = ViewUtils.getBehavior(binding.fab);
            if (behavior instanceof SlideOutBehavior) {
                ((SlideOutBehavior) behavior).setSlideOut(properties.slideOut());
            }
        }
    }

    private void resetMainButton() {
        if (binding.fab.getTranslationY() != 0) {
            binding.fab.animate().cancel();
            binding.fab.animate().translationY(0).start();
        }
    }

    private void invalidateNavigationDrawer(Fragment fragment) {
        MainFragmentType mainFragmentType = MainFragmentType.valueOf(fragment.getClass());
        if (mainFragmentType != null) {
            int position = mainFragmentType.position;
            if (position < binding.navigationView.getMenu().size()) {
                MenuItem menuItem = binding.navigationView.getMenu().getItem(position);
                selectNavigationDrawerMenuItem(menuItem);
            }
        }
    }

    public void showFragment(@IdRes int itemId) {
        MenuItem menuItem = binding.navigationView.getMenu().findItem(itemId);
        selectMenuItem(menuItem);
    }

    private void selectMenuItem(MenuItem menuItem) {
        if (menuItem != null) {
            switch (menuItem.getItemId()) {
                case R.id.nav_timeline:
                    showFragment(new TimelineFragment(), menuItem, false);
                    break;
                case R.id.nav_log:
                    showFragment(new LogFragment(), menuItem, false);
                    break;
                case R.id.nav_calculator:
                    showFragment(new CalculatorFragment(), menuItem, true);
                    break;
                case R.id.nav_food_database:
                    startActivity(new Intent(this, FoodSearchActivity.class));
                    break;
                case R.id.nav_statistics:
                    showFragment(new StatisticFragment(), menuItem, true);
                    break;
                case R.id.nav_export:
                    showFragment(new ExportFragment(), menuItem, true);
                    break;
                case R.id.nav_settings:
                    startActivity(PreferenceActivity.newInstance(this, PreferenceActivity.Link.NONE));
                    break;
                default:
                    showFragment(new DashboardFragment(), menuItem, false);
                    break;
            }
        }
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

    private void selectNavigationDrawerMenuItem(MenuItem menuItem) {
        if (menuItem != null) {
            // First uncheck all, then check current Fragment
            for (int index = 0; index < binding.navigationView.getMenu().size(); index++) {
                binding.navigationView.getMenu().getItem(index).setChecked(false);
            }
            menuItem.setChecked(true);
        }
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
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
