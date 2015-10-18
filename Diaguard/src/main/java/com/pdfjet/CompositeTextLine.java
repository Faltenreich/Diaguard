/**
 *  CompositeTextLine.java
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

/*
 *  This class was designed and implemented by Jon T. Swanson, Ph.D.
 *  
 *  Refactored and integrated into the project by Eugene Dragoev - 2nd June 2012.
 */


package com.pdfjet;

import java.util.*;


/**
 *  Used to create composite text line objects.
 *
 *
 */
public class CompositeTextLine implements Drawable {

    private static final int X = 0;
    private static final int Y = 1;

    private List<TextLine> textLines = new ArrayList<TextLine>();

    private float[] position = new float[2];
    private float[] current  = new float[2];

    // Subscript and Superscript size factors
    private float subscript_size_factor    = 0.583f;
    private float superscript_size_factor  = 0.583f;
    
    // Subscript and Superscript positions in relation to the base font
    private float superscript_position = 0.350f;
    private float subscript_position   = 0.141f;
    
    private float fontSize = 12f;
    

    public CompositeTextLine(float x, float y) {
        position[X] = x;
        position[Y] = y;
        current[X]  = x;
        current[Y]  = y;
    }


    /**
     *  Sets the font size.
     *
     *  @param fontSize the font size.
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }


    /**
     *  Gets the font size.
     *
     *  @return fontSize the font size.
     */
    public float getFontSize() {
        return fontSize;
    }


    /**
     *  Sets the superscript factor for this composite text line.
     *
     *  @param superscript the superscript size factor.
     */
    public void setSuperscriptFactor(float superscript) {
        this.superscript_size_factor = superscript;
    }

    
    /**
     *  Gets the superscript factor for this text line.
     *
     *  @return superscript the superscript size factor.
     */
    public float getSuperscriptFactor() {
        return superscript_size_factor;
    }


    /**
     *  Sets the subscript factor for this composite text line.
     *
     *  @param subscript the subscript size factor.
     */
    public void setSubscriptFactor(float subscript) {
        this.subscript_size_factor = subscript;
    }


    /**
     *  Gets the subscript factor for this text line.
     *
     *  @return subscript the subscript size factor.
     */
    public float getSubscriptFactor() {
        return subscript_size_factor;
    }


    /**
     *  Sets the superscript position for this composite text line.
     *
     *  @param superscript_position the superscript position.
     */
    public void setSuperscriptPosition(float superscript_position) {
        this.superscript_position = superscript_position;
    }
    

    /**
     *  Gets the superscript position for this text line.
     *
     *  @return superscript_position the superscript position.
     */
    public float getSuperscriptPosition() {
        return superscript_position;
    }


    /**
     *  Sets the subscript position for this composite text line.
     *
     *  @param subscript_position the subscript position.
     */
    public void setSubscriptPosition(float subscript_position) {
        this.subscript_position = subscript_position;
    }

    
    /**
     *  Gets the subscript position for this text line.
     *
     *  @return subscript_position the subscript position.
     */
    public float getSubscriptPosition() {
        return subscript_position;
    }

    
    /**
     *  Add a new text line.
     *
     *  Find the current font, current size and effects (normal, super or subscript)
     *  Set the position of the component to the starting stored as current position
     *  Set the size and offset based on effects
     *  Set the new current position
     *
     *  @param component the component.
     */
    public void addComponent(TextLine component) {
        if (component.getTextEffect() == Effect.SUPERSCRIPT) {
            component.getFont().setSize(fontSize * superscript_size_factor);
            component.setPosition(
                    current[X],
                    current[Y] - fontSize * superscript_position);
        }
        else if (component.getTextEffect() == Effect.SUBSCRIPT) {
            component.getFont().setSize(fontSize * subscript_size_factor);
            component.setPosition(
                    current[X],
                    current[Y] + fontSize * subscript_position);
        }
        else {
            component.getFont().setSize(fontSize);
            component.setPosition(current[X], current[Y]);
        }
        current[X] += component.getWidth();
        textLines.add(component);
    }


    /**
     *  Loop through all the text lines and reset their position based on
     *  the new position set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     */
    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);        
    }


    /**
     *  Loop through all the text lines and reset their position based on
     *  the new position set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     *  Loop through all the text lines and reset their location based on
     *  the new location set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     */
    public void setLocation(float x, float y) {
        position[X] = x;
        position[Y] = y;
        current[X]  = x;
        current[Y]  = y;

        if (textLines == null)
            return;
        int size = textLines.size();
        if (size == 0)
            return;

        for (TextLine component : textLines) {
            if (component.getTextEffect() == Effect.SUPERSCRIPT) {
                component.setPosition(
                        current[X], 
                        current[Y] - fontSize * superscript_position);
            }
            else if (component.getTextEffect() == Effect.SUBSCRIPT) {
                component.setPosition(
                        current[X], 
                        current[Y] + fontSize * subscript_position);
            }
            else {
                component.setPosition(current[X], current[Y]);
            }
            current[X] += component.getWidth();
        }
    }


    /**
     *  Return the position of this composite text line.
     *
     *  @return the position of this composite text line.
     */
    public float[] getPosition() {
        return position;
    }


    /**
     *  Return the nth entry in the TextLine array.
     *
     *  @param index the index of the nth element.
     *  @return the text line at the specified index.
     */
    public TextLine getTextLine(int index) {
        if (textLines == null)
            return null;
        int size = textLines.size();
        if (size == 0)
            return null;
        if (index < 0 || index > size - 1)
            return null;
        return textLines.get(index);
    }


    /**
     *  Returns the number of text lines.
     *
     *  @return the number of text lines.
     */
    public int getNumberOfTextLines() {
       return textLines.size();
    }


    /**
     *  Returns the vertical coordinates of the top left and bottom right corners
     *  of the bounding box of this composite text line.
     *
     *  @return the an array containing the vertical coordinates.
     */
    public float[] getMinMax() {
        float min = position[Y];
        float max = position[Y];
        float cur;

        for (TextLine component : textLines) {
            if (component.getTextEffect() == Effect.SUPERSCRIPT) {
                cur = (position[Y] - component.getFont().ascent) - fontSize * superscript_position;
                if (cur < min)
                    min = cur;
            }
            else if (component.getTextEffect() == Effect.SUBSCRIPT) {
                cur = (position[Y] - component.getFont().descent) + fontSize * subscript_position;
                if (cur > max)
                    max = cur;
            }
            else {
                cur = position[Y] - component.getFont().ascent;
                if (cur < min)
                    min = cur;
                cur = position[Y] - component.getFont().descent;
                if (cur > max)
                    max = cur;
            }
        }

        return new float[] {min, max};
    }


    /**
     *  Returns the height of this CompositeTextLine.
     *
     *  @return the height.
     */
    public float getHeight() {
        float[] yy = getMinMax();
        return yy[1] - yy[0];
    }


    /**
     *  Returns the width of this CompositeTextLine.
     *
     *  @return the width.
     */
    public float getWidth() {
        return (current[X] - position[X]);
    }


    /**
     *  Draws this line on the specified page.
     *
     *  @param page the page to draw this line on.
     */
    public void drawOn(Page page) throws Exception {
        // Loop through all the text lines and draw them on the page
        for (TextLine textLine : textLines) {
            textLine.drawOn(page);
        }
    }

}
