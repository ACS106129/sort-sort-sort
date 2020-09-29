package graphic.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JWindow;

import graphic.MainFrame;
import graphic.component.CTextPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import manager.MediaManager;
import setting.Setting;

public class StartupWindow extends JWindow implements Runnable {

    private static final long serialVersionUID = 1001L;

    private CTextPane text = new CTextPane();

    private JFXPanel panel = new JFXPanel();

    private int mode = 4;

    private boolean animation = false;

    public StartupWindow(int width, int height) {
        Group root = new Group();
        setSize(width, height);
        setLocationRelativeTo(null);
        setContentPane(panel);
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MediaPlayer mp = MediaManager.getMediaPlayer("pinkpink");
            mp.setOnPlaying(() -> {
                animation = true;
                new Thread(() -> {
                    slowDisplay(1500);
                }).start();
                new Thread(() -> {
                    textCycleDisplay(1000);
                }).start();
            });
            mp.setOnEndOfMedia(() -> {
                animation = false;
                MediaPlayer bgm = MediaManager.getMediaPlayer(Setting.bgmName);
                // default setting
                bgm.setCycleCount(MediaPlayer.INDEFINITE);
                bgm.setVolume(Setting.bgmVolume);
                bgm.play();
            });
            MediaView mv = new MediaView(mp);
            mv.setPreserveRatio(false);
            mv.setFitWidth(width);
            mv.setFitHeight(height);
            root.getChildren().add(mv);
            panel.setScene(new Scene(root, width, height));
            latch.countDown();
        });
        setAlwaysOnTop(true);
        setEnabled(false);
        setLayout(new GridBagLayout());
        text.setSize(width, height);
        text.append("Loading...\r\n", new Font("Consola", Font.BOLD, 64), Color.WHITE);
        text.append("    Please wait", new Font("Consola", Font.BOLD, 32), Color.WHITE);
        text.setEditable(false);
        text.setOpaque(false);
        text.setDragEnabled(false);
        add(text, new GridBagConstraints());
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return true if animating
     */
    public boolean isAnimation() {
        return animation;
    }

    @Override
    public void run() {
        synchronized (MainFrame.lock) {
            setVisible(true);
            MediaPlayer mp = MediaManager.getMediaPlayer("pinkpink");
            try {
                MainFrame.lock.notify();
                mp.play();
                MainFrame.lock.wait(Setting.startupMilliseconds * 2);
                while (mp.getCurrentTime().lessThan(mp.getTotalDuration()))
                    ;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            text.setForeground(Color.WHITE);
            setVisible(false);
        }
    }

    private void textCycleDisplay(float milliseconds) {
        while (animation) {
            long start = System.currentTimeMillis();
            long elapsed = System.currentTimeMillis() - start;
            int priAlpha = 0;
            while (elapsed < milliseconds) {
                int alpha = (int) (Math.sin(Math.toRadians(elapsed / milliseconds) * 180) * 255);
                int red = (int) (Math.sin(Math.toRadians(getRate(mode % 6, elapsed, milliseconds)) * 180) * 255);
                int green = (int) (Math.sin(Math.toRadians(getRate((mode + 4) % 6, elapsed, milliseconds)) * 180)
                        * 255);
                int blue = (int) (Math.sin(Math.toRadians(getRate((mode + 2) % 6, elapsed, milliseconds)) * 180) * 255);
                if (priAlpha != alpha) {
                    text.setForeground(new Color(red, green, blue, alpha));
                    priAlpha = alpha;
                }
                if (!animation) {
                    return;
                }
                elapsed = System.currentTimeMillis() - start;
            }
            mode++;
        }
    }

    private float getRate(int mode, long elapsed, float ms) {
        switch (mode) {
        case 0:
        case 1:
            return 0;
        case 2:
            // increase
            return elapsed / (ms * 2);
        case 3:
        case 4:
            return 0.5f;
        case 5:
            // decrease
            return (ms - elapsed) / (ms * 2);
        }
        throw new IllegalArgumentException("Undefined mode!");
    }

    private void slowDisplay(float milliseconds) {
        long start = System.currentTimeMillis();
        long elapsed = System.currentTimeMillis() - start;
        while (elapsed < milliseconds) {
            setOpacity(elapsed / milliseconds);
            elapsed = System.currentTimeMillis() - start;
        }
    }
}