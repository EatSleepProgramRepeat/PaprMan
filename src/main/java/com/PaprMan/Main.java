package com.PaprMan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab();
        tab1.setContent(new Label("Hello world from the first pane!"));

        Tab tab2 = new Tab();
        tab2.setContent(new Label("This is the second label."));

        tabPane.getTabs().addAll(tab1, tab2);

        Scene scene = new Scene(tabPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("PaprMan");
        stage.show();
    }
}
