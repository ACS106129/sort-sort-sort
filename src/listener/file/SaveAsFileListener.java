package listener.file;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.FileManager;
import manager.IOManager;

/**
 * Listener for save as file with action listener
 */
public class SaveAsFileListener extends ListenerSystem implements ActionListener {

    public SaveAsFileListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isSaveAsFileEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Save As...");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
        jfc.setCurrentDirectory(FileManager.getCurrentStayedDir());
        // choose approve button
        while (true) {
            int choosed = jfc.showSaveDialog(mainFrame);
            if (choosed == JFileChooser.APPROVE_OPTION) {
                File source = jfc.getSelectedFile();
                try {
                    boolean isWriteFile = true;
                    String path = source.getPath();
                    if (!path.matches("^.*\\.txt$")) {
                        path += ".txt";
                    }
                    source = new File(path);
                    if (source.exists()) {
                        String[] options = { "Yes", "No" };
                        int result = JOptionPane.showOptionDialog(mainFrame,
                                "The origin file will be overwrited, are you sure?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
                        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
                            isWriteFile = false;
                            continue;
                        }
                    }
                    if (isWriteFile) {
                        // if exist file use input field's text
                        if (FileManager.isCurrentFileExist()) {
                            for (Component comp : getComponents(ListenerType.InputField)) {
                                if (!(comp instanceof CTextPane)) {
                                    continue;
                                }
                                CTextPane inputTextPane = CTextPane.class.cast(comp);
                                if (inputTextPane.getForeground() != Color.GRAY) {
                                    FileManager
                                            .setFileArray(inputTextPane.getText().trim().split(IOManager.splitRegex));
                                    break;
                                }
                            }
                        } else {
                            String str = null;
                            for (Component comp : getComponents(ListenerType.InputField, ListenerType.OutputField)) {
                                if (!(comp instanceof CTextPane)) {
                                    continue;
                                }
                                CTextPane textPane = CTextPane.class.cast(comp);
                                if (textPane.getListenerType() == ListenerType.InputField
                                        && !textPane.getText().equals(textPane.getInitialText())) {
                                    str = textPane.getText();
                                    break;
                                } else if (textPane.getListenerType() == ListenerType.OutputField
                                        && !textPane.getText().equals(textPane.getInitialText())) {
                                    str = textPane.getText().replaceAll("^.*" + IOManager.finishMessage, "");
                                    break;
                                }
                            }
                            // use inputed string if no output and no input field text
                            if (str == null) {
                                try {
                                    str = IOManager.traceForwardStrings.peek();
                                } catch (EmptyStackException es) {
                                    try {
                                        str = IOManager.traceBackStrings.peek();
                                    } catch (EmptyStackException ese) {
                                        str = "";
                                    }
                                }
                            }
                            FileManager.setFileArray(str.trim().split(IOManager.splitRegex));
                        }
                        if (!IOManager.exportFilters.isEmpty()) {
                            FileManager.saveAsFile(path, IOManager.exportFilters);
                        } else {
                            FileManager.saveAsFile(path);
                        }
                        for (Component comp : getComponents(ListenerType.SaveFile, ListenerType.SaveAsFile)) {
                            comp.setEnabled(true);
                        }
                        JOptionPane.showMessageDialog(mainFrame, "File Save!", "Save Message",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (event.getSource().equals(mainFrame)) {
                            mainFrame.dispose();
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            } else

            {
                // when before exiting the program
                if (event.getSource().equals(mainFrame)) {
                    String[] options = { "Yes", "No", "Cancel" };
                    int sure = JOptionPane.showOptionDialog(mainFrame, "Are you sure to exit without save?", "Confirm",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
                    if (sure == JOptionPane.YES_OPTION) {
                        mainFrame.dispose();
                        break;
                    } else if (sure == JOptionPane.CANCEL_OPTION || sure == JOptionPane.CLOSED_OPTION) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        FileManager.setCurrentStayedDir(jfc.getCurrentDirectory());
        mainFrame.requestFocus();
    }
}