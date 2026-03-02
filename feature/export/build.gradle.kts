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
                implementation(project(":feature:preference"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":feature:startup"))
            }
        }
        androidMain {
            dependencies {
                implementation(project(":feature:export:pdfjet"))
                implementation("joda-time:joda-time:2.12.5")
            }
        }
    }
}