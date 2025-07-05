plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.0.12")
    implementation("org.springframework:spring-jdbc:6.0.12")
    implementation("org.springframework:spring-orm:6.0.12")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
    implementation("com.mysql:mysql-connector-j:8.0.33")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}