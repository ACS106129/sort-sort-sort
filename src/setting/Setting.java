package setting;

import java.awt.Color;

/**
 * Setting of Configuration
 */
public class Setting {

    public static volatile int outputFileElementPerLine = 5;

    // 0 ~ 255
    public static volatile int IOFieldAlpha = 200;

    public static final int IOFieldAlphaDefault = IOFieldAlpha;

    // milliseconds
    public static volatile int sortProcessInterval = 500;

    public static final int sortProcessIntervalDefault = sortProcessInterval;

    public static final int sortProcessMax = 1000;

    public static final int startupMilliseconds = 10000;

    public static volatile Theme theme = Theme.Classic;

    public static volatile Color inputPaneColor = new Color(255, 232, 245);

    public static volatile Color inputPaneTextColor = Color.BLACK;

    public static volatile Color outputPaneColor = new Color(30, 30, 30);

    public static volatile Color outputPaneTextColor = Color.WHITE;

    public static volatile Color resultPaneColor = Color.WHITE;

    public static volatile Color resultPaneTextColor = Color.BLACK;

    public static volatile Color rankPaneColor = Color.WHITE;

    public static volatile Color rankPaneTextColor = Color.BLACK;

    public static volatile Color changedIndexColor = Color.RED;

    public static volatile double bgmVolume = 0.5;

    public static volatile String bgmName = "bgm_1";

    public static final String resPath = "res";

    public static final String imagePath = resPath + "/image";

    public static final String mediaPath = resPath + "/media";

    public static final String dataBasePath = resPath + "/database";

    public static final String userFilePath = resPath + "/userfile";

    public static final String themePath = imagePath + "/theme";

    public static final String audioPath = mediaPath + "/audio";

    public static final String userObjectPath = userFilePath + "/object";

    public static final String versionInfo = "Ver 1.1.6.alpha";

    public static final boolean isTestMode = false;

}