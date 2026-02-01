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

val appNamespace by extra("com.faltenreich.diaguard")
val appVersionCode by extra(66)
val appVersionName by extra("4.0.0")
val appMinSdk by extra(23)
val appTargetSdk by extra(36)
val appCompileSdk by extra(36)
val javaVersion by extra(21)

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}