pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/snapshot")
    }
    plugins {
        kotlin("jvm").version(extra["kotlin.version"].toString())
        kotlin("plugin.serialization").version(extra["kotlin.version"].toString())
        kotlin("plugin.allopen").version(extra["kotlin.version"].toString())
        kotlin("plugin.noarg").version(extra["kotlin.version"].toString())
        kotlin("plugin.spring").version(extra["kotlin.version"].toString())
        id("org.springframework.boot").version(extra["springboot.version"].toString())
        id("io.spring.dependency-management").version(extra["spring_dependency_management.version"].toString())
    }
}

rootProject.name = "developer-test"
include(":backend")
