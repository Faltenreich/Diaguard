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

include(
    ":androidApp",
    ":shared",
    ":config:detekt",
    ":core:architecture",
    ":core:injection",
    ":core:localization",
    ":core:logging",
    ":core:database",
    ":core:datetime",
    ":core:serialization",
    ":core:view",
)