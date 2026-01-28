package com.PaprMan.rowformats;

import com.PaprMan.Constants;
import java.nio.file.Path;
import javafx.scene.image.Image;

public class ImageViewDataModel {
    private final Path imagePath;
    private boolean shaded;
    private final Image cachedImage;

    public ImageViewDataModel(Path imagePath, boolean shaded) {
        this.imagePath = imagePath;
        this.shaded = shaded;
        cachedImage = new Image(
                imagePath.toUri().toString(),
                Constants.FORCED_THUMBNAIL_WIDTH,
                Constants.FORCED_THUMBNAIL_HEIGHT,
                true,
                true,
                true);
    }

    public Path getImagePath() {
        return imagePath;
    }

    public Image getCachedImage() {
        return cachedImage;
    }
}
