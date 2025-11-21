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
    ":core:architecture",
    ":core:config",
    ":core:injection",
    ":core:localization",
    ":core:logging",
    ":core:datetime",
    ":core:network",
    ":core:persistence",
    ":core:quality",
    ":core:serialization",
    ":core:system",
    ":core:view",
)