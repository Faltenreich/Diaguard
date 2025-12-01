plugins {
    id("multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain"))
            }
        }
    }
}