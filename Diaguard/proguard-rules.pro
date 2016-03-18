-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep class com.faltenreich.diaguard.data.entity.**
-keepclassmembers class com.faltenreich.diaguard.data.entity.** { *; }
-keep enum com.faltenreich.diaguard.data.entity.**
-keepclassmembers enum com.faltenreich.diaguard.data.entity.** { *; }
-keep interface com.faltenreich.diaguard.data.entity.**
-keepclassmembers interface com.faltenreich.diaguard.data.entity.** { *; }

##---------------Begin: Android Support ----------
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
##---------------End: Android Support ----------

##---------------Begin: EventBus ----------
-keepclassmembers class ** {
    public void onEvent*(**);
}
##---------------End: EventBus ----------

##---------------Begin: ButterKnife ----------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
##---------------End: ButterKnife ----------

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
##---------------End: OrmLite ----------

##---------------Begin: JodaTime ----------
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }
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