package com.faltenreich.diaguard.shared.data.database.entity;

import androidx.annotation.StringRes;

import com.faltenreich.diaguard.R;

public enum FoodType {

   COMMON,
   BRANDED,
   CUSTOM,
   ;

   @StringRes
   public int getLabelResource() {
      switch (this) {
         case COMMON: return R.string.food_common;
         case BRANDED: return R.string.food_branded;
         case CUSTOM: return R.string.food_custom;
         default: throw new IllegalArgumentException("Missing label for " + this);
      }
   }
}
