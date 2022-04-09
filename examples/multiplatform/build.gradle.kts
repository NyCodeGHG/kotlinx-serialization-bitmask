plugins {
    `multiplatform-module`
    kotlin("plugin.serialization")
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
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                compileOnly(projects.annotations)
                implementation(projects.runtime)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
                // kotlinx-serialization-json is not required. it's just here because that's what the readme example uses.
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
    }
}

dependencies {
    kspCommonMainMetadata(projects.annotationProcessor)
}
