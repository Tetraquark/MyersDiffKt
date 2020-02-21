plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    implementation("com.android.tools.build:gradle:3.5.2")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
