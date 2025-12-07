plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:entry"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(project(":feature:statistic"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":feature:startup"))
            }
        }
    }
}