import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Jar

fun MavenPom.configurePom(project: Project) {
    name.set(project.name)
    description.set(project.description
        ?: "Generate kotlinx.serialization serializers for serializing booleans as bitmasks")
    url.set("https://github.com/NyCodeGHG/kotlinx-serialization-bitmask")

    licenses {
        license {
            name.set("Apache-2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0")
        }
    }
    developers {
        developer {
            name.set("NyCode")
            email.set("me@nycode.dev")
            url.set("https://nycode.dev")
        }
    }
    scm {
        connection.set("scm:git:https://github.com/NyCodeGHG/kotlinx-serialization-bitmask")
        developerConnection.set("scm:git:https://github.com/NyCodeGHG/kotlinx-serialization-bitmask")
        url.set("https://github.com/NyCodeGHG/kotlinx-serialization-bitmask")
    }
}

fun MavenPublication.javadocStub(project: Project) {
    val javadocJar = project.tasks.findByName("javadocJar") ?: project.tasks.create("javadocJar",
        Jar::class.java) {
        archiveClassifier.set("javadoc")
    }
    artifact(javadocJar)
}
