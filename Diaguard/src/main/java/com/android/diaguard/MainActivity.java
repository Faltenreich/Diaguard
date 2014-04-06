package com.android.diaguard;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.diaguard.adapters.DrawerListViewAdapter;
import com.android.diaguard.fragments.LogFragment;
import com.android.diaguard.fragments.MainFragment;
import com.android.diaguard.fragments.TimelineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        setTitle(getString(R.string.app_name));
        initialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initialize() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new MainFragment())
                .commit();
        initializeDrawer();
    }

    private void initializeDrawer() {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add(getString(R.string.home));
        menuItems.add(getString(R.string.timeline));
        menuItems.add(getString(R.string.log));
        menuItems.add(getString(R.string.calculator));
        menuItems.add(getString(R.string.export));

        int[] menuImages = new int[menuItems.size()];
        menuImages[0] = R.drawable.home;
        menuImages[1] = R.drawable.chart;
        menuImages[2] = R.drawable.log;
        menuImages[3] = R.drawable.calculator;
        menuImages[4] = R.drawable.export;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        DrawerListViewAdapter adapter = new DrawerListViewAdapter(this,
                menuItems.toArray(new String[menuItems.size()]), menuImages);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        fragment = new MainFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i)
                            fragmentManager.popBackStack();
                        break;
                    case 1:
                        fragment = new TimelineFragment();
                        break;
                    case 2:
                        fragment = new LogFragment();
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
                        return;
                    case 4:
                        startActivity(new Intent(MainActivity.this, ExportActivity.class));
                        return;
                    default:
                        return;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                // Highlight current item
                for (int i=0; i< drawerList.getChildCount(); i++)
                {
                    View v = drawerList.getChildAt(i);
                    TextView textViewListItem = (TextView) v.findViewById(R.id.title);
                    if(textViewListItem != null)
                        textViewListItem.setTypeface(null, Typeface.NORMAL);
                }
                ((TextView) view.findViewById(R.id.title)).setTypeface(null, Typeface.BOLD);

                drawerLayout.closeDrawer(drawerList);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(drawerList))
                    drawerLayout.closeDrawer(drawerList);
                else
                    drawerLayout.openDrawer(drawerList);
                return true;
            case R.id.action_settings:
                startActivity(new Intent (this, PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
