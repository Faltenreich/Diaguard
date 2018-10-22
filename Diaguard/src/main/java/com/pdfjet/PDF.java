/**
 *  PDF.java
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
import java.text.*;
import java.util.*;
import java.util.zip.*;


/**
 *  Used to create PDF objects that represent PDF documents.
 *
 *
 */
public class PDF {

    protected int objNumber = 0;
    protected int metadataObjNumber = 0;
    protected int outputIntentObjNumber = 0;
    protected List<Font> fonts = new ArrayList<Font>();
    protected List<Image> images = new ArrayList<Image>();
    protected List<Page> pages = new ArrayList<Page>();
    protected Map<String, Destination> destinations = new HashMap<String, Destination>();
    protected List<OptionalContentGroup> groups = new ArrayList<OptionalContentGroup>();
    protected Map<String, Integer> states = new HashMap<String, Integer>();
    protected static final DecimalFormat df = new DecimalFormat("0.###", new DecimalFormatSymbols(Locale.US));
    protected int compliance = 0;
    protected List<EmbeddedFile> embeddedFiles = new ArrayList<EmbeddedFile>();

    private OutputStream os = null;
    private List<Integer> objOffset = new ArrayList<Integer>();
    private String title = "";
    private String author = "";
    private String subject = "";
    private String keywords = "";
    private String creator = "";
    private String producer = "PDFjet v5.97 (http://pdfjet.com)";
    private String creationDate;
    private String modDate;
    private String createDate;
    private int byte_count = 0;
    private int pagesObjNumber = -1;
    private String pageLayout = null;
    private String pageMode = null;
    private String language = "en-US";

    protected Bookmark toc = null;
    protected List<String> importedFonts = new ArrayList<String>();
    protected String extGState = "";


    /**
     * The default constructor - use when reading PDF files.
     *
     * @throws Exception
     */
    public PDF() throws Exception {
    }


    /**
     *  Creates a PDF object that represents a PDF document.
     *
     *  @param os the associated output stream.
     */
    public PDF(OutputStream os) throws Exception { this(os, 0); }


    // Here is the layout of the PDF document:
    //
    // Metadata Object
    // Output Intent Object
    // Fonts
    // Images
    // Resources Object
    // Content1
    // Content2
    // ...
    // ContentN
    // Annot1
    // Annot2
    // ...
    // AnnotN
    // Page1
    // Page2
    // ...
    // PageN
    // Pages
    // StructElem1
    // StructElem2
    // ...
    // StructElemN
    // StructTreeRoot
    // Info
    // Root
    // xref table
    // Trailer
    /**
     *  Creates a PDF object that represents a PDF document.
     *  Use this constructor to create PDF/A compliant PDF documents.
     *  Please note: PDF/A compliance requires all fonts to be embedded in the PDF.
     *
     *  @param os the associated output stream.
     *  @param compliance must be: Compliance.PDF_A_1B
     */
    public PDF(OutputStream os, int compliance) throws Exception {

        this.os = os;
        this.compliance = compliance;

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        creationDate = sdf1.format(date);
        modDate = sdf1.format(date);
        createDate = sdf2.format(date);

        append("%PDF-1.5\n");
        append('%');
        append((byte) 0x00F2);
        append((byte) 0x00F3);
        append((byte) 0x00F4);
        append((byte) 0x00F5);
        append((byte) 0x00F6);
        append('\n');

        if (compliance == Compliance.PDF_A_1B ||
                compliance == Compliance.PDF_UA) {
            metadataObjNumber = addMetadataObject("", false);
            outputIntentObjNumber = addOutputIntentObject();
        }

    }


    protected void newobj() throws IOException {
        objOffset.add(byte_count);
        append(++objNumber);
        append(" 0 obj\n");
    }


    protected void endobj() throws IOException {
        append("endobj\n");
    }


    protected int addMetadataObject(String notice, boolean fontMetadataObject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xpacket begin='\uFEFF' id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n");
        sb.append("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n");
        sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n");

        if (fontMetadataObject) {
            sb.append("<rdf:Description rdf:about=\"\" xmlns:xmpRights=\"http://ns.adobe.com/xap/1.0/rights/\">\n");
            sb.append("<xmpRights:UsageTerms>\n");
            sb.append("<rdf:Alt>\n");
            sb.append("<rdf:li xml:lang=\"x-default\">\n");
            sb.append(notice);
            sb.append("</rdf:li>\n");
            sb.append("</rdf:Alt>\n");
            sb.append("</xmpRights:UsageTerms>\n");
            sb.append("</rdf:Description>\n");
        }
        else {
            sb.append("<rdf:Description rdf:about=\"\" xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\" pdf:Producer=\"");
            sb.append(producer);
            sb.append("\">\n</rdf:Description>\n");

            sb.append("<rdf:Description rdf:about=\"\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n");
            sb.append("  <dc:format>application/pdf</dc:format>\n");
            sb.append("  <dc:title><rdf:Alt><rdf:li xml:lang=\"x-default\">");
            sb.append(title);
            sb.append("</rdf:li></rdf:Alt></dc:title>\n");
            sb.append("  <dc:creator><rdf:Seq><rdf:li>");
            sb.append(author);
            sb.append("</rdf:li></rdf:Seq></dc:creator>\n");
            sb.append("  <dc:description><rdf:Alt><rdf:li xml:lang=\"x-default\">");
            sb.append(subject);
            sb.append("</rdf:li></rdf:Alt></dc:description>\n");
            sb.append("</rdf:Description>\n");

            sb.append("<rdf:Description rdf:about=\"\" xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\">\n");
            sb.append("  <pdfaid:part>1</pdfaid:part>\n");
            sb.append("  <pdfaid:conformance>B</pdfaid:conformance>\n");
            sb.append("</rdf:Description>\n");

            if (compliance == Compliance.PDF_UA) {
                sb.append("<rdf:Description rdf:about=\"\" xmlns:pdfuaid=\"http://www.aiim.org/pdfua/ns/id/\">\n");
                sb.append("  <pdfuaid:part>1</pdfuaid:part>\n");
                sb.append("</rdf:Description>\n");
            }

            sb.append("<rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\">\n");
            sb.append("<xmp:CreateDate>");
            sb.append(createDate + "Z");
            sb.append("</xmp:CreateDate>\n");
            sb.append("</rdf:Description>\n");
        }

        sb.append("</rdf:RDF>\n");
        sb.append("</x:xmpmeta>\n");

        if (!fontMetadataObject) {
            // Add the recommended 2000 bytes padding
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 10; j++) {
                    sb.append("          ");
                }
                sb.append("\n");
            }
        }

        sb.append("<?xpacket end=\"w\"?>");

        byte[] xml = sb.toString().getBytes("UTF-8");

        // This is the metadata object
        newobj();
        append("<<\n");
        append("/Type /Metadata\n");
        append("/Subtype /XML\n");
        append("/Length ");
        append(xml.length);
        append("\n");
        append(">>\n");
        append("stream\n");
        append(xml, 0, xml.length);
        append("\nendstream\n");
        endobj();

        return objNumber;
    }


    protected int addOutputIntentObject() throws Exception {
        newobj();
        append("<<\n");
        append("/N 3\n");

        append("/Length ");
        append(ICCBlackScaled.profile.length);
        append("\n");

        append("/Filter /FlateDecode\n");
        append(">>\n");
        append("stream\n");
        append(ICCBlackScaled.profile, 0, ICCBlackScaled.profile.length);
        append("\nendstream\n");
        endobj();

        // OutputIntent object
        newobj();
        append("<<\n");
        append("/Type /OutputIntent\n");
        append("/S /GTS_PDFA1\n");
        append("/OutputCondition (sRGB IEC61966-2.1)\n");
        append("/OutputConditionIdentifier (sRGB IEC61966-2.1)\n");
        append("/Info (sRGB IEC61966-2.1)\n");
        append("/DestOutputProfile ");
        append(objNumber - 1);
        append(" 0 R\n");
        append(">>\n");
        endobj();

        return objNumber;
    }


    private int addResourcesObject() throws Exception {
        newobj();
        append("<<\n");

        if (!extGState.equals("")) {
            append(extGState);
        }
        if (fonts.size() > 0 || importedFonts.size() > 0) {
            append("/Font\n");
            append("<<\n");
            for (String token : importedFonts) {
                append(token);
                if (token.equals("R")) {
                    append('\n');
                }
                else {
                    append(' ');
                }
            }
            for (Font font : fonts) {
                append("/F");
                append(font.objNumber);
                append(' ');
                append(font.objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }

        if (images.size() > 0) {
            append("/XObject\n");
            append("<<\n");
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                append("/Im");
                append(image.objNumber);
                append(' ');
                append(image.objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }

        if (groups.size() > 0) {
            append("/Properties\n");
            append("<<\n");
            for (int i = 0; i < groups.size(); i++) {
                OptionalContentGroup ocg = groups.get(i);
                append("/OC");
                append(i + 1);
                append(' ');
                append(ocg.objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }

        // String state = "/CA 0.5 /ca 0.5";
        if (states.size() > 0) {
            append("/ExtGState <<\n");
            for (String state : states.keySet()) {
                append("/GS");
                append(states.get(state));
                append(" << ");
                append(state);
                append(" >>\n");
            }
            append(">>\n");
        }

        append(">>\n");
        endobj();
        return objNumber;
    }


    private int addPagesObject() throws Exception {
        newobj();
        append("<<\n");
        append("/Type /Pages\n");
        append("/Kids [\n");
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            if (compliance == Compliance.PDF_UA) {
                page.setStructElementsPageObjNumber(page.objNumber);
            }
            append(page.objNumber);
            append(" 0 R\n");
        }
        append("]\n");
        append("/Count ");
        append(pages.size());
        append('\n');
        append(">>\n");
        endobj();
        return objNumber;
    }


    private int addInfoObject() throws Exception {
        // Add the info object
        newobj();
        append("<<\n");
        append("/Title <");
        append(toHex(title));
        append(">\n");
        append("/Author <");
        append(toHex(author));
        append(">\n");
        append("/Subject <");
        append(toHex(subject));
        append(">\n");
        append("/Keywords <");
        append(toHex(keywords));
        append(">\n");
        append("/Creator <");
        append(toHex(creator));
        append(">\n");
        append("/Producer (");
        append(producer);
        append(")\n");
        append("/CreationDate (D:");
        append(creationDate);
        append("Z)\n");
        append("/ModDate (D:");
        append(modDate);
        append("Z)\n");
        append(">>\n");
        endobj();
        return objNumber;
    }


    private int addStructTreeRootObject() throws Exception {
        newobj();
        append("<<\n");
        append("/Type /StructTreeRoot\n");
        append("/K [\n");
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            for (int j = 0; j < page.structures.size(); j++) {
                append(page.structures.get(j).objNumber);
                append(" 0 R\n");
            }
        }
        append("]\n");
        append("/ParentTree ");
        append(objNumber + 1);
        append(" 0 R\n");
        append(">>\n");
        endobj();
        return objNumber;
    }


    private void addStructElementObjects() throws Exception {
        int structTreeRootObjNumber = objNumber + 1;
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            structTreeRootObjNumber += page.structures.size();
        }

        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            for (int j = 0; j < page.structures.size(); j++) {
                newobj();
                StructElem element = page.structures.get(j);
                element.objNumber = objNumber;
                append("<<\n");
                append("/Type /StructElem\n");
                append("/S /");
                append(element.structure);
                append("\n");
                append("/P ");
                append(structTreeRootObjNumber);
                append(" 0 R\n");
                append("/Pg ");
                append(element.pageObjNumber);
                append(" 0 R\n");
                if (element.annotation == null) {
                    append("/K ");
                    append(element.mcid);
                    append("\n");
                }
                else {
                    append("/K <<\n");
                    append("/Type /OBJR\n");
                    append("/Obj ");
                    append(element.annotation.objNumber);
                    append(" 0 R\n");
                    append(">>\n");
                }
                if (element.language != null) {
                    append("/Lang (");
                    append(element.language);
                    append(")\n");
                }
                append("/Alt <");
                append(toHex(element.altDescription));
                append(">\n");
                append("/ActualText <");
                append(toHex(element.actualText));
                append(">\n");
                append(">>\n");
                endobj();
            }
        }
    }


    private String toHex(String str) {
        StringBuilder buf = new StringBuilder();
        if (str != null) {
            buf.append("FEFF");
            for (int i = 0; i < str.length(); i++) {
                buf.append(String.format("%04X", str.codePointAt(i)));
            }
        }
        return buf.toString();
    }


    private void addNumsParentTree() throws Exception {
        newobj();
        append("<<\n");
        append("/Nums [\n");
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            append(i);
            append(" [\n");
            for (int j = 0; j < page.structures.size(); j++) {
                StructElem element = page.structures.get(j);
                if (element.annotation == null) {
                    append(element.objNumber);
                    append(" 0 R\n");
                }
            }
            append("]\n");
        }

        int index = pages.size();
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            for (int j = 0; j < page.structures.size(); j++) {
                StructElem element = page.structures.get(j);
                if (element.annotation != null) {
                    append(index);
                    append(" ");
                    append(element.objNumber);
                    append(" 0 R\n");
                    index++;
                }
            }
        }
        append("]\n");
        append(">>\n");
        endobj();
    }


    private int addRootObject(
            int structTreeRootObjNumber, int outlineDictNumber) throws Exception {
        // Add the root object
        newobj();
        append("<<\n");
        append("/Type /Catalog\n");

        if (compliance == Compliance.PDF_UA) {
            append("/Lang (");
            append(language);
            append(")\n");

            append("/StructTreeRoot ");
            append(structTreeRootObjNumber);
            append(" 0 R\n");

            append("/MarkInfo <</Marked true>>\n");
            append("/ViewerPreferences <</DisplayDocTitle true>>\n");
        }

        if (pageLayout != null) {
            append("/PageLayout /");
            append(pageLayout);
            append("\n");
        }

        if (pageMode != null) {
            append("/PageMode /");
            append(pageMode);
            append("\n");
        }

        addOCProperties();

        append("/Pages ");
        append(pagesObjNumber);
        append(" 0 R\n");

        if (compliance == Compliance.PDF_A_1B ||
                compliance == Compliance.PDF_UA) {
            append("/Metadata ");
            append(metadataObjNumber);
            append(" 0 R\n");

            append("/OutputIntents [");
            append(outputIntentObjNumber);
            append(" 0 R]\n");
        }

        if (outlineDictNumber > 0) {
            append("/Outlines ");
            append(outlineDictNumber);
            append(" 0 R\n");
        }

        append(">>\n");
        endobj();
        return objNumber;
    }


    private void addPageBox(String boxName, Page page, float[] rect) throws Exception {
        append("/");
        append(boxName);
        append(" [");
        append(rect[0]);
        append(' ');
        append(page.height - rect[3]);
        append(' ');
        append(rect[2]);
        append(' ');
        append(page.height - rect[1]);
        append("]\n");
    }


    private void setDestinationObjNumbers() {
        int numberOfAnnotations = 0;
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            numberOfAnnotations += page.annots.size();
        }
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            for (Destination destination : page.destinations) {
                destination.pageObjNumber =
                        objNumber + numberOfAnnotations + i + 1;
                destinations.put(destination.name, destination);
            }
        }
    }


    private void addAllPages(int resObjNumber) throws Exception {

        setDestinationObjNumbers();
        addAnnotDictionaries();

        // Calculate the object number of the Pages object
        pagesObjNumber = objNumber + pages.size() + 1;

        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);

            // Page object
            newobj();
            page.objNumber = objNumber;
            append("<<\n");
            append("/Type /Page\n");
            append("/Parent ");
            append(pagesObjNumber);
            append(" 0 R\n");
            append("/MediaBox [0.0 0.0 ");
            append(page.width);
            append(' ');
            append(page.height);
            append("]\n");

            if (page.cropBox != null) {
                addPageBox("CropBox", page, page.cropBox);
            }
            if (page.bleedBox != null) {
                addPageBox("BleedBox", page, page.bleedBox);
            }
            if (page.trimBox != null) {
                addPageBox("TrimBox", page, page.trimBox);
            }
            if (page.artBox != null) {
                addPageBox("ArtBox", page, page.artBox);
            }

            append("/Resources ");
            append(resObjNumber);
            append(" 0 R\n");

            append("/Contents [ ");
            for (Integer n : page.contents) {
                append(n);
                append(" 0 R ");
            }
            append("]\n");
            if (page.annots.size() > 0) {
                append("/Annots [ ");
                for (Annotation annot : page.annots) {
                    append(annot.objNumber);
                    append(" 0 R ");
                }
                append("]\n");
            }

            if (compliance == Compliance.PDF_UA) {
                append("/Tabs /S\n");
                append("/StructParents ");
                append(i);
                append("\n");
            }

            append(">>\n");
            endobj();
        }
    }


    private void addPageContent(Page page) throws Exception {
        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();
        DeflaterOutputStream dos =
                new DeflaterOutputStream(baos, new Deflater());
        byte[] buf = page.buf.toByteArray();
        dos.write(buf, 0, buf.length);
        dos.finish();
        page.buf = null;    // Release the page content memory!

        newobj();
        append("<<\n");
        append("/Filter /FlateDecode\n");
        append("/Length ");
        append(baos.size());
        append("\n");
        append(">>\n");
        append("stream\n");
        append(baos);
        append("\nendstream\n");
        endobj();
        page.contents.add(objNumber);
    }

/*
    // Use this method on systems that don't have Deflater stream or when troubleshooting.
    private void addPageContent(Page page) throws Exception {
        newobj();
        append("<<\n");
        append("/Length ");
        append(page.buf.size());
        append("\n");
        append(">>\n");
        append("stream\n");
        append(page.buf);
        append("\nendstream\n");
        endobj();
        page.buf = null;    // Release the page content memory!
        page.contents.add(objNumber);
    }


    private void addPageContent(Page page) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new LZWEncode(page.buf.toByteArray(), baos);
        page.buf = null;    // Release the page content memory!

        newobj();
        append("<<\n");
        append("/Filter /LZWDecode\n");
        append("/Length ");
        append(baos.size());
        append("\n");
        append(">>\n");
        append("stream\n");
        append(baos);
        append("\nendstream\n");
        endobj();
        page.contents.add(objNumber);
    }
*/

    private int addAnnotationObject(Annotation annot, int index)
            throws Exception {
        newobj();
        annot.objNumber = objNumber;
        append("<<\n");
        append("/Type /Annot\n");
        if (annot.fileAttachment != null) {
            append("/Subtype /FileAttachment\n");
            append("/T (");
            append(annot.fileAttachment.title);
            append(")\n");
            append("/Contents (");
            append(annot.fileAttachment.contents);
            append(")\n");
            append("/FS ");
            append(annot.fileAttachment.embeddedFile.objNumber);
            append(" 0 R\n");
            append("/Name /");
            append(annot.fileAttachment.icon);
            append("\n");
        }
        else {
            append("/Subtype /Link\n");
        }
        append("/Rect [");
        append(annot.x1);
        append(' ');
        append(annot.y1);
        append(' ');
        append(annot.x2);
        append(' ');
        append(annot.y2);
        append("]\n");
        append("/Border [0 0 0]\n");
        if (annot.uri != null) {
            append("/F 4\n");
            append("/A <<\n");
            append("/S /URI\n");
            append("/URI (");
            append(annot.uri);
            append(")\n");
            append(">>\n");
        }
        else if (annot.key != null) {
            Destination destination = destinations.get(annot.key);
            if (destination != null) {
                append("/F 4\n");   // No Zoom
                append("/Dest [");
                append(destination.pageObjNumber);
                append(" 0 R /XYZ 0 ");
                append(destination.yPosition);
                append(" 0]\n");
            }
        }
        if (index != -1) {
            append("/StructParent ");
            append(index++);
            append("\n");
        }
        append(">>\n");
        endobj();

        return index;
    }


    private void addAnnotDictionaries() throws Exception {
        int index = pages.size();
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            if (page.structures.size() > 0) {
                for (int j = 0; j < page.structures.size(); j++) {
                    StructElem element = page.structures.get(j);
                    if (element.annotation != null) {
                        index = addAnnotationObject(element.annotation, index);
                    }
                }
            }
            else if (page.annots.size() > 0) {
                for (int j = 0; j < page.annots.size(); j++) {
                    Annotation annotation = page.annots.get(j);
                    if (annotation != null) {
                        addAnnotationObject(annotation, -1);
                    }
                }
            }
        }
    }


    private void addOCProperties() throws Exception {
        if (!groups.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            for (OptionalContentGroup ocg : this.groups) {
                buf.append(' ');
                buf.append(ocg.objNumber);
                buf.append(" 0 R");
            }

            append("/OCProperties\n");
            append("<<\n");
            append("/OCGs [");
            append(buf.toString());
            append(" ]\n");
            append("/D <<\n");

            append("/AS [\n");
            append("<< /Event /View /Category [/View] /OCGs [");
            append(buf.toString());
            append(" ] >>\n");
            append("<< /Event /Print /Category [/Print] /OCGs [");
            append(buf.toString());
            append(" ] >>\n");
            append("<< /Event /Export /Category [/Export] /OCGs [");
            append(buf.toString());
            append(" ] >>\n");
            append("]\n");

            append("/Order [[ ()");
            append(buf.toString());
            append(" ]]\n");

            append(">>\n");
            append(">>\n");
        }
    }


    public void addPage(Page page) throws Exception {
        int n = pages.size();
        if (n > 0) {
            addPageContent(pages.get(n - 1));
        }
        pages.add(page);
    }


    /**
     *  Writes the PDF object to the output stream.
     *  Does not close the underlying output stream.
     */
    public void flush() throws Exception {
        flush(false);
    }


    /**
     *  Writes the PDF object to the output stream and closes it.
     */
    public void close() throws Exception {
        flush(true);
    }


    private void flush(boolean close) throws Exception {
        if (pagesObjNumber == -1) {
            addPageContent(pages.get(pages.size() - 1));
            addAllPages(addResourcesObject());
            addPagesObject();
        }

        int structTreeRootObjNumber = 0;
        if (compliance == Compliance.PDF_UA) {
            addStructElementObjects();
            structTreeRootObjNumber = addStructTreeRootObject();
            addNumsParentTree();
        }

        int outlineDictNum = 0;
        if (toc != null && toc.getChildren() != null) {
            List<Bookmark> list = toc.toArrayList();
            outlineDictNum = addOutlineDict(toc);
            for (int i = 1; i < list.size(); i++) {
                Bookmark bookmark = list.get(i);
                addOutlineItem(outlineDictNum, i, bookmark);
            }
        }

        int infoObjNumber = addInfoObject();
        int rootObjNumber = addRootObject(structTreeRootObjNumber, outlineDictNum);

        int startxref = byte_count;

        // Create the xref table
        append("xref\n");
        append("0 ");
        append(rootObjNumber + 1);
        append('\n');

        append("0000000000 65535 f \n");
        for (int i = 0; i < objOffset.size(); i++) {
            int offset = objOffset.get(i);
            String str = Integer.toString(offset);
            for (int j = 0; j < 10 - str.length(); j++) {
                append('0');
            }
            append(str);
            append(" 00000 n \n");
        }
        append("trailer\n");
        append("<<\n");
        append("/Size ");
        append(rootObjNumber + 1);
        append('\n');

        String id = (new Salsa20()).getID();
        append("/ID[<");
        append(id);
        append("><");
        append(id);
        append(">]\n");

        append("/Info ");
        append(infoObjNumber);
        append(" 0 R\n");

        append("/Root ");
        append(rootObjNumber);
        append(" 0 R\n");

        append(">>\n");
        append("startxref\n");
        append(startxref);
        append('\n');
        append("%%EOF\n");

        os.flush();
        if (close) {
            os.close();
        }
    }


    /**
     *  Set the "Title" document property of the PDF file.
     *  @param title The title of this document.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     *  Set the "Author" document property of the PDF file.
     *  @param author The author of this document.
     */
    public void setAuthor(String author) {
        this.author = author;
    }


    /**
     *  Set the "Subject" document property of the PDF file.
     *  @param subject The subject of this document.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }


    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    public void setCreator(String creator) {
        this.creator = creator;
    }


    public void setPageLayout(String pageLayout) {
        this.pageLayout = pageLayout;
    }


    public void setPageMode(String pageMode) {
        this.pageMode = pageMode;
    }


    protected void append(int num) throws IOException {
        append(Integer.toString(num));
    }


    protected void append(float val) throws IOException {
        append(PDF.df.format(val));
    }


    protected void append(String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            os.write((byte) str.charAt(i));
        }
        byte_count += len;
    }


    protected void append(char ch) throws IOException {
        append((byte) ch);
    }


    protected void append(byte b) throws IOException {
        os.write(b);
        byte_count += 1;
    }


    protected void append(byte[] buf, int off, int len) throws IOException {
        os.write(buf, off, len);
        byte_count += len;
    }


    protected void append(ByteArrayOutputStream baos) throws IOException {
        baos.writeTo(os);
        byte_count += baos.size();
    }


    /**
     *  Returns a list of objects of type PDFobj read from input stream.
     *
     *  @param inputStream the PDF input stream.
     *
     *  @return List<PDFobj> the list of PDF objects.
     */
    public Map<Integer, PDFobj> read(InputStream inputStream) throws Exception {

        List<PDFobj> objects = new ArrayList<PDFobj>();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            baos.write(ch);
        }
        byte[] pdf = baos.toByteArray();

        int xref = getStartXRef(pdf);
        PDFobj obj1 = getObject(pdf, xref);
        if (obj1.dict.get(0).equals("xref")) {
            // Get the objects using xref table
            getObjects1(pdf, obj1, objects);
        }
        else {
            // Get the objects using XRef stream
            getObjects2(pdf, obj1, objects);
        }

        Map<Integer, PDFobj> pdfObjects = new TreeMap<Integer, PDFobj>();
        for (PDFobj obj : objects) {
            if (obj.dict.contains("stream")) {
                obj.setStream(pdf, obj.getLength(objects));
                if (obj.getValue("/Filter").equals("/FlateDecode")) {
                    Decompressor decompressor = new Decompressor(obj.stream);
                    obj.data = decompressor.getDecompressedData();
                }
                else {
                    // Assume no compression.
                    obj.data = obj.stream;
                }
            }

            if (obj.getValue("/Type").equals("/ObjStm")) {
                int first = Integer.valueOf(obj.getValue("/First"));
                PDFobj o2 = getObject(obj.data, 0, first);
                int count = o2.dict.size();
                for (int i = 0; i < count; i += 2) {
                    String num = o2.dict.get(i);
                    int off = Integer.valueOf(o2.dict.get(i + 1));
                    int end = obj.data.length;
                    if (i <= count - 4) {
                        end = first + Integer.valueOf(o2.dict.get(i + 3));
                    }
                    PDFobj o3 = getObject(obj.data, first + off, end);
                    o3.dict.add(0, "obj");
                    o3.dict.add(0, "0");
                    o3.dict.add(0, num);
                    pdfObjects.put(Integer.valueOf(num), o3);
                }
            }
            else if (obj.getValue("/Type").equals("/XRef")) {
                // Skip the stream XRef object.
            }
            else {
                pdfObjects.put(obj.number, obj);
            }
        }

        return pdfObjects;
    }


    private boolean process(
            PDFobj obj, StringBuilder sb1, byte[] buf, int off) {
        String str = sb1.toString().trim();
        if (!str.equals("")) {
            obj.dict.add(str);
        }
        sb1.setLength(0);

        if (str.equals("endobj")) {
            return true;
        }
        else if (str.equals("stream")) {
            obj.stream_offset = off;
            if (buf[off] == '\n') {
                obj.stream_offset += 1;
            }
            return true;
        }
        else if (str.equals("startxref")) {
            return true;
        }
        return false;
    }


    private PDFobj getObject(byte[] buf, int off) {
        return getObject(buf, off, buf.length);
    }


    private PDFobj getObject(byte[] buf, int off, int len) {

        PDFobj obj = new PDFobj(off);
        StringBuilder token = new StringBuilder();

        int p = 0;
        char c1 = ' ';
        boolean done = false;
        while (!done && off < len) {
            char c2 = (char) buf[off++];
            if (c2 == '(') {
                if (p == 0) {
                    done = process(obj, token, buf, off);
                }
                if (!done) {
                    token.append(c2);
                    ++p;
                }
            }
            else if (c2 == ')') {
                token.append(c2);
                --p;
                if (p == 0) {
                    done = process(obj, token, buf, off);
                }
            }
            else if (c2 == 0x00         // Null
                    || c2 == 0x09       // Horizontal Tab
                    || c2 == 0x0A       // Line Feed (LF)
                    || c2 == 0x0C       // Form Feed
                    || c2 == 0x0D       // Carriage Return (CR)
                    || c2 == 0x20) {    // Space
                done = process(obj, token, buf, off);
                if (!done) {
                    c1 = ' ';
                }
            }
            else if (c2 == '/') {
                done = process(obj, token, buf, off);
                if (!done) {
                    token.append(c2);
                    c1 = c2;
                }
            }
            else if (c2 == '<' || c2 == '>' || c2 == '%') {
                if (c2 != c1) {
                    done = process(obj, token, buf, off);
                    if (!done) {
                        token.append(c2);
                        c1 = c2;
                    }
                }
                else {
                    token.append(c2);
                    done = process(obj, token, buf, off);
                    if (!done) {
                        c1 = ' ';
                    }
                }
            }
            else if (c2 == '[' || c2 == ']' || c2 == '{' || c2 == '}') {
                done = process(obj, token, buf, off);
                if (!done) {
                    obj.dict.add(String.valueOf(c2));
                    c1 = c2;
                }
            }
            else {
                token.append(c2);
                if (p == 0) {
                    c1 = c2;
                }
            }
        }

        return obj;
    }


    /**
     * Converts an array of bytes to an integer.
     * @param buf byte[]
     * @return int
     */
    private int toInt(byte[] buf, int off, int len) {
        int i = 0;
        for (int j = 0; j < len; j++) {
            i |= buf[off + j] & 0xFF;
            if (j < len - 1) {
                i <<= 8;
            }
        }
        return i;
    }


    private void getObjects1(
            byte[] pdf,
            PDFobj obj,
            List<PDFobj> objects) throws Exception {

        String xref = obj.getValue("/Prev");
        if (!xref.equals("")) {
            getObjects1(
                    pdf,
                    getObject(pdf, Integer.valueOf(xref)),
                    objects);
        }

        int i = 1;
        while (true) {
            String token = obj.dict.get(i++);
            if (token.equals("trailer")) {
                break;
            }

            int n = Integer.valueOf(obj.dict.get(i++)); // Number of entries
            for (int j = 0; j < n; j++) {
                String offset = obj.dict.get(i++);      // Object offset
                String number = obj.dict.get(i++);      // Generation number
                String status = obj.dict.get(i++);      // Status keyword
                if (!status.equals("f")) {
                    PDFobj o2 = getObject(pdf, Integer.valueOf(offset));
                    o2.number = Integer.valueOf(o2.dict.get(0));
                    objects.add(o2);
                }
            }
        }

    }


    private void getObjects2(
            byte[] pdf,
            PDFobj obj,
            List<PDFobj> objects) throws Exception {

        String prev = obj.getValue("/Prev");
        if (!prev.equals("")) {
            getObjects2(
                    pdf,
                    getObject(pdf, Integer.valueOf(prev)),
                    objects);
        }

        obj.setStream(pdf, obj.getLength(objects));
        if (obj.getValue("/Filter").equals("/FlateDecode")) {
            Decompressor decompressor = new Decompressor(obj.stream);
            obj.data = decompressor.getDecompressedData();
        }
        else {
            // Assume no compression.
            obj.data = obj.stream;
        }

        int p1 = 0; // Predictor byte
        int f1 = 0; // Field 1
        int f2 = 0; // Field 2
        int f3 = 0; // Field 3
        for (int i = 0; i < obj.dict.size(); i++) {
            String token = obj.dict.get(i);
            if (token.equals("/Predictor")) {
                if (obj.dict.get(i + 1).equals("12")) {
                    p1 = 1;
                }
            }

            if (token.equals("/W")) {
                // "/W [ 1 3 1 ]"
                f1 = Integer.valueOf(obj.dict.get(i + 2));
                f2 = Integer.valueOf(obj.dict.get(i + 3));
                f3 = Integer.valueOf(obj.dict.get(i + 4));
            }
        }

        int n = p1 + f1 + f2 + f3;          // Number of bytes per entry
        byte[] entry = new byte[n];
        for (int i = 0; i < obj.data.length; i += n) {
            // Apply the 'Up' filter.
            for (int j = 0; j < n; j++) {
                entry[j] += obj.data[i + j];
            }

            // Process the entries in a cross-reference stream
            // Page 51 in PDF32000_2008.pdf
            if (entry[p1] == 1) {           // Type 1 entry
                PDFobj o2 = getObject(pdf, toInt(entry, p1 + f1, f2));
                o2.number = Integer.valueOf(o2.dict.get(0));
                objects.add(o2);
            }
        }

    }


    private int getStartXRef(byte[] buf) {
        StringBuilder sb = new StringBuilder();

        for (int i = (buf.length - 10); i > 10; i--) {
            if (buf[i] == 's' &&
                    buf[i + 1] == 't' &&
                    buf[i + 2] == 'a' &&
                    buf[i + 3] == 'r' &&
                    buf[i + 4] == 't' &&
                    buf[i + 5] == 'x' &&
                    buf[i + 6] == 'r' &&
                    buf[i + 7] == 'e' &&
                    buf[i + 8] == 'f') {

                int j = i + 10;
                if (buf[i + 9] == '\r' && buf[i + 10] == '\n') {
                    j = i + 11;
                }

                char ch = (char) buf[j];
                while (ch == ' ' || Character.isDigit(ch)) {
                    sb.append(ch);
                    ch = (char) buf[++j];
                }

                break;
            }
        }

        return Integer.valueOf(sb.toString().trim());
    }


    public int addOutlineDict(Bookmark toc) throws Exception {
        int numOfChildren = getNumOfChildren(0, toc);
        newobj();
        append("<<\n");
        append("/Type /Outlines\n");
        append("/First ");
        append(objNumber + 1);
        append(" 0 R\n");
        append("/Last ");
        append(objNumber + numOfChildren);
        append(" 0 R\n");
        append("/Count ");
        append(numOfChildren);
        append("\n");
        append(">>\n");
        endobj();
        return objNumber;
    }


    public void addOutlineItem(int parent, int i, Bookmark bm1) throws Exception {

        int prev = (bm1.getPrevBookmark() == null) ? 0 : parent + (i - 1);
        int next = (bm1.getNextBookmark() == null) ? 0 : parent + (i + 1);

        int first = 0;
        int last  = 0;
        int count = 0;
        if (bm1.getChildren() != null && bm1.getChildren().size() > 0) {
            first = parent + bm1.getFirstChild().objNumber;
            last  = parent + bm1.getLastChild().objNumber;
            count = (-1) * getNumOfChildren(0, bm1);
        }

        newobj();
        append("<<\n");
        append("/Title <");
        append(toHex(bm1.getTitle()));
        append(">\n");
        append("/Parent ");
        append(parent);
        append(" 0 R\n");
        if (prev > 0) {
            append("/Prev ");
            append(prev);
            append(" 0 R\n");
        }
        if (next > 0) {
            append("/Next ");
            append(next);
            append(" 0 R\n");
        }
        if (first > 0) {
            append("/First ");
            append(first);
            append(" 0 R\n");
        }
        if (last > 0) {
            append("/Last ");
            append(last);
            append(" 0 R\n");
        }
        if (count != 0) {
            append("/Count ");
            append(count);
            append("\n");
        }
        append("/F 4\n");       // No Zoom
        append("/Dest [");
        append(bm1.getDestination().pageObjNumber);
        append(" 0 R /XYZ 0 ");
        append(bm1.getDestination().yPosition);
        append(" 0]\n");
        append(">>\n");
        endobj();
    }


    private int getNumOfChildren(int numOfChildren, Bookmark bm1) {
        List<Bookmark> children = bm1.getChildren();
        if (children != null) {
            for (Bookmark bm2 : children) {
                numOfChildren = getNumOfChildren(++numOfChildren, bm2);
            }
        }
        return numOfChildren;
    }


    public void removePages(
            Set<Integer> pageNumbers,
            Map<Integer, PDFobj> objects) throws Exception {
        Set<Integer> pageObjectNumbers = new HashSet<Integer>();
        List<String> temp = new ArrayList<String>();
        PDFobj pages = getPagesObject(objects);
        List<String> dict = pages.getDict();
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Kids")) {
                temp.add(dict.get(i++));
                temp.add(dict.get(i++));
                int pageNumber = 1;
                while (!dict.get(i).equals("]")) {
                    if (!pageNumbers.contains(pageNumber)) {
                        temp.add(dict.get(i++));
                        temp.add(dict.get(i++));
                        temp.add(dict.get(i++));
                    }
                    else {
                        pageObjectNumbers.add(
                                Integer.valueOf(dict.get(i++)));
                        i++;
                        i++;
                    }
                    pageNumber++;
                }
                temp.add(dict.get(i));
            }
            else if (dict.get(i).equals("/Count")) {
                temp.add(dict.get(i++));
                int count = Integer.valueOf(dict.get(i)) - pageNumbers.size();
                temp.add(String.valueOf(count));
            }
            else {
                temp.add(dict.get(i));
            }
        }
        pages.setDict(temp);
        Iterator<Integer> iter = pageObjectNumbers.iterator();
        while (iter.hasNext()) {
            objects.remove(iter.next());
        }
    }


    public void addObjects(Map<Integer, PDFobj> objects) throws Exception {
        this.pagesObjNumber = Integer.valueOf(getPagesObject(objects).dict.get(0));
        addObjectsToPDF(objects);
    }


    public PDFobj getPagesObject(
            Map<Integer, PDFobj> objects) throws Exception {
        for (PDFobj obj : objects.values()) {
            if (obj.getValue("/Type").equals("/Pages") &&
                    obj.getValue("/Parent").equals("")) {
                return obj;
            }
        }
        return null;
    }


    public List<PDFobj> getPageObjects(
            Map<Integer, PDFobj> objects) throws Exception {
        List<PDFobj> pages = new ArrayList<PDFobj>();
        getPageObjects(getPagesObject(objects), objects, pages);
        return pages;
    }


    private void getPageObjects(
            PDFobj pdfObj,
            Map<Integer, PDFobj> objects,
            List<PDFobj> pages) throws Exception {
        List<Integer> kids = pdfObj.getObjectNumbers("/Kids");
        for (Integer number : kids) {
            PDFobj obj =  objects.get(number);
            if (isPageObject(obj)) {
                pages.add(obj);
            }
            else {
                getPageObjects(obj, objects, pages);
            }
        }
    }


    private boolean isPageObject(PDFobj obj) {
        boolean isPage = false;
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("/Type") &&
                    obj.dict.get(i + 1).equals("/Page")) {
                isPage = true;
            }
        }
        return isPage;
    }


    private String getExtGState(
            PDFobj resources, Map<Integer, PDFobj> objects) {
        StringBuilder buf = new StringBuilder();
        List<String> dict = resources.getDict();
        int level = 0;
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/ExtGState")) {
                buf.append("/ExtGState << ");
                ++i;
                ++level;
                while (level > 0) {
                    String token = dict.get(++i);
                    if (token.equals("<<")) {
                        ++level;
                    }
                    else if (token.equals(">>")) {
                        --level;
                    }
                    buf.append(token);
                    if (level > 0) {
                        buf.append(' ');
                    }
                    else {
                        buf.append('\n');
                    }
                }
                break;
            }
        }
        return buf.toString();
    }


    private List<String> removeTheNameEntry(List<String> dict) {
        List<String> cleanDict = new ArrayList<String>();
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Name")) {
                i += 1;
            }
            else {
                cleanDict.add(dict.get(i));
            }
        }
        return cleanDict;
    }


    private List<PDFobj> getFontObjects(
            PDFobj resources, Map<Integer, PDFobj> objects) {
        List<PDFobj> fonts = new ArrayList<PDFobj>();
        List<String> dict = resources.getDict();
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Font")) {
                if (!dict.get(i + 2).equals(">>")) {
                    PDFobj fontObj = objects.get(Integer.valueOf(dict.get(i + 3)));
                    fontObj.setDict(removeTheNameEntry(fontObj.getDict()));
                    fonts.add(fontObj);
                }
            }
        }

        if (fonts.size() == 0) {
            return null;
        }

        int i = 4;
        while (true) {
            if (dict.get(i).equals("/Font")) {
                i += 2;
                break;
            }
            i += 1;
        }
        while (!dict.get(i).equals(">>")) {
            importedFonts.add(dict.get(i));
            i += 1;
        }

        return fonts;
    }


    private List<PDFobj> getDescendantFonts(
            PDFobj font, Map<Integer, PDFobj> objects) {
        List<PDFobj> descendantFonts = new ArrayList<PDFobj>();
        List<String> dict = font.getDict();
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/DescendantFonts")) {
                if (!dict.get(i + 2).equals("]")) {
                    descendantFonts.add(objects.get(Integer.valueOf(dict.get(i + 2))));
                }
            }
        }
        return descendantFonts;
    }


    private PDFobj getObject(
            String name, PDFobj obj, Map<Integer, PDFobj> objects) {
        List<String> dict = obj.getDict();
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals(name)) {
                return objects.get(Integer.valueOf(dict.get(i + 1)));
            }
        }
        return null;
    }


    public void addResourceObjects(Map<Integer, PDFobj> objects) throws Exception {
        Map<Integer, PDFobj> resources = new TreeMap<Integer, PDFobj>();

        List<PDFobj> pages = getPageObjects(objects);
        for (PDFobj page : pages) {
            PDFobj resObj = page.getResourcesObject(objects);
            List<PDFobj> fonts = getFontObjects(resObj, objects);
            if (fonts != null) {
                for (PDFobj font : fonts) {
                    resources.put(font.getNumber(), font);
                    PDFobj obj = getObject("/ToUnicode", font, objects);
                    if (obj != null) {
                        resources.put(obj.getNumber(), obj);
                    }
                    List<PDFobj> descendantFonts = getDescendantFonts(font, objects);
                    for (PDFobj descendantFont : descendantFonts) {
                        resources.put(descendantFont.getNumber(), descendantFont);
                        obj = getObject("/FontDescriptor", descendantFont, objects);
                        resources.put(obj.getNumber(), obj);
                        obj = getObject("/FontFile2", obj, objects);
                        resources.put(obj.getNumber(), obj);
                    }
                }
            }
            extGState = getExtGState(resObj, objects);
        }

        if (resources.size() > 0) {
            addObjectsToPDF(resources);
        }
    }


    private void addObjectsToPDF(Map<Integer, PDFobj> objects) throws Exception {

        int maxObjNumber = Collections.max(objects.keySet());
        for (int i = 1; i <= maxObjNumber; i++) {
            if (objects.get(i) == null) {
                PDFobj obj = new PDFobj();
                obj.setNumber(i);
                objects.put(obj.number, obj);
            }
        }

        for (PDFobj obj : objects.values()) {
            objNumber = obj.number;
            objOffset.add(byte_count);

            if (obj.offset == 0) {
                append(obj.number);
                append(" 0 obj\n");
                if (obj.dict != null) {
                    for (int i = 0; i < obj.dict.size(); i++) {
                        append(obj.dict.get(i));
                        append(' ');
                    }
                }
                if (obj.stream != null) {
                    if (obj.dict.size() == 0) {
                        append("<< /Length ");
                        append(obj.stream.length);
                        append(" >>");
                    }
                    append("\nstream\n");
                    for (int i = 0; i < obj.stream.length; i++) {
                        append(obj.stream[i]);
                    }
                    append("\nendstream\n");
                }
                append("endobj\n");
            }
            else {
                boolean link = false;
                int n = obj.dict.size();
                String token = null;
                for (int i = 0; i < n; i++) {
                    token = obj.dict.get(i);
                    append(token);
                    if (token.startsWith("(http:")) {
                        link = true;
                    }
                    else if (link == true && token.endsWith(")")) {
                        link = false;
                    }
                    if (i < (n - 1)) {
                        if (!link) {
                            append(' ');
                        }
                    }
                    else {
                        append('\n');
                    }
                }
                if (obj.stream != null) {
                    for (int i = 0; i < obj.stream.length; i++) {
                        append(obj.stream[i]);
                    }
                    append("\nendstream\n");
                }
                if (!token.equals("endobj")) {
                    append("endobj\n");
                }
            }
        }

    }

}   // End of PDF.java
