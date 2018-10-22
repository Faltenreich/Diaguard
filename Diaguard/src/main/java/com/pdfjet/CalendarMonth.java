/**
 *  CalendarMonth.java
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
import java.io.*;
import java.util.*;


public class CalendarMonth {

    Font f1 = null;
    Font f2 = null;

    float x1 = 75f;
    float y1 = 75f;
    float dx = 23f;
    float dy = 20f;

    String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
    // DAY_OF_WEEK     1     2     3     4     5     6     7

    int days_in_month;
    int day_of_week;


    public CalendarMonth(Font f1, Font f2, int year, int month) {
        this.f1 = f1;
        this.f2 = f2;
        days_in_month = getDaysInMonth(year, month - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
    }


    public void setHeadFont(Font font) {
        this.f1 = font;
    }


    public void setBodyFont(Font font) {
        this.f2 = font;
    }


    public void setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
    }


    public void setCellWidth(float width) {
        this.dx = width;
    }


    public void setCellHeight(float height) {
        this.dy = height;
    }


    public void drawOn(Page page) throws Exception {
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 0) {
                    float offset = (dx - f1.stringWidth(days[col])) / 2;
                    TextLine text = new TextLine(f1, days[col]);
                    text.setLocation(x1 + col*dx + offset, x1 + row*dy);
                    text.drawOn(page);
                    // Draw the line separating the title from the dates.
                    Line line = new Line(
                            x1,
                            y1 + dx/4,
                            x1 + 7*dx,
                            y1 + dx/4);
                    line.setWidth(0.5f);
                    line.drawOn(page);
                }
                else {
                    int day_of_month = ((7*row + col) - 6) - (day_of_week - 1);
                    if (day_of_month > 0 && day_of_month <= days_in_month) {
                        String s1 = String.valueOf(day_of_month);
                        float offset = (dx - f2.stringWidth(s1)) / 2;
                        TextLine text = new TextLine(f2, s1);
                        text.setLocation(x1 + col*dx + offset, y1 + row*dy);
                        text.drawOn(page);

                        page.setPenWidth(1.5f);
                        page.setPenColor(Color.blue);
                        page.drawEllipse(x1 + col*dx + dx/2, y1 + row*dy - dy/5, 8f, 8f);
                    }
                }
            }
        }
    }


    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    private int getDaysInMonth(
            int year,
            int month) {
        int daysInFebruary = isLeapYear(year) ? 29 : 28;
        int[] daysInMonth = {31, daysInFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return daysInMonth[month];
    }

}
