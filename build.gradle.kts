import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    `maven-publish`
    signing
}

description = "Support library for working with 2D matrices. This includes matrices with gaps."

group = "io.github.pdvrieze.matrixlib"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    target {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        mavenPublication {
            version = project.version as String
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
