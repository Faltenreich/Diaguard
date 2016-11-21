package com.faltenreich.diaguard.networking;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Faltenreich on 09.11.2016.
 */

public class NetworkTypeFloatAdapter extends TypeAdapter<Float> {

    private static String TAG = NetworkTypeFloatAdapter.class.getSimpleName();

    @Override
    public Float read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            try {
                return Float.parseFloat(reader.nextString());
            } catch (NumberFormatException exception) {
                return null;
            }
        }
    }

    @Override
    public void write(JsonWriter writer, Float value) throws IOException {
        writer.value(value);
    }
}
