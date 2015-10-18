/**
 *  Arabic.java
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

import java.lang.*;
import java.util.*;


public class Arabic {

    public static String reorderVisually(String str) {
        StringBuilder buf1 = new StringBuilder();
        StringBuilder buf2 = new StringBuilder();
        boolean arabic = true;
        char ch = 0x00;
        for (int i = (str.length() - 1); i >= 0; i--) {
            ch = str.charAt(i);
            if (arabic) {
                if (isArabic(ch)) {
                    buf1.append(ch);
                }
                else {
                    buf2.append(ch);
                    arabic = false;
                }
            }
            else {
                if (isArabic(ch)) {
                    buf1.append(buf2.reverse());
                    buf2.setLength(0);
                    arabic = true;
                    buf1.append(ch);
                }
                else {
                    buf2.append(ch);
                }
            }
        }
        if (buf2.length() > 0) {
            buf1.append(buf2.reverse());
        }
        return buf1.toString();
    }


    public static boolean isArabic(char ch) {
        return (ch >= 0x0600 && ch <= 0x06FF);
    }

}
