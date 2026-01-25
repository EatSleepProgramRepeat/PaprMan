package com.PaprMan;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

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
