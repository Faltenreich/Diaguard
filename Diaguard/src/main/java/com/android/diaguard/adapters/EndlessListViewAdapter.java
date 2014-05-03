package com.android.diaguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.commonsware.cwac.endless.EndlessAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 03.05.2014.
 */
public class EndlessListViewAdapter extends EndlessAdapter {

    private RotateAnimation rotate;
    private View pendingView;

    private DatabaseDataSource dataSource;
    private List<Event> cachedEvents;
    private Calendar currentDate;
    private Event.Category[] categories;

    public boolean isScrollingUp;
    public boolean isScrollingDown;

    public EndlessListViewAdapter(Context context, Calendar startDate, Event.Category[] categories) {
        super(new ListViewAdapterEvents(context));

        this.dataSource = new DatabaseDataSource(context);
        this.currentDate = Calendar.getInstance();
        this.currentDate.setTime(startDate.getTime());
        this.categories = categories;

        this.isScrollingUp = false;
        this.isScrollingDown = false;

        // First set of events
        dataSource.open();
        while(getWrappedEventAdapter().events.size() < 20) {
            getWrappedEventAdapter().events.addAll(dataSource.getEventsOfDay(currentDate, categories));
            currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1);
        }
        dataSource.close();

        // Rotation of PendingView
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(600);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
    }

    protected ListViewAdapterEvents getWrappedEventAdapter() {
        return (ListViewAdapterEvents)getWrappedAdapter();
    }

    @Override
    protected View getPendingView(ViewGroup parent) {
        // Placeholder
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);

        pendingView = row.findViewById(android.R.id.text1);
        pendingView.setVisibility(View.GONE);
        pendingView = row.findViewById(R.id.throbber);
        pendingView.setVisibility(View.VISIBLE);
        startProgressAnimation();

        return row;
    }

    @Override
    protected boolean cacheInBackground() {

        if(isScrollingDown || isScrollingUp) {

            cachedEvents = new ArrayList<Event>();
            dataSource.open();

            if(isScrollingDown)
                currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) - 1);
            else if(isScrollingUp)
                currentDate.set(Calendar.DAY_OF_YEAR, currentDate.get(Calendar.DAY_OF_YEAR) + 1);

            cachedEvents.addAll(dataSource.getEventsOfDay(currentDate, categories));
            dataSource.close();
        }

        // Stop loading until user reaches one end of the ListView
        return isScrollingDown || isScrollingUp;
    }

    @Override
    protected void appendCachedData() {
        if (isScrollingDown) {
            getWrappedEventAdapter().events.addAll(cachedEvents);
            isScrollingDown = false;
        }
        else if (isScrollingUp) {
            for(Event event : cachedEvents)
                getWrappedEventAdapter().events.add(0, event);
            isScrollingUp = false;
        }
    }

    void startProgressAnimation() {
        if (pendingView!=null) {
            pendingView.startAnimation(rotate);
        }
    }
}