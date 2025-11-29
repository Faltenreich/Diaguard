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
    ":app:common",
    ":app:android",
    ":shared",

    ":feature:dashboard",
    ":feature:datetime",
    ":feature:navigation",
    ":feature:preference",
    ":feature:startup",
    ":feature:statistic",
    
    ":data",

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
    ":core:test",
    ":core:view",
)