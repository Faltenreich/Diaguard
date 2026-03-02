plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:config"))
                implementation(project(":feature:datetime"))
                implementation(project(":feature:measurement"))
                implementation(project(":feature:navigation"))
                implementation(project(":feature:preference"))
                implementation(libs.kotlinx.io)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":feature:startup"))
            }
        }
    }
}