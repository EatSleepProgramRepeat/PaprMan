package com.PaprMan;

public class Constants {
    public static final String VERSION_NUMBER = "v1.0-ALPHA";
    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean IS_LINUX = Constants.OS_NAME.matches(".*(nix|nux).*");

    public static final int DEFAULT_STAGE_HEIGHT = 400;
    public static final int DEFAULT_STAGE_WIDTH = 570;
    public static final int THUMBNAIL_WIDTH = 160;
    public static final int THUMBNAIL_HEIGHT = 90;
}
