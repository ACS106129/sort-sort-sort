package graphic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import graphic.component.CImage;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentSystem;
import graphic.window.StartupWindow;
import javafx.scene.media.MediaPlayer;
import listener.base.ListenerType;
import listener.file.SaveAsFileListener;
import listener.file.SaveFileListener;
import manager.CObjectManager;
import manager.FileManager;
import manager.ImageManager;
import manager.MediaManager;
import setting.Setting;
import setting.Theme;

public class MainFrame extends JFrame implements Runnable {

    private static final long serialVersionUID = 1000L;

    public static Object lock = new Object();

    private CImage background = null;

    private Thread startupThread = null;

    private StartupWindow startupWindow = null;

    /**
     * MainFrame Constructor
     * 
     * @param width  frame width
     * @param height frame height
     * @param title  frame title
     */
    public MainFrame(int width, int height, String title) {
        super(title);
        if (!Setting.isTestMode) {
            startupWindow = new StartupWindow(500, 400);
            startupThread = new Thread(startupWindow);
            startupThread.start();
        }
        background = new CImage(new Dimension(width, height));
        setContentPane(background);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        setSize(width, height);
        addListener();
        setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        if (!Setting.isTestMode) {
            synchronized (lock) {
                lock.notify();
                try {
                    // still not perform animation
                    if (startupThread.isAlive() && !startupWindow.isAnimation()) {
                        lock.wait(Setting.startupMilliseconds * 2);
                        lock.notify();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            try {
                startupThread.join(Setting.startupMilliseconds * 2);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        setVisible(true);
        setAlwaysOnTop(true);
        setAlwaysOnTop(false);
        CObjectManager.initialize(this);
    }

    @Override
    public void setLocale(Locale loc) {
        super.setLocale(loc);
        Locale.setDefault(loc);
    }

    /**
     * Set the theme style
     * 
     * @param theme theme
     */
    public void setTheme(Theme theme) throws IllegalArgumentException {
        boolean isThrow = true;
        for (Theme th : Theme.values()) {
            if (th == theme) {
                isThrow = false;
                Setting.theme = theme;
                break;
            }
        }
        if (isThrow)
            throw new IllegalArgumentException("Theme not found!");
        String themeSuffix = Setting.theme == Theme.Classic ? "" : "_" + Setting.theme.toString().toLowerCase();
        List<Component> comps = new ArrayList<>();
        findComponents(comps, this);
        comps.parallelStream().forEach(comp -> {
            if (comp instanceof ComponentSystem) {
                ComponentName compName = ComponentSystem.class.cast(comp).getComponentName();
                if (compName == ComponentName.Undefined) {
                    return;
                }
                if (comp instanceof AbstractButton) {
                    AbstractButton button = AbstractButton.class.cast(comp);
                    String name = compName.toString();
                    Icon icon = null;
                    if (button.getPressedIcon() != null) {
                        icon = button.getPressedIcon();
                        button.setPressedIcon(ImageManager.getImageIcon(name + "_pressed" + themeSuffix,
                                new Dimension(icon.getIconWidth(), icon.getIconHeight())));
                    }
                    if (button.getIcon() != null) {
                        icon = button.getIcon();
                        button.setIcon(ImageManager.getImageIcon(name + themeSuffix,
                                new Dimension(icon.getIconWidth(), icon.getIconHeight())));
                    }
                }
            }
        });
        setBackground(ImageManager.getFile(ComponentName.Background.toString() + themeSuffix));
    }

    /**
     * Set the main frame background, if background exist will be overwritten
     * 
     * @param imageFile image file
     */
    public void setBackground(File imageFile) {
        try {
            File oriImageFile = background.getImageFile();
            if (oriImageFile != null && !oriImageFile.getPath().matches(".*res[\\\\/]image[\\\\/].*")) {
                ImageManager.removeImage(background.getImageFile().toPath());
            }
            int height = getJMenuBar() == null ? 0 : getJMenuBar().getHeight();
            ImageManager.loadImageFromFile(imageFile);
            background.setImage(imageFile, new Dimension(getInnerSize().width, getInnerSize().height - height));
            background.setLocation(0, height);
            repaint();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Set the main frame background, if background exist will be overwritten
     * 
     * @param imageFilePath image file path
     */
    public void setBackground(String imageFilePath) {
        setBackground(new File(imageFilePath));
    }

    /**
     * Get the main frame inner size
     */
    public Dimension getInnerSize() {
        Dimension size = getSize();
        Insets insets = getInsets();
        return new Dimension(size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
    }

    private void findComponents(List<Component> components, Container container) {
        Component[] comps = container.getComponents();
        for (Component comp : comps) {
            components.add(comp);
            if (comp instanceof JMenuBar) {
                JMenuBar bar = JMenuBar.class.cast(comp);
                for (int i = 0; i < bar.getMenuCount(); i++) {
                    findMenuItems(components, bar.getMenu(i));
                }
            } else if (comp instanceof Container) {
                findComponents(components, Container.class.cast(comp));
            }
        }
    }

    private void findMenuItems(List<Component> components, JMenu menu) {
        components.add(menu);
        for (int i = 0; i < menu.getMenuComponentCount(); i++) {
            Component menuItemComp = menu.getMenuComponent(i);
            components.add(menuItemComp);
            if (menuItemComp instanceof JMenu) {
                findMenuItems(components, JMenu.class.cast(menuItemComp));
            }
        }
    }

    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainFrame.this.requestFocus();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] options = { "Save and Exit", "Exit", "Cancel" };

                int choosed = JOptionPane.showOptionDialog(MainFrame.this, "Are you sure to exit?", "Exit",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (choosed == JOptionPane.YES_OPTION) {
                    List<Component> comps = new ArrayList<>();
                    findComponents(comps, MainFrame.this);
                    for (Component comp : comps) {
                        if (!(comp instanceof ComponentSystem)) {
                            continue;
                        }
                        if (!FileManager.isCurrentFileExist()) {
                            if (ComponentSystem.class.cast(comp).getListenerType() != ListenerType.SaveAsFile) {
                                continue;
                            }
                            for (ActionListener a : comp.getListeners(ActionListener.class)) {
                                if (!(a instanceof SaveAsFileListener)) {
                                    continue;
                                }
                                SaveAsFileListener.class.cast(a).actionPerformed(new ActionEvent(MainFrame.this,
                                        ActionEvent.ACTION_PERFORMED, "Save as before exit program"));
                                break;
                            }
                            break;
                        } else {
                            if (ComponentSystem.class.cast(comp).getListenerType() != ListenerType.SaveFile) {
                                continue;
                            }
                            for (ActionListener a : comp.getListeners(ActionListener.class)) {
                                if (!(a instanceof SaveFileListener)) {
                                    continue;
                                }
                                SaveFileListener.class.cast(a).actionPerformed(new ActionEvent(MainFrame.this,
                                        ActionEvent.ACTION_PERFORMED, "Save before exit program"));
                                break;
                            }
                            break;
                        }
                    }
                } else if (choosed == JOptionPane.NO_OPTION) {
                    MainFrame.this.dispose();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (Setting.isTestMode) {
                    System.exit(0);
                    return;
                }
                MediaPlayer bgm = MediaManager.getMediaPlayer(Setting.bgmName);
                bgm.stop();
                bgm.dispose();
                MediaPlayer mp = MediaManager.getMediaPlayer("exit_sound");
                mp.setVolume(100);
                mp.setOnEndOfMedia(() -> {
                    System.exit(0);
                });
                mp.setAutoPlay(true);
            }
        });
    }
}