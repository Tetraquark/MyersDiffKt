plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("maven-publish")
}

group = "ru.tetraquark.kmplibs"
version = Versions.MyersDiffKt

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)

        versionCode = 2
        versionName = Versions.MyersDiffKt
    }

    sourceSets.forEach {
        it.manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    jvm()
    macosX64()
    ios()

    sourceSets {
        // for unit-tests purposes
        all {
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }

        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
            }
        }
        val androidMain by getting
        val jvmMain by getting
        val macosX64Main by getting
        val iosArm64Main by getting
        val iosX64Main by getting

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
            }
        }
    }
}
