package listener.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;

/**
 * Listener to exit the program successfully
 */
public class ExitProgramListener extends ListenerSystem implements ActionListener {

    public ExitProgramListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isExitEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        mainFrame.requestFocus();
    }
}