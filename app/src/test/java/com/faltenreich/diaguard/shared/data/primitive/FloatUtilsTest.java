package com.faltenreich.diaguard.shared.data.primitive;

import org.junit.Assert;
import org.junit.Test;

public class FloatUtilsTest {

   @Test
   public void formatsFloatWhenRoundingDown() {
      int scale = 3;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale);
      Assert.assertEquals("0,123", output);
   }

   @Test
   public void formatsFloatWhenRoundingUp() {
      int scale = 6;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale);
      Assert.assertEquals("0,123457", output);
   }

   @Test
   public void formatsFloatWithLargeScale() {
      int scale = 5;
      float input = 0.123f;
      String output = FloatUtils.parseFloat(input, scale);
      Assert.assertEquals("0,123", output);
   }
}
