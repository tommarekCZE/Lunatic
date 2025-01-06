import xyz.jpenilla.runpaper.task.RunServer
import java.net.URI

plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "org.evlis"
version = "1.2.0"

val targetJavaVersion = 21

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = URI("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = URI("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "aikars-framework"
        url = URI("https://repo.aikar.co/content/groups/aikar/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:4.18.0")

    testImplementation("com.github.seeseemelk:MockBukkit-v1.21:3.133.1")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.21:3.133.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

java {
    sourceCompatibility = JavaVersion.toVersion(targetJavaVersion)
    targetCompatibility = JavaVersion.toVersion(targetJavaVersion)
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

tasks.withType<JavaCompile>().all {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
    options.isFork = true
    options.release.set(targetJavaVersion)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    build { dependsOn(shadowJar) }
    shadowJar {
        relocate("co.aikar.commands", "Lunamatic.acf")
        relocate("co.aikar.locales", "Lunamatic.locales")
    }
    test {
        useJUnitPlatform()
        testLogging { events("passed", "skipped", "failed") }
    }
    runServer {
        // Keep runServer task to inherit project plugin
        minecraftVersion("1.21.1")
    }
}

// Test Paper run & immediately shut down, for github actions
tasks.register<RunServer>("runServerTest") {
    dependsOn(tasks.shadowJar)
    minecraftVersion("1.21.1")
    downloadPlugins {
        github("Ifiht", "AutoStop", "v1.2.0", "AutoStop-1.2.0.jar")
    }
    pluginJars.from(tasks.shadowJar)
}
// Start a local test server for login & manual testing
tasks.register<RunServer>("runServerInteractive") {
    dependsOn(tasks.shadowJar)
    minecraftVersion("1.21.1")
    pluginJars.from(tasks.shadowJar)
}

// Start a local Folia server for manual testing
runPaper.folia.registerTask {
    minecraftVersion("1.20.6")
}

tasks.register("checkServerLogs") {
    doLast {
        // Path to the latest.log file
        val logFile = File("run/logs/latest.log")

        // Check if the log file exists
        if (!logFile.exists()) {
            throw GradleException("Log file not found: " + logFile.absolutePath)
        }

        // Read the log file line by line
        val logContent = logFile.readLines()

        // Find lines that contain the " ERROR]:" substring
        val errorLines = logContent.filter { it.contains("ERROR]:") }

        if (!errorLines.isEmpty()) {
            println("Errors were found:")
            errorLines.forEach(::println)
            throw GradleException("Errors found in log file.")
        } else {
            println("No errors found in log file.")
        }
    }
}