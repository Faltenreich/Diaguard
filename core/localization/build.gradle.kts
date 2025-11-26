plugins {
    id("multiplatform-convention")
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:injection"))
                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.faltenreich.diaguard.resource"
    publicResClass = true
}