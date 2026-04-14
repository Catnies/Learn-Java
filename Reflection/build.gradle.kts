plugins {
    id("java")
}

group = "top.catnies"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://repo.momirealms.net/releases/") // for sparrow reflection
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation("org.ow2.asm:asm:9.9.1")
    implementation("net.momirealms:sparrow-reflection:0.25")
}
