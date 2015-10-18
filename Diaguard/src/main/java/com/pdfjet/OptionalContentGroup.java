/**
 *  OptionalContentGroup.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Container for drawable objects that can be drawn on a page as part of Optional Content Group. 
 * Please see the PDF specification and Example_30 for more details.
 *
 * @author Mark Paxton
 */
public class OptionalContentGroup {

    protected String name;
    protected int ocgNumber;
    protected int objNumber;
    protected boolean visible;
    protected boolean printable;
    protected boolean exportable;
    private List<Drawable> components;

    public OptionalContentGroup(String name) {
        this.name = name;
        this.components = new ArrayList<Drawable>();
    }

    public void add(Drawable d) {
        components.add(d);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public void drawOn(Page p) throws Exception {
        if (!components.isEmpty()) {
            p.pdf.groups.add(this);
            ocgNumber = p.pdf.groups.size();

            p.pdf.newobj();
            p.pdf.append("<<\n");
            p.pdf.append("/Type /OCG\n");
            p.pdf.append("/Name (" + name + ")\n");
            p.pdf.append(">>\n");
            p.pdf.endobj();

            objNumber = p.pdf.objNumber;

            p.append("/OC /OC");
            p.append(ocgNumber);
            p.append(" BDC\n");
            for (Drawable component : components) {
                component.drawOn(p);
            }
            p.append("\nEMC\n");
        }
    }
}   // End of OptionalContentGroup.java
