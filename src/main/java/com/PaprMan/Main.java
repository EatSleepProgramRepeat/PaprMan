package com.PaprMan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Menu Bar setup tests...
        MenuBar mainMenuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        // Exit menu configuration
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        // Add things to the menus (and bar)
        fileMenu.getItems().add(exitItem);
        mainMenuBar.getMenus().add(fileMenu);

        // Tab Pane set up
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab();
        tab1.setContent(new Label("Hello world from the first pane!"));
        Tab tab2 = new Tab();
        tab2.setContent(new Label("This is the second label."));
        tabPane.getTabs().addAll(tab1, tab2);

        BorderPane root = new BorderPane();
        root.setTop(mainMenuBar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/style/style.css")
                ).toExternalForm())
        ;

        stage.setScene(scene);
        stage.setTitle("PaprMan " + Constants.VERSION_NUMBER);
        stage.show();
    }
}
