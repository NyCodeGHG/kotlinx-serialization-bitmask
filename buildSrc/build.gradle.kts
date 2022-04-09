plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.6.20"))
    implementation(kotlin("serialization", "1.6.20"))
    implementation("com.google.devtools.ksp", "symbol-processing-gradle-plugin", "1.6.20-1.0.5")
}
