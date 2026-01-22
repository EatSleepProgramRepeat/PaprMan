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

        // Tab Pane set up
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab1 = new Tab("Hello, World!");
        tab1.setContent(new Label("Hello world from the first pane!"));
        Tab tab2 = new Tab("Tab Name Two");
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
        stage.setWidth(Constants.DEFAULT_STAGE_WIDTH);
        stage.setHeight(Constants.DEFAULT_STAGE_HEIGHT);
        stage.show();
    }
}