package com.faltenreich.diaguard.shared.data.primitive;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class FloatUtilsTest {

   @Test
   public void formatsFloatForScaleEqualsZero() {
      int scale = 0;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0", output);
   }

   @Test
   public void formatsFloatForScaleEqualsOne() {
      int scale = 1;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0.1", output);
   }

   @Test
   public void formatsFloatForScaleEqualsTwo() {
      int scale = 2;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0.12", output);
   }

   @Test
   public void formatsFloatForScaleEqualsThree() {
      int scale = 3;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0.123", output);
   }

   @Test
   public void formatsFloatForEnglish() {
      int scale = 1;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.ENGLISH);
      Assert.assertEquals("0.1", output);
   }

   @Test
   public void formatsFloatForGerman() {
      int scale = 1;
      float input = 0.123456789f;
      String output = FloatUtils.parseFloat(input, scale, Locale.GERMANY);
      Assert.assertEquals("0,1", output);
   }

   @Test
   public void formatsFloatWhenRoundingDown() {
      int scale = 1;
      float input = 0.54f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0.5", output);
   }

   @Test
   public void formatsFloatWhenRoundingUp() {
      int scale = 1;
      float input = 0.55f;
      String output = FloatUtils.parseFloat(input, scale, Locale.US);
      Assert.assertEquals("0.6", output);
   }
}
