plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-webmvc:6.0.12")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springframework:spring-context:6.0.12")
    implementation("org.springframework:spring-orm:6.0.12")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")

    implementation("org.postgresql:postgresql:42.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<War> {
    archiveFileName.set("Space-reservation-App-Spring.war")
}