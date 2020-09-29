package listener.task;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;

public class StopListener extends ListenerSystem implements ActionListener {

    public StopListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isStopEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (execWorker == null || execWorker.isDone())
            return;
        int choosed = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure to stop\r\n(Current sort will be terminated)?", "Stop sorting",
                JOptionPane.OK_CANCEL_OPTION);
        if (choosed == JOptionPane.OK_OPTION) {
            if (execWorker.isPaused()) {
                for (Component comp : getComponents(ListenerType.PauseResume)) {
                    for (ActionListener a : comp.getListeners(ActionListener.class)) {
                        if (!(a instanceof PauseResumeListener))
                            continue;
                        a.actionPerformed(new ActionEvent(comp, ActionEvent.ACTION_PERFORMED, ""));
                        break;
                    }
                    break;
                }
            }
            execWorker.cancel(false);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
}