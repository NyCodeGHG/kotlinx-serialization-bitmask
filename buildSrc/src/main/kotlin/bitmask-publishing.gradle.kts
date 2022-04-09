plugins {
    `maven-publish`
    signing
}

publishing {
    repositories {
        val repo = if ("SNAPSHOT" in version.toString())
            "https://s01.oss.sonatype.org/content/repositories/snapshots/"
        else "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
        maven {
            setUrl(repo)
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }

    publications {
        withType<MavenPublication> {
            javadocStub(project)
            pom {
                configurePom(project)
            }
        }
    }
}

signing {
    val signingKey = findProperty("signingKey")?.toString()
    val signingPassword = findProperty("signingPassword")?.toString()
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(
            String(java.util.Base64.getDecoder().decode(signingKey.toByteArray())),
            signingPassword
        )
        publishing.publications.withType<MavenPublication> {
            sign(this)
        }
    }
}
