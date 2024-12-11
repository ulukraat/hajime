plugins {
    id("java")
}

group = "org.ulukraat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources> {
    doLast {
        filesMatching("**/*.properties") {
            filter { line -> line.toString().replace("charset=ISO-8859-1", "charset=UTF-8") }
        }
    }
}




