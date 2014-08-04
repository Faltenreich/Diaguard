package com.faltenreich.diaguard;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.faltenreich.diaguard.adapters.DrawerListViewAdapter;
import com.faltenreich.diaguard.fragments.LogFragment;
import com.faltenreich.diaguard.fragments.MainFragment;
import com.faltenreich.diaguard.fragments.TimelineFragment;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    public enum FragmentType {
        Home,
        Timeline,
        Log,
        Calculator,
        Export,
        Settings
    }

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        initialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Animate Toggle
        if(drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    private void initialize() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initializeDrawer();
        PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        replaceFragment(preferenceHelper.getStartFragment());
    }

    private void initializeDrawer() {
        List<String> menuItems = new ArrayList<String>();
        menuItems.add(getString(R.string.home));
        menuItems.add(getString(R.string.timeline));
        menuItems.add(getString(R.string.log));
        //menuItems.add(getString(R.string.statistics));
        menuItems.add(getString(R.string.calculator));
        menuItems.add(getString(R.string.export));
        menuItems.add(getString(R.string.settings));

        int[] menuImages = new int[3];
        menuImages[0] = R.drawable.calculator;
        menuImages[1] = R.drawable.export;
        menuImages[2] = R.drawable.settings;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_navigation);
        DrawerListViewAdapter adapter = new DrawerListViewAdapter(this,
                menuItems.toArray(new String[menuItems.size()]), menuImages, 3);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replaceFragment(FragmentType.values()[position]);
                drawerLayout.closeDrawer(drawerList);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                if(Build.VERSION.SDK_INT < 11)
                    supportInvalidateOptionsMenu();
                else
                    invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                if(Build.VERSION.SDK_INT < 11)
                    supportInvalidateOptionsMenu();
                else
                    invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        /*
        // Hint for the Navigation Drawer
        drawerLayout.openDrawer(drawerList);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawer(drawerList);
                }
        }, 1300);
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Open a new Fragment
     * @param fragmentType Enum to detect the specific Fragment to open
     */
    public void replaceFragment(FragmentType fragmentType) {

        // Highlighting
        if(drawerList != null && drawerList.getChildCount() > 0 &&
                fragmentType.ordinal() < ((DrawerListViewAdapter)drawerList.getAdapter()).fragmentCount) {

            // De-highlight every item
            for (int i = 0; i < drawerList.getChildCount(); i++) {
                View v = drawerList.getChildAt(i);
                if(v != null) {
                    TextView textViewListItem = (TextView) v.findViewById(R.id.title);
                    if (textViewListItem != null)
                        textViewListItem.setTypeface(null, Typeface.NORMAL);
                }
            }

            // Highlight selected item
            TextView selectedChild = (TextView) drawerList.getChildAt(fragmentType.ordinal()).
                    findViewById(R.id.title);
            if (selectedChild != null)
                selectedChild.setTypeface(null, Typeface.BOLD);
        }

        // Do nothing if the user wants to reopen the current visible Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentType.toString());
        if(fragment != null && fragment.isVisible())
            return;

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
                startActivity(new Intent(this, PreferencesActivity.class));
                return;
            default:
                return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment, fragmentType.toString());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
