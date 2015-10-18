package com.pdfjet;


class StandardFont {

    protected String name;
    protected int bBoxLLx;
    protected int bBoxLLy;
    protected int bBoxURx;
    protected int bBoxURy;
    protected int underlinePosition;
    protected int underlineThickness;
    protected int[][] metrics;

    protected static StandardFont getInstance(CoreFont coreFont) {
        StandardFont font = new StandardFont();
        switch (coreFont) {
            case COURIER:
            font.name = Courier.name;
            font.bBoxLLx = Courier.bBoxLLx;
            font.bBoxLLy = Courier.bBoxLLy;
            font.bBoxURx = Courier.bBoxURx;
            font.bBoxURy = Courier.bBoxURy;
            font.underlinePosition = Courier.underlinePosition;
            font.underlineThickness = Courier.underlineThickness;
            font.metrics = Courier.metrics;
            break;
            
            case COURIER_BOLD:
            font.name  = Courier_Bold.name;
            font.bBoxLLx = Courier_Bold.bBoxLLx;
            font.bBoxLLy = Courier_Bold.bBoxLLy;
            font.bBoxURx = Courier_Bold.bBoxURx;
            font.bBoxURy = Courier_Bold.bBoxURy;
            font.underlinePosition = Courier_Bold.underlinePosition;
            font.underlineThickness = Courier_Bold.underlineThickness;
            font.metrics = Courier_Bold.metrics;
            break;
            
            case COURIER_OBLIQUE:
            font.name  = Courier_Oblique.name;
            font.bBoxLLx = Courier_Oblique.bBoxLLx;
            font.bBoxLLy = Courier_Oblique.bBoxLLy;
            font.bBoxURx = Courier_Oblique.bBoxURx;
            font.bBoxURy = Courier_Oblique.bBoxURy;
            font.underlinePosition = Courier_Oblique.underlinePosition;
            font.underlineThickness = Courier_Oblique.underlineThickness;
            font.metrics = Courier_Oblique.metrics;
            break;

            case COURIER_BOLD_OBLIQUE:
            font.name  = Courier_BoldOblique.name;
            font.bBoxLLx = Courier_BoldOblique.bBoxLLx;
            font.bBoxLLy = Courier_BoldOblique.bBoxLLy;
            font.bBoxURx = Courier_BoldOblique.bBoxURx;
            font.bBoxURy = Courier_BoldOblique.bBoxURy;
            font.underlinePosition = Courier_BoldOblique.underlinePosition;
            font.underlineThickness = Courier_BoldOblique.underlineThickness;
            font.metrics = Courier_BoldOblique.metrics;
            break;

            case HELVETICA:
            font.name  = Helvetica.name;
            font.bBoxLLx = Helvetica.bBoxLLx;
            font.bBoxLLy = Helvetica.bBoxLLy;
            font.bBoxURx = Helvetica.bBoxURx;
            font.bBoxURy = Helvetica.bBoxURy;
            font.underlinePosition = Helvetica.underlinePosition;
            font.underlineThickness = Helvetica.underlineThickness;
            font.metrics = Helvetica.metrics;
            break;

            case HELVETICA_BOLD:
            font.name = Helvetica_Bold.name;
            font.bBoxLLx = Helvetica_Bold.bBoxLLx;
            font.bBoxLLy = Helvetica_Bold.bBoxLLy;
            font.bBoxURx = Helvetica_Bold.bBoxURx;
            font.bBoxURy = Helvetica_Bold.bBoxURy;
            font.underlinePosition = Helvetica_Bold.underlinePosition;
            font.underlineThickness = Helvetica_Bold.underlineThickness;
            font.metrics = Helvetica_Bold.metrics;
            break;

            case HELVETICA_OBLIQUE:
            font.name = Helvetica_Oblique.name;
            font.bBoxLLx = Helvetica_Oblique.bBoxLLx;
            font.bBoxLLy = Helvetica_Oblique.bBoxLLy;
            font.bBoxURx = Helvetica_Oblique.bBoxURx;
            font.bBoxURy = Helvetica_Oblique.bBoxURy;
            font.underlinePosition = Helvetica_Oblique.underlinePosition;
            font.underlineThickness = Helvetica_Oblique.underlineThickness;
            font.metrics = Helvetica_Oblique.metrics;
            break;

            case HELVETICA_BOLD_OBLIQUE:
            font.name = Helvetica_BoldOblique.name;
            font.bBoxLLx = Helvetica_BoldOblique.bBoxLLx;
            font.bBoxLLy = Helvetica_BoldOblique.bBoxLLy;
            font.bBoxURx = Helvetica_BoldOblique.bBoxURx;
            font.bBoxURy = Helvetica_BoldOblique.bBoxURy;
            font.underlinePosition = Helvetica_BoldOblique.underlinePosition;
            font.underlineThickness = Helvetica_BoldOblique.underlineThickness;
            font.metrics = Helvetica_BoldOblique.metrics;
            break;

            case TIMES_ROMAN:
            font.name = Times_Roman.name;
            font.bBoxLLx = Times_Roman.bBoxLLx;
            font.bBoxLLy = Times_Roman.bBoxLLy;
            font.bBoxURx = Times_Roman.bBoxURx;
            font.bBoxURy = Times_Roman.bBoxURy;
            font.underlinePosition = Times_Roman.underlinePosition;
            font.underlineThickness = Times_Roman.underlineThickness;
            font.metrics = Times_Roman.metrics;
            break;

            case TIMES_BOLD:
            font.name = Times_Bold.name;
            font.bBoxLLx = Times_Bold.bBoxLLx;
            font.bBoxLLy = Times_Bold.bBoxLLy;
            font.bBoxURx = Times_Bold.bBoxURx;
            font.bBoxURy = Times_Bold.bBoxURy;
            font.underlinePosition = Times_Bold.underlinePosition;
            font.underlineThickness = Times_Bold.underlineThickness;
            font.metrics = Times_Bold.metrics;
            break;

            case TIMES_ITALIC:
            font.name = Times_Italic.name;
            font.bBoxLLx = Times_Italic.bBoxLLx;
            font.bBoxLLy = Times_Italic.bBoxLLy;
            font.bBoxURx = Times_Italic.bBoxURx;
            font.bBoxURy = Times_Italic.bBoxURy;
            font.underlinePosition = Times_Italic.underlinePosition;
            font.underlineThickness = Times_Italic.underlineThickness;
            font.metrics = Times_Italic.metrics;
            break;

            case TIMES_BOLD_ITALIC:
            font.name = Times_BoldItalic.name;
            font.bBoxLLx = Times_BoldItalic.bBoxLLx;
            font.bBoxLLy = Times_BoldItalic.bBoxLLy;
            font.bBoxURx = Times_BoldItalic.bBoxURx;
            font.bBoxURy = Times_BoldItalic.bBoxURy;
            font.underlinePosition = Times_BoldItalic.underlinePosition;
            font.underlineThickness = Times_BoldItalic.underlineThickness;
            font.metrics = Times_BoldItalic.metrics;
            break;

            case SYMBOL:
            font.name = Symbol.name;
            font.bBoxLLx = Symbol.bBoxLLx;
            font.bBoxLLy = Symbol.bBoxLLy;
            font.bBoxURx = Symbol.bBoxURx;
            font.bBoxURy = Symbol.bBoxURy;
            font.underlinePosition = Symbol.underlinePosition;
            font.underlineThickness = Symbol.underlineThickness;
            font.metrics = Symbol.metrics;
            break;

            case ZAPF_DINGBATS:
            font.name = ZapfDingbats.name;
            font.bBoxLLx = ZapfDingbats.bBoxLLx;
            font.bBoxLLy = ZapfDingbats.bBoxLLy;
            font.bBoxURx = ZapfDingbats.bBoxURx;
            font.bBoxURy = ZapfDingbats.bBoxURy;
            font.underlinePosition = ZapfDingbats.underlinePosition;
            font.underlineThickness = ZapfDingbats.underlineThickness;
            font.metrics = ZapfDingbats.metrics;
            break;
        }

        return font;
    }

}
