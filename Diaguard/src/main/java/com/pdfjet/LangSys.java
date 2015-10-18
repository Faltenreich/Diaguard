package com.pdfjet;

class LangSys {
    int lookupOrder;        // = NULL (reserved for an offset to a reordering table)
    int reqFeatureIndex;    // Index of a feature required for this language system- if no required features = 0xFFFF
    int featureCount;       // Number of FeatureIndex values for this language system-excludes the required feature
    int[] featureIndex;     // Array of indices into the FeatureList - in arbitrary order
}
