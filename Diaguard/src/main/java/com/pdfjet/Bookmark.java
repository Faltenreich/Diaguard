/**
 *  Bookmark.java
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

import java.lang.*;
import java.util.*;

/**
 * Please see Example_51 and Example_52
 *
 */
public class Bookmark {

    private int destNumber = 0;
    private Page page = null;
    private float y = 0f;
    private String key = null;
    private String title = null;
    private Bookmark parent = null;
    private Bookmark prev = null;
    private Bookmark next = null;
    private List<Bookmark> children = null;
    private Destination dest = null;

    protected int objNumber = 0;
    protected String prefix = null;


    public Bookmark(PDF pdf) {
        pdf.toc = this;
    }


    private Bookmark(Page page, float y, String key, String title) {
        this.page = page;
        this.y = y;
        this.key = key;
        this.title = title;
    }


    public Bookmark addBookmark(Page page, Title title) {
        return addBookmark(page, title.text.getY(), title.text.getText());
    }


    public Bookmark addBookmark(Page page, float y, String title) {
        Bookmark bm = this;
        while (bm.parent != null) {
            bm = bm.getParent();
        }
        String key = bm.next();

        Bookmark bookmark = new Bookmark(page, y, key, title.replaceAll("\\s+"," "));
        bookmark.parent = this;
        bookmark.dest = page.addDestination(key, y);
        if (children == null) {
            children = new ArrayList<Bookmark>();
        }
        else {
            bookmark.prev = children.get(children.size() - 1);
            children.get(children.size() - 1).next = bookmark;
        }
        children.add(bookmark);
        return bookmark;
    }


    public String getDestKey() {
        return this.key;
    }


    public String getTitle() {
        return this.title;
    }


    public Bookmark getParent() {
        return this.parent;
    }


    public Bookmark autoNumber(TextLine text) {
        Bookmark bm = getPrevBookmark();
        if (bm == null) {
            bm = getParent();
            if (bm.prefix == null) {
                prefix = "1";
            }
            else {
                prefix = bm.prefix + ".1";
            }
        }
        else {
            if (bm.prefix == null) {
                if (bm.getParent().prefix == null) {
                    prefix = "1";
                }
                else {
                    prefix = bm.getParent().prefix + ".1";
                }
            }
            else {
                int index = bm.prefix.lastIndexOf('.');
                if (index == -1) {
                    prefix = String.valueOf(Integer.valueOf(bm.prefix) + 1);
                }
                else {
                    prefix = bm.prefix.substring(0, index) + ".";
                    prefix += String.valueOf(Integer.valueOf(bm.prefix.substring(index + 1)) + 1);
                }
            }
        }
        text.setText(prefix);
        title = prefix + " " + title;
        return this;
    }


    protected List<Bookmark> toArrayList() {
        int objNumber = 0;
        List<Bookmark> list = new ArrayList<Bookmark>();
        Queue<Bookmark> queue = new java.util.LinkedList<Bookmark>();
        queue.add(this);
        while (!queue.isEmpty()) {
            Bookmark bm = queue.poll();
            bm.objNumber = objNumber++;
            list.add(bm);
            if (bm.getChildren() != null) {
                queue.addAll(bm.getChildren());
            }
        }
        return list;
    }


    protected List<Bookmark> getChildren() {
        return this.children;
    }


    protected Bookmark getPrevBookmark() {
        return this.prev;
    }


    protected Bookmark getNextBookmark() {
        return this.next;
    }


    protected Bookmark getFirstChild() {
        return this.children.get(0);
    }


    protected Bookmark getLastChild() {
        return children.get(children.size() - 1);
    }


    protected Destination getDestination() {
        return this.dest;
    }


    private String next() {
        ++destNumber;
        return "dest#" + String.valueOf(destNumber);
    }

}   // End of Bookmark.java
