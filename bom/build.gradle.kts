plugins {
    `java-platform`
    `bitmask-publishing`
}

val me = project
rootProject.subprojects {
    if (name != me.name) {
        me.evaluationDependsOn(path)
    }
}

settings {
    stubJavadocJar.set(false)
}

dependencies {
    constraints {
        rootProject.subprojects.forEach { subproject ->
            if (subproject.plugins.hasPlugin("maven-publish") && subproject.name != name) {
                subproject.publishing.publications.withType<MavenPublication> {
                    if (!artifactId.endsWith("-metadata") &&
                        !artifactId.endsWith("-kotlinMultiplatform")
                    ) {
                        api("$groupId:$artifactId:$version")
                    }
                }
            }
        }
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["javaPlatform"])
    }
}
