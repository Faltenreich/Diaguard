plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:system"))
                implementation(project(":feature:backup"))
                implementation(project(":feature:dashboard"))
                implementation(project(":feature:entry"))
                implementation(project(":feature:export"))
                implementation(project(":feature:food"))
                implementation(project(":feature:log"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(project(":feature:startup"))
                implementation(project(":feature:statistic"))
                implementation(project(":feature:tag"))
                implementation(project(":feature:timeline"))
            }
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.ktor.core)
            }
        }
    }
}