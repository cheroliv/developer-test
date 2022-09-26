@file:Suppress(
    "GradlePackageUpdate",
    "DEPRECATION",
)

import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import java.io.ByteArrayOutputStream

plugins {
    id("com.github.node-gradle.node")
}


tasks.register("frontend") {
    group = "application"
    description = "Run frontend locally"
}

tasks.register<Delete>("cleanResources") {
    description = "Delete directory build/resources"
    group = "build"
    delete("build/resources")
}
