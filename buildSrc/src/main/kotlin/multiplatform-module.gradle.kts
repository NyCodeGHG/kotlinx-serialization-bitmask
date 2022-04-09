plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

repositories {
    mavenCentral()
}
