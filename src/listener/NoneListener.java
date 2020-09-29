package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import graphic.MainFrame;
import listener.base.ListenerSystem;

public class NoneListener extends ListenerSystem implements ActionListener {
    public NoneListener(MainFrame mainFrame, JComponent jComp) {
        this.mainFrame = mainFrame;
        comps.add(jComp);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
    }
}