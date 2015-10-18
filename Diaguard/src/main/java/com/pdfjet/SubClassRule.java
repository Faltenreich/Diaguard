package com.pdfjet;

class SubClassRule {
    int glyphCount;     // Total number of classes specified for the context in the rule - includes the first class
    int substCount;     // Number of SubstLookupRecords
    // Array of classes - beginning with the second class - to be matched to the input glyph class sequence
    int[/* glyphCount - 1 */] classArray;
    SubstLookupRecord[/* substCount */] substLookupRecord;
}
