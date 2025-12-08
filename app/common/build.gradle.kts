plugins {
    id("feature-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // TODO: Remove unnecessary dependencies
                implementation(project(":core:architecture"))
                implementation(project(":core:config"))
                implementation(project(":core:datetime"))
                implementation(project(":core:injection"))
                implementation(project(":core:localization"))
                implementation(project(":core:logging"))
                implementation(project(":core:network"))
                implementation(project(":core:persistence"))
                implementation(project(":core:serialization"))
                implementation(project(":core:system"))
                implementation(project(":core:view"))
                implementation(project(":data"))
                implementation(project(":feature:backup"))
                implementation(project(":feature:dashboard"))
                implementation(project(":feature:datetime"))
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
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
            }
        }
        androidInstrumentedTest {
            dependencies {
                implementation(libs.ktor.core)
            }
        }
    }
}