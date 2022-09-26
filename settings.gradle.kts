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
        id("io.spring.dependency-management").version(extra["spring_bom.version"].toString())
        id("com.google.cloud.tools.jib").version(extra["jib.version"].toString())
        id("com.github.node-gradle.node").version(extra["gradle_node.version"].toString())
        id("com.github.andygoossens.gradle-modernizer-plugin").version(extra["modernizer.version"].toString())
        id("io.spring.nohttp").version(extra["no_http_checkstyle.version"].toString())
    }
}

rootProject.name = "developer-test"
include(":backend")
include(":frontend")