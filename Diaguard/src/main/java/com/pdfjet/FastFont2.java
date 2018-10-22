/**
 *  FastFont2.java
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

import java.io.*;
import java.util.*;
import java.util.zip.*;


class FastFont2 {

    protected static void register(
            Map<Integer, PDFobj> objects,
            Font font,
            InputStream inputStream) throws Exception {

        int len = inputStream.read();
        byte[] fontName = new byte[len];
        inputStream.read(fontName, 0, len);
        font.name = new String(fontName, "UTF8");
        // System.out.println(font.name);

        len = getInt24(inputStream);
        byte[] fontInfo = new byte[len];
        inputStream.read(fontInfo, 0, len);
        font.info = new String(fontInfo, "UTF8");
        // System.out.println(font.info);

        byte[] buf = new byte[getInt32(inputStream)];
        inputStream.read(buf, 0, buf.length);
        Decompressor decompressor = new Decompressor(buf);
        ByteArrayInputStream stream =
                new ByteArrayInputStream(decompressor.getDecompressedData());

        font.unitsPerEm = getInt32(stream);
        font.bBoxLLx = getInt32(stream);
        font.bBoxLLy = getInt32(stream);
        font.bBoxURx = getInt32(stream);
        font.bBoxURy = getInt32(stream);
        font.ascent = getInt32(stream);
        font.descent = getInt32(stream);
        font.firstChar = getInt32(stream);
        font.lastChar = getInt32(stream);
        font.capHeight = getInt32(stream);
        font.underlinePosition = getInt32(stream);
        font.underlineThickness = getInt32(stream);

        len = getInt32(stream);
        font.advanceWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.advanceWidth[i] = getInt16(stream);
        }

        len = getInt32(stream);
        font.glyphWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.glyphWidth[i] = getInt16(stream);
        }

        len = getInt32(stream);
        font.unicodeToGID = new int[len];
        for (int i = 0; i < len; i++) {
            font.unicodeToGID[i] = getInt16(stream);
        }

        font.cff = (inputStream.read() == 'Y') ? true : false;
        font.uncompressed_size = getInt32(inputStream);
        font.compressed_size = getInt32(inputStream);

        embedFontFile(objects, font, inputStream);
        addFontDescriptorObject(objects, font);
        addCIDFontDictionaryObject(objects, font);
        addToUnicodeCMapObject(objects, font);

        // Type0 Font Dictionary
        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Type");
        dict.add("/Font");
        dict.add("/Subtype");
        dict.add("/Type0");
        dict.add("/BaseFont");
        dict.add("/" + font.name);
        dict.add("/Encoding");
        dict.add("/Identity-H");
        dict.add("/DescendantFonts");
        dict.add("[");
        dict.add(String.valueOf(font.getCidFontDictObjNumber()));
        dict.add("0");
        dict.add("R");
        dict.add("]");
        dict.add("/ToUnicode");
        dict.add(String.valueOf(font.getToUnicodeCMapObjNumber()));
        dict.add("0");
        dict.add("R");
        dict.add(">>");
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);
        font.objNumber = obj.number;
    }


    private static int addMetadataObject(
            Map<Integer, PDFobj> objects, Font font) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("<?xpacket begin='\uFEFF' id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n");
        sb.append("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n");
        sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n");
        sb.append("<rdf:Description rdf:about=\"\" xmlns:xmpRights=\"http://ns.adobe.com/xap/1.0/rights/\">\n");
        sb.append("<xmpRights:UsageTerms>\n");
        sb.append("<rdf:Alt>\n");
        sb.append("<rdf:li xml:lang=\"x-default\">\n");
        sb.append(font.info);
        sb.append("</rdf:li>\n");
        sb.append("</rdf:Alt>\n");
        sb.append("</xmpRights:UsageTerms>\n");
        sb.append("</rdf:Description>\n");
        sb.append("</rdf:RDF>\n");
        sb.append("</x:xmpmeta>\n");
        sb.append("<?xpacket end=\"w\"?>");

        byte[] xml = sb.toString().getBytes("UTF-8");

        // This is the metadata object
        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Type");
        dict.add("/Metadata");
        dict.add("/Subtype");
        dict.add("/XML");
        dict.add("/Length");
        dict.add(String.valueOf(xml.length));
        dict.add(">>");
        obj.setStream(xml);
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);

        return obj.number;
    }


    private static void embedFontFile(
            Map<Integer, PDFobj> objects,
            Font font,
            InputStream inputStream) throws Exception {

        int metadataObjNumber = addMetadataObject(objects, font);

        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Metadata");
        dict.add(String.valueOf(metadataObjNumber));
        dict.add("0");
        dict.add("R");
        dict.add("/Filter");
        dict.add("/FlateDecode");
        dict.add("/Length");
        dict.add(String.valueOf(font.compressed_size));
        if (font.cff) {
            dict.add("/Subtype");
            dict.add("/CIDFontType0C");
        }
        else {
            dict.add("/Length1");
            dict.add(String.valueOf(font.uncompressed_size));
        }
        dict.add(">>");
        ByteArrayOutputStream buf2 = new ByteArrayOutputStream();
        byte[] buf = new byte[16384];
        int len;
        while ((len = inputStream.read(buf, 0, buf.length)) > 0) {
            buf2.write(buf, 0, len);
        }
        inputStream.close();
        obj.setStream(buf2.toByteArray());
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);
        font.fileObjNumber = obj.number;
    }


    private static void addFontDescriptorObject(
            Map<Integer, PDFobj> objects, Font font) throws Exception {

        float factor = 1000f / font.unitsPerEm;

        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Type");
        dict.add("/FontDescriptor");
        dict.add("/FontName");
        dict.add("/" + font.name);
        dict.add("/FontFile" + (font.cff ? "3" : "2"));
        dict.add(String.valueOf(font.fileObjNumber));
        dict.add("0");
        dict.add("R");
        dict.add("/Flags");
        dict.add("32");
        dict.add("/FontBBox");
        dict.add("[");
        dict.add(String.valueOf((int) (font.bBoxLLx * factor)));
        dict.add(String.valueOf((int) (font.bBoxLLy * factor)));
        dict.add(String.valueOf((int) (font.bBoxURx * factor)));
        dict.add(String.valueOf((int) (font.bBoxURy * factor)));
        dict.add("]");
        dict.add("/Ascent");
        dict.add(String.valueOf((int) (font.ascent * factor)));
        dict.add("/Descent");
        dict.add(String.valueOf((int) (font.descent * factor)));
        dict.add("/ItalicAngle");
        dict.add("0");
        dict.add("/CapHeight");
        dict.add(String.valueOf((int) (font.capHeight * factor)));
        dict.add("/StemV");
        dict.add("79");
        dict.add(">>");
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);
        font.setFontDescriptorObjNumber(obj.number);
    }


    private static void addToUnicodeCMapObject(
            Map<Integer, PDFobj> objects, Font font) throws Exception {

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

        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Length");
        dict.add(String.valueOf(sb.length()));
        dict.add(">>");
        obj.setStream(sb.toString().getBytes("UTF-8"));
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);
        font.setToUnicodeCMapObjNumber(obj.number);
    }


    private static void addCIDFontDictionaryObject(
            Map<Integer, PDFobj> objects, Font font) throws Exception {

        PDFobj obj = new PDFobj();
        List<String> dict = obj.getDict();
        dict.add("<<");
        dict.add("/Type");
        dict.add("/Font");
        dict.add("/Subtype");
        dict.add("/CIDFontType" + (font.cff ? "0" : "2"));
        dict.add("/BaseFont");
        dict.add("/" + font.name);
        dict.add("/CIDSystemInfo");
        dict.add("<<");
        dict.add("/Registry");
        dict.add("(Adobe)");
        dict.add("/Ordering");
        dict.add("(Identity)");
        dict.add("/Supplement");
        dict.add("0");
        dict.add(">>");
        dict.add("/FontDescriptor");
        dict.add(String.valueOf(font.getFontDescriptorObjNumber()));
        dict.add("0");
        dict.add("R");
        dict.add("/DW");
        dict.add(String.valueOf((int)
                ((1000f / font.unitsPerEm) * font.advanceWidth[0])));
        dict.add("/W");
        dict.add("[");
        dict.add("0");
        dict.add("[");
        for (int i = 0; i < font.advanceWidth.length; i++) {
            dict.add(String.valueOf((int)
                    ((1000f / font.unitsPerEm) * font.advanceWidth[i])));
        }
        dict.add("]");
        dict.add("]");
        dict.add("/CIDToGIDMap");
        dict.add("/Identity");
        dict.add(">>");
        obj.number = Collections.max(objects.keySet()) + 1;
        objects.put(obj.number, obj);
        font.setCidFontDictObjNumber(obj.number);
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


    private static int getInt16(InputStream stream) throws Exception {
        return stream.read() << 8 | stream.read();
    }


    private static int getInt24(InputStream stream) throws Exception {
        return stream.read() << 16 |
                stream.read() << 8 | stream.read();
    }


    private static int getInt32(InputStream stream) throws Exception {
        return stream.read() << 24 | stream.read() << 16 |
                stream.read() << 8 | stream.read();
    }

}   // End of FastFont2.java
