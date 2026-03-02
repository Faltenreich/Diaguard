/**
 *  TextFrame.java
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
 *  Please see Example_47
 *
 */
public class TextFrame {

    private List<Paragraph> paragraphs;
    private Font font;
    private Font fallbackFont;
    private float x;
    private float y;
    private float w;
    private float h;
    private float x_text;
    private float y_text;
    private float leading;
    private float paragraphLeading;
    private List<float[]> beginParagraphPoints;
    private List<float[]> endParagraphPoints;
    private float spaceBetweenTextLines;


    public TextFrame(List<Paragraph> paragraphs) throws Exception {
        if (paragraphs != null) {
            this.paragraphs = paragraphs;
            this.font = paragraphs.get(0).list.get(0).getFont();
            this.fallbackFont = paragraphs.get(0).list.get(0).getFallbackFont();
            this.leading = font.getBodyHeight();
            this.paragraphLeading = 2*leading;
            this.beginParagraphPoints = new ArrayList<float[]>();
            this.endParagraphPoints = new ArrayList<float[]>();
            this.spaceBetweenTextLines = font.stringWidth(fallbackFont, Single.space);
        }
    }


    public TextFrame setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public TextFrame setWidth(float w) {
        this.w = w;
        return this;
    }


    public TextFrame setHeight(float h) {
        this.h = h;
        return this;
    }


    public TextFrame setLeading(float leading) {
        this.leading = leading;
        return this;
    }


    public TextFrame setParagraphLeading(float paragraphLeading) {
        this.paragraphLeading = paragraphLeading;
        return this;
    }


    public List<float[]> getBeginParagraphPoints() {
        return this.beginParagraphPoints;
    }


    public List<float[]> getEndParagraphPoints() {
        return this.endParagraphPoints;
    }


    public TextFrame setSpaceBetweenTextLines(float spaceBetweenTextLines) {
        this.spaceBetweenTextLines = spaceBetweenTextLines;
        return this;
    }


    public List<Paragraph> getParagraphs() {
        return this.paragraphs;
    }


    public TextFrame drawOn(Page page) throws Exception {
        return drawOn(page, true);
    }


    public TextFrame drawOn(Page page, boolean draw) throws Exception {
        this.x_text = x;
        this.y_text = y + font.getAscent();

        Paragraph paragraph = null;
        for (int i = 0; i < paragraphs.size(); i++) {
            paragraph = paragraphs.get(i);

            StringBuilder buf = new StringBuilder();
            for (TextLine textLine : paragraph.list) {
                buf.append(textLine.getText());
                buf.append(Single.space);
            }

            int numOfTextLines = paragraph.list.size();
            for (int j = 0; j < numOfTextLines; j++) {
                TextLine textLine1 = paragraph.list.get(j);
                if (j == 0) {
                    beginParagraphPoints.add(new float[] { x_text, y_text });
                }
                textLine1.setAltDescription((j == 0) ? buf.toString() : Single.space);
                textLine1.setActualText((j == 0) ? buf.toString() : Single.space);

                TextLine textLine2 = drawTextLine(
                        page, x_text, y_text, textLine1, draw);
                if (!textLine2.getText().equals("")) {
                    List<Paragraph> theRest = new ArrayList<Paragraph>();
                    Paragraph paragraph2 = new Paragraph(textLine2);
                    j++;
                    while (j < numOfTextLines) {
                        paragraph2.add(paragraph.list.get(j));
                        j++;
                    }
                    theRest.add(paragraph2);
                    i++;
                    while (i < paragraphs.size()) {
                        theRest.add(paragraphs.get(i));
                        i++;
                    }
                    return new TextFrame(theRest);
                }

                if (j == (numOfTextLines - 1)) {
                    endParagraphPoints.add(new float[] { textLine2.x, textLine2.y });
                }
                x_text = textLine2.x;
                if (textLine1.getTrailingSpace()) {
                    x_text += spaceBetweenTextLines;
                }
                y_text = textLine2.y;
            }
            x_text = x;
            y_text += paragraphLeading;
        }

        TextFrame textFrame = new TextFrame(null);
        textFrame.setLocation(x_text, y_text + font.getDescent());
        return textFrame;
    }


    public TextLine drawTextLine(
            Page page,
            float x_text,
            float y_text,
            TextLine textLine,
            boolean draw) throws Exception {

        TextLine textLine2 = null;
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

                if (y_text + font.getDescent() > (y + h)) {
                    i++;
                    while (i < tokens.length) {
                        buf.append(Single.space);
                        buf.append(tokens[i]);
                        i++;
                    }
                    textLine2 = new TextLine(font, buf.toString());
                    textLine2.setLocation(x, y_text);
                    return textLine2;
                }
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

        textLine2 = new TextLine(font, "");
        textLine2.setLocation(x_text, y_text);
        return textLine2;
    }

}   // End of TextFrame.java
