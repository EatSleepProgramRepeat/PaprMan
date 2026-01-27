plugins {
    java
    id("org.openjfx.javafxplugin") version "0.0.13"
    application

    // Spotless gradle plugin
    id("com.diffplug.spotless") version "8.2.0"
}

group =  "com.PaprMan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "22.0.1"
    modules("javafx.controls", "javafx.fxml", "javafx.swing")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

application {
    mainClass.set("com.PaprMan.Main")
}

// Clean code setup
spotless {
    java {
        palantirJavaFormat()

        // Other tasks
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()

        target("src/**/*.java")
    }
}