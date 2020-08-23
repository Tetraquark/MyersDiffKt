buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.android.tools.build:gradle:3.5.4")
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}
