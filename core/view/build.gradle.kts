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
                implementation(project(":core:localization"))
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.runtime)
                implementation(libs.paging)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.emojipicker)
            }
        }
        all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }
    }
}

compose.resources.publicResClass = true