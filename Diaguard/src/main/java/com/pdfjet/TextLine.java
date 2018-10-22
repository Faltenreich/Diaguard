/**
 *  TextLine.java
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


/**
 *  Used to create text line objects.
 *
 *
 */
public class TextLine implements Drawable {

    protected float x;
    protected float y;

    protected Font font;
    protected Font fallbackFont;
    protected String str;
    protected boolean trailingSpace = true;

    private String uri;
    private String key;

    private boolean underline = false;
    private boolean strikeout = false;
    private String underlineTTS = "underline";
    private String strikeoutTTS = "strikeout";

    private int degrees = 0;
    private int color = Color.black;

    private float box_x;
    private float box_y;
    
    private int textEffect = Effect.NORMAL;
    private float verticalOffset = 0f;

    private String language = null;
    private String altDescription = null;
    private String actualText = null;

    private String uriLanguage = null;
    private String uriAltDescription = null;
    private String uriActualText = null;


    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     */
    public TextLine(Font font) {
        this.font = font;
    }


    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     *  @param text the text.
     */
    public TextLine(Font font, String text) {
        this.font = font;
        this.str = text;
        if (this.altDescription == null) {
            this.altDescription = text;
        }
        if (this.actualText == null) {
            this.actualText = text;
        }
    }


    /**
     *  Sets the text.
     *
     *  @param text the text.
     *  @return this TextLine.
     */
    public TextLine setText(String text) {
        this.str = text;
        if (this.altDescription == null) {
            this.altDescription = text;
        }
        if (this.actualText == null) {
            this.actualText = text;
        }
        return this;
    }


    /**
     *  Returns the text.
     *
     *  @return the text.
     */
    public String getText() {
        return str;
    }


    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     *  @return this TextLine.
     */
    public TextLine setPosition(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     *  @return this TextLine.
     */
    public TextLine setPosition(float x, float y) {
        return setLocation(x, y);
    }


    /**
     *  Sets the location where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     *  @return this TextLine.
     */
    public TextLine setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Sets the font to use for this text line.
     *
     *  @param font the font to use.
     *  @return this TextLine.
     */
    public TextLine setFont(Font font) {
        this.font = font;
        return this;
    }


    /**
     *  Gets the font to use for this text line.
     *
     *  @return font the font to use.
     */
    public Font getFont() {
        return font;
    }


    /**
     *  Sets the font size to use for this text line.
     *
     *  @param fontSize the fontSize to use.
     *  @return this TextLine.
     */
    public TextLine setFontSize(float fontSize) {
        this.font.setSize(fontSize);
        return this;
    }


    /**
     *  Sets the fallback font.
     *
     *  @param fallbackFont the fallback font.
     *  @return this TextLine.
     */
    public TextLine setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
        return this;
    }


    /**
     *  Sets the fallback font size to use for this text line.
     *
     *  @param fallbackFontSize the fallback font size.
     *  @return this TextLine.
     */
    public TextLine setFallbackFontSize(float fallbackFontSize) {
        this.fallbackFont.setSize(fallbackFontSize);
        return this;
    }


    /**
     *  Returns the fallback font.
     *
     *  @return the fallback font.
     */
    public Font getFallbackFont() {
        return this.fallbackFont;
    }


    /**
     *  Sets the color for this text line.
     *
     *  @param color the color is specified as an integer.
     *  @return this TextLine.
     */
    public TextLine setColor(int color) {
        this.color = color;
        return this;
    }


    /**
     *  Sets the pen color.
     * 
     *  @param color the color. See the Color class for predefined values or define your own using 0x00RRGGBB packed integers.
     *  @return this TextLine.
     */
    public TextLine setColor(int[] color) {
        this.color = color[0] << 16 | color[1] << 8 | color[2];
        return this;
    }


    /**
     *  Returns the text line color.
     *
     *  @return the text line color.
     */
    public int getColor() {
        return this.color;
    }


    /**
     * Returns the y coordinate of the destination.
     * 
     * @return the y coordinate of the destination.
     */
    public float getDestinationY() {
        return y - font.getSize();
    }


    /**
     * Returns the y coordinate of the destination.
     * 
     * @return the y coordinate of the destination.
     */
    public float getY() {
        return getDestinationY();
    }


    /**
     *  Returns the width of this TextLine.
     *
     *  @return the width.
     */
    public float getWidth() {
        if (fallbackFont == null) {
            return font.stringWidth(str);
        }
        return font.stringWidth(fallbackFont, str);
    }


    /**
     *  Returns the height of this TextLine.
     *
     *  @return the height.
     */
    public float getHeight() {
        return font.getHeight();
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI
     *  @return this TextLine.
     */
    public TextLine setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    /**
     *  Returns the action URI.
     * 
     *  @return the action URI.
     */
    public String getURIAction() {
        return this.uri;
    }


    /**
     *  Sets the destination key for the action.
     *
     *  @param key the destination name.
     *  @return this TextLine.
     */
    public TextLine setGoToAction(String key) {
        this.key = key;
        return this;
    }


    /**
     * Returns the GoTo action string.
     * 
     * @return the GoTo action string.
     */
    public String getGoToAction() {
        return this.key;
    }


    /**
     *  Sets the underline variable.
     *  If the value of the underline variable is 'true' - the text is underlined.
     *
     *  @param underline the underline flag.
     *  @return this TextLine.
     */
    public TextLine setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }


    /**
     * Returns the underline flag.
     * 
     * @return the underline flag.
     */
    public boolean getUnderline() {
        return this.underline;
    }


    /**
     *  Sets the strike variable.
     *  If the value of the strike variable is 'true' - a strike line is drawn through the text.
     *
     *  @param strikeout the strikeout flag.
     *  @return this TextLine.
     */
    public TextLine setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
        return this;
    }


    /**
     *  Returns the strikeout flag.
     * 
     *  @return the strikeout flag.
     */
    public boolean getStrikeout() {
        return this.strikeout;
    }


    /**
     *  Sets the direction in which to draw the text.
     *
     *  @param degrees the number of degrees.
     *  @return this TextLine.
     */
    public TextLine setTextDirection(int degrees) {
        this.degrees = degrees;
        return this;
    }


    /**
     * Returns the text direction.
     * 
     * @return the text direction.
     */
    public int getTextDirection() {
        return degrees;
    }


    /**
     *  Sets the text effect.
     * 
     *  @param textEffect Effect.NORMAL, Effect.SUBSCRIPT or Effect.SUPERSCRIPT.
     *  @return this TextLine.
     */
    public TextLine setTextEffect(int textEffect) {
        this.textEffect = textEffect;
        return this;
    }


    /**
     *  Returns the text effect.
     * 
     *  @return the text effect.
     */
    public int getTextEffect() {
        return textEffect;
    }


    /**
     *  Sets the vertical offset of the text.
     * 
     *  @param verticalOffset the vertical offset.
     *  @return this TextLine.
     */
    public TextLine setVerticalOffset(float verticalOffset) {
        this.verticalOffset = verticalOffset;
        return this;
    }


    /**
     *  Returns the vertical text offset.
     * 
     *  @return the vertical text offset.
     */
    public float getVerticalOffset() {
        return verticalOffset;
    }


    /**
     *  Sets the trailing space after this text line when used in paragraph.
     * 
     *  @param trailingSpace the trailing space.
     *  @return this TextLine.
     */
    public TextLine setTrailingSpace(boolean trailingSpace) {
        this.trailingSpace = trailingSpace;
        return this;
    }


    /**
     *  Returns the trailing space.
     * 
     *  @return the trailing space.
     */
    public boolean getTrailingSpace() {
        return trailingSpace;
    }


    public TextLine setLanguage(String language) {
        this.language = language;
        return this;
    }


    public String getLanguage() {
        return this.language;
    }


    /**
     *  Sets the alternate description of this text line.
     *
     *  @param altDescription the alternate description of the text line.
     *  @return this TextLine.
     */
    public TextLine setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    public String getAltDescription() {
        return altDescription;
    }


    /**
     *  Sets the actual text for this text line.
     *
     *  @param actualText the actual text for the text line.
     *  @return this TextLine.
     */
    public TextLine setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    public String getActualText() {
        return actualText;
    }


    public TextLine setURILanguage(String uriLanguage) {
        this.uriLanguage = uriLanguage;
        return this;
    }


    public TextLine setURIAltDescription(String uriAltDescription) {
        this.uriAltDescription = uriAltDescription;
        return this;
    }


    public TextLine setURIActualText(String uriActualText) {
        this.uriActualText = uriActualText;
        return this;
    }


    /**
     *  Places this text line in the specified box.
     *
     *  @param box the specified box.
     *  @return this TextLine.
     */
    public TextLine placeIn(Box box) throws Exception {
        placeIn(box, 0f, 0f);
        return this;
    }


    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     *  @return this TextLine.
     */
    public TextLine placeIn(
            Box box,
            double x_offset,
            double y_offset) throws Exception {
        return placeIn(box, (float) x_offset, (float) y_offset);
    }


    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     *  @return this TextLine.
     */
    public TextLine placeIn(
            Box box,
            float x_offset,
            float y_offset) throws Exception {
        box_x = box.x + x_offset;
        box_y = box.y + y_offset;
        return this;
    }


    /**
     *  Draws this text line on the specified page.
     *
     *  @param page the page to draw this text line on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page) throws Exception {
        return drawOn(page, true);
    }


    /**
     *  Draws this text line on the specified page if the draw parameter is true.
     *
     *  @param page the page to draw this text line on.
     *  @param draw if draw is false - no action is performed.
     */
    protected float[] drawOn(Page page, boolean draw) throws Exception {
        if (page == null || !draw || str == null || str.equals("")) {
            return new float[] {x, y};
        }

        page.setTextDirection(degrees);

        x += box_x;
        y += box_y;

        page.setBrushColor(color);
        page.addBMC(StructElem.SPAN, language, altDescription, actualText);
        if (fallbackFont == null) {
            page.drawString(font, str, x, y);
        }
        else {
            page.drawString(font, fallbackFont, str, x, y);
        }
        page.addEMC();

        double radians = Math.PI * degrees / 180.0;
        if (underline) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double x_adjust = font.underlinePosition * Math.sin(radians);
            double y_adjust = font.underlinePosition * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(StructElem.SPAN, language, underlineTTS, underlineTTS);
            page.moveTo(x + x_adjust, y + y_adjust);
            page.lineTo(x2 + x_adjust, y2 + y_adjust);
            page.strokePath();
            page.addEMC();
        }

        if (strikeout) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double x_adjust = ( font.body_height / 4.0 ) * Math.sin(radians);
            double y_adjust = ( font.body_height / 4.0 ) * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(StructElem.SPAN, language, strikeoutTTS, strikeoutTTS);
            page.moveTo(x - x_adjust, y - y_adjust);
            page.lineTo(x2 - x_adjust, y2 - y_adjust);
            page.strokePath();
            page.addEMC();
        }

        if (uri != null || key != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    page.height - (y - font.ascent),
                    x + font.stringWidth(str),
                    page.height - (y - font.descent),
                    uriLanguage,
                    uriAltDescription,
                    uriActualText));
        }
        page.setTextDirection(0);

        float len = font.stringWidth(str);
        double x_max = Math.max((double) x, x + len*Math.cos(radians));
        double y_max = Math.max((double) y, y - len*Math.sin(radians));

        return new float[] {(float) x_max, (float) y_max};
    }

}   // End of TextLine.java
