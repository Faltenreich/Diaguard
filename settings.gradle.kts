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
    ":core:config:detekt",
    ":core:injection",
    ":core:localization",
    ":core:logging",
    ":core:datetime",
    ":core:network",
    ":core:permission",
    ":core:persistence",
    ":core:serialization",
    ":core:view",
)