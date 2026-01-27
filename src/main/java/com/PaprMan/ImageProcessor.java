package com.PaprMan;

import com.PaprMan.wrappers.ImageViewPathWrapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void generateLowResolutionImages(Path[] paths) throws IOException {
        for (Path path : paths) {
            CompletableFuture.supplyAsync(
                            () -> {
                                try {
                                    Image thumbnail = new Image(
                                            path.toUri().toString(),
                                            Constants.FORCED_THUMBNAIL_WIDTH,
                                            Constants.FORCED_THUMBNAIL_HEIGHT,
                                            true,
                                            true);

                                    return new ImageViewPathWrapper(new ImageView(thumbnail), path);
                                } catch (Exception e) {
                                    return null;
                                }
                            },
                            executorService)
                    .thenAccept(wrapper -> {
                        if (wrapper != null) {
                            Platform.runLater(() -> {
                                main.getMainImagePane()
                                        .getChildren()
                                        .add(main.generateRow(
                                                wrapper.getImageView(), wrapper.getPath(), lastImageFaded));
                                lastImageFaded = !lastImageFaded;
                            });
                        }
                    });
        }
    }

    public File getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(File imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
}
