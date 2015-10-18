/**
 *  FastFont.java
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

import java.io.*;
import java.util.*;


class FastFont {

    protected static void register(
            PDF pdf,
            Font font,
            InputStream inputStream) throws Exception {

        int len = inputStream.read();
        byte[] fontName = new byte[len];
        inputStream.read(fontName, 0, len);
        font.name = new String(fontName);
        // System.out.println(font.name);

        font.unitsPerEm = getInt32(inputStream);
        font.bBoxLLx = getInt32(inputStream);
        font.bBoxLLy = getInt32(inputStream);
        font.bBoxURx = getInt32(inputStream);
        font.bBoxURy = getInt32(inputStream);
        font.ascent = getInt32(inputStream);
        font.descent = getInt32(inputStream);
        font.firstChar = getInt32(inputStream);
        font.lastChar = getInt32(inputStream);
        font.capHeight = getInt32(inputStream);
        font.underlinePosition = getInt32(inputStream);
        font.underlineThickness = getInt32(inputStream);

        len = getInt32(inputStream);
        font.advanceWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.advanceWidth[i] = getInt16(inputStream);
        }

        len = getInt32(inputStream);
        font.glyphWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.glyphWidth[i] = getInt16(inputStream);
        }

        len = getInt32(inputStream);
        font.unicodeToGID = new int[len];
        for (int i = 0; i < len; i++) {
            font.unicodeToGID[i] = getInt16(inputStream);
        }

        font.uncompressed_size = getInt32(inputStream);
        font.compressed_size = getInt32(inputStream);

        embedFontFile(pdf, font, inputStream);
        addFontDescriptorObject(pdf, font);
        addCIDFontDictionaryObject(pdf, font);
        addToUnicodeCMapObject(pdf, font);

        // Type0 Font Dictionary
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type0\n");
        pdf.append("/BaseFont /");
        pdf.append(font.name);
        pdf.append('\n');
        pdf.append("/Encoding /Identity-H\n");
        pdf.append("/DescendantFonts [");
        pdf.append(font.getCidFontDictObjNumber());
        pdf.append(" 0 R]\n");
        pdf.append("/ToUnicode ");
        pdf.append(font.getToUnicodeCMapObjNumber());
        pdf.append(" 0 R\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.objNumber = pdf.objNumber;
    }


    private static void embedFontFile(PDF pdf, Font font, InputStream inputStream)
            throws Exception {

        // Check if the font file is already embedded
        for (int i = 0; i < pdf.fonts.size(); i++) {
            Font f = pdf.fonts.get(i);
            if (f.name.equals(font.name) && f.fileObjNumber != -1) {
                font.fileObjNumber = f.fileObjNumber;
                return;
            }
        }
/*
        int metadataObjNumber = pdf.addMetadataObject(DejaVu.FONT_LICENSE, true);
*/
        pdf.newobj();
        pdf.append("<<\n");
/*
        pdf.append("/Metadata ");
        pdf.append(metadataObjNumber);
        pdf.append(" 0 R\n");
*/
        pdf.append("/Filter /FlateDecode\n");
        pdf.append("/Length ");
        pdf.append(font.compressed_size);
        pdf.append("\n");

        pdf.append("/Length1 ");
        pdf.append(font.uncompressed_size);
        pdf.append('\n');

        pdf.append(">>\n");
        pdf.append("stream\n");
        byte[] buf = new byte[2048];
        int len;
        while ((len = inputStream.read(buf, 0, buf.length)) > 0) {
            pdf.append(buf, 0, len);
        }
        inputStream.close();
        pdf.append("\nendstream\n");
        pdf.endobj();

        font.fileObjNumber = pdf.objNumber;
    }


    private static void addFontDescriptorObject(PDF pdf, Font font) throws Exception {
        float factor = 1000f / font.unitsPerEm;

        for (int i = 0; i < pdf.fonts.size(); i++) {
            Font f = pdf.fonts.get(i);
            if (f.name.equals(font.name) && f.getFontDescriptorObjNumber() != -1) {
                font.setFontDescriptorObjNumber(f.getFontDescriptorObjNumber());
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /FontDescriptor\n");
        pdf.append("/FontName /");
        pdf.append(font.name);
        pdf.append('\n');
        pdf.append("/FontFile2 ");
        pdf.append(font.fileObjNumber);
        pdf.append(" 0 R\n");
        pdf.append("/Flags 32\n");
        pdf.append("/FontBBox [");
        pdf.append(font.bBoxLLx * factor);
        pdf.append(' ');
        pdf.append(font.bBoxLLy * factor);
        pdf.append(' ');
        pdf.append(font.bBoxURx * factor);
        pdf.append(' ');
        pdf.append(font.bBoxURy * factor);
        pdf.append("]\n");
        pdf.append("/Ascent ");
        pdf.append(font.ascent * factor);
        pdf.append('\n');
        pdf.append("/Descent ");
        pdf.append(font.descent * factor);
        pdf.append('\n');
        pdf.append("/ItalicAngle 0\n");
        pdf.append("/CapHeight ");
        pdf.append(font.capHeight * factor);
        pdf.append('\n');
        pdf.append("/StemV 79\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.setFontDescriptorObjNumber(pdf.objNumber);
    }


    private static void addToUnicodeCMapObject(PDF pdf, Font font) throws Exception {

        for (int i = 0; i < pdf.fonts.size(); i++) {
            Font f = pdf.fonts.get(i);
            if (f.name.equals(font.name) && f.getToUnicodeCMapObjNumber() != -1) {
                font.setToUnicodeCMapObjNumber(f.getToUnicodeCMapObjNumber());
                return;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("/CIDInit /ProcSet findresource begin\n");
        sb.append("12 dict begin\n");
        sb.append("begincmap\n");
        sb.append("/CIDSystemInfo <</Registry (Adobe) /Ordering (Identity) /Supplement 0>> def\n");
        sb.append("/CMapName /Adobe-Identity def\n");
        sb.append("/CMapType 2 def\n");

        sb.append("1 begincodespacerange\n");
        sb.append("<0000> <FFFF>\n");
        sb.append("endcodespacerange\n");

        List<String> list = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (int cid = 0; cid <= 0xffff; cid++) {
            int gid = font.unicodeToGID[cid];
            if (gid > 0) {
                buf.append('<');
                buf.append(toHexString(gid));
                buf.append("> <");
                buf.append(toHexString(cid));
                buf.append(">\n");
                list.add(buf.toString());
                buf.setLength(0);
                if (list.size() == 100) {
                    writeListToBuffer(list, sb);
                }
            }
        }
        if (list.size() > 0) {
            writeListToBuffer(list, sb);
        }

        sb.append("endcmap\n");
        sb.append("CMapName currentdict /CMap defineresource pop\n");
        sb.append("end\nend");

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Length ");
        pdf.append(sb.length());
        pdf.append("\n");
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(sb.toString());
        pdf.append("\nendstream\n");
        pdf.endobj();

        font.setToUnicodeCMapObjNumber(pdf.objNumber);
    }


    private static void addCIDFontDictionaryObject(PDF pdf, Font font) throws Exception {

        for (int i = 0; i < pdf.fonts.size(); i++) {
            Font f = pdf.fonts.get(i);
            if (f.name.equals(font.name) && f.getCidFontDictObjNumber() != -1) {
                font.setCidFontDictObjNumber(f.getCidFontDictObjNumber());
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /CIDFontType2\n");
        pdf.append("/BaseFont /");
        pdf.append(font.name);
        pdf.append('\n');
        pdf.append("/CIDSystemInfo <</Registry (Adobe) /Ordering (Identity) /Supplement 0>>\n");
        pdf.append("/FontDescriptor ");
        pdf.append(font.getFontDescriptorObjNumber());
        pdf.append(" 0 R\n");
        pdf.append("/DW ");
        pdf.append((int)
                ((1000f / font.unitsPerEm) * font.advanceWidth[0]));
        pdf.append('\n');
        pdf.append("/W [0[\n");
        for (int i = 0; i < font.advanceWidth.length; i++) {
            pdf.append((int)
                    ((1000f / font.unitsPerEm) * font.advanceWidth[i]));
            if ((i + 1) % 10 == 0) {
                pdf.append('\n');
            }
            else {
                pdf.append(' ');
            }
        }
        pdf.append("]]\n");
        pdf.append("/CIDToGIDMap /Identity\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.setCidFontDictObjNumber(pdf.objNumber);
    }


    private static String toHexString(int code) {
        String str = Integer.toHexString(code);
        if (str.length() == 1) {
            return "000" + str;
        }
        else if (str.length() == 2) {
            return "00" + str;
        }
        else if (str.length() == 3) {
            return "0" + str;
        }
        return str;
    }


    private static void writeListToBuffer(List<String> list, StringBuilder sb) {
        sb.append(list.size());
        sb.append(" beginbfchar\n");
        for (String str : list) {
            sb.append(str);
        }
        sb.append("endbfchar\n");
        list.clear();
    }


    private static int getInt16(InputStream inputStream) throws Exception {
        byte[] buf = new byte[2];
        inputStream.read(buf, 0, 2);
        int val = 0;
        val |= buf[0] & 0xff;
        val <<= 8;
        val |= buf[1] & 0xff;
        return val;
    }


    private static int getInt32(InputStream inputStream) throws Exception {
        byte[] buf = new byte[4];
        inputStream.read(buf, 0, 4);
        int val = 0;
        val |= buf[0] & 0xff;
        val <<= 8;
        val |= buf[1] & 0xff;
        val <<= 8;
        val |= buf[2] & 0xff;
        val <<= 8;
        val |= buf[3] & 0xff;
        return val;
    }

}   // End of FastFont.java
