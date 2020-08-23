plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion
    defaultConfig {
        applicationId = "ru.tetraquark.kmplibs.myersdiffkt.sample.android"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)

        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.core:core-ktx:${Versions.coreKtx}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}")

    implementation(project(":MyersDiffKt"))
}
