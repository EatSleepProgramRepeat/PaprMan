package com.PaprMan.wrappers;

import javafx.scene.image.ImageView;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ImageViewPathWrapper {
    private ImageView imageView;
    private Path path;

    public ImageViewPathWrapper(ImageView imageView, Path path) {
        this.imageView = imageView;
        this.path = path;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
