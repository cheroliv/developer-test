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
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")
    kotlin("plugin.serialization")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap/")
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}

dependencies {
//    implementation(project(path = ":common"))
    //Kotlin lib: jdk8, reflexion, coroutines
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${properties["kotlinx_serialization_json.version"]}")
    // kotlin TDD
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${properties["mockito_kotlin_version"]}")

    //jackson mapping (json/xml)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    //strings manipulation
    implementation("org.apache.commons:commons-lang3")
    //Http Request Exception to Problem Response
    implementation("org.zalando:problem-spring-webflux:${properties["zalando_problem.version"]}")
    //spring conf
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    //spring dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    //spring actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //spring r2dbc
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    //Spring bean validation JSR 303
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //spring webflux reactive http
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    //H2database
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    //Postgresql
//    runtimeOnly("io.r2dbc:r2dbc-postgresql")
//    runtimeOnly("org.postgresql:postgresql")
    //SSL
    implementation("io.netty:netty-tcnative-boringssl-static:${properties["boring_ssl.version"]}")
    // spring Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") { exclude(module = "mockito-core") }
    // Mocking
    testImplementation("io.mockk:mockk:${properties["mockk.version"]}")
    testImplementation("com.github.tomakehurst:wiremock-jre8:${properties["wiremock.version"]}")
    testImplementation("com.ninja-squad:springmockk:3.1.0")
}

configurations {
    compileOnly { extendsFrom(configurations.annotationProcessor.get()) }
    implementation.configure {
        setOf(
            "org.junit.vintage" to "junit-vintage-engine",
            "org.springframework.boot" to "spring-boot-starter-tomcat",
            "org.apache.tomcat" to null
        ).map { exclude(it.first, it.second) }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(properties["free_compiler_args_value"].toString())
        jvmTarget = VERSION_1_8.toString()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging { events(FAILED, SKIPPED) }
    reports {
        html.isEnabled = true
        ignoreFailures = true
    }
}


tasks.register<Delete>("cleanResources") {
    description = "Delete directory build/resources"
    group = "build"
    delete("build/resources")
}



tasks.register<TestReport>("testReport") {
    description = "Generates an HTML test report from the results of testReport task."
    group = "report"
    destinationDir = file("$buildDir/reports/tests")
    reportOn("test")
}

//Google App Engine command line call
open class DeployGAE : Exec() {
    init {
        workingDir = project.rootDir
        this.commandLine(
            "/snap/bin/gcloud",
            "-v"
//            "app",
//            "deploy",
//            "${projectDir.absolutePath}/src/main/appengine/app.yml"
        )
        standardOutput = ByteArrayOutputStream()
    }
}


tasks.register<DeployGAE>("deployGAE") {
    group = "application"
    val cmd = "gcloud app deploy src/main/appengine/app.flexible.yml"
    doLast { println(cmd) }
}