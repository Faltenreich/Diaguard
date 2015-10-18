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
 * Polynomial
 * @author Kazuhiko Arase
 */
class Polynomial {

    private final int[] num;


    public Polynomial(int[] num) {
        this(num, 0);
    }

    public Polynomial(int[] num, int shift) {
        int offset = 0;

        while (offset < num.length && num[offset] == 0) {
            offset++;
        }

        this.num = new int[num.length - offset + shift];
        System.arraycopy(num, offset, this.num, 0, num.length - offset);
    }

    public int get(int index) {
        return num[index];
    }

    public int getLength() {
        return num.length;
    }

    public Polynomial multiply(Polynomial e) {
        int[] num = new int[getLength() + e.getLength() - 1];

        for (int i = 0; i < getLength(); i++) {
            for (int j = 0; j < e.getLength(); j++) {
                num[i + j] ^= QRMath.gexp(QRMath.glog(get(i)) + QRMath.glog(e.get(j)));
            }
        }

        return new Polynomial(num);
    }

    public Polynomial mod(Polynomial e) {
        if (getLength() - e.getLength() < 0) {
            return this;
        }

        int ratio = QRMath.glog(get(0)) - QRMath.glog(e.get(0));
        int[] num = new int[getLength()];
        for (int i = 0; i < getLength(); i++) {
            num[i] = get(i);
        }

        for (int i = 0; i < e.getLength(); i++) {
            num[i] ^= QRMath.gexp(QRMath.glog(e.get(i)) + ratio);
        }

        return new Polynomial(num).mod(e);
    }
}
