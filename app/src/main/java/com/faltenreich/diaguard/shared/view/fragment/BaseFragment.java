package com.faltenreich.diaguard.shared.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.Navigation;
import com.faltenreich.diaguard.feature.navigation.OnFragmentChangeListener;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarOwner;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements Navigating, ToolbarProperties {

    @MenuRes private final int menuResId;

    private String title;

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResId, @MenuRes int menuResId) {
        super(layoutResourceId);
        this.title = titleResId != -1 ? DiaguardApplication.getContext().getString(titleResId) : null;
        this.menuResId = menuResId;
    }

    public BaseFragment(@LayoutRes int layoutResourceId, @StringRes int titleResId) {
        this(layoutResourceId, titleResId, -1);
    }

    public BaseFragment(@LayoutRes int layoutResourceId) {
        this(layoutResourceId, -1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            throw new IllegalStateException("View must not be null");
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this instanceof ToolbarOwner) {
            ToolbarOwner toolbarOwner = (ToolbarOwner) this;
            if (getActivity() instanceof AppCompatActivity) {
                ToolbarManager.applyToolbar((AppCompatActivity) getActivity(), toolbarOwner.getToolbar());
            }
            TextView titleView = toolbarOwner.getTitleView();
            if (this instanceof ToolbarCallback) {
                titleView.setClickable(true);
                titleView.setOnClickListener(childView -> ((ToolbarCallback) BaseFragment.this).action());
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
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
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
        if (this instanceof ToolbarOwner) {
            ToolbarOwner toolbarOwner = (ToolbarOwner) this;
            toolbarOwner.getTitleView().setText(title);
        } else if (getActivity() != null) {
            getActivity().setTitle(title);
        }
    }

    public void setTitle(@StringRes int titleResId) {
        setTitle(getString(titleResId));
    }

    protected void showFragment(BaseFragment fragment) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openFragment(fragment, null, true);
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

    @Override
    public void openFragment(@NonNull Fragment fragment, Navigation.Operation operation, boolean addToBackStack) {
        Navigating navigating = (Navigating) getActivity();
        if (navigating != null) {
            navigating.openFragment(fragment, operation, addToBackStack);
        }
    }

    interface ToolbarCallback {
        void action();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EntryDeletedEvent event) {
        ViewUtils.showSnackbar(getView(), getString(R.string.entry_deleted), v -> {
            Entry entry = event.context;
            EntryDao.getInstance().createOrUpdate(entry);
            for (Measurement measurement : entry.getMeasurementCache()) {
                measurement.setEntry(entry);
                //noinspection unchecked
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
            }
            for (EntryTag entryTag : event.entryTags) {
                entryTag.setEntry(entry);
                EntryTagDao.getInstance().createOrUpdate(entryTag);
            }
            Events.post(new EntryAddedEvent(entry, event.entryTags, event.foodEatenList));
        });
    }
}
