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


/**
 * QRUtil
 * @author Kazuhiko Arase
 */
class QRUtil {

    protected static Polynomial getErrorCorrectPolynomial(int errorCorrectLength) {
        Polynomial a = new Polynomial(new int[] {1});
        for (int i = 0; i < errorCorrectLength; i++) {
            a = a.multiply(new Polynomial(new int[] { 1, QRMath.gexp(i) }));
        }
        return a;
    }

    protected static boolean getMask(int maskPattern, int i, int j) {
        switch (maskPattern) {

        case MaskPattern.PATTERN000 : return (i + j) % 2 == 0;
        case MaskPattern.PATTERN001 : return (i % 2) == 0;
        case MaskPattern.PATTERN010 : return (j % 3) == 0;
        case MaskPattern.PATTERN011 : return (i + j) % 3 == 0;
        case MaskPattern.PATTERN100 : return (i / 2 + j / 3) % 2 == 0;
        case MaskPattern.PATTERN101 : return (i * j) % 2 + (i * j) % 3 == 0;
        case MaskPattern.PATTERN110 : return ((i * j) % 2 + (i * j) % 3) % 2 == 0;
        case MaskPattern.PATTERN111 : return ((i * j) % 3 + (i + j) % 2) % 2 == 0;

        default :
            throw new IllegalArgumentException("mask: " + maskPattern);
        }
    }

    protected static int getLostPoint(QRCode qrCode) {
        int moduleCount = qrCode.getModuleCount();
        int lostPoint = 0;

        // LEVEL1
        for (int row = 0; row < moduleCount; row++) {
            for (int col = 0; col < moduleCount; col++) {
                int sameCount = 0;
                boolean dark = qrCode.isDark(row, col);

                for (int r = -1; r <= 1; r++) {
                    if (row + r < 0 || moduleCount <= row + r) {
                        continue;
                    }

                    for (int c = -1; c <= 1; c++) {
                        if (col + c < 0 || moduleCount <= col + c) {
                            continue;
                        }

                        if (r == 0 && c == 0) {
                            continue;
                        }

                        if (dark == qrCode.isDark(row + r, col + c)) {
                            sameCount++;
                        }
                    }
                }

                if (sameCount > 5) {
                    lostPoint += (3 + sameCount - 5);
                }
            }
        }

        // LEVEL2
        for (int row = 0; row < moduleCount - 1; row++) {
            for (int col = 0; col < moduleCount - 1; col++) {
                int count = 0;
                if (qrCode.isDark(row,     col    )) count++;
                if (qrCode.isDark(row + 1, col    )) count++;
                if (qrCode.isDark(row,     col + 1)) count++;
                if (qrCode.isDark(row + 1, col + 1)) count++;
                if (count == 0 || count == 4) {
                    lostPoint += 3;
                }
            }
        }

        // LEVEL3
        for (int row = 0; row < moduleCount; row++) {
            for (int col = 0; col < moduleCount - 6; col++) {
                if (qrCode.isDark(row, col)
                        && !qrCode.isDark(row, col + 1)
                        &&  qrCode.isDark(row, col + 2)
                        &&  qrCode.isDark(row, col + 3)
                        &&  qrCode.isDark(row, col + 4)
                        && !qrCode.isDark(row, col + 5)
                        &&  qrCode.isDark(row, col + 6)) {
                    lostPoint += 40;
                }
            }
        }

        for (int col = 0; col < moduleCount; col++) {
            for (int row = 0; row < moduleCount - 6; row++) {
                if (qrCode.isDark(row, col)
                        && !qrCode.isDark(row + 1, col)
                        &&  qrCode.isDark(row + 2, col)
                        &&  qrCode.isDark(row + 3, col)
                        &&  qrCode.isDark(row + 4, col)
                        && !qrCode.isDark(row + 5, col)
                        &&  qrCode.isDark(row + 6, col)) {
                    lostPoint += 40;
                }
            }
        }

        // LEVEL4
        int darkCount = 0;
        for (int col = 0; col < moduleCount; col++) {
            for (int row = 0; row < moduleCount; row++) {
                if (qrCode.isDark(row, col)) {
                    darkCount++;
                }
            }
        }

        int ratio = Math.abs(100 * darkCount / moduleCount / moduleCount - 50) / 5;
        lostPoint += ratio * 10;

        return lostPoint;
    }

    private static final int G15 = (1 << 10) | (1 << 8) | (1 << 5) | (1 << 4) | (1 << 2) | (1 << 1) | (1 << 0);

    private static final int G15_MASK = (1 << 14) | (1 << 12) | (1 << 10) | (1 << 4) | (1 << 1);

    public static int getBCHTypeInfo(int data) {
        int d = data << 10;
        while (getBCHDigit(d) - getBCHDigit(G15) >= 0) {
            d ^= (G15 << (getBCHDigit(d) - getBCHDigit(G15)));
        }
        return ((data << 10) | d) ^ G15_MASK;
    }

    private static int getBCHDigit(int data) {
        int digit = 0;
        while (data != 0) {
            digit++;
            data >>>= 1;
        }
        return digit;
    }

}
