package listener.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import manager.ImageManager;

/**
 * Listener to import background image
 */
public class ImportBackgroundImageListener extends ListenerSystem implements ActionListener {

    public ImportBackgroundImageListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isImportBackgroundImageEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Import background image");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        File defaultDir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures");
        jfc.setCurrentDirectory(ImageManager.getCurrentStayedDir() == null
                ? defaultDir == null ? jfc.getFileSystemView().getHomeDirectory() : defaultDir
                : ImageManager.getCurrentStayedDir());
        // choose approve button
        while (true) {
            int choosed = jfc.showOpenDialog(mainFrame);
            if (choosed == JFileChooser.APPROVE_OPTION) {
                File source = jfc.getSelectedFile();
                if (!source.exists()) {
                    JOptionPane.showMessageDialog(mainFrame, "Image is not exist!", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                mainFrame.setBackground(source);
            }
            break;
        }
        ImageManager.setCurrentStayedDir(jfc.getCurrentDirectory());
        mainFrame.requestFocus();
    }
}