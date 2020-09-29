package process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import javax.swing.UIManager;

import javafx.application.Platform;
import manager.ImageManager;
import manager.MediaManager;
import setting.Setting;

/**
 * Initial Process of kernel system
 */
public class InitProcess {

    private InitProcess() {
    }

    /**
     * Init for only one application
     */
    public static void run() {
        File startRecord = new File(".opened");
        try {
            if (!startRecord.createNewFile() && startRecord.isHidden()) {
                System.exit(0);
            }
            Files.setAttribute(startRecord.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        startRecord.deleteOnExit();
        // initialize javafx component
        Platform.startup(() -> {
        });
        Platform.setImplicitExit(false);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.defaultButtonFollowsFocus", false);
            UIManager.put("TabbedPane.contentOpaque", false);
            System.setProperty("awt.useSystemAAFontSettings", "on");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageManager.initialize(Setting.imagePath);
        MediaManager.initialize(Setting.mediaPath);
    }
}