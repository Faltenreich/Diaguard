package com.faltenreich.diaguard.shared.data.serialization;

import android.util.Log;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;

import java.util.ArrayList;
import java.util.List;

public class CategorySerializer implements Serializer<Category[], String> {

    private static final String TAG = Measurement.class.getSimpleName();

    @Override
    @Nullable
    public String serialize(Category[] categories) {
        if (categories != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Category category : categories) {
                stringBuilder.append(category.getStableId());
                if (category != categories[categories.length - 1]) {
                    stringBuilder.append(';');
                }
            }
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Category[] deserialize(String string) {
        if (string != null) {
            String[] ids = string.split(";");
            List<Category> categories = new ArrayList<>();
            for (String id : ids) {
                try {
                    int stableId = Integer.parseInt(id);
                    Category category = Category.fromStableId(stableId);
                    if (category != null) {
                        categories.add(category);
                    }
                } catch (NumberFormatException exception) {
                    Log.e(TAG, exception.getMessage());
                }
            }
            return categories.toArray(new Category[categories.size()]);
        } else {
            return null;
        }
    }
}
