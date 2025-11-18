@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    includeBuild("plugin")
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "diaguard"

include(":androidApp")
include(":shared")
include(":config:detekt")
include(":core:core")
include(":core:database")
include(":core:datetime")