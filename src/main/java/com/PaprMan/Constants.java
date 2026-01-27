package com.PaprMan;

@SuppressWarnings("unused")
public class Constants {
    public static final String VERSION_NUMBER = "v1.0-ALPHA";
    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean IS_LINUX = Constants.OS_NAME.matches(".*(nix|nux).*");

    public static final int DEFAULT_STAGE_WIDTH = 800;
    public static final int DEFAULT_STAGE_HEIGHT = 600;

    // One of the two following variables must be equal to zero.
    // If neither are zero, width is prioritized.
    public static final int FORCED_THUMBNAIL_WIDTH = 0;
    public static final int FORCED_THUMBNAIL_HEIGHT = 150;

    public static final int SMALL_ICON_IMAGE_HEIGHT = 20;
    public static final int MEDIUM_ICON_IMAGE_HEIGHT = 50;
    public static final int LARGE_ICON_IMAGE_HEIGHT = 80;
    public static final int SMALL_ICON_IMAGE_HEIGHT_GRID = 50;
    public static final int MEDIUM_ICON_IMAGE_HEIGHT_GRID = 125;
    public static final int LARGE_ICON_IMAGE_HEIGHT_GRID = 200;

    public static final int MAX_THREADS = 4;
}
