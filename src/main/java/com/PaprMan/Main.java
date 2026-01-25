package com.PaprMan;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Main extends Application {
    private final ImageProcessor imageProcessor = new ImageProcessor(this);
    private File selectedFile;
    private int selectedHeight = Constants.MEDIUM_ICON_IMAGE_HEIGHT;

    private VBox mainImagePane = new VBox();

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
        mainImagePane = new VBox();

        // ScrollPane setup
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainImagePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-viewport-background: transparent;"
        );
        System.out.println(scrollPane.getStyle());

        // BorderPane root configuration
        BorderPane root = new BorderPane();
        root.setTop(mainMenuBar);
        Label helpLabel = new Label("Click on File > Open Folder... to select your wallpaper folder!");
        helpLabel.setStyle("-fx-text-fill: white;");
        root.setCenter(helpLabel);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/style/style.css")
                ).toExternalForm())
        ;

        // Fix for Linux systems
        if (Constants.IS_LINUX) {
            stage.initStyle(StageStyle.UTILITY);
        }

        // #######################
        // ### EVENT LISTENERS ###
        // #######################

        // #################################
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
                    mainImagePane.getChildren().removeAll();

                    // Replace help label with image pane
                    if (root.getCenter() instanceof Label) {
                        root.setCenter(scrollPane);
                    }

                    try (Stream<Path> stream = Files.list(selectedFile.toPath())) {
                        // Match all image files for list
                        List<Path> images = stream.filter(
                                p -> p.getFileName()
                                        .toString()
                                        .toLowerCase()
                                        .matches(".*\\.(png|jpg|jpeg|gif)$"))
                                .toList();
                        ImageView[] thumbnails = new ImageView[0];
                        Path[] paths = images.toArray(new Path[0]);
                        try {
                            imageProcessor.generateLowResolutionImages(paths);
                        } catch (IOException ex) {
                            showAlert("There was an error loading one of the images. Here's more info:" + ex.getLocalizedMessage(), "Critical Error");
                        }
//                        int i = 0;
//                        for (ImageView b : thumbnails) {
//                            // Recursively loop over thumbnails
//                            BorderPane generatedRow = generateRow(b, paths[i], i % 2 == 0);
//                            mainImagePane.getChildren().add(generatedRow);
//                            i++;
//                        }
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

        // #################################
        // View menu options event listeners

        // Large icons
        viewIconsLarge.setOnAction(e -> adjustThumbnailSize(Constants.LARGE_ICON_IMAGE_HEIGHT, mainImagePane));

        // Medium icons
        viewIconsMedium.setOnAction(e -> adjustThumbnailSize(Constants.MEDIUM_ICON_IMAGE_HEIGHT, mainImagePane));

        // Small Icons
        viewIconsSmall.setOnAction(e -> adjustThumbnailSize(Constants.SMALL_ICON_IMAGE_HEIGHT, mainImagePane));

        stage.setScene(scene);
        stage.setTitle("PaprMan " + Constants.VERSION_NUMBER);
        stage.setWidth(Constants.DEFAULT_STAGE_WIDTH);
        stage.setHeight(Constants.DEFAULT_STAGE_HEIGHT);
        stage.show();

        if (!Constants.IS_LINUX) {
            showAlert("Warning: Your computer may not work with this program. This program is specifically designed for Linux computers with Hyprpaper. You may continue, but it is not recommended.", "Warning");
        }
    }

    private void showAlert(String bt, String tt) {
        Dialog<Boolean> dialog = new Dialog<>();
        ButtonType buttonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.setTitle(tt);
        dialog.getDialogPane().getButtonTypes().add(buttonType);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/alert.css")).toExternalForm());
        dialog.initStyle(StageStyle.UTILITY);


        Label information = new Label(bt);
        dialog.getDialogPane().setContent(information);

        dialog.show();
    }

    public BorderPane generateRow(ImageView iv, Path path, boolean shaded) {
        BorderPane borderPane = new BorderPane();

        iv.setPreserveRatio(true);
        iv.setFitHeight(selectedHeight);
        borderPane.setLeft(iv);
        Label label = new Label(path.getFileName().toString());
        label.setStyle("-fx-text-fill: white;");
        borderPane.setCenter(label);

        Button button = new Button("Apply");
        VBox buttonBox = new VBox(button);
        buttonBox.setAlignment(Pos.CENTER);

        borderPane.setRight(buttonBox);

        if (shaded) {
            borderPane.setStyle("-fx-background-color: #676767;");
        }
        borderPane.setStyle(borderPane.getStyle() + "-fx-padding: 5px;");

        return borderPane;
    }

    private void adjustThumbnailSize(int size, VBox root) {
        selectedHeight = size;
        for (Node child : root.getChildren()) {
            if (child instanceof BorderPane borderPane) {
                Node imageViewNode = borderPane.getLeft();
                if (imageViewNode instanceof ImageView imageView) {
                    imageView.setFitHeight(size);
                }
            }
        }
    }

    public VBox getMainImagePane() {
        return mainImagePane;
    }

    public void setMainImagePane(VBox mainImagePane) {
        this.mainImagePane = mainImagePane;
    }
}