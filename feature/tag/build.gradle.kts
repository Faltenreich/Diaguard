plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:navigation"))
                implementation(project(":shared"))
                implementation(libs.paging)
            }
        }
    }
}