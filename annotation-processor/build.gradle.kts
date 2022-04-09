plugins {
    `jvm-module`
    `ksp-module`
    `bitmask-publishing`
}

dependencies {
    implementation("com.google.devtools.ksp", "symbol-processing-api", "1.6.20-1.0.5")
    implementation("com.squareup", "kotlinpoet", "1.11.0")
    implementation("com.squareup", "kotlinpoet-ksp", "1.11.0")
    implementation(projects.annotations)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.3.2")
    testImplementation(kotlin("test-junit5"))
    kspTest(projects.annotationProcessor)
    testImplementation(projects.runtime)
    testImplementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.2")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter", "junit-jupiter-params")
}

description = "kotlinx-serialization-bitmask annotation processor which generates the serializer."

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifact(sourcesJar)
        }
    }
}
