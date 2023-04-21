plugins {
    kotlin("multiplatform") version Versions.kotlin apply false
    kotlin("plugin.serialization") version Versions.kotlin apply false
    kotlin("android") version Versions.kotlin apply false
    id("com.android.application") version Versions.androidGradle apply false
    id("com.android.library") version Versions.androidGradle apply false
    id("org.jetbrains.compose") version Versions.compose apply false
    id("com.google.devtools.ksp") version Versions.ksp apply false
}

buildscript {
    dependencies {
        classpath(Dependencies.Moko.resourcesPlugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}