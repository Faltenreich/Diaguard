package com.faltenreich.diaguard.shared.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.navigation.Navigating;
import com.faltenreich.diaguard.feature.navigation.OnFragmentChangeListener;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarManager;
import com.faltenreich.diaguard.feature.navigation.ToolbarOwner;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment<BINDING extends ViewBinding> extends Fragment implements ViewBindable<BINDING>, Navigating {

    private BINDING binding;

    public BaseFragment() {
        super();
    }

    protected abstract BINDING createBinding(LayoutInflater layoutInflater);

    @Override
    public BINDING getBinding() {
        return binding;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean hasOptionsMenu = this instanceof ToolbarDescribing && ((ToolbarDescribing) this).getToolbarProperties().getMenuResId() != null;
        setHasOptionsMenu(hasOptionsMenu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = createBinding(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof OnFragmentChangeListener) {
            ((OnFragmentChangeListener) getActivity()).onFragmentChanged(this);
        }

        if (getView() != null) {
            ViewUtils.hideKeyboard(getView());
        }

        if (this instanceof ToolbarOwner) {
            ToolbarOwner toolbarOwner = (ToolbarOwner) this;
            if (getActivity() instanceof AppCompatActivity) {
                ToolbarManager.applyToolbar((AppCompatActivity) getActivity(), toolbarOwner.getToolbar());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        if (this instanceof ToolbarDescribing) {
            try {
                ToolbarDescribing describing = (ToolbarDescribing) this;
                int menuResId = describing.getToolbarProperties().getMenuResId();
                inflater.inflate(menuResId, menu);
            } catch (Resources.NotFoundException ignored) {}
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setTitle(String title) {
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
    public void openFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        if (getActivity() instanceof Navigating) {
            ((Navigating) getActivity()).openFragment(fragment, addToBackStack);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final EntryDeletedEvent event) {
        if (getView() == null) {
            return;
        }
        ViewUtils.showSnackbar(getView(), getString(R.string.entry_deleted), v -> {
            Entry entry = event.context;
            EntryDao.getInstance().createOrUpdate(entry);
            for (Measurement measurement : entry.getMeasurementCache()) {
                measurement.setEntry(entry);
                if (measurement instanceof Meal) {
                    Meal meal = (Meal) measurement;
                    meal.getFoodEatenCache().clear();
                    meal.getFoodEatenCache().addAll(event.foodEatenList);
                }
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
