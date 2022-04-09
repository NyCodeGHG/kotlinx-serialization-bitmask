# kotlinx-serialization-bitmask

Kotlin tooling for
generating [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) serializers for
serializing a class as a bitmask.

## Example

```kotlin
@SerializableBitmask
@Serializable(with = UnixPermissionSerializer::class)
data class UnixPermission(
    val read: Boolean,
    val write: Boolean,
    val execute: Boolean
)
```

```kotlin
val permission = UnixPermission(read = true, write = true, execute = true)
println(Json.encodeToString(permission)) // 7
```

## Setup

kotlinx-serialization-bitmask uses [ksp](https://github.com/google/ksp) for annotation processing.

### JVM Only

```kotlin
plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    id("com.google.devtools.ksp") version "1.6.20-1.0.5"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("dev.nycode.kotlinx-serialization-bitmask:bom:1.0.0"))
    compileOnly("dev.nycode.kotlinx-serialization-bitmask", "annotations")
    implementation("dev.nycode.kotlinx-serialization-bitmask", "runtime")
    ksp("dev.nycode.kotlinx-serialization-bitmask", "annotation-processor")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.3.2")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
        test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}
```

### Multiplatform

KSP needs some more configuration to work correctly on multiplatform projects. You need to use the
correct configuration to apply the processor to the correct source set.

| Name       | Source Set | Configuration         |
|------------|------------|-----------------------|
| Common[^1] | commonMain | kspCommonMainMetadata |
| JVM        | jvmMain    | kspJvm                |
| JS         | jsMain     | kspJs                 |
| Other      | other      | kspOther              |

[^1]: (Only works if multiple source sets are defined):

```kotlin
plugins {
    kotlin("multiplatform") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    id("com.google.devtools.ksp") version "1.6.20-1.0.5"
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(BOTH) {
        browser()
        nodejs()
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
        commonMain {
            dependencies {
                compileOnly("dev.nycode.kotlinx-serialization-bitmask:annotations:1.0.0")
                implementation("dev.nycode.kotlinx-serialization-bitmask:runtime:1.0.0")
                ksp("dev.nycode.kotlinx-serialization-bitmask:annotation-processor:1.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
    }
}

dependencies {
    kspCommonMainMetadata("dev.nycode.kotlinx-serialization-bitmask",
        "annotation-processor",
        "1.0.0")
}
```
