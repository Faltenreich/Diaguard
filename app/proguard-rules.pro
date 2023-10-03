-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

##---------------Begin: Custom ----------
-keep class com.faltenreich.diaguard.feature.preference.**
-keep class com.faltenreich.diaguard.shared.data.database.entity.**
-keepclassmembers class com.faltenreich.diaguard.shared.data.database.entity.** { *; }
-keep enum com.faltenreich.diaguard.shared.data.database.entity.**
-keepclassmembers enum com.faltenreich.diaguard.shared.data.database.entity.** { *; }
-keep interface com.faltenreich.diaguard.shared.data.database.entity.**
-keepclassmembers interface com.faltenreich.diaguard.shared.data.database.entity.** { *; }
-keep class com.faltenreich.diaguard.feature.**
##---------------End: Custom ----------

##---------------Begin: Test ----------
-keep class androidx.test.runner.**
-keepclassmembers class androidx.test.runner.** { *; }
##---------------End: Test ----------

##---------------Begin: EventBus ----------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
##---------------End: EventBus ----------

##---------------Begin: OrmLite ----------
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keepclassmembers class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
  public <init>(android.content.Context);
}
-keepclassmembers class **DateTime {
    <init>(long);
    long getMillis();
}
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.logger.**
-dontwarn com.j256.ormlite.misc.**
##---------------End: OrmLite ----------

##---------------Begin: JodaTime ----------
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
-keep class net.danlew.** { *; }
##---------------End: JodaTime ----------

##---------------Begin: Picasso ----------
-dontwarn com.squareup.okhttp.**
##---------------End: Picasso ----------

##---------------Begin: MPAndroidChart ----------
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**
##---------------End: MPAndroidChart ----------

##---------------Begin: ParallaxEverywhere ----------
-dontwarn com.fmsirvent.ParallaxEverywhere.**
##---------------End: ParallaxEverywhere ----------

##---------------Begin: OpenCsv ----------
-dontwarn com.opencsv.**
##---------------End: OpenCsv ----------

##---------------Begin: Retrofit ----------
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**

-keepattributes *Annotation*,Signature
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-dontwarn rx.**
-dontwarn com.squareup.okhttp.*
-dontwarn retrofit.appengine.**

-keep class retrofit2.** { *; }
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-keep class com.faltenreich.diaguard.feature.food.networking.dto.**
-keepclassmembers class com.faltenreich.diaguard.feature.food.networking.** {
  public void set*(***);
  public *** get*();
  public *** is*();
}
-keepclassmembers class com.faltenreich.diaguard.feature.food.networking.dto.** {
      public <fields>;
}
##---------------End: Retrofit ----------

##---------------Begin: Gson ----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
##---------------End: Gson ----------

##---------------Begin: Apache Commons ----------
-dontnote org.apache.commons.lang3.ObjectUtils
-dontwarn org.apache.**
##---------------End: Apache Commons ----------

##---------------Begin: AboutLibraries ----------
-keep class **.R$* {
    <fields>;
}
##---------------End: AboutLibraries ----------