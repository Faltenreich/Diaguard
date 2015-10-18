/**
 *  Paragraph.java
 *
Copyright (c) 2015, Innovatics Inc.
All rights reserved.
*/

package com.pdfjet;

import java.util.*;


/**
 *  Used to create paragraph objects.
 *  See the TextColumn class for more information.
 *
 */
public class Paragraph {

    protected List<TextLine> list = null;
    protected int alignment = Align.LEFT;


    /**
     *  Constructor for creating paragraph objects.
     *
     */
    public Paragraph() {
        list = new ArrayList<TextLine>();
    }


    /**
     *  Adds a text line to this paragraph.
     *
     *  @param text the text line to add to this paragraph.
     *  @return this paragraph.
     */
    public Paragraph add(TextLine text) {
        list.add(text);
        return this;
    }


    /**
     *  Removes the last text line added to this paragraph.
     *
     */
    public void removeLastTextLine() {
        if (list.size() >= 1) {
            list.remove(list.size() - 1);
        }
    }


    /**
     *  Sets the alignment of the text in this paragraph.
     *
     *  @param alignment the alignment code.
     *  @return this paragraph.
     *
     *  <pre>Supported values: Align.LEFT, Align.RIGHT, Align.CENTER and Align.JUSTIFY.</pre>
     */
    public Paragraph setAlignment(int alignment) {
        this.alignment = alignment;
        return this;
    }

}   // End of Paragraph.java
