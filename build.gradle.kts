buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.6.RELEASE")
    }
}

plugins {
    idea
    java
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.springframework.boot") version "2.1.6.RELEASE"
}

group = "org.stefata"
version = "1.0-SNAPSHOT"

tasks {
    bootJar {
        archiveBaseName.set(rootProject.name)
        archiveVersion.set(version)
    }
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.projectlombok:lombok")
    implementation("com.google.guava:guava:28.0-jre")
    implementation("commons-io:commons-io:2.6")
    implementation("org.jsoup:jsoup:1.12.1")
    implementation("com.pivovarit:throwing-function:1.4")
    implementation("de.jkeylockmanager:jkeylockmanager:2.1.0")
    implementation("com.turn:ttorrent-core:1.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntime("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.awaitility:awaitility:3.1.6")
    testImplementation("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}