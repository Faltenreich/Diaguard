package com.faltenreich.diaguard.shared.networking;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Faltenreich on 09.11.2016.
 */

public class NetworkTypeDoubleAdapter extends TypeAdapter<Double> {

    private static String TAG = NetworkTypeDoubleAdapter.class.getSimpleName();

    @Override
    public Double read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            try {
                return Double.parseDouble(reader.nextString());
            } catch (NumberFormatException exception) {
                return null;
            }
        }
    }

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {
        writer.value(value);
    }
}
