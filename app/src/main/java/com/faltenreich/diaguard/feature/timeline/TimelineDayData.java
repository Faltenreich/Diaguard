package com.faltenreich.diaguard.feature.timeline;

import com.faltenreich.diaguard.feature.timeline.chart.DayChartData;
import com.faltenreich.diaguard.feature.timeline.table.CategoryValueListItem;

import org.joda.time.DateTime;

import java.util.List;

class TimelineDayData {

    private DateTime day;
    private DayChartData chartData;
    private List<CategoryValueListItem> listData;

    public TimelineDayData(DateTime day) {
        this.day = day;
    }

    public void reset() {
        listData = null;
        chartData = null;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public boolean needsChartData() {
        return chartData == null;
    }

    public DayChartData getChartData() {
        return chartData;
    }

    public void setChartData(DayChartData chartData) {
        this.chartData = chartData;
    }

    public boolean needsListData() {
        return listData == null;
    }

    public List<CategoryValueListItem> getListData() {
        return listData;
    }

    public void setListData(List<CategoryValueListItem> listData) {
        this.listData = listData;
    }
}
