package listener.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.IOManager;

/**
 * Listener to redo the current input field to next state
 */
public class RedoListener extends ListenerSystem implements ActionListener {

    public RedoListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isRedoEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (IOManager.traceForwardStrings.empty()) {
            for (Component redo : getComponents(ListenerType.Redo)) {
                redo.setEnabled(false);
            }
            return;
        }
        for (Component comp : getComponents(ListenerType.InputField)) {
            if (comp instanceof CTextPane) {
                CTextPane textPane = CTextPane.class.cast(comp);
                if (textPane.getForeground() == Color.GRAY) {
                    continue;
                }
                // push to back string
                IOManager.traceBackStrings.push(textPane.getText());
                // del if over the max times
                int size = IOManager.traceBackStrings.size();
                for (int i = IOManager.maxTraceBackTimes; i < size; ++i) {
                    // remove index from 0 to overed Indexes
                    IOManager.traceBackStrings.remove(i - IOManager.maxTraceBackTimes);
                }
                textPane.setText(IOManager.traceForwardStrings.pop());
            }
        }
        for (Component undo : getComponents(ListenerType.Undo)) {
            undo.setEnabled(true);
        }
    }
}