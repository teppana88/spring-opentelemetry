import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
	war
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "fi.awave"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// These are basic Spring Boot dependencies
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// These are opentelemetry dependencies
	// For Spring Boot native - OpenTelemetry core instrumentation for Spring Boot
	implementation(platform(SpringBootPlugin.BOM_COORDINATES))
	implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.10.0"))
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:2.10.0")
	// Enable metrics
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	// enable tracing bridge so that traceID is propagated
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	// DB traces for monitoring (not used in this example but if you want to monitor DB)
//	implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.5")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
