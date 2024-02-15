# MatrixLib
[![GitHub license](https://img.shields.io/badge/License-Apache%202-blue.svg?style=flat)](COPYING)
[![Download](https://img.shields.io/maven-central/v/io.github.pdvrieze.matrixlib/matrixlib)](https://search.maven.org/artifact/io.github.pdvrieze.matrixlib/matrixlib)
![Validate Gradle Wrapper](https://github.com/pdvrieze/matrixlib/workflows/Validate%20Gradle%20Wrapper/badge.svg)

Matrixlib is a library of `Array`-like classes that are 2-dimensional rather than 1-dimensional. In
adition to the `Matrix` types 

## How to use
The library is a Kotlin only module targeting the JVM/Android.

### Add dependency
```kotlin
dependencies {
    implementation("io.github.pdvrieze.matrixlib:matrixlib:1.0.1")
}
```

### Add repository
The project's Maven access is hosted on OSS Sonatype (and available from Maven Central).

Releases can be added from **maven central**

Snapshots are available from:
```groovy
repositories {
	maven {
		url  "https://s01.oss.sonatype.org/content/repositories/snapshots/"
	}
}
```

## Documentation
The API documentation is available [here](https://pdvrieze.github.io/matrixlib/)