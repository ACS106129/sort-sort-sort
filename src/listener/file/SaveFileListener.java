package listener.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import manager.FileManager;
import manager.IOManager;

/**
 * Listener for save file with action listener
 */
public class SaveFileListener extends ListenerSystem implements ActionListener {

    public SaveFileListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isSaveFileEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // save file to file manager
        try {
            boolean isConfirmSave = true;
            if (!FileManager.isCurrentFileExist()) {
                JOptionPane.showMessageDialog(mainFrame, "File is not exist or loaded!", "Error Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                if (!event.getSource().equals(mainFrame)) {
                    int choosed = JOptionPane.showConfirmDialog(mainFrame, "Are you sure to save?", "Save",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (choosed == JOptionPane.NO_OPTION || choosed == JOptionPane.CLOSED_OPTION) {
                        isConfirmSave = false;
                    }
                }
                if (isConfirmSave) {
                    if (!IOManager.exportFilters.isEmpty()) {
                        FileManager.saveFile(IOManager.exportFilters);
                    } else {
                        FileManager.saveFile();
                    }
                    JOptionPane.showMessageDialog(mainFrame, "File Save!", "Save Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    if (event.getSource().equals(mainFrame)) {
                        mainFrame.dispose();
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        mainFrame.requestFocus();
    }
}