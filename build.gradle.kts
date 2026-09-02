plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

extra.apply {
    set("appNamespace", "com.faltenreich.diaguard")
    set("appVersionCode", 66)
    set("appVersionName", "4.0.0")
    set("appMinSdk", 23)
    set("appTargetSdk", 37)
    set("appCompileSdk", 37)
    set("javaVersion", 21)
}