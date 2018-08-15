import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.gradle.plugin.*
import org.gradle.script.lang.kotlin.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

group = "com.improve_future"
version = "1.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.2.51"
    extra["kotlin_version"] = kotlinVersion

    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.0.3" +
                ".RELEASE")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-reflect")
        classpath("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    }
}

plugins {
    kotlin("jvm") version "1.2.51"
    id("org.springframework.boot") version "2.0.3.RELEASE"
}

apply {
    //kotlin("jvm") version "1.2.51"
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
}

dependencies {
    val kotlinVersion = extra["kotlin_version"] as String
    compile(kotlin("stdlib-jdk8"))
    testCompile("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-security")
    // https://mvnrepository.com/artifact/org.json/json
    compile("org.json:json:20160810")
    compile("org.springframework.boot:spring-boot-starter-web")

    val kotlinxHtmlVersion = "0.6.11"
    compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
    compile("org.jetbrains.kotlin:kotlin-reflect")

    compile("com.nulab-inc:backlog4j:2.2.0")
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
