package com.faltenreich.diaguard.networking;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Faltenreich on 09.11.2016.
 */

public class NetworkTypeIntegerAdapter extends TypeAdapter<Integer> {

    private static String TAG = NetworkTypeIntegerAdapter.class.getSimpleName();

    @Override
    public Integer read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else {
            try {
                return Integer.parseInt(reader.nextString());
            } catch (NumberFormatException exception) {
                return null;
            }
        }
    }

    @Override
    public void write(JsonWriter writer, Integer value) throws IOException {
        writer.value(value);
    }
}
