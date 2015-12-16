package com.faltenreich.diaguard.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.table.CategoryTableRow;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.TintImageView;
import com.pdfjet.Drawable;

import org.joda.time.DateTimeConstants;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by Faltenreich on 15.12.2015.
 */
public class CategoryTableDataAdapter extends TableDataAdapter<CategoryTableRow> {

    private static final int COLUMN_INDEX_ICON = 0;

    public CategoryTableDataAdapter(Context context, List<CategoryTableRow> data) {
        super(context, data);
        setColumnCount(data.get(0).getValues().length);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        CategoryTableRow row = getRowData(rowIndex);

        if (columnIndex == COLUMN_INDEX_ICON) {
            Measurement.Category category = row.getCategory();
            int categoryImageResourceId = PreferenceHelper.getInstance().getCategoryImageResourceId(category);

            TintImageView imageView = (TintImageView) getLayoutInflater().inflate(R.layout.list_item_table_category_image, null);
            imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), categoryImageResourceId));
            return imageView;
        } else {
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.list_item_table_category_value, null);
            textView.setText(row.getValue(columnIndex - 1));
            return textView;
        }
    }
}
