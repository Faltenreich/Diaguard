package com.faltenreich.diaguard.export.pdf.view;

import android.util.Log;

import com.pdfjet.Cell;
import com.pdfjet.Table;

import java.util.List;

public class SizedTablePdfView extends Table {

    private static final String TAG = SizedTablePdfView.class.getSimpleName();

    private int rowCount;

    public SizedTablePdfView() {
        super();
    }

    public int getRowCount() {
        return rowCount;
    }

    @Override
    public void setData(List<List<Cell>> tableData) throws Exception {
        super.setData(tableData);
        rowCount = tableData.size();
    }

    public float getHeight() {
        float height = 0;
        for (int row = 0; row < getRowCount(); row++) {
            try {
                height += getRowAtIndex(row).get(0).getHeight();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }
        return height;
    }
}
