/**
 *  Line.java
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
 *  Used to create line objects.
 *
 *  Please see Example_01.
 */
public class Line implements Drawable {

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private float box_x;
    private float box_y;

    private int color = Color.black;
    private float width = 0.3f;
    private String pattern = "[] 0";
    private int capStyle = 0;

    private String language = null;
    private String altDescription = Single.space;
    private String actualText = Single.space;


    /**
     *  The default constructor.
     *
     *
     */
    public Line() {
    }


    /**
     *  Create a line object.
     *
     *  @param x1 the x coordinate of the start point.
     *  @param y1 the y coordinate of the start point.
     *  @param x2 the x coordinate of the end point.
     *  @param y2 the y coordinate of the end point.     
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
    }


    /**
     *  Create a line object.
     *
     *  @param x1 the x coordinate of the start point.
     *  @param y1 the y coordinate of the start point.
     *  @param x2 the x coordinate of the end point.
     *  @param y2 the y coordinate of the end point.     
     */
    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    /**
     *  The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
     *  It is specified by a dash array and a dash phase.
     *  The elements of the dash array are positive numbers that specify the lengths of
     *  alternating dashes and gaps.
     *  The dash phase specifies the distance into the dash pattern at which to start the dash.
     *  The elements of both the dash array and the dash phase are expressed in user space units.
     *  <pre>
     *  Examples of line dash patterns:
     *
     *      "[Array] Phase"     Appearance          Description
     *      _______________     _________________   ____________________________________
     *
     *      "[] 0"              -----------------   Solid line
     *      "[3] 0"             ---   ---   ---     3 units on, 3 units off, ...
     *      "[2] 1"             -  --  --  --  --   1 on, 2 off, 2 on, 2 off, ...
     *      "[2 1] 0"           -- -- -- -- -- --   2 on, 1 off, 2 on, 1 off, ...
     *      "[3 5] 6"             ---     ---       2 off, 3 on, 5 off, 3 on, 5 off, ...
     *      "[2 3] 11"          -   --   --   --    1 on, 3 off, 2 on, 3 off, 2 on, ...
     *  </pre>
     *
     *  @param pattern the line dash pattern.
     *  @return this Line object.
     */
    public Line setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }


    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setStartPoint(double x, double y) {
        this.x1 = (float) x;
        this.y1 = (float) y;
        return this;
    }


    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setStartPoint(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }


    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setPointA(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }


    /**
     *  Returns the start point of this line.
     *
     *  @return Point the point.
     */
    public Point getStartPoint() {
        return new Point(x1, y1);
    }


    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the y coordinate of the end point.
     *  @return this Line object.
     */
    public Line setEndPoint(double x, double y) {
        this.x2 = (float) x;
        this.y2 = (float) y;
        return this;
    }


    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the t coordinate of the end point.
     *  @return this Line object.
     */
    public Line setEndPoint(float x, float y) {
        this.x2 = x;
        this.y2 = y;
        return this;
    }


    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the t coordinate of the end point.
     *  @return this Line object.
     */
    public Line setPointB(float x, float y) {
        this.x2 = x;
        this.y2 = y;
        return this;
    }

    
    /**
     *  Returns the end point of this line.
     *
     *  @return Point the point.
     */
    public Point getEndPoint() {
        return new Point(x2, y2);
    }


    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     *  @return this Line object.
     */
    public Line setWidth(double width) {
        this.width = (float) width;
        return this;
    }


    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     *  @return this Line object.
     */
    public Line setWidth(float width) {
        this.width = width;
        return this;
    }


    /**
     *  Sets the color for this line.
     *
     *  @param color the color specified as an integer.
     *  @return this Line object.
     */
    public Line setColor(int color) {
        this.color = color;
        return this;
    }


    /**
     *  Sets the line cap style.
     *
     *  @param style the cap style of the current line. Supported values: Cap.BUTT, Cap.ROUND and Cap.PROJECTING_SQUARE
     *  @return this Line object.
     */
    public Line setCapStyle(int style) {
        this.capStyle = style;
        return this;
    }


    /**
     *  Returns the line cap style.
     *
     *  @return the cap style.
     */
    public int getCapStyle() {
        return capStyle;
    }


    /**
     *  Sets the alternate description of this line.
     *
     *  @param altDescription the alternate description of the line.
     *  @return this Line.
     */
    public Line setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this line.
     *
     *  @param actualText the actual text for the line.
     *  @return this Line.
     */
    public Line setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  Places this line in the specified box at position (0.0f, 0.0f).
     *
     *  @param box the specified box.
     *  @return this Line object.
     */
    public Line placeIn(Box box) throws Exception {
        return placeIn(box, 0f, 0f);
    }


    /**
     *  Places this line in the specified box.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     *  @return this Line object.
     */
    public Line placeIn(
            Box box,
            double x_offset,
            double y_offset) throws Exception {
        placeIn(box, (float) x_offset, (float) y_offset);
        return this;
    }


    /**
     *  Places this line in the specified box.
     *
     *  @param box the specified box.
     *  @param x_offset the x offset from the top left corner of the box.
     *  @param y_offset the y offset from the top left corner of the box.
     *  @return this Line object.
     */
    public Line placeIn(
            Box box,
            float x_offset,
            float y_offset) throws Exception {
        box_x = box.x + x_offset;
        box_y = box.y + y_offset;
        return this;
    }


    /**
     *  Scales this line by the spacified factor.
     *
     *  @param factor the factor used to scale the line.
     *  @return this Line object.
     */
    public Line scaleBy(double factor) throws Exception {
        return scaleBy((float) factor);
    }


    /**
     *  Scales this line by the spacified factor.
     *
     *  @param factor the factor used to scale the line.
     *  @return this Line object.
     */
    public Line scaleBy(float factor) throws Exception {
        this.x1 *= factor;
        this.x2 *= factor;
        this.y1 *= factor;
        this.y2 *= factor;
        return this;
    }


    /**
     *  Draws this line on the specified page.
     *
     *  @param page the page to draw this line on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page) throws Exception {
        page.setPenColor(color);
        page.setPenWidth(width);
        page.setLineCapStyle(capStyle);
        page.setLinePattern(pattern);
        page.addBMC(StructElem.SPAN, language, altDescription, actualText);
        page.drawLine(
                x1 + box_x,
                y1 + box_y,
                x2 + box_x,
                y2 + box_y);
        page.addEMC();

        float x_max = Math.max(x1 + box_x, x2 + box_x);
        float y_max = Math.max(y1 + box_y, y2 + box_y);
        return new float[] {x_max, y_max};
    }

}   // End of Line.java
