plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:navigation"))
                implementation(project(":feature:entry"))
                implementation(libs.paging)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":feature:startup"))
            }
        }
    }
}