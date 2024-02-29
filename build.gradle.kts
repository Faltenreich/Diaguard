import java.util.Calendar

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.moko.resources) apply false
    alias(libs.plugins.sqldelight) apply false
}

buildscript {
    dependencies {
        classpath(libs.moko.resources.plugin)
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        buildUponDefaultConfig = true
        config.setFrom("$rootDir/config/detekt.yml")
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
    val versionName = libs.versions.app.version.name.get()
    val with = "$string$versionName"
    // TODO: ant.replaceregexp(file: "${rootProject.projectDir}/README.md", match: regex, flags: 'g', replace: with)
    println("Updating version badge in README.md: $with")
}

tasks.register("applyDateToReadme") {
    dependsOn("assemble")
    val string = "2013-"
    val regex = "$string([0-9.]+)".toRegex()
    val year = Calendar.getInstance().get(Calendar.YEAR)
    val with = "$string$year"
    // TODO: ant.replaceregexp(file: "${rootProject.projectDir}/README.md", match: regex, flags: 'g', replace: with)
    println("Updating copyright timeframe in README.md: $with")
}