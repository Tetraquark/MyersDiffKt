plugins {
    kotlin("multiplatform")
}

group = "ru.tetraquark.myersdiffkt"
version = "1.0"

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                //implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.61")
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
    }
}
