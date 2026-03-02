package com.pdfjet;

import java.io.*;
import java.util.*;


public class LZWEncode {

    private LookupTable table = new LookupTable();
    private int bitBuffer = 0;
    private int bitsInBuffer = 0;

    public LZWEncode(byte[] buffer, OutputStream stream) throws Exception {
        int code1 = 0;
        int code2 = 258;
        int length = 9;
        writeCode(256, length, stream);                 // Clear Table code

        List<Byte> key = new ArrayList<Byte>();
        int i = 0;
        while (i < buffer.length) {
            key.add(buffer[i]);
            int code = table.at(key, /*put:*/ code2);
            if (code >= 0) {
                code1 = code;
                i += 1;
                if (i < buffer.length) {
                    continue;
                }
                writeCode(code1, length, stream);
            }
            else {
                writeCode(code1, length, stream);
                code2 += 1;
                if (code2 == 512) {
                    length = 10;
                }
                else if (code2 == 1024) {
                    length = 11;
                }
                else if (code2 == 2048) {
                    length = 12;
                }
                else if (code2 == 4095) {               // EarlyChange is 1
                    writeCode(256, length, stream);     // Clear Table code
                    code2 = 258;
                    length = 9;
                    table.clear();
                }
                key.clear();
            }
        }

        writeCode(257, length, stream);                 // EOD
        if (bitsInBuffer > 0) {
            stream.write((bitBuffer << (8 - bitsInBuffer)) & 0xFF);
        }
    }

    private void writeCode(
            int code, int length, OutputStream stream) throws Exception {
        bitBuffer <<= length;
        bitBuffer |= code;
        bitsInBuffer += length;
        while (bitsInBuffer >= 8) {
            stream.write((bitBuffer >>> (bitsInBuffer - 8)) & 0xFF);
            bitsInBuffer -= 8;
        }
    }
/*
    private static String toBinaryString(
            int value,
            int numOfDigits) {
        StringBuilder buf = new StringBuilder();
        String str = Integer.toBinaryString(value).toUpperCase();
        int len = str.length();
        while (len < numOfDigits) {
            buf.append("0");
            len += 1;
        }
        buf.append(str);
        str = buf.toString();
        if (numOfDigits == 32) {
            buf.setLength(0);
            for (int i = 0; i < str.length(); i++) {
                if (i > 0 && i % 8 == 0) {
                    buf.append(" ");
                }
                buf.append(str.charAt(i));
            }
            str =  buf.toString();
        }
        return str;
    }

    public static void main(String[] args) throws Exception {
        String fileName = "data/world-communications.txt";
        int fileSize = (int) (new File(fileName)).length();
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream("world-commnunications.lzw"));
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buf = new byte[fileSize];
        int len = fis.read(buf, 0, buf.length);
long time1 = System.currentTimeMillis();
        new LZWEncode(buf, stream);
long time2 = System.currentTimeMillis();
System.out.println(time2 - time1);
        fis.close();
        stream.close();
    }
*/
}
