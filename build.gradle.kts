plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.about.libraries) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jetbrains.kotlinx.kover")

    detekt {
        buildUponDefaultConfig = true
        config.setFrom("$rootDir/config/detekt.yml")
    }

    kover {
        reports {
            filters {
                excludes {
                    annotatedBy("androidx.compose.runtime.Composable")
                    classes("*Screen")
                }
            }
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude {
            it.file.relativeTo(projectDir).startsWith(project.layout.buildDirectory.asFile.get().relativeTo(projectDir))
        }
        reports {
            html.required.set(true)
            xml.required.set(true)
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
    val versionName = Constants.VersionName
    val with = "$string$versionName"
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