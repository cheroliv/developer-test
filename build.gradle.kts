buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${properties["kotlin.version"]}")
    }
}

group = properties["artifact.group"].toString()
version = properties["artifact.version"].toString()

allprojects { repositories { mavenCentral() } }