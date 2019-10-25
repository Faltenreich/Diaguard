package com.faltenreich.diaguard.export.pdf.view;

import com.pdfjet.Cell;
import com.pdfjet.Table;

import java.util.List;

public class SizedTablePdfView extends Table {

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
}
