package com.faltenreich.diaguard.shared.data.database.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import org.joda.time.DateTime;

public class DateTimeConverter {

   @TypeConverter
   public static DateTime fromMillis(@Nullable Long millis) {
      return millis != null ? new DateTime(millis) : null;
   }

   @TypeConverter
   public static Long fromDateTime(@Nullable DateTime dateTime) {
      return dateTime != null ? dateTime.getMillis() : null;
   }
}
