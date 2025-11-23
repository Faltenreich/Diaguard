plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
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

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")

    kover {
        reports {
            filters {
                excludes {
                    annotatedBy("androidx.compose.runtime.Composable")
                    classes("*.*ComposableSingletons*")
                    classes("*Screen*")
                    classes("diaguard.shared.generated.resources.*")
                }
            }
        }
    }

}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("applyVersionToReadme") {
    dependsOn("assemble")
    val string = "img.shields.io/badge/Release-"
    val regex = "$string([0-9.]+)".toRegex()
    val with = "$string$appVersionName"
    val file = file("README.md")
    file.writeText(file.readText().replace(regex, with))
    println("Updating version badge in README.md: $with")
}

tasks.register("applyDateToReadme") {
    dependsOn("assemble")
    val string = "2013-"
    val regex = "$string([0-9.]+)".toRegex()
    val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val with = "$string$year"
    val file = file("README.md")
    file.writeText(file.readText().replace(regex, with))
    println("Updating copyright timeframe in README.md: $with")
}