import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.6.7" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21" apply false
    kotlin("plugin.jpa") version "1.6.21" apply false
    kotlin("plugin.allopen") version "1.5.31"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    val queryDslVer = "5.0.0"
    val modelMapperVer = "2.4.4"

    extra["log4j_version"] = "2.15.0" // kotlin-logging
    extra["log4j2.version"] = "2.15.0" // spring-boot

    apply {
        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("kotlin-spring")
        plugin("kotlin-jpa")
        plugin("kotlin-allopen")
        plugin("org.asciidoctor.jvm.convert")
    }

    allOpen {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.Embeddable")
        annotation("javax.persistence.MappedSuperclass")
    }

    group = "com.wedo"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        implementation("org.springframework.session:spring-session-core")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.github.microutils:kotlin-logging:1.7.10")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.querydsl:querydsl-jpa:$queryDslVer")
        implementation("com.querydsl:querydsl-core:$queryDslVer")
        implementation("org.modelmapper:modelmapper:$modelMapperVer")
        implementation("software.amazon.awssdk:sns:2.17.10")
        implementation("io.netty:netty-resolver-dns-native-macos:4.1.75.Final:osx-aarch_64")

        runtimeOnly("com.h2database:h2")
        runtimeOnly("mysql:mysql-connector-java")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
        testImplementation("com.ninja-squad:springmockk:3.0.1")

        kapt("com.querydsl:querydsl-apt:$queryDslVer:jpa")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("com.querydsl:querydsl-apt:$queryDslVer:jpa")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        dependsOn(tasks.ktlintCheck)
        reports.html.required.set(true)
    }

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        disabledRules.addAll("import-ordering", "no-wildcard-imports", "filename", "indent", "parameter-list-wrapping")
    }
}

project(":core") {
    dependencies {
    }

    tasks.getByName<Jar>("jar") { enabled = true }
    tasks.getByName<BootJar>("bootJar") { enabled = false }
}

project(":common") {
    dependencies {
        implementation(project(":core"))
    }

    tasks.getByName<Jar>("jar") { enabled = true }
    tasks.getByName<BootJar>("bootJar") { enabled = false }
}

project(":api") {
    dependencies {
        implementation(project(":core"))
        implementation(project(":common"))

        implementation("org.flywaydb:flyway-core")
        implementation("org.apache.commons:commons-lang3:3.11")
        implementation("org.springframework.boot:spring-boot-starter-websocket")

        // RestDocs
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

        // redis
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("org.springframework.session:spring-session-data-redis")
    }

    val snppetsDir = file("build/generated-snippets")

    tasks.test {
        outputs.dir(snppetsDir)
    }

    tasks.asciidoctor {
        inputs.dir(snppetsDir)
        dependsOn(tasks.test)
    }

    tasks.withType<BootJar> {
        archiveFileName.set("mwohalkka-api.jar")
    }

    tasks.register<Zip>("zip") {
        dependsOn("bootJar")
        archiveFileName.set("mwohalkka-api.zip")

        from("build/libs/mwohalkka-api.jar") { into("") }
        from("../.platform") { into(".platform") }
        from("../procfiles/api/Procfile") { into("") }
    }
}

project(":admin") {
    dependencies {
        implementation(project(":core"))
        implementation(project(":common"))

        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.4.1")

        implementation("org.apache.commons:commons-csv:1.9.0")

        // google otp
        implementation("commons-codec:commons-codec:1.15")
        implementation("de.taimos:totp:1.0")
        implementation("com.google.zxing:javase:3.4.1")
    }

    tasks.withType<BootJar> {
        archiveFileName.set("mwohalkka-admin.jar")
    }

    tasks.register<Zip>("zip") {
        dependsOn("bootJar")
        archiveFileName.set("mwohalkka-admin.zip")

        from("build/libs/mwohalkka-admin.jar") { into("") }
        from("../.platform") { into(".platform") }
        from("../procfiles/admin/Procfile") { into("") }
    }
}
