package com.pdfjet;

class Script {
    int defaultLangSys;     // Offset to DefaultLangSys table-from beginning of Script table-may be NULL
    int langSysCount;       // Number of LangSysRecords for this script-excluding the DefaultLangSys
    LangSysRecord[] langSysRecord;  // Array of LangSysRecords-listed alphabetically by LangSysTag
}
