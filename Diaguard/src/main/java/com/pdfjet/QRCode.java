/**
 *
Copyright (c) 2009 Kazuhiko Arase

URL: http://www.d-project.com/

Licensed under the MIT license:
  http://www.opensource.org/licenses/mit-license.php

The word "QR Code" is registered trademark of 
DENSO WAVE INCORPORATED
  http://www.denso-wave.com/qrcode/faqpatent-e.html
*/

package com.pdfjet;

import java.io.UnsupportedEncodingException;


/**
 * Used to create 2D QR Code barcodes. Please see Example_20.
 * 
 * @author Kazuhiko Arase
 */
public class QRCode implements Drawable {

    private static final int PAD0 = 0xEC;
    private static final int PAD1 = 0x11;
    private Boolean[][] modules;
    private int moduleCount = 33;   // Magic Number
    private int errorCorrectLevel = ErrorCorrectLevel.M;

    private float x;
    private float y;

    private byte[] qrData;
    private float m1 = 2.0f;        // Module length


    /**
     * Used to create 2D QR Code barcodes.
     * 
     * @param str the string to encode.
     * @param errorCorrectLevel the desired error correction level.
     * @throws UnsupportedEncodingException
     */
    public QRCode(String str, int errorCorrectLevel) throws UnsupportedEncodingException {
        this.qrData = str.getBytes("UTF-8");
        this.errorCorrectLevel = errorCorrectLevel;
        this.make(false, getBestMaskPattern());
    }
    
    /**
     *  Sets the position where this barcode will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the barcode.
     *  @param y the y coordinate of the top left corner of the barcode.
     */
    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);
    }

    /**
     *  Sets the position where this barcode will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the barcode.
     *  @param y the y coordinate of the top left corner of the barcode.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Sets the location where this barcode will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the barcode.
     *  @param y the y coordinate of the top left corner of the barcode.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     *  Sets the module length of this barcode.
     *  The default value is 2.0f
     *
     *  @param moduleLength the specified module length.
     */
    public void setModuleLength(double moduleLength) {
        this.m1 = (float) moduleLength;
    }


    /**
     *  Sets the module length of this barcode.
     *  The default value is 2.0f
     *
     *  @param moduleLength the specified module length.
     */
    public void setModuleLength(float moduleLength) {
        this.m1 = moduleLength;
    }

    /**
     *  Draws this barcode on the specified page.
     *
     *  @param page the specified page.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page) throws Exception {
        for (int row = 0; row < modules.length; row++) {
            for (int col = 0; col < modules.length; col++) {
                if (isDark(row, col)) {
                    page.fillRect(x + col*m1, y + row*m1, m1, m1);
                }
            }
        }

        float w = m1*modules.length;
        float h = m1*modules.length;
        return new float[] {x + w, y + h};
    }

    public Boolean[][] getData() {
        return modules;
    }

    /**
     *  @param row the row.
     *  @param col the column.
     */
    protected boolean isDark(int row, int col) {
        if (modules[row][col] != null) {
            return modules[row][col];
        }
        else {
            return false;
        }
    }

    protected int getModuleCount() {
        return moduleCount;
    }

    protected int getBestMaskPattern() {
        int minLostPoint = 0;
        int pattern = 0;

        for (int i = 0; i < 8; i++) {
            make(true, i);
            int lostPoint = QRUtil.getLostPoint(this);
            if (i == 0 || minLostPoint >  lostPoint) {
                minLostPoint = lostPoint;
                pattern = i;
            }
        }

        return pattern;
    }

    protected void make(boolean test, int maskPattern) {
        modules = new Boolean[moduleCount][moduleCount];

        setupPositionProbePattern(0, 0);
        setupPositionProbePattern(moduleCount - 7, 0);
        setupPositionProbePattern(0, moduleCount - 7);

        setupPositionAdjustPattern();
        setupTimingPattern();
        setupTypeInfo(test, maskPattern);

        mapData(createData(errorCorrectLevel), maskPattern);
    }

    private void mapData(byte[] data, int maskPattern) {
        int inc = -1;
        int row = moduleCount - 1;
        int bitIndex = 7;
        int byteIndex = 0;

        for (int col = moduleCount - 1; col > 0; col -= 2) {
            if (col == 6) col--;
            while (true) {
                for (int c = 0; c < 2; c++) {
                    if (modules[row][col - c] == null) {
                        boolean dark = false;

                        if (byteIndex < data.length) {
                            dark = (((data[byteIndex] >>> bitIndex) & 1) == 1);
                        }

                        boolean mask = QRUtil.getMask(maskPattern, row, col - c);
                        if (mask) {
                            dark = !dark;
                        }

                        modules[row][col - c] = dark;
                        bitIndex--;
                        if (bitIndex == -1) {
                            byteIndex++;
                            bitIndex = 7;
                        }
                    }
                }

                row += inc;
                if (row < 0 || moduleCount <= row) {
                    row -= inc;
                    inc = -inc;
                    break;
                }
            }
        }
    }

    private void setupPositionAdjustPattern() {
        int[] pos = {6, 26};    // Magic Numbers
        for (int i = 0; i < pos.length; i++) {
            for (int j = 0; j < pos.length; j++) {
                int row = pos[i];
                int col = pos[j];

                if (modules[row][col] != null) {
                    continue;
                }

                for (int r = -2; r <= 2; r++) {
                    for (int c = -2; c <= 2; c++) {
                        modules[row + r][col + c] =
                                r == -2 || r == 2 || c == -2 || c == 2 || (r == 0 && c == 0);
                    }
                }
            }
        }
    }

    private void setupPositionProbePattern(int row, int col) {
        for (int r = -1; r <= 7; r++) {
            for (int c = -1; c <= 7; c++) {
                if (row + r <= -1 || moduleCount <= row + r
                        || col + c <= -1 || moduleCount <= col + c) {
                    continue;
                }

                modules[row + r][col + c] =
                        (0 <= r && r <= 6 && (c == 0 || c == 6)) ||
                        (0 <= c && c <= 6 && (r == 0 || r == 6)) ||
                        (2 <= r && r <= 4 && 2 <= c && c <= 4);
            }
        }
    }

    private void setupTimingPattern() {
        for (int r = 8; r < moduleCount - 8; r++) {
            if (modules[r][6] != null) {
                continue;
            }
            modules[r][6] = (r % 2 == 0);
        }
        for (int c = 8; c < moduleCount - 8; c++) {
            if (modules[6][c] != null) {
                continue;
            }
            modules[6][c] = (c % 2 == 0);
        }
    }

    private void setupTypeInfo(boolean test, int maskPattern) {
        int data = (errorCorrectLevel << 3) | maskPattern;
        int bits = QRUtil.getBCHTypeInfo(data);

        for (int i = 0; i < 15; i++) {
            Boolean mod = (!test && ((bits >> i) & 1) == 1);
            if (i < 6) {
                modules[i][8] = mod;
            }
            else if (i < 8) {
                modules[i + 1][8] = mod;
            }
            else {
                modules[moduleCount - 15 + i][8] = mod;
            }
        }

        for (int i = 0; i < 15; i++) {
            Boolean mod = (!test && ((bits >> i) & 1) == 1);
            if (i < 8) {
                modules[8][moduleCount - i - 1] = mod;
            }
            else if (i < 9) {
                modules[8][15 - i - 1 + 1] = mod;
            }
            else {
                modules[8][15 - i - 1] = mod;
            }
        }

        modules[moduleCount - 8][8] = !test;
    }

    private byte[] createData(int errorCorrectLevel) {
        RSBlock[] rsBlocks = RSBlock.getRSBlocks(errorCorrectLevel);

        BitBuffer buffer = new BitBuffer();
        buffer.put(4, 4);
        buffer.put(qrData.length, 8);
        for (int i = 0; i < qrData.length; i++) {
            buffer.put(qrData[i], 8);
        }

        int totalDataCount = 0;
        for (int i = 0; i < rsBlocks.length; i++) {
            totalDataCount += rsBlocks[i].getDataCount();
        }

        if (buffer.getLengthInBits() > totalDataCount * 8) {
            throw new IllegalArgumentException("String length overflow. ("
                + buffer.getLengthInBits()
                + ">"
                +  totalDataCount * 8
                + ")");
        }

        if (buffer.getLengthInBits() + 4 <= totalDataCount * 8) {
            buffer.put(0, 4);
        }

        // padding
        while (buffer.getLengthInBits() % 8 != 0) {
            buffer.put(false);
        }

        // padding
        while (true) {
            if (buffer.getLengthInBits() >= totalDataCount * 8) {
                break;
            }
            buffer.put(PAD0, 8);

            if (buffer.getLengthInBits() >= totalDataCount * 8) {
                break;
            }
            buffer.put(PAD1, 8);
        }

        return createBytes(buffer, rsBlocks);
    }

    private byte[] createBytes(BitBuffer buffer, RSBlock[] rsBlocks) {
        int offset = 0;
        int maxDcCount = 0;
        int maxEcCount = 0;

        int[][] dcdata = new int[rsBlocks.length][];
        int[][] ecdata = new int[rsBlocks.length][];

        for (int r = 0; r < rsBlocks.length; r++) {
            int dcCount = rsBlocks[r].getDataCount();
            int ecCount = rsBlocks[r].getTotalCount() - dcCount;

            maxDcCount = Math.max(maxDcCount, dcCount);
            maxEcCount = Math.max(maxEcCount, ecCount);

            dcdata[r] = new int[dcCount];
            for (int i = 0; i < dcdata[r].length; i++) {
                dcdata[r][i] = 0xff & buffer.getBuffer()[i + offset];
            }
            offset += dcCount;

            Polynomial rsPoly = QRUtil.getErrorCorrectPolynomial(ecCount);
            Polynomial rawPoly = new Polynomial(dcdata[r], rsPoly.getLength() - 1);

            Polynomial modPoly = rawPoly.mod(rsPoly);
            ecdata[r] = new int[rsPoly.getLength() - 1];
            for (int i = 0; i < ecdata[r].length; i++) {
                int modIndex = i + modPoly.getLength() - ecdata[r].length;
                ecdata[r][i] = (modIndex >= 0) ? modPoly.get(modIndex) : 0;
            }
        }

        int totalCodeCount = 0;
        for (int i = 0; i < rsBlocks.length; i++) {
            totalCodeCount += rsBlocks[i].getTotalCount();
        }

        byte[] data = new byte[totalCodeCount];
        int index = 0;
        for (int i = 0; i < maxDcCount; i++) {
            for (int r = 0; r < rsBlocks.length; r++) {
                if (i < dcdata[r].length) {
                    data[index++] = (byte) dcdata[r][i];
                }
            }
        }

        for (int i = 0; i < maxEcCount; i++) {
            for (int r = 0; r < rsBlocks.length; r++) {
                if (i < ecdata[r].length) {
                    data[index++] = (byte) ecdata[r][i];
                }
            }
        }

        return data;
    }

}
