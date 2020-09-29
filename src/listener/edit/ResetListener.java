package listener.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.FileManager;
import manager.IOManager;
import setting.Setting;

/**
 * Listener to reset to last submitted input
 */
public class ResetListener extends ListenerSystem implements ActionListener {

    public ResetListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isResetEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String[] inputs = FileManager.getFileArray();
        if (inputs == null || inputs.length == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No record input!", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] options = { "Yes", "No" };
        int choosed = JOptionPane.showOptionDialog(mainFrame,
                "Are you sure to reset to\r\nlast submit(current input will gone)?", "Reset", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, null);
        if (choosed == JOptionPane.YES_OPTION) {
            IOManager.traceBackStrings.clear();
            IOManager.traceForwardStrings.clear();
            for (Component component : getComponents(ListenerType.InputField, ListenerType.OutputField,
                    ListenerType.ResultField, ListenerType.RankField)) {
                if (component instanceof CTextPane) {
                    CTextPane textPane = CTextPane.class.cast(component);
                    if (textPane.getListenerType() == ListenerType.InputField) {
                        textPane.setText(String.join(IOManager.displaySeparate, inputs));
                        textPane.setForeground(Setting.inputPaneTextColor);
                    } else {
                        textPane.setText("");
                        textPane.append(textPane.getInitialText(), Color.GRAY);
                    }
                }
            }
            for (Component clearOutput : getComponents(ListenerType.ClearOutput, ListenerType.Redo,
                    ListenerType.Undo)) {
                clearOutput.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Cancel!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        mainFrame.requestFocus();
    }
}