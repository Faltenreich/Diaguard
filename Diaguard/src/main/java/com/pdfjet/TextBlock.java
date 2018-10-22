/**
 *  TextBlock.java
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
 *  Class for creating blocks of text.
 *
 */
public class TextBlock {

    private Font font = null;
    private Font fallbackFont = null;

    private String text = null;
    private float space = 0f;
    private int textAlign = Align.LEFT;

    private float x;
    private float y;
    private float w = 300f;
    private float h = 200f;

    private int background = Color.white;
    private int brush = Color.black;


    /**
     *  Creates a text block.
     *
     *  @param font the text font.
     */
    public TextBlock(Font font) {
        this.font = font;
        this.space = this.font.getDescent();
    }


    /**
     *  Sets the fallback font.
     *
     *  @param fallbackFont the fallback font.
     *  @return the TextBlock object.
     */
    public TextBlock setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
        return this;
    }


    /**
     *  Sets the block text.
     *
     *  @param text the block text.
     *  @return the TextBlock object.
     */
    public TextBlock setText(String text) {
        this.text = text;
        return this;
    }


    /**
     *  Sets the location where this text block will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text block.
     *  @param y the y coordinate of the top left corner of the text block.
     *  @return the TextBlock object.
     */
    public TextBlock setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Sets the width of this text block.
     *
     *  @param width the specified width.
     *  @return the TextBlock object.
     */
    public TextBlock setWidth(float width) {
        this.w = width;
        return this;
    }


    /**
     *  Returns the text block width.
     *
     *  @return the text block width.
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Sets the height of this text block.
     *
     *  @param height the specified height.
     *  @return the TextBlock object.
     */
    public TextBlock setHeight(float height) {
        this.h = height;
        return this;
    }


    /**
     *  Returns the text block height.
     *
     *  @return the text block height.
     */
    public float getHeight() throws Exception {
        return drawOn(null).h;
    }


    /**
     *  Sets the space between two lines of text.
     *
     *  @param space the space between two lines.
     *  @return the TextBlock object.
     */
    public TextBlock setSpaceBetweenLines(float space) {
        this.space = space;
        return this;
    }


    /**
     *  Returns the space between two lines of text.
     *
     *  @return float the space.
     */
    public float getSpaceBetweenLines() {
        return space;
    }


    /**
     *  Sets the text alignment.
     *
     *  @param textAlign the alignment parameter.
     *  Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public TextBlock setTextAlignment(int textAlign) {
        this.textAlign = textAlign;
        return this;
    }


    /**
     *  Returns the text alignment.
     *
     *  @return the alignment code.
     */
    public int getTextAlignment() {
        return this.textAlign;
    }


    /**
     *  Sets the background to the specified color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     *  @return the TextBlock object.
     */
    public TextBlock setBgColor(int color) {
        this.background = color;
        return this;
    }


    /**
     *  Returns the background color.
     *
     * @return int the color as 0xRRGGBB integer.
     */
    public int getBgColor() {
        return this.background;
    }


    /**
     *  Sets the brush color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     *  @return the TextBlock object.
     */
    public TextBlock setBrushColor(int color) {
        this.brush = color;
        return this;
    }


    /**
     * Returns the brush color.
     *
     * @return int the brush color specified as 0xRRGGBB integer.
     */
    public int getBrushColor() {
        return this.brush;
    }


    // Is the text Chinese, Japanese or Korean?
    private boolean isCJK(String text) {
        int cjk = 0;
        int other = 0;
        for (char ch : text.toCharArray()) {
            if (ch >= 0x4E00 && ch <= 0x9FFF ||     // Unified CJK
                ch >= 0xAC00 && ch <= 0xD7AF ||     // Hangul (Korean)
                ch >= 0x30A0 && ch <= 0x30FF ||     // Katakana (Japanese)
                ch >= 0x3040 && ch <= 0x309F) {     // Hiragana (Japanese)
                cjk += 1;
            }
            else {
                other += 1;
            }
        }
        return cjk > other;
    }


    /**
     *  Draws this text block on the specified page.
     *
     *  @param page the page to draw this text block on.
     *  @return the TextBlock object.
     */
    public TextBlock drawOn(Page page) throws Exception {
        if (page != null) {
            if (getBgColor() != Color.white) {
                page.setBrushColor(this.background);
                page.fillRect(x, y, w, h);
            }
            page.setBrushColor(this.brush);
        }
        return drawText(page);
    }


    private TextBlock drawText(Page page) throws Exception {

        List<String> list = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            if (isCJK(line)) {
                buf.setLength(0);
                for (int i = 0; i < line.length(); i++) {
                    Character ch = line.charAt(i);
                    if (font.stringWidth(fallbackFont, buf.toString() + ch) < this.w) {
                        buf.append(ch);
                    }
                    else {
                        list.add(buf.toString());
                        buf.setLength(0);
                        buf.append(ch);
                    }
                }
                if (!buf.toString().trim().equals("")) {
                    list.add(buf.toString().trim());
                }
            }
            else {
                if (font.stringWidth(fallbackFont, line) < this.w) {
                    list.add(line);
                }
                else {
                    buf.setLength(0);
                    String[] tokens = line.split("\\s+");
                    for (String token : tokens) {
                        if (font.stringWidth(fallbackFont,
                                buf.toString() + " " + token) < this.w) {
                            buf.append(" " + token);
                        }
                        else {
                            list.add(buf.toString().trim());
                            buf.setLength(0);
                            buf.append(token);
                        }
                    }

                    if (!buf.toString().trim().equals("")) {
                        list.add(buf.toString().trim());
                    }
                }
            }
        }
        lines = list.toArray(new String[] {});

        float x_text;
        float y_text = y + font.getAscent();

        for (int i = 0; i < lines.length; i++) {
            if (textAlign == Align.LEFT) {
                x_text = x;
            }
            else if (textAlign == Align.RIGHT) {
                x_text = (x + this.w) - (font.stringWidth(fallbackFont, lines[i]));
            }
            else if (textAlign == Align.CENTER) {
                x_text = x + (this.w - font.stringWidth(fallbackFont, lines[i]))/2;
            }
            else {
                throw new Exception("Invalid text alignment option.");
            }

            if (page != null) {
                page.drawString(
                        font, fallbackFont, lines[i], x_text, y_text);
            }

            if (i < (lines.length - 1)) {
                y_text += font.getBodyHeight() + space;
            }
            else {
                y_text += font.getDescent() + space;
            }
        }

        this.h = y_text - y;

        return this;
    }

}   // End of TextBlock.java
