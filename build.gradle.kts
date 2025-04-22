plugins {
    id("java")

}

group = "pcd.ass02"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.github.javaparser:javaparser-core:3.25.10")

    implementation("io.vertx:vertx-core:4.5.3")
    testImplementation("io.vertx:vertx-junit5:4.5.3")
}

tasks.test {
    useJUnitPlatform()
}