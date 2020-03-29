/**
 *  Form.java
 *
Copyright (c) 2018, Innovatics Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.pdfjet;

import java.util.*;


/**
 *  Please see Example_45
 */
public class Form implements Drawable {

    private List<Field> fields;
    private float x;
    private float y;
    private Font f1;
    private float labelFontSize = 8f;
    private Font f2;
    private float valueFontSize = 10f;
    private int numberOfRows;
    private float rowLength = 500f;
    private float rowHeight = 12f;
    private int labelColor = Color.black;
    private int valueColor = Color.blue;
    private List<float[]> endOfLinePoints;


    public Form(List<Field> fields) {
        this.fields = fields;
        this.endOfLinePoints = new ArrayList<float[]>();
    }


    public Form setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public Form setRowLength(float rowLength) {
        this.rowLength = rowLength;
        return this;
    }


    public Form setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
        return this;
    }


    public Form setLabelFont(Font f1) {
        this.f1 = f1;
        return this;
    }


    public Form setLabelFontSize(float labelFontSize) {
        this.labelFontSize = labelFontSize;
        return this;
    }


    public Form setValueFont(Font f2) {
        this.f2 = f2;
        return this;
    }


    public Form setValueFontSize(float valueFontSize) {
        this.valueFontSize = valueFontSize;
        return this;
    }


    public Form setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        return this;
    }


    public Form setValueColor(int valueColor) {
        this.valueColor = valueColor;
        return this;
    }


    public List<float[]> getEndOfLinePoints() {
        return endOfLinePoints;
    }


    /**
     *  Draws this Form on the specified page.
     *
     *  @param page the page to draw this form on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page) throws Exception {
        for (Field field : fields) {
            if (field.format) {
                field.values = format(field.values[0], field.values[1], this.f2, this.rowLength);
                field.altDescription = new String[field.values.length];
                field.actualText = new String[field.values.length];
                for (int i = 0; i < field.values.length; i++) {
                    field.altDescription[i] = field.values[i];
                    field.actualText[i] = field.values[i];
                }
            }
            if (field.x == 0f) {
                numberOfRows += field.values.length;
            }
        }

        if (numberOfRows == 0) {
            return new float[] { x, y };
        }

        float boxHeight = rowHeight*numberOfRows;
        Box box = new Box();
        box.setLocation(x, y);
        box.setSize(rowLength, boxHeight);
        box.drawOn(page);

        float field_y = 0f;
        int row_span = 1;
        float row_y = 0;
        for (Field field : fields) {
            if (field.x == 0f) {
                row_y += row_span*rowHeight;
                row_span = field.values.length;
            }
            field_y = row_y;
            for (int i = 0; i < field.values.length; i++) {
                Font font = (i == 0) ? f1 : f2;
                float fontSize = (i == 0) ? labelFontSize : valueFontSize;
                int color = (i == 0) ? labelColor : valueColor;
                new TextLine(font, field.values[i])
                        .setFontSize(fontSize)
                        .setColor(color)
                        .placeIn(box, field.x + f1.getDescent(), field_y - font.getDescent())
                        .setAltDescription((i == 0) ? field.altDescription[i] : (field.altDescription[i] + ","))
                        .setActualText((i == 0) ? field.actualText[i] : (field.actualText[i] + ","))
                        .drawOn(page);
                endOfLinePoints.add(new float[] {
                        field.x + f1.getDescent() + font.stringWidth(field.values[i]),
                        field_y - font.getDescent(),
                });
                if (i == (field.values.length - 1)) {
                    new Line(0f, 0f, rowLength, 0f)
                            .placeIn(box, 0f, field_y)
                            .drawOn(page);
                    if (field.x != 0f) {
                        new Line(0f, -(field.values.length-1)*rowHeight, 0f, 0f)
                                .placeIn(box, field.x, field_y)
                                .drawOn(page);
                    }
                }
                field_y += rowHeight;
            }
        }

        return new float[] { x + rowLength, y + boxHeight };
    }


    public static String[] format(String title, String text, Font font, float width) {

        String[] original = text.split("\\r?\\n");
        List<String> lines = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < original.length; i++) {
            String line = original[i];
            if (font.stringWidth(line) < width) {
                lines.add(line);
                continue;
            }

            buf.setLength(0);
            for (int j = 0; j < line.length(); j++) {
                buf.append(line.charAt(j));
                if (font.stringWidth(buf.toString()) > (width - font.stringWidth("   "))) {
                    while (j > 0 && line.charAt(j) != ' ') {
                        j -= 1;
                    }
                    String str = line.substring(0, j).replaceAll("\\s+$", "");
                    lines.add(str);
                    buf.setLength(0);
                    while (j < line.length() && line.charAt(j) == ' ') {
                        j += 1;
                    }
                    line = line.substring(j);
                    j = 0;
                }
            }

            if (!line.equals("")) {
                lines.add(line);
            }
        }

        int count = lines.size();
        String[] data = new String[1 + count];
        data[0] = title;
        for (int i = 0; i < count; i++) {
            data[i + 1] = lines.get(i);
        }

        return data;
    }

}   // End of Form.java
