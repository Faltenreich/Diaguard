/**
 *  EmbeddedFile.java
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;


/**
 *  Used to embed file objects.
 *  The file objects must added to the PDF before drawing on the first page.
 *
 */
public class EmbeddedFile {

    protected int objNumber = -1;
    protected String fileName = null;


    public EmbeddedFile(PDF pdf, String fileName, InputStream stream, boolean compress) throws Exception {
        this.fileName = fileName;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	byte[] buf = new byte[2048];
    	int number;
    	while ((number = stream.read(buf, 0, buf.length)) > 0) {
    		baos.write(buf, 0, number);
    	}
        stream.close();

        if (compress) {
            buf = baos.toByteArray();
            baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater());
  		    dos.write(buf, 0, buf.length);
            dos.finish();
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /EmbeddedFile\n");
        if (compress) {
            pdf.append("/Filter /FlateDecode\n");
        }
        pdf.append("/Length ");
        pdf.append(baos.size());
        pdf.append("\n");
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(baos);
        pdf.append("\nendstream\n");
        pdf.endobj();

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Filespec\n");
        pdf.append("/F (");
        pdf.append(fileName);
        pdf.append(")\n");
        pdf.append("/EF <</F ");
        pdf.append(pdf.objNumber - 1);
        pdf.append(" 0 R>>\n");
        pdf.append(">>\n");
        pdf.endobj();

        this.objNumber = pdf.objNumber;

        pdf.embeddedFiles.add(this);
    }


    public String getFileName() {
        return fileName;
    }

}   // End of EmbeddedFile.java
