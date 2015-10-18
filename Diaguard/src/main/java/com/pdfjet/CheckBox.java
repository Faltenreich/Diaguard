/**
 *  CheckBox.java
 *
Copyright (c) 2014, Innovatics Inc.
All rights reserved.

Portions provided by Shirley C. Christenson
Shirley Christenson Consulting

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
 *  Creates a CheckBox, which can be set checked or unchecked.
 *  By default the check box is unchecked.
 */
public class CheckBox {

    private float x;
    private float y;
    private float w;
    private float h;
    private int boxColor = Color.black;
    private int checkColor = Color.black;
    private float penWidth;
    private float checkWidth;
    private int mark = 0;
    private Font font = null;
    private String label = "";
    private String uri = null;

    private String language = null;
    private String altDescription = Single.space;
    private String actualText = Single.space;


    /**
     *  Creates a CheckBox with black check mark.
     *
     */
    public CheckBox(Font font, String label) {
        this.font = font;
        this.label = label;
    }


    /**
     *  Sets the font size to use for this text line.
     *
     *  @param fontSize the fontSize to use.
     *  @return this CheckBox.
     */
    public CheckBox setFontSize(float fontSize) {
        this.font.setSize(fontSize);
        return this;
    }


    /**
     *  Sets the color of the check box.
     *
     *  @param boxColor the check box color specified as an 0xRRGGBB integer.
     *  @return this CheckBox.
     */
    public CheckBox setBoxColor(int boxColor) {
        this.boxColor = boxColor;
        return this;
    }


    /**
     *  Sets the color of the check mark.
     *
     *  @param checkColor the check mark color specified as an 0xRRGGBB integer.
     *  @return this CheckBox.
     */
    public CheckBox setCheckmark(int checkColor) {
        this.checkColor = checkColor;
        return this;
    }


    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this CheckBox.
     */
    public CheckBox setPosition(float x, float y) {
        return setLocation(x, y);
    }


    /**
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this CheckBox.
     */
    public CheckBox setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Gets the height of the CheckBox.
     *
     */
    public float getHeight() {
        return this.h;
    }


    /**
     *  Gets the width of the CheckBox.
     *
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Checks or unchecks this check box. See the Mark class for available options.
     *
     *  @return this CheckBox.
     */
    public CheckBox check(int mark) {
        this.mark = mark;
        return this;
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI.
     *  @return this CheckBox.
     */
    public CheckBox setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    /**
     *  Sets the alternate description of this check box.
     *
     *  @param altDescription the alternate description of the check box.
     *  @return this Checkbox.
     */
    public CheckBox setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this check box.
     *
     *  @param actualText the actual text for the check box.
     *  @return this CheckBox.
     */
    public CheckBox setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  Draws this CheckBox on the specified Page.
     *
     *  @param page the Page where the CheckBox is to be drawn.
     */
    public float[] drawOn(Page page) throws Exception {
        page.addBMC(StructElem.SPAN, language, altDescription, actualText);

        this.w = font.getAscent();
        this.h = this.w;
        this.penWidth = this.w/15;
        this.checkWidth = this.w/5;

        float y_box = y - font.getAscent();
        page.setPenWidth(penWidth);
        page.setPenColor(boxColor);
        page.setLinePattern("[] 0");
        page.drawRect(x, y_box, w, h);

        if (mark == Mark.CHECK || mark == Mark.X) {
        	page.setPenWidth(checkWidth);
        	page.setPenColor(checkColor);
        	if (mark == Mark.CHECK) {
                // Draw check mark
        		page.moveTo(x + checkWidth, y_box + h/2);
        		page.lineTo(x + w/6 + checkWidth, (y_box + h) - 4f*checkWidth/3f);
        		page.lineTo((x + w) - checkWidth, y_box + checkWidth);
        	    page.strokePath();
        	}
        	else if (mark == Mark.X) {
                // Draw 'X' mark
                page.moveTo(x + checkWidth, y_box + checkWidth);
                page.lineTo((x + w) - checkWidth, (y_box + h) - checkWidth);
                page.moveTo((x + w) - checkWidth, y_box + checkWidth);
                page.lineTo(x + checkWidth, (y_box + h) - checkWidth);
                page.strokePath();
        	}
        }

        if (uri != null) {
            page.setBrushColor(Color.blue);
        }
        page.drawString(font, label, x + 3f*w/2f, y);
        page.setPenWidth(0f);
        page.setPenColor(Color.black);
        page.setBrushColor(Color.black);

        page.addEMC();

        if (uri != null) {
            // Please note: The font descent is a negative number.
            page.addAnnotation(new Annotation(
                    uri,
                    null,
                    x + 3f*w/2f,
                    page.height - y,
                    x + 3f*w/2f + font.stringWidth(label),
                    page.height - (y - font.getAscent()),
                    language,
                    altDescription,
                    actualText));
        }

        return new float[] { x + 3f*w + font.stringWidth(label), y + font.getBodyHeight() };
    }

}   // End of CheckBox.java
