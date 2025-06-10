plugins {
    id("java")
}

group = "top.catnies"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    // 开发工具
    compileOnly("org.projectlombok:lombok:1.18.34") // Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.34") // Lombok
    implementation("io.netty:netty-all:4.1.119.Final")  // Netty
}