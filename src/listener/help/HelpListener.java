package listener.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import graphic.MainFrame;
import listener.base.ListenerSystem;

public class HelpListener extends ListenerSystem implements ActionListener {
    public HelpListener(MainFrame mainFrame, JComponent jComp) {
        this.mainFrame = mainFrame;
        comps.add(jComp);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
    }
}