package com.faltenreich.diaguard.ui.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.EntryDetailActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.fragments.EntryDetailFragment;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 04.11.13.
 */
public class LogRecyclerAdapter extends BaseAdapter<Measurement, RecyclerView.ViewHolder> {

    private enum ViewType {
        SECTION,
        ENTRY
    }

    private Context context;

    private DateTime maxVisibleDate;
    private DateTime minVisibleDate;

    public LogRecyclerAdapter(Context context, DateTime firstVisibleDay) {
        this.context = context;
        this.items = new ArrayList<>();

        minVisibleDate = firstVisibleDay.withDayOfMonth(1);
        maxVisibleDate = minVisibleDate;

        appendNextMonth();

        if (firstVisibleDay.dayOfMonth().get() >= (firstVisibleDay.dayOfMonth().getMaximumValue() -
                EndlessScrollListener.VISIBLE_THRESHOLD) - 1) {
            // Workaround to support visible threshold
            appendNextMonth();
        } else if (firstVisibleDay.dayOfMonth().get() == 1) {
            // Workaround to support showing header instead of first day
            appendPreviousMonth();
        }
    }

    public void appendRows(EndlessScrollListener.Direction direction) {
        if (direction == EndlessScrollListener.Direction.DOWN) {
            appendNextMonth();
        } else {
            appendPreviousMonth();
        }
    }

    private void appendNextMonth() {
        // Header
        items.add(new RecyclerSection(maxVisibleDate));
        notifyItemInserted(items.size() - 1);

        DateTime targetDate = maxVisibleDate.plusMonths(1);
        while (maxVisibleDate.isBefore(targetDate)) {
            items.add(new RecyclerEntry(maxVisibleDate, fetchData(maxVisibleDate)));
            notifyItemInserted(items.size() - 1);
            maxVisibleDate = maxVisibleDate.plusDays(1);
        }
    }

    private void appendPreviousMonth() {
        DateTime targetDate = minVisibleDate.minusMonths(1);
        while (minVisibleDate.isAfter(targetDate)) {
            minVisibleDate = minVisibleDate.minusDays(1);
            items.add(0, new RecyclerEntry(minVisibleDate, fetchData(minVisibleDate)));
            notifyItemInserted(0);
        }
        // Header
        items.add(0, new RecyclerSection(minVisibleDate));
        notifyItemInserted(0);
    }

    private List<Entry> fetchData(DateTime day) {
        List<Entry> entriesOfDay = DatabaseFacade.getInstance().getEntriesOfDay(day);
        return entriesOfDay;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return ViewType.ENTRY.ordinal();
        } else {
            RecyclerItem item = items.get(position);
            if (item instanceof RecyclerSection) {
                return ViewType.SECTION.ordinal();
            } else if (item instanceof RecyclerEntry) {
                return ViewType.ENTRY.ordinal();
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
        ViewType viewType = ViewType.values()[viewTypeInt];
        switch (viewType) {
            case SECTION:
                return new ViewHolderRowSection(LayoutInflater.from(context).inflate(R.layout.recycler_log_row_section, parent, false));
            default:
                return new ViewHolderRowEntry(LayoutInflater.from(context).inflate(R.layout.recycler_log_row_entry, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewType viewType = ViewType.values()[holder.getItemViewType()];
        switch (viewType) {
            case SECTION:
                bindMonth((ViewHolderRowSection) holder, (RecyclerSection) items.get(position));
                break;
            case ENTRY:
                bindDay((ViewHolderRowEntry) holder, (RecyclerEntry) items.get(position));
                break;
        }
    }

    @Override
    public void onViewRecycled (RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderRowEntry) {
            ((ViewHolderRowEntry) holder).entries.removeAllViews();
        }
    }

    private void bindMonth(ViewHolderRowSection vh, RecyclerSection recyclerSection) {
        int maximum = Measurement.Category.values().length - 1;
        int position = recyclerSection.getDateTime().monthOfYear().get() % maximum;
        Measurement.Category category = Measurement.Category.values()[position];
        int color = context.getResources().getColor(PreferenceHelper.getInstance().getCategoryColorResourceId(category));
        vh.layer.setBackgroundColor(color);
        vh.month.setText(recyclerSection.getDateTime().toString("MMMM YYYY"));
    }

    private void bindDay(ViewHolderRowEntry vh, RecyclerEntry recyclerEntry) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vh.day.setText(recyclerEntry.getDateTime().toString("dd"));
        vh.weekDay.setText(recyclerEntry.getDateTime().dayOfWeek().getAsShortText());

        // Highlight current day
        boolean isToday = recyclerEntry.getDateTime().withTimeAtStartOfDay().isEqual(DateTime.now().withTimeAtStartOfDay());
        int textColor =  isToday ?
                context.getResources().getColor(R.color.green) :
                context.getResources().getColor(R.color.gray_dark);
        vh.day.setTextColor(textColor);
        vh.weekDay.setTextColor(textColor);

        if (recyclerEntry.hasEntries()) {
            vh.entries.setVisibility(View.VISIBLE);
            vh.emptyView.setVisibility(View.GONE);

            for (final Entry entry : recyclerEntry.getEntries()) {
                View viewEntry = inflate.inflate(R.layout.recycler_log_entry, vh.entries, false);
                viewEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, EntryDetailActivity.class);
                        intent.putExtra(EntryDetailFragment.EXTRA_ENTRY, entry.getId());
                        context.startActivity(intent);
                    }
                });

                TextView time = (TextView) viewEntry.findViewById(R.id.time);
                time.setText(entry.getDate().toString("HH:mm"));

                ViewGroup layoutEntries = (ViewGroup) viewEntry.findViewById(R.id.measurements);
                try {
                    List<Measurement> measurements = DatabaseFacade.getInstance().getMeasurements(entry);
                    for (Measurement measurement : measurements) {
                        Measurement.Category category = measurement.getMeasurementType();
                        View viewMeasurement = inflate.inflate(R.layout.recycler_log_measurement, vh.entries, false);
                        ImageView categoryImage = (ImageView) viewMeasurement.findViewById(R.id.image);
                        int imageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);
                        categoryImage.setImageDrawable(context.getResources().getDrawable(imageResourceId));
                        categoryImage.setColorFilter(context.getResources().getColor(R.color.gray_dark), PorterDuff.Mode.SRC_ATOP.SRC_ATOP);
                        TextView value = (TextView) viewMeasurement.findViewById(R.id.value);

                        switch (category) {
                            case BloodSugar:
                                BloodSugar bloodSugar = (BloodSugar) measurement;
                                if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                                    int backgroundColor = context.getResources().getColor(R.color.green);
                                    if (bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                                        backgroundColor = context.getResources().getColor(R.color.red);
                                    } else if (bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                                        backgroundColor = context.getResources().getColor(R.color.blue);
                                    }
                                    categoryImage.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP.SRC_ATOP);
                                }
                            default:
                                value.setText(measurement.toString() + " " + PreferenceHelper.getInstance().getUnitAcronym(category));
                        }

                        layoutEntries.addView(viewMeasurement);
                    }
                } catch (SQLException exception) {
                    Log.e("LogRecyclerAdapter", exception.getMessage());
                }
                vh.entries.addView(viewEntry);
            }
        } else {
            vh.entries.setVisibility(View.GONE);
            vh.emptyView.setVisibility(View.VISIBLE);
        }

        // Add indicator behind last entry
        if (isToday) {
            View indicator = inflate.inflate(R.layout.recycler_log_indicator, vh.entries, false);
            vh.entries.addView(indicator);
        }
    }

    private static class ViewHolderRowSection extends RecyclerView.ViewHolder {
        View layer;
        TextView month;
        public ViewHolderRowSection(View view) {
            super(view);
            this.layer = view.findViewById(R.id.layer);
            this.month = (TextView) view.findViewById(R.id.month);
        }
    }

    private static class ViewHolderRowEntry extends RecyclerView.ViewHolder {
        TextView day;
        TextView weekDay;
        ViewGroup entries;
        TextView emptyView;
        public ViewHolderRowEntry(View view) {
            super(view);
            this.day = (TextView) view.findViewById(R.id.day);
            this.weekDay = (TextView) view.findViewById(R.id.weekday);
            this.entries = (ViewGroup) view.findViewById(R.id.entries);
            this.emptyView = (TextView) view.findViewById(R.id.empty_view);
        }
    }

    private class FetchDataTask extends AsyncTask<DateTime, Void, List<Entry>> {

        protected List<Entry> doInBackground(DateTime... params) {
            List<Entry> entriesOfDay = DatabaseFacade.getInstance().getEntriesOfDay(params[0]);
            // TODO: Fetch measurements, too
            return entriesOfDay;
        }

        protected void onPostExecute(List<Entry> data) {
            // TODO: Set data
        }
    }
}