package listener.option;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import graphic.dialog.IOFilterDialog;
import listener.base.ListenerSystem;

/**
 * Listener for input/output filter setting
 */
public class IOFilterListener extends ListenerSystem implements ActionListener {

    public IOFilterListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isIOFilterEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        new IOFilterDialog(mainFrame);
        mainFrame.requestFocus();
    }
}