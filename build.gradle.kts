import kotlinx.validation.ExperimentalBCVApi
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlinJvm)
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
    alias(libs.plugins.binaryValidator)
}

description = "Support library for working with 2D matrices. This includes matrices with gaps."

group = "io.github.pdvrieze.matrixlib"
version = "1.0.3"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
        freeCompilerArgs.add("-Xjvm-default=all")
    }
    target {
        mavenPublication {
            version = project.version as String
        }
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaJavadoc"))
}

apiValidation {
    @OptIn(ExperimentalBCVApi::class)
    klib {
        enabled = true
    }
}

tasks.withType<DokkaTask> {
    moduleName.set("MatrixLib")
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

val repositoryDir = project.layout.buildDirectory.dir("project-local-repository")

publishing {
    repositories {
        when {
            "SNAPSHOT" in version.toString().uppercase() -> maven {
                name = "OSS_registry"
                url = uri("https://central.sonatype.com/repository/maven-snapshots/")
                credentials {
                    username = project.findProperty("ossrh.username") as String?
                    password = project.findProperty("ossrh.password") as String?
                }

            }

            else -> maven {
                name ="projectLocal"
                url = uri(repositoryDir.map { it.asFile.toURI() })
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
    val privKey: String? = System.getenv("GPG_PRIV_KEY")
    val passphrase: String? = System.getenv("GPG_PASSPHRASE")
    if (privKey == null || passphrase == null) {
        logger.warn("No private key information found in environment. Falling back to gnupg.")
        useGpgCmd()
    } else {
        useInMemoryPgpKeys(privKey, passphrase)
    }
    setRequired { gradle.taskGraph.run { hasTask("publish") || hasTask("publishNative") } }
}

val repositoryArchive = tasks.register<Zip>("createRepositoryArchive") {
    group = PublishingPlugin.PUBLISH_TASK_GROUP
    description = "Create zip file for repository"
    destinationDirectory = project.layout.buildDirectory.dir("repositoryArchive")
    archiveBaseName = "${project.name}-${project.version}-publishing"
}

tasks.withType<PublishToMavenRepository> {
    if (isEnabled) {
        if(repository?.name == "projectLocal") {
            if(repositoryDir.isPresent) {
                repositoryDir.get().asFile.deleteRecursively()
            }

            val publishTask = this

            repositoryArchive.configure {
                dependsOn(publishTask)
                from(repositoryDir)
            }
        }
    }
}
