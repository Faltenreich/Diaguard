plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }
    }
}

compose.resources.publicResClass = true