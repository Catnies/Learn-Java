plugins {
    id("java")
}

group = "top.catnies"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven("https://repo.momirealms.net/releases/") // for sparrow reflection
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    // 开发工具
    compileOnly("org.projectlombok:lombok:1.18.34") // Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.34") // Lombok
    implementation("io.netty:netty-all:4.1.119.Final")  // Netty
    implementation("net.bytebuddy:byte-buddy:1.17.5")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.5")
    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    // Required ASM dependency (keep updated to latest version)
    implementation("org.ow2.asm:asm:9.9.1")
    // Check build.gradle.kts for the latest version
    implementation("net.momirealms:sparrow-reflection:0.25")

}