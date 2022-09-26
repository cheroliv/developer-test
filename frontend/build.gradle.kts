@file:Suppress("GradlePackageUpdate")

plugins {
    id("com.github.node-gradle.node")
}

repositories {
    mavenCentral()
}

node {
    download.set(true)
    version.set("16.17.0")
}

tasks.register("c3po") {
    group = "application"
    description = "Run frontend locally"
    logger.warn("launch C3PO")
    finalizedBy("npm_start")
}
