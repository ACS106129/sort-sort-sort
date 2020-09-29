package listener.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;

/**
 * Listener for setting the look and feel of system interface
 */
public class LookAndFeelSelectListener extends ListenerSystem implements ActionListener {

    private static Map<String, UIManager.LookAndFeelInfo> lookAndFeelsMap = ((Supplier<Map<String, UIManager.LookAndFeelInfo>>) () -> {
        Map<String, UIManager.LookAndFeelInfo> maps = new LinkedHashMap<>();
        UIManager.LookAndFeelInfo[] plafInfos = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < plafInfos.length; i++) {
            maps.put(plafInfos[i].getName(), plafInfos[i]);
        }
        return maps;
    }).get();

    public LookAndFeelSelectListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isLookAndFeelSelectEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            Object choosed = JOptionPane.showInputDialog(mainFrame, "Look & Feel selection: ", "Look & Feel",
                    JOptionPane.INFORMATION_MESSAGE, null, lookAndFeelsMap.keySet().toArray(new String[0]),
                    UIManager.getLookAndFeel().getName());
            int sure = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure to set the look & feel to\r\n" + choosed.toString() + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (sure == JOptionPane.YES_OPTION) {
                new Thread(() -> {
                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            try {
                                UIManager.setLookAndFeel(lookAndFeelsMap.get(choosed.toString()).getClassName());
                                SwingUtilities.updateComponentTreeUI(mainFrame);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NullPointerException nrp) {
            JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        mainFrame.requestFocus();
    }
}