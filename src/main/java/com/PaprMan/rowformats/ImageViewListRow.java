package com.PaprMan.rowformats;

import com.PaprMan.Main;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ImageViewListRow extends ListCell<ImageViewDataModel> {
    private final BorderPane borderPane = new BorderPane();
    private final Button button = new Button("Button");
    private final Label pathLabel = new Label();
    private final ImageView imageView = new ImageView();
    private final Main main;
    public int imageFitHeight;

    public ImageViewListRow(Main main) {
        this.main = main;
        imageFitHeight = main.getSelectedHeight();

        button.setOnAction(_ -> {
            System.out.println("Button was pressed.");
        });

        imageView.setPreserveRatio(true);
        imageView.setFitHeight(imageFitHeight);

        borderPane.setLeft(imageView);
        borderPane.setCenter(pathLabel);

        // Button VBox setup (for centering)
        VBox buttonBox = new VBox(button);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setRight(buttonBox);
    }

    @Override
    protected void updateItem(ImageViewDataModel imageViewDataModel, boolean empty) {
        super.updateItem(imageViewDataModel, empty);

        if (empty || imageViewDataModel == null) {
            setGraphic(null);
            setStyle("");
        } else {
            imageFitHeight = main.getSelectedHeight();

            pathLabel.setText(imageViewDataModel.getImagePath().getFileName().toString());

            imageView.setFitHeight(imageFitHeight);
            imageView.setImage(imageViewDataModel.getCachedImage());

            setGraphic(borderPane);
        }
    }

    public void setImageFitHeight(int imageFitHeight) {
        imageView.setFitHeight(imageFitHeight);
    }
}
