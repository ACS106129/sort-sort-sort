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
 * Listener to undo the current state to previous state
 */
public class UndoListener extends ListenerSystem implements ActionListener {

    public UndoListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isUndoEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (IOManager.traceBackStrings.empty()) {
            for (Component undo : getComponents(ListenerType.Undo)) {
                undo.setEnabled(false);
            }
            return;
        }
        for (Component comp : getComponents(ListenerType.InputField)) {
            if (comp instanceof CTextPane) {
                CTextPane textPane = CTextPane.class.cast(comp);
                if (textPane.getForeground() == Color.GRAY) {
                    continue;
                }
                // push to forward string
                IOManager.traceForwardStrings.push(textPane.getText());
                // del if over the max times
                int size = IOManager.traceForwardStrings.size();
                for (int i = IOManager.maxTraceForwardTimes; i < size; ++i) {
                    IOManager.traceForwardStrings.remove(i);
                }
                textPane.setText(IOManager.traceBackStrings.pop());
            }
        }
        for (Component redo : getComponents(ListenerType.Redo)) {
            redo.setEnabled(true);
        }
    }
}