package com.PaprMan;

import javafx.scene.shape.Path;

import java.awt.image.BufferedImage;

public class BufferedImagePathWrapper {
    private BufferedImage bufferedImage;
    private Path path;

    public BufferedImagePathWrapper(BufferedImage bufferedImage, Path path) {
        this.bufferedImage = bufferedImage;
        this.path = path;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
