package com.PaprMan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ImageProcessor {
    private File imageDirectory;

    public ImageProcessor() {}

    public boolean imagesPresent() {
        String pattern = "glob:**.{png,jpg,jpeg,bmp,gif}";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

        try (Stream<Path> stream = Files.list(imageDirectory.toPath())) {
            return stream.anyMatch(matcher::matches);
        } catch (IOException ex) {
            return false;
        }
    }

    public BufferedImage[] generateLowResolutionImages(Path[] paths) throws IOException {
        ArrayList<BufferedImage> imageArrayList = new ArrayList<>();
        for (Path p : paths) {
            BufferedImage originalImage = ImageIO.read(p.toFile());
            BufferedImage downscaledImage = new BufferedImage(
                    Constants.THUMBNAIL_WIDTH,
                    Constants.THUMBNAIL_HEIGHT,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = downscaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    originalImage,
                    0,
                    0,
                    Constants.THUMBNAIL_WIDTH,
                    Constants.THUMBNAIL_HEIGHT,
                    null
            );
            g.dispose();

            imageArrayList.add(downscaledImage);
        }
        return imageArrayList.toArray(new BufferedImage[0]);
    }

    public ImageProcessor(File imageDirectory) {
            this.imageDirectory = imageDirectory;
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(File imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
}
