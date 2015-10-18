/**
 *  Text.java
 *
Copyright (c) 2014, Innovatics Inc.
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
public class Text {

    private List<Paragraph> paragraphs;
    private Font font;
    private Font fallbackFont;
    private float x;
    private float y;
    private float w;
    private float x_text;
    private float y_text;
    private float leading;
    private float paragraphLeading;
    private List<float[]> beginParagraphPoints;
    private List<float[]> endParagraphPoints;
    private float spaceBetweenTextLines;


    public Text(List<Paragraph> paragraphs) throws Exception {
        this.paragraphs = paragraphs;
        this.font = paragraphs.get(0).list.get(0).getFont();
        this.fallbackFont = paragraphs.get(0).list.get(0).getFallbackFont();
        this.leading = font.getBodyHeight();
        this.paragraphLeading = 2*leading;
        this.beginParagraphPoints = new ArrayList<float[]>();
        this.endParagraphPoints = new ArrayList<float[]>();
        this.spaceBetweenTextLines = font.stringWidth(fallbackFont, Single.space);
    }


    public Text setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public Text setWidth(float w) {
        this.w = w;
        return this;
    }


    public Text setLeading(float leading) {
        this.leading = leading;
        return this;
    }


    public Text setParagraphLeading(float paragraphLeading) {
        this.paragraphLeading = paragraphLeading;
        return this;
    }


    public List<float[]> getBeginParagraphPoints() {
        return this.beginParagraphPoints;
    }


    public List<float[]> getEndParagraphPoints() {
        return this.endParagraphPoints;
    }


    public Text setSpaceBetweenTextLines(float spaceBetweenTextLines) {
        this.spaceBetweenTextLines = spaceBetweenTextLines;
        return this;
    }


    public float[] drawOn(Page page) throws Exception {
        return drawOn(page, true);
    }


    public float[] drawOn(Page page, boolean draw) throws Exception {
        this.x_text = x;
        this.y_text = y + font.getAscent();
        for (Paragraph paragraph : paragraphs) {
            int numberOfTextLines = paragraph.list.size();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < numberOfTextLines; i++) {
                TextLine textLine = paragraph.list.get(i);
                buf.append(textLine.getText());
            }
            for (int i = 0; i < numberOfTextLines; i++) {
                TextLine textLine = paragraph.list.get(i);
                if (i == 0) {
                    beginParagraphPoints.add(new float[] { x_text, y_text });
                }
                textLine.setAltDescription((i == 0) ? buf.toString() : Single.space);
                textLine.setActualText((i == 0) ? buf.toString() : Single.space);
                float[] point = drawTextLine(
                        page, x_text, y_text, textLine, draw);
                if (i == (numberOfTextLines - 1)) {
                    endParagraphPoints.add(new float[] { point[0], point[1] });
                }
                x_text = point[0];
                if (textLine.getTrailingSpace()) {
                    x_text += spaceBetweenTextLines;
                }
                y_text = point[1];
            }
            x_text = x;
            y_text += paragraphLeading;
        }
        return new float[] { x_text, y_text + font.getDescent() };
    }


    public float[] drawTextLine(
            Page page,
            float x_text,
            float y_text,
            TextLine textLine,
            boolean draw) throws Exception {

        Font font = textLine.getFont();
        Font fallbackFont = textLine.getFallbackFont();
        int color = textLine.getColor();

        StringBuilder buf = new StringBuilder();
        String[] tokens = textLine.getText().split("\\s+");
        boolean firstTextSegment = true;
        for (int i = 0; i < tokens.length; i++) {
            String token = (i == 0) ? tokens[i] : (Single.space + tokens[i]);
            if (font.stringWidth(fallbackFont, token) < (this.w - (x_text - x))) {
                buf.append(token);
                x_text += font.stringWidth(fallbackFont, token);
            }
            else {
                if (draw) {
                    new TextLine(font, buf.toString())
                            .setFallbackFont(fallbackFont)
                            .setLocation(x_text - font.stringWidth(fallbackFont, buf.toString()),
                                    y_text + textLine.getVerticalOffset())
                            .setColor(color)
                            .setUnderline(textLine.getUnderline())
                            .setStrikeout(textLine.getStrikeout())
                            .setLanguage(textLine.getLanguage())
                            .setAltDescription(firstTextSegment ? textLine.getAltDescription() : Single.space)
                            .setActualText(firstTextSegment ? textLine.getActualText() : Single.space)
                            .drawOn(page);
                    firstTextSegment = false;
                }
                x_text = x + font.stringWidth(fallbackFont, tokens[i]);
                y_text += leading;
                buf.setLength(0);
                buf.append(tokens[i]);
            }
        }
        if (draw) {
            new TextLine(font, buf.toString())
                    .setFallbackFont(fallbackFont)
                    .setLocation(x_text - font.stringWidth(fallbackFont, buf.toString()),
                            y_text + textLine.getVerticalOffset())
                    .setColor(color)
                    .setUnderline(textLine.getUnderline())
                    .setStrikeout(textLine.getStrikeout())
                    .setLanguage(textLine.getLanguage())
                    .setAltDescription(firstTextSegment ? textLine.getAltDescription() : Single.space)
                    .setActualText(firstTextSegment ? textLine.getActualText() : Single.space)
                    .drawOn(page);
            firstTextSegment = false;
        }

        return new float[] { x_text, y_text };
    }

}   // End of Text.java
