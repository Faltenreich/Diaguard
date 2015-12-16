package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.CategoryTableDataAdapter;
import com.faltenreich.diaguard.adapter.table.CategoryTableRow;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ArrayUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryTable extends TableView {

    private static final int SKIP_EVERY_X_HOUR = 2;

    private DateTime day;
    private Measurement.Category[] categories;

    public CategoryTable(Context context) {
        super(context);
        setup();
    }

    public CategoryTable(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup();
    }

    public CategoryTable(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);
        setup();
    }

    public void setDay(DateTime day) {
        this.day = day;
        new UpdateDataTask().execute();
    }

    private void setup() {
        if (!isInEditMode()) {
            Measurement.Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
            categories = Arrays.copyOfRange(activeCategories, 1, activeCategories.length);

            int colorEvenRows = ContextCompat.getColor(getContext(), android.R.color.transparent);
            int colorOddRows = ContextCompat.getColor(getContext(), R.color.gray_lighter);
            setDataRowColoriser(TableDataRowColorizers.alternatingRows(colorEvenRows, colorOddRows));

            setColumnWeight(0, 2);
        }
    }

    private class UpdateDataTask extends AsyncTask<Void, Void, List<CategoryTableRow>> {

        protected List<CategoryTableRow> doInBackground(Void... params) {
            HashMap<Measurement.Category, String[]> values = new LinkedHashMap<>();
            for (Measurement.Category category : categories) {
                values.put(category, new String[DateTimeConstants.HOURS_PER_DAY / SKIP_EVERY_X_HOUR]);
            }

            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
            for (Entry entry : entries) {
                List<Measurement> measurementsOfEntry = EntryDao.getInstance().getMeasurements(entry, categories);
                for (Measurement measurement : measurementsOfEntry) {
                    int index = entry.getDate().getHourOfDay() / SKIP_EVERY_X_HOUR;
                    Measurement.Category category = measurement.getCategory();

                    String oldValueString = values.get(category)[index];
                    float oldValue = oldValueString != null ?
                            Float.parseFloat(oldValueString) : 0;
                    float newValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, ArrayUtils.sum(measurement.getValues()));
                    float avg = (oldValue + newValue) / 2;

                    String valueForUi = PreferenceHelper.getInstance().getDecimalFormat(category).format(avg);
                    values.get(category)[index] = valueForUi;
                }
            }

            List<CategoryTableRow> rowList = new ArrayList<>();
            for (Map.Entry<Measurement.Category, String[]> mapEntry : values.entrySet()) {
                rowList.add(new CategoryTableRow(mapEntry.getKey(), mapEntry.getValue()));
            }
            return rowList;
        }

        protected void onPostExecute(List<CategoryTableRow> measurements) {
            CategoryTableDataAdapter adapter = new CategoryTableDataAdapter(getContext(), measurements);
            setDataAdapter(adapter);
        }
    }
}
