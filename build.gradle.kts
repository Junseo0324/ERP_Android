// Top-level build file where you can add configuration options common to all sub-projects/modules.
    plugins {
        id("com.android.application") version "8.4.0" apply false
        id("org.jetbrains.kotlin.android") version "1.9.0" apply false
        kotlin("kapt") version "1.9.0"

    }
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

