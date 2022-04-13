plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

dependencies {
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-validation")

    implementation("org.springframework.boot", "spring-boot-starter-webflux")

    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive")
    implementation("io.github.hzpz.spring.boot:mongeez-spring-boot-starter:2.0.1")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.1")
    implementation("com.auth0:java-jwt:3.19.1")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test:3.4.16")
    testImplementation("org.springframework.security", "spring-security-test")

    developmentOnly("org.springframework.boot", "spring-boot-devtools")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
}

repositories {
    mavenCentral()
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = "17" }
    }
    test { useJUnitPlatform() }

    register<Copy>("copyFront") {
        from("../frontend-client/dist")
        into("/src/main/resources/public")
    }
}

defaultTasks("copyFront", "build")
