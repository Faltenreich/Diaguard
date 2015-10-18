/**
 *  TextBox.java
 *
Copyright (c) 2015, Innovatics Inc.
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
 *  A box containing line-wrapped text.
 *  
 *  <p>Defaults:<br />
 *  x = 0f<br />
 *  y = 0f<br />
 *  width = 300f<br />
 *  height = 200f<br />
 *  alignment = Align.LEFT<br />
 *  valign = Align.TOP<br />
 *  spacing = 3f<br />
 *  margin = 1f<br />
 *  </p>
 *  
 *  This class was originally developed by Ronald Bourret.
 *  It was completely rewritten in 2013 by Eugene Dragoev.
 */
public class TextBox implements Drawable {

    protected Font font;
    protected String text;

    protected float x;
    protected float y;

    protected float width = 300f;
    protected float height = 200f;
    protected float spacing = 3f;
    protected float margin = 1f;
    private float lineWidth = 0f;

    private int background = Color.white;
    private int pen = Color.black;
    private int brush = Color.black;
    private int valign = 0;
    private Font fallbackFont;
    private Map<String, Integer> colors = null;

    // TextBox properties
    // Future use:
    // bits 0 to 15
    // Border:
    // bit 16 - top
    // bit 17 - bottom
    // bit 18 - left
    // bit 19 - right
    // Text Alignment:
    // bit 20
    // bit 21
    // Text Decoration:
    // bit 22 - underline
    // bit 23 - strikeout
    // Future use:
    // bits 24 to 31
    private int properties = 0x000F0001;


    /**
     *  Creates a text box and sets the font.
     *
     *  @param font the font.
     */
    public TextBox(Font font) {
        this.font = font;
    }


    /**
     *  Creates a text box and sets the font and the text.
     *
     *  @param font the font.
     *  @param text the text.
     *  @param width the width.
     *  @param height the height.
     */
    public TextBox(Font font, String text, double width, double height) {
        this(font, text, (float) width, (float) height);
    }


    /**
     *  Creates a text box and sets the font and the text.
     *
     *  @param font the font.
     *  @param text the text.
     *  @param width the width.
     *  @param height the height.
     */
    public TextBox(Font font, String text, float width, float height) {
        this.font = font;
        this.text = text;
        this.width = width;
        this.height = height;
    }


    /**
     *  Sets the font for this text box.
     *
     *  @param font the font.
     */
    public void setFont(Font font) {
        this.font = font;
    }


    /**
     *  Returns the font used by this text box.
     *
     *  @return the font.
     */
    public Font getFont() {
        return font;
    }


    /**
     *  Sets the text box text.
     *
     *  @param text the text box text.
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     *  Returns the text box text.
     *
     *  @return the text box text.
     */
    public String getText() {
        return text;
    }


    /**
     *  Sets the position where this text box will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text box.
     *  @param y the y coordinate of the top left corner of the text box.
     */
    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);
    }


    /**
     *  Sets the position where this text box will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text box.
     *  @param y the y coordinate of the top left corner of the text box.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location where this text box will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text box.
     *  @param y the y coordinate of the top left corner of the text box.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
    

    /**
     *  Gets the x coordinate where this text box will be drawn on the page.
     *
     *  @return the x coordinate of the top left corner of the text box.
     */
    public float getX() {
        return x;
    }    


    /**
     *  Gets the y coordinate where this text box will be drawn on the page.
     *
     *  @return the y coordinate of the top left corner of the text box.
     */
    public float getY() {
        return y;
    }    


    /**
     *  Sets the width of this text box.
     *
     *  @param width the specified width.
     */
    public void setWidth(double width) {
        this.width = (float) width;
    }


    /**
     *  Sets the width of this text box.
     *
     *  @param width the specified width.
     */
    public void setWidth(float width) {
        this.width = width;
    }


    /**
     *  Returns the text box width.
     *
     *  @return the text box width.
     */
    public float getWidth() {
        return width;
    }


    /**
     *  Sets the height of this text box.
     *
     *  @param height the specified height.
     */
    public void setHeight(double height) {
        this.height = (float) height;
    }


    /**
     *  Sets the height of this text box.
     *
     *  @param height the specified height.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    
    /**
     *  Returns the text box height.
     *
     *  @return the text box height.
     */
    public float getHeight() {
        return height;
    }


    /**
     *  Sets the margin of this text box.
     *
     *  @param margin the margin between the text and the box
     */
    public void setMargin(double margin) {
        this.margin = (float) margin;
    }


    /**
     *  Sets the margin of this text box.
     *
     *  @param margin the margin between the text and the box
     */
    public void setMargin(float margin) {
        this.margin = margin;
    }

    
    /**
     *  Returns the text box margin.
     *
     *  @return the margin between the text and the box
     */
    public float getMargin() {
        return margin;
    }


    /**
     *  Sets the border line width.
     *
     *  @param lineWidth double
     */
    public void setLineWidth(double lineWidth) {
        this.lineWidth = (float) lineWidth;
    }


    /**
     *  Sets the border line width.
     *
     *  @param lineWidth float
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }


    /**
     *  Returns the border line width.
     *
     *  @return float the line width.
     */
    public float getLineWidth() {
        return lineWidth;
    }


    /**
     *  Sets the spacing between lines of text.
     *
     *  @param spacing the spacing
     */
    public void setSpacing(double spacing) {
        this.spacing = (float) spacing;
    }


    /**
     *  Sets the spacing between lines of text.
     *
     *  @param spacing
     */
    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }


    /**
     *  Returns the spacing between lines of text.
     *
     *  @return float the spacing.
     */
    public float getSpacing() {
        return spacing;
    }


    /**
     *  Sets the background to the specified color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     */
    public void setBgColor(int color) {
        this.background = color;
    }


    /**
     *  Sets the background to the specified color.
     *
     *  @param color the color specified as array of integer values from 0x00 to 0xFF.
     */
    public void setBgColor(int[] color) {
        this.background = color[0] << 16 | color[1] << 8 | color[2];
    }


    /**
     *  Sets the background to the specified color.
     *
     *  @param color the color specified as array of double values from 0.0 to 1.0.
     */
    public void setBgColor(double[] color) {
        setBgColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
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
     *  Sets the pen and brush colors to the specified color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int color) {
        this.pen = color;
        this.brush = color;
    }


    /**
     *  Sets the pen and brush colors to the specified color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int[] color) {
        this.pen = color[0] << 16 | color[1] << 8 | color[2];
        this.brush = pen;
    }


    /**
     *  Sets the foreground pen and brush colors to the specified color.
     *
     *  @param color the color specified as an array of double values from 0.0 to 1.0.
     */
    public void setFgColor(double[] color) {
        setPenColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
        setBrushColor(pen);
    }


    /**
     *  Sets the pen color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     */
    public void setPenColor(int color) {
        this.pen = color;
    }


    /**
     *  Sets the pen color.
     *
     *  @param color the color specified as an array of int values from 0x00 to 0xFF.
     */
    public void setPenColor(int[] color) {
        this.pen = color[0] << 16 | color[1] << 8 | color[2];
    }


    /**
     *  Sets the pen color.
     *
     *  @param color the color specified as an array of double values from 0.0 to 1.0.
     */
    public void setPenColor(double[] color) {
        setPenColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
    }


    /**
     *  Returns the pen color as 0xRRGGBB integer.
     *
     * @return int the pen color.
     */
    public int getPenColor() {
        return this.pen;
    }


    /**
     *  Sets the brush color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     */
    public void setBrushColor(int color) {
        this.brush = color;
    }


    /**
     *  Sets the brush color.
     *
     *  @param color the color specified as an array of int values from 0x00 to 0xFF.
     */
    public void setBrushColor(int[] color) {
        this.brush = color[0] << 16 | color[1] << 8 | color[2];
    }


    /**
     *  Sets the brush color.
     *
     *  @param color the color specified as an array of double values from 0.0 to 1.0.
     */
    public void setBrushColor(double[] color) {
        setBrushColor(new int [] { (int) color[0], (int) color[1], (int) color[2] });
    }


    /**
     * Returns the brush color.
     *
     * @return int the brush color specified as 0xRRGGBB integer.
     */
    public int getBrushColor() {
        return this.brush;
    }


    /**
     *  Sets the TextBox border object.
     *
     *  @param border the border object.
     */
    public void setBorder(int border, boolean visible) {
        if (visible) {
            this.properties |= border;
        }
        else {
            this.properties &= (~border & 0x00FFFFFF);
        }
    }


    /**
     *  Returns the text box border.
     *
     *  @return boolean the text border object.
     */
    public boolean getBorder(int border) {
        return (this.properties & border) != 0;
    }


    /**
     *  Sets all borders to be invisible.
     *  This cell will have no borders when drawn on the page.
     */
    public void setNoBorders() {
        this.properties &= 0x00F0FFFF;
    }


    /**
     *  Sets the cell text alignment.
     *
     *  @param alignment the alignment code.
     *  Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public void setTextAlignment(int alignment) {
        this.properties &= 0x00CFFFFF;
        this.properties |= (alignment & 0x00300000);
    }


    /**
     *  Returns the text alignment.
     *
     *  @return alignment the alignment code. Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public int getTextAlignment() {
        return (this.properties & 0x00300000);
    }


    /**
     *  Sets the underline variable.
     *  If the value of the underline variable is 'true' - the text is underlined.
     *
     *  @param underline the underline flag.
     */
    public void setUnderline(boolean underline) {
        if (underline) {
            this.properties |= 0x00400000;
        }
        else {
            this.properties &= 0x00BFFFFF;
        }
    }


    /**
     *  Whether the text will be underlined.
     *  
     *  @return whether the text will be underlined
     */
    public boolean getUnderline() {
        return (properties & 0x00400000) != 0;
    }


    /**
     *  Sets the srikeout flag.
     *  In the flag is true - draw strikeout line through the text.
     *
     *  @param strikeout the strikeout flag.
     */
    public void setStrikeout(boolean strikeout) {
        if (strikeout) {
            this.properties |= 0x00800000;
        }
        else {
            this.properties &= 0x007FFFFF;
        }
    }


    /**
     *  Returns the strikeout flag.
     *
     *  @return boolean the strikeout flag.
     */
    public boolean getStrikeout() {
        return (properties & 0x00800000) != 0;
    }


    public void setFallbackFont(Font font) {
        this.fallbackFont = font;
    }


    /**
     *  Sets the vertical alignment of the text in this TextBox.
     *
     *  @param alignment - valid values are TextAlign.TOP, TextAlign.BOTTOM and TextAlign.CENTER
     */
    public void setVerticalAlignment(int alignment) {
        this.valign = alignment;
    }


    public int getVerticalAlignment() {
        return this.valign;
    }


    public void setTextColors(Map<String, Integer> colors) {
        this.colors = colors;
    }


    /**
     *  Draws this text box on the specified page.
     *
     */
    public void drawOn(Page page) throws Exception {
        if (getBgColor() != Color.white) {
            drawBackground(page);
        }
        drawBorders(page);
        drawText(page);
    }


    private void drawBackground(Page page) throws Exception {
        page.setBrushColor(background);
        page.fillRect(x, y, width, height);
    }


    private void drawBorders(Page page) throws Exception {
        page.setPenColor(pen);
        page.setPenWidth(lineWidth);

        if (getBorder(Border.TOP) &&
                getBorder(Border.BOTTOM) &&
                getBorder(Border.LEFT) &&
                getBorder(Border.RIGHT)) {
            page.drawRect(x, y, width, height);
        }
        else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + width, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + height);
                page.lineTo(x + width, y + height);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + height);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + width, y);
                page.lineTo(x + width, y + height);
                page.strokePath();
            }
        }
    }


    private void drawText(Page page) throws Exception {
 
        page.setPenColor(this.pen);
        page.setBrushColor(this.brush);
        page.setPenWidth(this.font.underlineThickness);
 
        float textAreaWidth = width - 2*margin;
        List<String> list = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (font.stringWidth(line) < textAreaWidth) {
                list.add(line);
            }
            else {
                buf.setLength(0);
 
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {
                    if (font.stringWidth(buf.toString() + " " + token) < textAreaWidth) {
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
        lines = list.toArray(new String[] {} );
 
        float lineHeight = font.getBodyHeight() + spacing;
        float x_text;
        float y_text = y + font.ascent + margin;
        if (valign == TextAlign.BOTTOM) {
            y_text += height - lines.length*lineHeight;
        }
        else if (valign == TextAlign.CENTER) {
            y_text += (height - lines.length*lineHeight)/2;
        }
 
        for (int i = 0; i < lines.length; i++) {
 
            if (getTextAlignment() == Align.RIGHT) {
                x_text = (x + width) - (font.stringWidth(lines[i]) + margin);
            }
            else if (getTextAlignment() == Align.CENTER) {
                x_text = x + (width - font.stringWidth(lines[i]))/2;
            }
            else {
                // Align.LEFT
                x_text = x + margin;
            }
 
            if (y_text + font.getBodyHeight() + spacing + font.getDescent() >= y + height
                    && i < (lines.length - 1)) {
                String str = lines[i];
                int index = str.lastIndexOf(' ');
                if (index != -1) {
                    lines[i] = str.substring(0, index) + " ...";
                }
                else {
                    lines[i] = str + " ...";
                }
            }

            if (y_text + font.getDescent() < y + height) {
                if (fallbackFont == null) {
                    if (colors == null) {
                        page.drawString(font, lines[i], x_text, y_text);
                    }
                    else {
                        page.drawString(font, lines[i], x_text, y_text, colors);
                    }
                }
                else {
                    page.drawString(font, fallbackFont, lines[i], x_text, y_text);
                }
 
                float lineLength = font.stringWidth(lines[i]);
                if (getUnderline()) {
                    float y_adjust = font.underlinePosition;
                    page.moveTo(x_text, y_text + y_adjust);
                    page.lineTo(x_text + lineLength, y_text + y_adjust);
                    page.strokePath();
                }
                if (getStrikeout()) {
                    float y_adjust = font.body_height/4;
                    page.moveTo(x_text, y_text - y_adjust);
                    page.lineTo(x_text + lineLength, y_text - y_adjust);
                    page.strokePath();
                }
 
                y_text += font.getBodyHeight() + spacing;
            }
        }
    }

}   // End of TextBox.java
