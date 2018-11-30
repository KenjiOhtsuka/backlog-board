import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

group = "com.improve_future"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.3.0"
    extra["kotlin_version"] = kotlinVersion

    repositories {
        jcenter()
        mavenCentral()
        maven("https://repo.spring.io/milestone")
        maven("http://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://jcenter.bintray.com")
    }
    dependencies {
        classpath(
            "org.springframework.boot:spring-boot-gradle-plugin:2.1.0" +
                ".RELEASE")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-reflect")
        classpath("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
}

plugins {
    //id("org.jetbrains.kotlin.jvm") version "1.3.0-rc-116"
    //id("org.jetbrains.kotlin.jvm") version "1.3.0-rc-57"
    kotlin("jvm") version "1.3.0"
    id("org.springframework.boot") version "2.1.0.RELEASE"
}

apply {
    //kotlin("jvm") version "1.2.61"
    plugin("io.spring.dependency-management")
    plugin("kotlin")
    plugin("kotlin-spring")
}
springBoot {
    //isExecutable = true
    //executable = true
}


repositories {
    mavenCentral()
    jcenter()
    maven("https://repo.spring.io/milestone")
    maven("http://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://jcenter.bintray.com")
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
}

dependencies {
    val kotlinVersion = extra["kotlin_version"] as String
    implementation(kotlin("stdlib-jdk8"))
    testCompile("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    //compile("org.springframework.boot:spring-boot-starter-webflux")
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20160810")

    val kotlinxHtmlVersion = "0.6.11"
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.nulab-inc:backlog4j:2.2.0")
}

tasks.withType<KotlinCompile>(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType(org.gradle.language.jvm.tasks.ProcessResources::class.java) {
    this.filter(
            mapOf(
                    "tokens" to mapOf(
                            "activeProfile" to (project.properties["activeProfile"] ?: "default")
                    )
            ),
            org.apache.tools.ant.filters.ReplaceTokens::class.java
    )
}
