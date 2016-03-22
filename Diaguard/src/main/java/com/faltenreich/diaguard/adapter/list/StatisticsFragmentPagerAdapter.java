package com.faltenreich.diaguard.adapter.list;

import android.support.v4.app.FragmentManager;

import com.faltenreich.diaguard.adapter.BasePagerAdapter;
import com.faltenreich.diaguard.ui.fragment.StatisticsFragment;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 02.02.2016.
 */
public class StatisticsFragmentPagerAdapter extends BasePagerAdapter<StatisticsFragment> {

    public StatisticsFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<StatisticsFragment> fragments) {
        super(fragmentManager, fragments);
    }
}
