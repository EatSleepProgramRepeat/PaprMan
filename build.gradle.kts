plugins {
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
}

group =  "com.PaprMan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "25.0.2"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.PaprMan.Main")
}