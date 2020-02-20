plugins {
    kotlin("multiplatform")
}

group = "ru.tetraquark.myersdiffkt"
version = "1.0"

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        // for unit-tests purposes
        all {
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))

            }
        }
    }
}
