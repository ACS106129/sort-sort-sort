package listener.option;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import graphic.dialog.SortProcessIntervalDialog;
import listener.base.ListenerSystem;

/**
 * Listener for set the sort process interval
 */
public class SortProcessIntervalListener extends ListenerSystem implements ActionListener {

    public SortProcessIntervalListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isSortProcessIntervalEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        new SortProcessIntervalDialog(mainFrame);
        mainFrame.requestFocus();
    }
}