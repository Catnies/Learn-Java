plugins {
    id("java")
}

group = "top.catnies"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.momirealms.net/releases/") // for sparrow reflection
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("io.netty:netty-all:4.1.119.Final")
    implementation("net.bytebuddy:byte-buddy:1.17.5")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.5")
    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation("org.ow2.asm:asm:9.9.1")
    implementation("net.momirealms:sparrow-reflection:0.25")
}

subprojects {
    apply(plugin = "java")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        maven("https://repo.momirealms.net/releases/")
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        add("implementation", "org.jetbrains:annotations:15.0")
        add("compileOnly", "org.projectlombok:lombok:1.18.34")
        add("annotationProcessor", "org.projectlombok:lombok:1.18.34")
        add("implementation", "io.netty:netty-all:4.1.119.Final")
        add("implementation", "net.bytebuddy:byte-buddy:1.17.5")
        add("implementation", "net.bytebuddy:byte-buddy-agent:1.17.5")
        add("implementation", "org.openjdk.jmh:jmh-core:1.37")
        add("annotationProcessor", "org.openjdk.jmh:jmh-generator-annprocess:1.37")
        add("implementation", "org.ow2.asm:asm:9.9.1")
        add("implementation", "net.momirealms:sparrow-reflection:0.25")
    }
}
