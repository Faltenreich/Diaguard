package com.pdfjet;

class ClassDefFormat1 {
    int classFormat;        // Format identifier-format = 1
    int startGlyph;         // First GlyphID of the ClassValueArray
    int glyphCount;         // Size of the ClassValueArray
    int[] classValueArray;  // Array of Class Values-one per GlyphID
}
