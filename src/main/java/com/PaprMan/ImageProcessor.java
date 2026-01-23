package com.PaprMan;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

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

    public ImageView[] generateLowResolutionImages(Path[] paths) throws IOException {
        ArrayList<ImageView> imageArrayList = new ArrayList<>();
        for (Path p : paths) {
            BufferedImage originalImage = ImageIO.read(p.toFile());

            int scaledDownWidth, scaledDownHeight;
            if (Constants.FORCED_THUMBNAIL_WIDTH != 0) {
                scaledDownWidth = Constants.FORCED_THUMBNAIL_WIDTH;
                double scaleFactor = (double) Constants.FORCED_THUMBNAIL_WIDTH / originalImage.getWidth();
                scaledDownHeight = (int) (originalImage.getHeight() * scaleFactor);
            } else if (Constants.FORCED_THUMBNAIL_HEIGHT != 0) {
                scaledDownHeight = Constants.FORCED_THUMBNAIL_HEIGHT;
                double scaleFactor = (double) Constants.FORCED_THUMBNAIL_HEIGHT / originalImage.getHeight();
                scaledDownWidth = (int) (originalImage.getWidth() * scaleFactor);
            } else {
                throw new RuntimeException(
                        "Both Constants.FORCED_THUMBNAIL_WIDTH and Constants.FORCED_THUMBNAIL_HEIGHT may not both be zero."
                );
            }

            BufferedImage downscaledImage = new BufferedImage(
                    scaledDownWidth,
                    scaledDownHeight,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = downscaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    originalImage,
                    0,
                    0,
                    scaledDownWidth,
                    scaledDownHeight,
                    null
            );
            g.dispose();

            WritableImage wr = new WritableImage(scaledDownWidth, scaledDownHeight);
            SwingFXUtils.toFXImage(downscaledImage, wr);

            ImageView iv = new ImageView(wr);
            imageArrayList.add(iv);
        }
        return imageArrayList.toArray(new ImageView[0]);
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
