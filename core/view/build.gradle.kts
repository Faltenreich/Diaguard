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
                implementation(libs.compose.ui)
                implementation(libs.compose.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.preview)
                implementation(libs.compose.runtime)
                implementation(libs.paging)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.android.emojipicker)
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