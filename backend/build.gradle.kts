plugins {
    jacoco
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

    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin", "reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor")

    implementation("com.auth0:java-jwt:3.19.1")

    val openapi: String by project
    implementation("org.springdoc:springdoc-openapi-kotlin:$openapi")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:$openapi")
    implementation("org.springdoc:springdoc-openapi-security:$openapi")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.springframework.security", "spring-security-test")
    testImplementation("io.projectreactor", "reactor-test")

//    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.4.5")

    developmentOnly("org.springframework.boot", "spring-boot-devtools")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")
}

repositories { mavenLocal(); mavenCentral() }

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { val java: String by project; kotlinOptions { jvmTarget = java } }
    register<Copy>("copyFront") {
        from("../frontend/dist")
        into("/src/main/resources/public")
    }
    test { useJUnitPlatform() }
    jacocoTestReport { reports { xml.required.set(true) } }
}

defaultTasks("copyFront", "build")
