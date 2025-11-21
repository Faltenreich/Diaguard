plugins {
    id("kotlin")
    alias(libs.plugins.detekt)
}

dependencies {
    compileOnly(libs.detekt.api)
    testImplementation(libs.detekt.test)
    testImplementation(kotlin("test"))
}