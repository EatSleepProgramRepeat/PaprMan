package com.PaprMan;

import com.PaprMan.wrappers.BufferedImagePathWrapper;
import com.PaprMan.wrappers.ImageViewPathWrapper;
import javafx.application.Platform;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ImageProcessor {
    private boolean lastImageFaded = false;

    private File imageDirectory;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.MAX_THREADS);
    // private final CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);

    private final Main main;

    public ImageProcessor(Main main) {
        this.main = main;
    }

    public boolean imagesPresent() {
        String pattern = "glob:**.{png,jpg,jpeg,bmp,gif}";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

        try (Stream<Path> stream = Files.list(imageDirectory.toPath())) {
            return stream.anyMatch(matcher::matches);
        } catch (IOException ex) {
            return false;
        }
    }

    public ImageProcessor(File imageDirectory, Main main) {
        this.imageDirectory = imageDirectory;
        this.main = main;
    }

    public BufferedImagePathWrapper[][] splitImageArrays(BufferedImagePathWrapper[] ia, int chunkCount) {
        BufferedImagePathWrapper[][] chunks = new BufferedImagePathWrapper[chunkCount][];
        int chunkSize = (int) Math.ceil((double) ia.length / chunkCount);
        for (int i = 0; i < chunkCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, ia.length);
            BufferedImagePathWrapper[] chunk = new BufferedImagePathWrapper[end - start];
            if (end - start >= 0) System.arraycopy(ia, start, chunk, 0, end - start);
            chunks[i] = chunk;
        }

        return chunks;
    }

    public void generateLowResolutionImages(Path[] paths) throws IOException {
        BufferedImagePathWrapper[] bufferedImages = new BufferedImagePathWrapper[paths.length];
        for (int i = 0; i < paths.length; i++) {
            bufferedImages[i] = new BufferedImagePathWrapper(ImageIO.read(paths[i].toFile()), paths[i]);
        }

        // Send them off to the splitter
        BufferedImagePathWrapper[][] bufferedImageChunks = splitImageArrays(bufferedImages, Constants.MAX_THREADS);

        List<CompletableFuture<ImageViewPathWrapper[]>> futures = new ArrayList<>();
        for (BufferedImagePathWrapper[] b : bufferedImageChunks) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                ImageViewPathWrapper[] finalImages = new ImageViewPathWrapper[b.length];
                for (int i = 0; i < b.length; i++) {
                    Path path = b[i].getPath();
                    BufferedImage bb = b[i].getBufferedImage();
                    int scaledDownWidth, scaledDownHeight;
                    if (Constants.FORCED_THUMBNAIL_WIDTH != 0) {
                        scaledDownWidth = Constants.FORCED_THUMBNAIL_WIDTH;
                        double scaleFactor = (double) Constants.FORCED_THUMBNAIL_WIDTH / bb.getWidth();
                        scaledDownHeight = (int) (bb.getHeight() * scaleFactor);
                    } else if (Constants.FORCED_THUMBNAIL_HEIGHT != 0) {
                        scaledDownHeight = Constants.FORCED_THUMBNAIL_HEIGHT;
                        double scaleFactor = (double) Constants.FORCED_THUMBNAIL_HEIGHT / bb.getHeight();
                        scaledDownWidth = (int) (bb.getWidth() * scaleFactor);
                    } else {
                        throw new RuntimeException(
                                "Both Constants.FORCED_THUMBNAIL_WIDTH and Constants.FORCED_THUMBNAIL_HEIGHT may not both be zero.");
                    }

                    BufferedImage downscaledImage =
                            new BufferedImage(scaledDownWidth, scaledDownHeight, BufferedImage.TYPE_INT_ARGB);

                    Graphics2D g = downscaledImage.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(bb, 0, 0, scaledDownWidth, scaledDownHeight, null);
                    g.dispose();

                    WritableImage wr = new WritableImage(scaledDownWidth, scaledDownHeight);
                    SwingFXUtils.toFXImage(downscaledImage, wr);

                    ImageView iv = new ImageView(wr);
                    finalImages[i] = new ImageViewPathWrapper(iv, path);
                }
                return finalImages;
            }));
        }

        // Code to run when the futures complete
        for (CompletableFuture<ImageViewPathWrapper[]> cf : futures) {
            cf.thenAccept(images -> Platform.runLater(() -> {
                for (ImageViewPathWrapper iv : images) {
                    main.getMainImagePane().getChildren().add(main.generateRow(iv.getImageView(), iv.getPath(), lastImageFaded));
                    lastImageFaded = !lastImageFaded;
                }
            }));
        }
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(File imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
}
