package listener.option;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import graphic.dialog.IOFieldAlphaDialog;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import setting.Setting;

/**
 * Listener for set the input/output field alpha
 */
public class IOFieldAlphaListener extends ListenerSystem implements ActionListener {

    public IOFieldAlphaListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isIOFieldAlphaEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        new IOFieldAlphaDialog(mainFrame);
        for (Component comp : getComponents(ListenerType.InputField, ListenerType.OutputField, ListenerType.ResultField,
                ListenerType.RankField)) {
            if (comp instanceof CTextPane) {
                CTextPane textPane = CTextPane.class.cast(comp);
                textPane.setOpacity(Setting.IOFieldAlpha);
                textPane.repaint();
            }
        }
        mainFrame.requestFocus();
    }
}