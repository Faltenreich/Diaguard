package com.faltenreich.diaguard.ui.view;

import androidx.fragment.app.Fragment;

import com.faltenreich.diaguard.ui.fragment.BaseFragment;
import com.faltenreich.diaguard.ui.fragment.ChartFragment;
import com.faltenreich.diaguard.ui.fragment.ExportFragment;
import com.faltenreich.diaguard.ui.fragment.LogFragment;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;

public enum MainFragmentType {
    HOME(com.faltenreich.diaguard.ui.fragment.MainFragment.class, 0),
    TIMELINE(ChartFragment.class, 1),
    LOG(LogFragment.class, 2),
    STATISTICS(StatisticsFragment.class, 5),
    EXPORT(ExportFragment.class, 6);

    public Class<? extends BaseFragment> fragmentClass;
    public int position;

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