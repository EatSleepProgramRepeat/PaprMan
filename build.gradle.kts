plugins {
    java
    id("org.openjfx.javafxplugin") version "0.0.13"
    application
}

group =  "com.PaprMan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "22.0.1"
    modules("javafx.controls", "javafx.fxml")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

application {
    mainClass.set("com.PaprMan.Main")
}
