package com.PaprMan;

import com.PaprMan.rowformats.ImageViewDataModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ImageProcessor {
    public static boolean lastImageFaded = false;

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
            executorService.submit(() -> {
                if (path != null) {
                    ImageViewDataModel dataModel = new ImageViewDataModel(path, lastImageFaded);
                    main.getMainImagePane().getItems().add(dataModel);
                    lastImageFaded = !lastImageFaded;
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
