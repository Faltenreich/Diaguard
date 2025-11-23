val libs = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/core/quality/detekt.yml")
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

dependencies {
    detektPlugins(project(":core:quality"))
    detektPlugins(libs.detekt.compose)
}