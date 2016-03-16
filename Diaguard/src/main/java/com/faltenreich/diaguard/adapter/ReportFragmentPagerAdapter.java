package com.faltenreich.diaguard.adapter;

import android.support.v4.app.FragmentManager;

import com.faltenreich.diaguard.ui.fragment.BaseFragment;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;
import com.faltenreich.diaguard.ui.fragment.TrendFragment;

/**
 * Created by Faltenreich on 02.02.2016.
 */
public class ReportFragmentPagerAdapter extends BasePagerAdapter<BaseFragment> {

    public ReportFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, new BaseFragment[] {
                new StatisticsFragment(),
                new TrendFragment()
        });
    }
}
