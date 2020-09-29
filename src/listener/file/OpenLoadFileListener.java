package listener.file;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

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
import setting.Setting;

/**
 * Listener for open file with action listener
 */
public class OpenLoadFileListener extends ListenerSystem implements ActionListener {

    public OpenLoadFileListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isOpenLoadFileEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open/Load file");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("DataBase Files (*.sql)", "sql"));
        jfc.addChoosableFileFilter(new FileNameExtensionFilter("Custom Object Files (*.cobj)", "cobj"));
        jfc.setCurrentDirectory(FileManager.getCurrentStayedDir());
        while (true) {
            int choosed = jfc.showOpenDialog(mainFrame);
            // choose approve button
            if (choosed == JFileChooser.APPROVE_OPTION) {
                File source = jfc.getSelectedFile();
                try {
                    if (!source.exists()) {
                        JOptionPane.showMessageDialog(mainFrame, "File is not exist!", "Error Message",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    // load file to file manager
                    if (!IOManager.importFilters.isEmpty()) {
                        FileManager.loadFile(source, IOManager.importFilters);
                    } else {
                        FileManager.loadFile(source);
                    }
                    for (Component comp : getComponents(ListenerType.InputField, ListenerType.OutputField,
                            ListenerType.ResultField)) {
                        if (comp instanceof CTextPane) {
                            CTextPane textPane = CTextPane.class.cast(comp);
                            if (textPane.getListenerType() == ListenerType.InputField) {
                                textPane.setText(String.join(IOManager.displaySeparate, FileManager.getFileArray()));
                                textPane.setForeground(Setting.inputPaneTextColor);
                            } else {
                                textPane.setText("");
                                textPane.append(textPane.getInitialText(), Color.GRAY);
                            }
                        }
                    }
                    for (Component comp : getComponents(ListenerType.SaveFile, ListenerType.SaveAsFile)) {
                        comp.setEnabled(true);
                    }
                    for (Component comp : getComponents(ListenerType.ClearOutput)) {
                        comp.setEnabled(false);
                    }
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                }
            }
            break;
        }
        FileManager.setCurrentStayedDir(jfc.getCurrentDirectory());
        mainFrame.requestFocus();
    }
}