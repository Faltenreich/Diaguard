plugins {
    id("multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":core:injection"))
                implementation(project(":data"))
                implementation(kotlin("test"))
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
    }
}