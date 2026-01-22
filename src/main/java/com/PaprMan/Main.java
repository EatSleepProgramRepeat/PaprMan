package com.PaprMan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Menu Bar setup tests...
        MenuBar mainMenuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        Menu optionsMenu = new Menu("Options");

        // Exit menu configuration
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        // File menu options
        MenuItem fileOpenFolder = new MenuItem("Open Folder...");
        MenuItem fileOpenConfig = new MenuItem("Open Config File...");
        MenuItem fileSave = new MenuItem("Save");

        //ToggleGroup for view options
        ToggleGroup viewOptionToggleGroup = new ToggleGroup();

        // View menu options
        RadioMenuItem viewIconsSmall = new RadioMenuItem("Small Icons");
        RadioMenuItem viewIconsMedium = new RadioMenuItem("Medium Icons");
        RadioMenuItem viewIconsLarge = new RadioMenuItem("Large Icons");
        RadioMenuItem viewList = new RadioMenuItem("List");
        MenuItem viewHelp = new MenuItem("Help");

        // Options menu options
        MenuItem optionsSettings = new MenuItem("Settings");

        // Set toggle group
        viewIconsSmall.setToggleGroup(viewOptionToggleGroup);
        viewIconsMedium.setToggleGroup(viewOptionToggleGroup);
        viewIconsLarge.setToggleGroup(viewOptionToggleGroup);
        viewList.setToggleGroup(viewOptionToggleGroup);

        // Add things to the menus (and bar)
        fileMenu.getItems().addAll(
                fileSave, fileOpenFolder, fileOpenConfig, exitItem
        );
        // editMenu.getItems().addAll();
        viewMenu.getItems().addAll(
                viewIconsSmall, viewIconsMedium, viewIconsLarge, viewList,
                new SeparatorMenuItem(),
                viewHelp
        );
        optionsMenu.getItems().addAll(
                optionsSettings
        );
        mainMenuBar.getMenus().addAll(
                fileMenu, editMenu, viewMenu, optionsMenu
        );

        // Main image pane setup
        GridPane mainImagePane = new GridPane();
        mainImagePane.setMaxWidth(Double.MAX_VALUE);
        mainImagePane.setMaxHeight(Double.MAX_VALUE);

        // Column constraints (will be used)
        int numberOfColumns = 3;
        // Iterate over all columns to set constraints
        for (int i = 0; i < numberOfColumns; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth((double) 100 / numberOfColumns);
            mainImagePane.getColumnConstraints().add(constraints);
        }

        // DEMONSTRATION CODE
        Label label1 = new Label("This is a test");
        Label label2 = new Label("This is a test");
        Label label3 = new Label("This is a test");
        Label label4 = new Label("This is a test");
        Label label5 = new Label("This is a test");
        Label label6 = new Label("This is a test");
        GridPane.setConstraints(label1, 0, 0);
        GridPane.setConstraints(label2, 1, 0);
        GridPane.setConstraints(label3, 2, 0);
        GridPane.setConstraints(label4, 0, 1);
        GridPane.setConstraints(label5, 1, 1);
        GridPane.setConstraints(label6, 2, 1);
        mainImagePane.getChildren().addAll(label1, label2, label3, label4, label5, label6);

        BorderPane root = new BorderPane();
        root.setTop(mainMenuBar);
        root.setCenter(mainImagePane);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/style/style.css")
                ).toExternalForm())
        ;

        stage.setScene(scene);
        stage.setTitle("PaprMan " + Constants.VERSION_NUMBER);
        stage.setWidth(Constants.DEFAULT_STAGE_WIDTH);
        stage.setHeight(Constants.DEFAULT_STAGE_HEIGHT);
        stage.show();
    }
}