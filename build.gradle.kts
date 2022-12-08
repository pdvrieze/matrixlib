import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
    alias(libs.plugins.binaryValidator)
    `java-library`
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
//    explicitApi()
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

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaHtml"))
}


tasks.withType<DokkaTask> {
    moduleName.set(project.displayName)
    dokkaSourceSets.configureEach {
        noAndroidSdkLink.set(true)
        noJdkLink.set(false)
        includeNonPublic.set(false)
        skipEmptyPackages.set(true)
        skipDeprecated.set(true)
        perPackageOption {
            matchingRegex.set(".*\\.(impl|internal)(|\\..*)")
            suppress.set(true)
        }
        val readme = project.file(project.relativePath("src/README.md"))
        if (readme.exists() && readme.canRead()) {
            includes.from(listOf(readme))
        } else {
            logger.warn("Missing $readme for project ${project.name}")
        }

    }
}

publishing {
    repositories {
        maven {
            name = "OSS_registry"
            url = when {
                "SNAPSHOT" in version.toString().toUpperCase() ->
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

                else                                           ->
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = project.findProperty("ossrh.username") as String?
                password = project.findProperty("ossrh.password") as String?
            }
        }
    }

    publications {

        register<MavenPublication>("Jvm") {
            artifact(javadocJar)
            artifact(tasks.named("kotlinSourcesJar"))
            artifact(tasks.named(kotlin.target.artifactsTaskName))

            signing.sign(this)

            pom {
                name.set(project.displayName)
                description.set(project.description)

                url.set("https://github.com/pdvrieze/matrixlib")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("pdvrieze")
                        name.set("Paul de Vrieze")
                        email.set("pdvrieze@bournemouth.ac.uk")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/pdvrieze/matrixlib.git")
                    developerConnection.set("scm:git:https://github.com/pdvrieze/matrixlib.git")
                    url.set("https://github.com/pdvrieze/matrixlib")
                }
            }

        }
    }


}

signing {
    val priv_key: String? = System.getenv("GPG_PRIV_KEY")
    val passphrase: String? = System.getenv("GPG_PASSPHRASE")
    if (priv_key == null || passphrase == null) {
        logger.warn("No private key information found in environment. Falling back to gnupg.")
        useGpgCmd()
    } else {
        useInMemoryPgpKeys(priv_key, passphrase)
    }
    setRequired { gradle.taskGraph.run { hasTask("publish") || hasTask("publishNative") } }
}

