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

/**
 * Listener to clear up the output result record
 */
public class ClearOutputListener extends ListenerSystem implements ActionListener {

    public ClearOutputListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isClearOutputEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        for (Component component : getComponents(ListenerType.OutputField, ListenerType.ResultField,
                ListenerType.RankField)) {
            if (component instanceof CTextPane) {
                CTextPane textPane = CTextPane.class.cast(component);
                textPane.setText("");
                textPane.append(textPane.getInitialText(), Color.GRAY);
            }
        }
        for (Component clearOutput : getComponents(ListenerType.ClearOutput)) {
            clearOutput.setEnabled(false);
        }
        mainFrame.requestFocus();
    }
}