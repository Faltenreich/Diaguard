package com.faltenreich.diaguard.feature.navigation;

import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.feature.dashboard.DashboardFragment;
import com.faltenreich.diaguard.feature.timeline.TimelineFragment;
import com.faltenreich.diaguard.feature.log.LogFragment;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.feature.export.ExportFragment;
import com.faltenreich.diaguard.feature.statistic.StatisticFragment;

public enum MainFragmentType {
    HOME(DashboardFragment.class, 0),
    TIMELINE(TimelineFragment.class, 1),
    LOG(LogFragment.class, 2),
    STATISTICS(StatisticFragment.class, 5),
    EXPORT(ExportFragment.class, 6);

    public final Class<? extends BaseFragment> fragmentClass;
    public final int position;

    MainFragmentType(Class<? extends BaseFragment> fragmentClass, int position) {
        this.fragmentClass = fragmentClass;
        this.position = position;
    }

    public static MainFragmentType valueOf(Class<? extends Fragment> fragmentClass) {
        for (MainFragmentType mainFragmentType : MainFragmentType.values()) {
            if (mainFragmentType.fragmentClass == fragmentClass) {
                return mainFragmentType;
            }
        }
        return null;
    }

    public static MainFragmentType valueOf(int position) {
        for (MainFragmentType mainFragmentType : MainFragmentType.values()) {
            if (mainFragmentType.position == position) {
                return mainFragmentType;
            }
        }
        return null;
    }
}