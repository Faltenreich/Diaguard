package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.EntryTagDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.EntryTag;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.ui.activity.BaseActivity;
import com.faltenreich.diaguard.ui.activity.MainActivity;
import com.faltenreich.diaguard.ui.view.ToolbarBehavior;
import com.faltenreich.diaguard.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements ToolbarBehavior {

    @LayoutRes private int layoutResourceId;
    @StringRes private int titleResId;
    @MenuRes private int menuResId;

    private String title;

    private BaseFragment() {
        // Forbidden
    }

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResId, @MenuRes int menuResId) {
        this();
        this.layoutResourceId = layoutResourceId;
        this.titleResId = titleResId;
        this.menuResId = menuResId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getString(titleResId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResourceId, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View titleView = getActivity() != null && getActivity() instanceof BaseActivity ? ((BaseActivity) getActivity()).getTitleView() : null;
        if (titleView != null) {
            if (this instanceof ToolbarCallback) {
                titleView.setClickable(true);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ToolbarCallback) BaseFragment.this).action();
                    }
                });
            } else {
                titleView.setClickable(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);

        if (getActivity() instanceof OnFragmentChangeListener) {
            ((OnFragmentChangeListener) getActivity()).onFragmentChanged(this);
        }
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (menuResId >= 0) {
            inflater.inflate(menuResId, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (getActivity() != null) {
            getActivity().setTitle(title);
        }
    }

    public void setTitle(@StringRes int titleResId) {
        setTitle(getString(titleResId));
    }

    public void setToolbarBackgroundColor(@ColorRes int colorResId) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setToolbarBackgroundColor(colorResId);
        }
    }

    public void showFragment(BaseFragment fragment) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(fragment, null, true);
        }
    }

    public void startActivity(Intent intent, ActivityOptionsCompat options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
                getActivity().getWindow().setExitTransition(null);
            }
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    protected void finish() {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            } else {
                getActivity().finish();
            }
        }
    }

    interface ToolbarCallback {
        void action();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EntryDeletedEvent event) {
        ViewUtils.showSnackbar(getView(), getString(R.string.entry_deleted), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entry entry = event.context;
                EntryDao.getInstance().createOrUpdate(entry);
                for (Measurement measurement : entry.getMeasurementCache()) {
                    measurement.setEntry(entry);
                    MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
                }
                for (EntryTag entryTag : event.entryTags) {
                    entryTag.setEntry(entry);
                    EntryTagDao.getInstance().createOrUpdate(entryTag);
                }
                Events.post(new EntryAddedEvent(entry, event.entryTags));
            }
        });
    }
}
