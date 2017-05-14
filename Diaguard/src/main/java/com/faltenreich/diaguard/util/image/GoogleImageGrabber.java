package com.faltenreich.diaguard.util.image;

import com.faltenreich.diaguard.util.export.Export;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class GoogleImageGrabber {

    private static final String FILE_INPUT_NAME = "W:\\Android Studio\\Diaguard\\Diaguard\\src\\main\\assets\\food_common.csv";
    private static final String FILE_OUTPUT_NAME = "W:\\Android Studio\\Diaguard\\Diaguard\\src\\main\\assets\\food_common_img.csv";
    private static final String URL_FORMAT = "https://www.google.de/search?q=%s&tbm=isch&tbs=sur:fc";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36";
    private static final String JSON_START = "rg_meta\">";
    private static final String JSON_END = "}";

    public static void main(String[] args) {
        HttpURLConnection connection = null;
        try {
            InputStream inputStream = new FileInputStream(FILE_INPUT_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            CSVReader reader = new CSVReader(bufferedReader, Export.CSV_DELIMITER);

            FileWriter fileWriter = new FileWriter(FILE_OUTPUT_NAME);
            CSVWriter writer = new CSVWriter(fileWriter, Export.CSV_DELIMITER);

            String[] lineIn = reader.readNext();
            writer.writeNext(lineIn);

            while ((lineIn = reader.readNext()) != null) {
                String name = lineIn[3].trim();
                String urlString = String.format(URL_FORMAT, URLEncoder.encode(name, "UTF-8"));
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", USER_AGENT);
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                String body = response.toString();

                int jsonStart = body.indexOf(JSON_START);
                int jsonEnd = body.indexOf(JSON_END, jsonStart);
                String json = body.substring(jsonStart + JSON_START.length(), jsonEnd + JSON_END.length());
                String imageUrl = "";
                try {
                    GoogleImageDto dto = new Gson().fromJson(json, GoogleImageDto.class);
                    imageUrl = dto.imageUrl;
                    System.out.println("Stored image for: " + name + " (" + dto.imageUrl + ")");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String[] lineOut = Arrays.copyOf(lineIn, lineIn.length + 1);
                lineOut[lineOut.length - 1] = imageUrl;
                writer.writeNext(lineOut);
            }

            reader.close();
            writer.close();

        } catch (IOException exception) {
            exception.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}