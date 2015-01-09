package com.faltenreich.diaguard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.faltenreich.diaguard.adapters.DrawerListViewAdapter;
import com.faltenreich.diaguard.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.fragments.EntryListFragment;
import com.faltenreich.diaguard.fragments.LogFragment;
import com.faltenreich.diaguard.fragments.MainFragment;
import com.faltenreich.diaguard.fragments.TimelineFragment;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.faltenreich.diaguard.helpers.ViewHelper;

public class MainActivity extends ActionBarActivity implements EntryListFragment.CallbackList {

    public static final int REQUEST_EVENT_CREATED = 1;
    public static final String ENTRY_CREATED = "ENTRY_CREATED";
    public static final String ENTRY_DELETED = "ENTRY_DELETED";

    public enum FragmentType {
        Home,
        Timeline,
        Log,
        Calculator,
        Export,
        Settings
    }

    PreferenceHelper preferenceHelper;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
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
        drawerList = (ListView) findViewById(R.id.drawer);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
    }

    private void initialize() {
        preferenceHelper = new PreferenceHelper(this);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close) {
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

        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        DrawerListViewAdapter adapter = new DrawerListViewAdapter(this);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replaceFragment(FragmentType.values()[position]);
                drawerLayout.closeDrawer(drawerList);
            }
        });
        drawerList.setSelector(R.drawable.background_drawer);

        // TODO: Better initialization
        drawerList.performItemClick(null, preferenceHelper.getStartScreen().ordinal(), 0);
        //replaceFragment(preferenceHelper.getStartScreen());
    }

    /**
     * Open a new Fragment
     * @param fragmentType Enum to detect the specific Fragment to open
     */
    public void replaceFragment(FragmentType fragmentType) {

        // Do nothing if the user wants to reopen the current visible Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentType.toString());
        if(fragment != null && fragment.isVisible()) {
            return;
        }

        switch (fragmentType) {
            case Home:
                fragment = new MainFragment();
                break;
            case Timeline:
                fragment = new TimelineFragment();
                break;
            case Log:
                fragment = new LogFragment();
                break;
            case Calculator:
                startActivity(new Intent(this, CalculatorActivity.class));
                return;
            case Export:
                startActivity(new Intent(this, ExportActivity.class));
                return;
            case Settings:
                if(Build.VERSION.SDK_INT >= 11) {
                    startActivity(new Intent(this, PreferenceActivity.class));
                }
                else {
                    startActivity(new Intent(this, PreferenceDeprecatedActivity.class));
                }
                return;
            default:
                return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, fragmentType.toString());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EVENT_CREATED) {
            if (data.hasExtra(ENTRY_CREATED)) {
                int eventsCreated = data.getExtras().getInt(ENTRY_CREATED);
                if(eventsCreated > 0) {
                    ViewHelper.showSnackbar(this, getString(R.string.entry_added));
                }
            }
            else if (data.hasExtra(ENTRY_DELETED) && data.getExtras().getBoolean(ENTRY_DELETED)) {
                // TODO: Undo functionality
                ViewHelper.showSnackbar(this, getString(R.string.entry_deleted));
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
                if(!drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.openDrawer(Gravity.START);
                }
                else {
                    drawerLayout.closeDrawer(Gravity.START);
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
            // Tablet
            Bundle arguments = new Bundle();
            arguments.putLong(EntryDetailFragment.ENTRY_ID, id);
            EntryDetailFragment fragment = new EntryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.entry_detail, fragment)
                    .commit();
        }
        else {
            // Phone
            Intent intent = new Intent(this, EntryDetailActivity.class);
            intent.putExtra(EntryDetailFragment.ENTRY_ID, id);
            startActivityForResult(intent, MainActivity.REQUEST_EVENT_CREATED);
        }
    }
}
