/**
 *  RadioButton.java
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


/**
 *  Creates a RadioButton, which can be set selected or unselected.
 *
 */
public class RadioButton {

    private boolean selected = false;
    private float x;
    private float y;
    private float r1;
    private float r2;
    private float penWidth;
    private Font font = null;
    private String label = "";
    private String uri = null;

    private String language = null;
    private String altDescription = Single.space;
    private String actualText = Single.space;


    /**
     *  Creates a RadioButton that is not selected.
     *
     */
    public RadioButton(Font font, String label) {
        this.font = font;
        this.label = label;
    }


    /**
     *  Sets the font size to use for this text line.
     *
     *  @param fontSize the fontSize to use.
     *  @return this RadioButton.
     */
    public RadioButton setFontSize(float fontSize) {
        this.font.setSize(fontSize);
        return this;
    }


    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this RadioButton.
     */
    public RadioButton setPosition(float x, float y) {
        return setLocation(x, y);
    }


    /**
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this RadioButton.
     */
    public RadioButton setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI.
     *  @return this RadioButton.
     */
    public RadioButton setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    /**
     *  Selects or deselects this radio button.
     *
     *  @param selected the selection flag.
     *  @return this RadioButton.
     */
    public RadioButton select(boolean selected) {
        this.selected = selected;
        return this;
    }


    /**
     *  Sets the alternate description of this radio button.
     *
     *  @param altDescription the alternate description of the radio button.
     *  @return this RadioButton.
     */
    public RadioButton setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this radio button.
     *
     *  @param actualText the actual text for the radio button.
     *  @return this RadioButton.
     */
    public RadioButton setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  Draws this RadioButton on the specified Page.
     *
     *  @param page the Page where the RadioButton is to be drawn.
     */
    public float[] drawOn(Page page) throws Exception {
        page.addBMC(StructElem.SPAN, language, altDescription, actualText);

        this.r1 = font.getAscent()/2;
        this.r2 = r1/2;
        this.penWidth = r1/10;

        float y_box = y - font.getAscent();
        page.setPenWidth(1f);
        page.setPenColor(Color.black);
        page.setLinePattern("[] 0");
        page.setBrushColor(Color.black);
        page.drawCircle(x + r1, y_box + r1, r1);
        
        if (this.selected) {
            page.drawCircle(x + r1, y_box + r1, r2, Operation.FILL);
        }

        if (uri != null) {
            page.setBrushColor(Color.blue);
        }
        page.drawString(font, label, x + 3*r1, y);
        page.setPenWidth(0f);
        page.setBrushColor(Color.black);

        page.addEMC();

        if (uri != null) {
            // Please note: The font descent is a negative number.
            page.addAnnotation(new Annotation(
                    uri,
                    null,
                    x + 3*r1,
                    page.height - y,
                    x + 3*r1 + font.stringWidth(label),
                    page.height - (y - font.getAscent()),
                    language,
                    altDescription,
                    actualText));
        }

        return new float[] { x + 6*r1 + font.stringWidth(label), y + font.getBodyHeight() };
    }

}   // End of RadioButton.java
