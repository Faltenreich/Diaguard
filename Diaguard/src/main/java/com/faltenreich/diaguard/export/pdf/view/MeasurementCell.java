package com.faltenreich.diaguard.export.pdf.view;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.export.pdf.meta.PdfExportCache;
import com.faltenreich.diaguard.ui.list.item.ListItemCategoryValue;
import com.faltenreich.diaguard.util.Helper;
import com.pdfjet.Align;
import com.pdfjet.Color;

public class MeasurementCell extends Cell {

    public MeasurementCell(PdfExportCache cache, ListItemCategoryValue item, int valueIndex, int backgroundColor, float cellWidth) {
        super(cache.getFontNormal());

        Measurement.Category category = item.getCategory();
        float value = 0;
        switch (valueIndex) {
            case -1:
                value = item.getValueTotal();
                break;
            case 0:
                value = item.getValueOne();
                break;
            case 1:
                value = item.getValueTwo();
                break;
            case 2:
                value = item.getValueThree();
                break;
        }
        int textColor = Color.black;
        if (category == Measurement.Category.BLOODSUGAR && cache.getConfig().isHighlightLimits()) {
            if (value > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                textColor = cache.getColorHyperglycemia();
            } else if (value < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                textColor = cache.getColorHypoglycemia();
            }
        }
        float customValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, value);
        String text = customValue > 0 ? Helper.parseFloat(customValue) : "";
        setText(text);
        setBgColor(backgroundColor);
        setFgColor(textColor);
        setWidth(cellWidth);
        setTextAlignment(Align.CENTER);
    }
}
