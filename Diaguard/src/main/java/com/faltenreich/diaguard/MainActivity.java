package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.faltenreich.diaguard.fragments.ChartFragment;
import com.faltenreich.diaguard.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.fragments.EntryListFragment;
import com.faltenreich.diaguard.fragments.LogFragment;
import com.faltenreich.diaguard.fragments.MainFragment;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

public class MainActivity extends BaseActivity implements EntryListFragment.CallbackList {

    public static final int REQUEST_EVENT_CREATED = 1;
    public static final String ENTRY_CREATED = "ENTRY_CREATED";
    public static final String ENTRY_DELETED = "ENTRY_DELETED";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView drawer;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Put in DiaguardApplication.java (needs Activity?)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        getComponents();
        initialize();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void getComponents() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (NavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initialize() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        menuItem.setChecked(true);
                        replaceFragment(new MainFragment());
                        break;
                    case R.id.nav_timeline:
                        menuItem.setChecked(true);
                        replaceFragment(new ChartFragment());
                        break;
                    case R.id.nav_log:
                        menuItem.setChecked(true);
                        replaceFragment(new LogFragment());
                        break;
                    case R.id.nav_statistics:
                        startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                        break;
                    case R.id.nav_calculator:
                        startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
                        break;
                    case R.id.nav_export:
                        startActivity(new Intent(MainActivity.this, ExportActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(MainActivity.this, PreferenceActivity.class));
                        break;
                    default:
                        menuItem.setChecked(true);
                        replaceFragment(new MainFragment());
                        break;
                }
                return true;
            }
        });

        // Setup start fragment
        int startScreen = PreferenceHelper.getInstance().getStartScreen();
        drawer.getMenu().getItem(startScreen).setChecked(true);
        switch (startScreen) {
            case 0:
                replaceFragment(new MainFragment());
                break;
            case 1:
                replaceFragment(new ChartFragment());
                break;
            case 2:
                replaceFragment(new LogFragment());
                break;
            default:
                replaceFragment(new MainFragment());
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.toString());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EVENT_CREATED) {
            if (data.hasExtra(ENTRY_CREATED)) {
                int eventsCreated = data.getExtras().getInt(ENTRY_CREATED);
                if(eventsCreated > 0) {
                    // TODO ViewHelper.showSnackbar(this, getString(R.string.entry_added));
                }
            }
            else if (data.hasExtra(ENTRY_DELETED) && data.getExtras().getBoolean(ENTRY_DELETED)) {
                // TODO: Undo functionality
                // TODO ViewHelper.showSnackbar(this, getString(R.string.entry_deleted));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            case R.id.action_newevent:
                startActivityForResult(new Intent(this, NewEventActivity.class), MainActivity.REQUEST_EVENT_CREATED);
                return true;
        }
        return false;
    }

    /**
     * Callback from MessageListFragment to respond to a selected ListItem
     */
    @Override
    public void onItemSelected(long id) {
        if (ViewHelper.isLargeScreen(this)) {
            Bundle arguments = new Bundle();
            arguments.putLong(EntryDetailFragment.ENTRY_ID, id);
            EntryDetailFragment fragment = new EntryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.entry_detail, fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, EntryDetailActivity.class);
            intent.putExtra(EntryDetailFragment.ENTRY_ID, id);
            startActivityForResult(intent, MainActivity.REQUEST_EVENT_CREATED);
        }
    }
}
