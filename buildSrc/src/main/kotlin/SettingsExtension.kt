import org.gradle.api.provider.Property

abstract class SettingsExtension {
    abstract val stubJavadocJar: Property<Boolean>

    init {
        stubJavadocJar.convention(true)
    }
}
