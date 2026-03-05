plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":core:persistence"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":feature:startup"))
            }
        }
    }
}