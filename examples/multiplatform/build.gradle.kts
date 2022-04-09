plugins {
    `multiplatform-module`
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
        commonMain {
            dependencies {
                compileOnly(projects.annotations)
                implementation(projects.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
                // kotlinx-serialization-json is not required, it's just here because that's what the readme example uses.
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
            kotlin.srcDir("build/generated/ksp/commonMain/kotlin")
        }
    }
}
