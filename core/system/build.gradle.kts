plugins {
    id("multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:injection"))
                implementation(project(":core:logging"))
            }
        }
    }
}