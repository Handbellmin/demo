plugins {
	java
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("war")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("junit:junit:4.13.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.5")
	implementation("org.eclipse.dirigible:dirigible-database-h2:6.3.23")
	implementation("mysql:mysql-connector-java:8.0.28")
	implementation("org.springframework.boot:spring-boot-starter-security:2.7.6")
	implementation("com.auth0:java-jwt:3.10.3")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	implementation("io.jsonwebtoken:jjwt:0.9.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
