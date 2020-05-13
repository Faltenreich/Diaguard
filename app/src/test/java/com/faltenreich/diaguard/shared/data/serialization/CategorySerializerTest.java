package com.faltenreich.diaguard.shared.data.serialization;

import com.faltenreich.diaguard.shared.data.database.entity.Category;

import org.junit.Assert;
import org.junit.Test;

public class CategorySerializerTest {

    private Category[] expectedInput = new Category[] {
        Category.BLOODSUGAR,
        Category.INSULIN,
        Category.MEAL,
        Category.ACTIVITY,
        Category.HBA1C,
        Category.WEIGHT,
        Category.PULSE,
        Category.PRESSURE,
        Category.OXYGEN_SATURATION
    };
    private String expectedOutput = "1;2;3;4;5;6;7;8;9";

    @Test
    public void serializes() {
        String output = new CategorySerializer().serialize(expectedInput);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void deserializes() {
        Category[] input = new CategorySerializer().deserialize(expectedOutput);
        Assert.assertArrayEquals(expectedInput, input);
    }
}