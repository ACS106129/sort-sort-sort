package listener.option;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import setting.Setting;

/**
 * Listener to select the color of input/output
 */
public class ColorSelectListener extends ListenerSystem implements ActionListener {

    public ColorSelectListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isColorSelectEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            String[] fields = { "Input pane", "Input pane Text", "Output pane", "Output pane Text", "Result pane",
                    "Result pane Text", "Rank pane", "Rank pane Text", "Changed index Text" };
            Object choosed = JOptionPane.showInputDialog(mainFrame, "Choose item to change color: ", "Change color",
                    JOptionPane.INFORMATION_MESSAGE, null, fields, null);
            Color iniColor = null;
            if (choosed == fields[0]) {
                iniColor = Setting.inputPaneColor;
            } else if (choosed == fields[1]) {
                iniColor = Setting.inputPaneTextColor;
            } else if (choosed == fields[2]) {
                iniColor = Setting.outputPaneColor;
            } else if (choosed == fields[3]) {
                iniColor = Setting.outputPaneTextColor;
            } else if (choosed == fields[4]) {
                iniColor = Setting.resultPaneColor;
            } else if (choosed == fields[5]) {
                iniColor = Setting.resultPaneTextColor;
            } else if (choosed == fields[6]) {
                iniColor = Setting.rankPaneColor;
            } else if (choosed == fields[7]) {
                iniColor = Setting.rankPaneTextColor;
            } else if (choosed == fields[8]) {
                iniColor = Setting.changedIndexColor;
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Color color = JColorChooser.showDialog(mainFrame, "Choose " + choosed.toString() + " color", iniColor);
            if (color == null)
                throw new NullPointerException();
            int sure = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure to set the color " + "[r=" + color.getRed() + ",g=" + color.getGreen() + ",b="
                            + color.getBlue() + "]" + " to\r\n" + choosed.toString() + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (sure == JOptionPane.YES_OPTION) {
                // input fields (pane/text)
                if (choosed == fields[0] || choosed == fields[1]) {
                    if (choosed == fields[0])
                        Setting.inputPaneColor = color;
                    else
                        Setting.inputPaneTextColor = color;
                    setColor(ListenerType.InputField, choosed == fields[0], color);
                    // output fields (pane/text)
                } else if (choosed == fields[2] || choosed == fields[3]) {
                    if (choosed == fields[2])
                        Setting.outputPaneColor = color;
                    else
                        Setting.outputPaneTextColor = color;
                    setColor(ListenerType.OutputField, choosed == fields[2], color);
                    // result fields (pane/text)
                } else if (choosed == fields[4] || choosed == fields[5]) {
                    if (choosed == fields[4])
                        Setting.resultPaneColor = color;
                    else
                        Setting.resultPaneTextColor = color;
                    setColor(ListenerType.ResultField, choosed == fields[4], color);
                    // rank fields (pane/text)
                } else if (choosed == fields[6] || choosed == fields[7]) {
                    if (choosed == fields[6])
                        Setting.rankPaneColor = color;
                    else
                        Setting.rankPaneTextColor = color;
                    setColor(ListenerType.RankField, choosed == fields[6], color);
                } else if (choosed == fields[8]) {
                    Setting.changedIndexColor = color;
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NullPointerException nrp) {
            JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setColor(ListenerType type, boolean isTargetPane, Color changeColor) {
        for (Component comp : getComponents(type)) {
            if (comp instanceof CTextPane) {
                CTextPane pane = CTextPane.class.cast(comp);
                if (isTargetPane) {
                    pane.setBackground(changeColor);
                    pane.repaint();
                    continue;
                }
                if (type == ListenerType.InputField) {
                    if (pane.getForeground() != Color.GRAY && !pane.getText().equals(pane.getInitialText()))
                        pane.setForeground(changeColor);
                } else
                    pane.append("\r\nNext submit will be effective.", changeColor);
            }
        }
    }
}