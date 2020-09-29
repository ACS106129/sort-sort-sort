package listener.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import setting.Setting;
import setting.Theme;

/**
 * Listener to select theme
 */
public class ThemeSelectListener extends ListenerSystem implements ActionListener {

    public ThemeSelectListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isThemeSelectEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            Object choosed = JOptionPane.showInputDialog(mainFrame, "Theme selection: ", "Theme",
                    JOptionPane.INFORMATION_MESSAGE, null, Theme.values(), Setting.theme);
            int sure = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure to set the theme to\r\n" + choosed.toString() + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (sure == JOptionPane.YES_OPTION) {
                mainFrame.setTheme((Theme) choosed);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NullPointerException nrp) {
            JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        mainFrame.requestFocus();
    }
}