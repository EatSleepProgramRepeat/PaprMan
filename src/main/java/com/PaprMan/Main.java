package com.PaprMan;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Main extends Application {
    private final ImageProcessor imageProcessor = new ImageProcessor();

    private File selectedFile;

    @SuppressWarnings("unused")
    @Override
    public void start(Stage stage) throws Exception {
        // Menu Bar setup tests...
        MenuBar mainMenuBar = new MenuBar();
        mainMenuBar.setUseSystemMenuBar(false);     // Fix for Linux systems (specifically Wayland)
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
        viewIconsMedium.setSelected(true);

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

        // BorderPane root configuration
        BorderPane root = new BorderPane();
        root.setTop(mainMenuBar);
        root.setCenter(mainImagePane);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/style/style.css")
                ).toExternalForm())
        ;

        // Fix for Linux systems
        if (!Constants.OS_NAME.contains("win")) {
            stage.initStyle(StageStyle.UTILITY);
        }

        if (!Constants.OS_NAME.matches(".*(nix|nux).*")) {
            showAlert("Warning: Your computer may not work with this program. This program is specifically designed for Linux computers with Hyprpaper. You may continue, but it is not recommended.", "Warning");
        }

        // #######################
        // ### EVENT LISTENERS ###
        // #######################

        // File menu options event listeners
        // Open Folder...
        fileOpenFolder.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select a Wallpaper Folder...");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            selectedFile = directoryChooser.showDialog(stage);

            if (selectedFile != null) {
                // Check for any image files
                imageProcessor.setImageDirectory(selectedFile);
                if (imageProcessor.imagesPresent()) {
                    try (Stream<Path> stream = Files.list(selectedFile.toPath())) {
                        // Match all image files for list
                        List<Path> images = stream.filter(
                                p -> p.getFileName()
                                        .toString()
                                        .toLowerCase()
                                        .matches(".*\\.(png|jpg|jpeg|gif)$"))
                                .toList();
                        BufferedImage[] thumbnails = new BufferedImage[0];
                        try {
                            thumbnails = imageProcessor.generateLowResolutionImages(images.toArray(new Path[0]));
                        } catch (IOException ex) {
                            showAlert("There was an error loading one of the images. Here's more info:" + ex.getLocalizedMessage(), "Critical Error");
                        }
                        int row = 0; int col = 0;
                        for (BufferedImage b : thumbnails) {
                            if (col > 2) {row++; col=0;}
                            WritableImage wr = new WritableImage(b.getWidth(), b.getHeight());
                            SwingFXUtils.toFXImage(b, wr);
                            ImageView imageView = new ImageView(wr);
                            GridPane.setConstraints(imageView, col, row);
                            mainImagePane.getChildren().add(imageView);
                            col++;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                } else {
                    showAlert(
                            "There were no image files found in the directory you selected.",
                            "Error"
                    );
                }
            }

        });

        // View menu options event listeners
        // Small Icons
        viewIconsSmall.setOnAction(e -> updateColumnConstraints(4, mainImagePane));

        // Medium Icons
        viewIconsMedium.setOnAction(e -> updateColumnConstraints(3, mainImagePane));

        // Large Icons
        viewIconsLarge.setOnAction(e -> updateColumnConstraints(2, mainImagePane));

        updateColumnConstraints(3, mainImagePane);

        stage.setScene(scene);
        stage.setTitle("PaprMan " + Constants.VERSION_NUMBER);
        stage.setWidth(Constants.DEFAULT_STAGE_WIDTH);
        stage.setHeight(Constants.DEFAULT_STAGE_HEIGHT);
        stage.show();
    }

    private void updateColumnConstraints(int c , GridPane p) {
        // Iterate over all columns to set constraints
        for (int i = 0; i < c; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth((double) 100 / c);
            p.getColumnConstraints().removeAll();
            p.getColumnConstraints().add(constraints);
        }
    }

    private void showAlert(String bt, String tt) {
        Dialog<Boolean> dialog = new Dialog<>();
        ButtonType buttonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.setTitle(tt);
        dialog.getDialogPane().getButtonTypes().add(buttonType);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm());
        dialog.initStyle(StageStyle.UTILITY);


        Label information = new Label(bt);
        dialog.getDialogPane().setContent(information);

        dialog.show();
    }
}