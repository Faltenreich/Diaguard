package com.faltenreich.diaguard.shared.data.database.importing;

import android.content.Context;
import android.content.res.AssetManager;

import com.faltenreich.diaguard.feature.export.job.csv.CsvMeta;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

interface CsvImport {

    static int getLanguageColumn(String languageCode, String[] row) {
        // Default is EN (English)
        int languageColumn = 1;

        for (int column = 0; column < row.length; column++) {
            String availableLanguageCode = row[column];
            if (languageCode.startsWith(availableLanguageCode.substring(0, 1))) {
                languageColumn = column;
                break;
            }
        }
        return languageColumn;
    }

    static CSVReader getCsvReader(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return new CSVReaderBuilder(bufferedReader)
            .withCSVParser(new CSVParserBuilder()
                .withSeparator(CsvMeta.CSV_DELIMITER)
                .build())
            .build();
    }
}
