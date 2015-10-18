/**
 *  Path.java
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
 *  Used to create path objects.
 *  The path objects may consist of lines, splines or both.
 *
 *  Please see Example_02.
 */
public class Path implements Drawable {

    private int color = Color.black;
    private float width = 0.3f;
    private String pattern = "[] 0";
    private boolean fill_shape = false;
    private boolean close_path = false;

    private List<Point> points = null;

    private float box_x;
    private float box_y;

    private int lineCapStyle = 0;
    private int lineJoinStyle = 0;


    /**
     *  The default constructor.
     *
     *
     */
    public Path() {
        points = new ArrayList<Point>();
    }


    /**
     *  Adds a point to this path.
     *
     *  @param point the point to add.
     */
    public void add(Point point) {
        points.add(point);
    }


    /**
     *  Sets the line dash pattern for this path.
     *
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
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    /**
     *  Sets the pen width that will be used to draw the lines and splines that are part of this path.
     *
     *  @param width the pen width.
     */
    public void setWidth(double width) {
        this.width = (float) width;
    }


    /**
     *  Sets the pen width that will be used to draw the lines and splines that are part of this path.
     *
     *  @param width the pen width.
     */
    public void setWidth(float width) {
        this.width = width;
    }


    /**
     *  Sets the pen color that will be used to draw this path.
     *
     *  @param color the color is specified as an integer.
     */
    public void setColor(int color) {
        this.color = color;
    }


    /**
     *  Sets the close_path variable.
     *
     *  @param close_path if close_path is true a line will be draw between the first and last point of this path.
     */
    public void setClosePath(boolean close_path) {
        this.close_path = close_path;
    }


    /**
     *  Sets the fill_shape private variable. If fill_shape is true - the shape of the path will be filled with the current brush color.
     *
     *  @param fill_shape the fill_shape flag.
     */
    public void setFillShape(boolean fill_shape) {
        this.fill_shape = fill_shape;
    }


    /**
     *  Sets the line cap style.
     *
     *  @param style the cap style of this path. Supported values: Cap.BUTT, Cap.ROUND and Cap.PROJECTING_SQUARE
     */
    public void setLineCapStyle(int style) {
        this.lineCapStyle = style;
    }


    /**
     *  Returns the line cap style for this path.
     *
     *  @return the line cap style for this path.
     */
    public int getLineCapStyle() {
        return this.lineCapStyle;
    }


    /**
     *  Sets the line join style.
     *
     *  @param style the line join style code. Supported values: Join.MITER, Join.ROUND and Join.BEVEL
     */
    public void setLineJoinStyle(int style) {
        this.lineJoinStyle = style;
    }


    /**
     *  Returns the line join style.
     *
     *  @return the line join style.
     */
    public int getLineJoinStyle() {
        return this.lineJoinStyle;
    }


    /**
     *  Places this path in the specified box at position (0.0, 0.0).
     *
     *  @param box the specified box.
     */
    public void placeIn(Box box) throws Exception {
        placeIn(box, 0.0f, 0.0f);
    }

    
    /**
     *  Places the path inside the spacified box at coordinates (x_offset, y_offset) of the top left corner.
     *
     *  @param box the specified box.
     *  @param x_offset the x_offset.
     *  @param y_offset the y_offset.
     */
    public void placeIn(
            Box box,
            double x_offset,
            double y_offset) throws Exception {
    	placeIn(box, (float) x_offset, (float) y_offset);
    }


    /**
     *  Places the path inside the spacified box at coordinates (x_offset, y_offset) of the top left corner.
     *
     *  @param box the specified box.
     *  @param x_offset the x_offset.
     *  @param y_offset the y_offset.
     */
    public void placeIn(
            Box box,
            float x_offset,
            float y_offset) throws Exception {
        box_x = box.x + x_offset;
        box_y = box.y + y_offset;
    }


    /**
     *  Scales the path using the specified factor.
     *
     *  @param factor the specified factor.
     */
    public void scaleBy(double factor) throws Exception {
        scaleBy((float) factor);
    }


    /**
     *  Scales the path using the specified factor.
     *
     *  @param factor the specified factor.
     */
    public void scaleBy(float factor) throws Exception {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x *= factor;
            point.y *= factor;
        }
    }
    

    /**
     * Returns a list containing the start point, first control point, second control point and the end point of elliptical curve segment.
     * Please see Example_18.
     * 
     * @param x the x coordinate of the center of the ellipse.
     * @param y the y coordinate of the center of the ellipse.
     * @param r1 the horizontal radius of the ellipse.
     * @param r2 the vertical radius of the ellipse.
     * @param segment the segment to draw - please see the Segment class.
     * @return a list of the curve points.
     * @throws Exception
     */
    public static List<Point> getCurvePoints(
            float x,
            float y,
            float r1,
            float r2,
            int segment) throws Exception {
        // The best 4-spline magic number
        float m4 = 0.551784f;
        List<Point> list = new ArrayList<Point>();

        if (segment == 0) {
            list.add(new Point(x, y - r2));
            list.add(new Point(x + m4*r1, y - r2, Point.CONTROL_POINT));
            list.add(new Point(x + r1, y - m4*r2, Point.CONTROL_POINT));
            list.add(new Point(x + r1, y));
        }
        else if (segment == 1) {
            list.add(new Point(x + r1, y));
            list.add(new Point(x + r1, y + m4*r2, Point.CONTROL_POINT));
            list.add(new Point(x + m4*r1, y + r2, Point.CONTROL_POINT));
            list.add(new Point(x, y + r2));
        }
        else if (segment == 2) {
            list.add(new Point(x, y + r2));
            list.add(new Point(x - m4*r1, y + r2, Point.CONTROL_POINT));
            list.add(new Point(x - r1, y + m4*r2, Point.CONTROL_POINT));
            list.add(new Point(x - r1, y));
        }
        else if (segment == 3) {
            list.add(new Point(x - r1, y));
            list.add(new Point(x - r1, y - m4*r2, Point.CONTROL_POINT));
            list.add(new Point(x - m4*r1, y - r2, Point.CONTROL_POINT));
            list.add(new Point(x, y - r2));
        }

        return list;
    }


    /**
     *  Draws this path on the page using the current selected color, pen width, line pattern and line join style.
     *
     *  @param page the page to draw this path on.
     */
    public void drawOn(Page page) throws Exception {
        if (fill_shape) {
            page.setBrushColor(color);
        }
        else {
            page.setPenColor(color);
        }
        page.setPenWidth(width);
        page.setLinePattern(pattern);
        page.setLineCapStyle(lineCapStyle);
        page.setLineJoinStyle(lineJoinStyle);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x += box_x;
            point.y += box_y;
        }

        if (fill_shape) {
            page.drawPath(points, 'f');
        }
        else {
            if (close_path) {
                page.drawPath(points, 's');
            }
            else {
                page.drawPath(points, 'S');
            }
        }

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x -= box_x;
            point.y -= box_y;
        }
    }

}   // End of Path.java
