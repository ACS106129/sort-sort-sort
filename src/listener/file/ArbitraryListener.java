package listener.file;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.FileManager;
import manager.IOManager;
import setting.Setting;
import utility.Arbitrary;
import utility.Regex;

/**
 * Listener to select theme
 */
public class ArbitraryListener extends ListenerSystem implements ActionListener {

    public ArbitraryListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isThemeSelectEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            String amount;
            String floor;
            String ceil;
            while (true) {
                amount = JOptionPane.showInputDialog(mainFrame, "Enter the generate amount(1~200): ", "Arbitrary",
                        JOptionPane.INFORMATION_MESSAGE);
                if (!Regex.isInteger(amount)
                        || new BigInteger(amount).compareTo(new BigInteger("200")) == 1
                        || new BigInteger(amount).compareTo(new BigInteger("1")) == -1) {
                    JOptionPane.showMessageDialog(mainFrame, "Error input!", "Message", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            }
            while (true) {
                floor = JOptionPane.showInputDialog(mainFrame, "Enter the floor of number: ", "Arbitrary",
                        JOptionPane.INFORMATION_MESSAGE);
                if (!Regex.isNumber(floor)) {
                    JOptionPane.showMessageDialog(mainFrame, "Error input!", "Message", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            }
            while (true) {
                ceil = JOptionPane.showInputDialog(mainFrame, "Enter the ceil of number: ", "Arbitrary",
                        JOptionPane.INFORMATION_MESSAGE);
                if (!Regex.isNumber(ceil)
                        || new BigDecimal(ceil).compareTo(new BigDecimal(floor)) == -1) {
                    JOptionPane.showMessageDialog(mainFrame, "Error input!", "Message", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            }
            try {
                DecimalFormat df = new DecimalFormat();
                String[] numbers = Arbitrary.generateNumber(df.parse(amount).intValue(),
                        df.parse(floor.replaceAll("^\\+", "")),
                        df.parse(ceil.replaceAll("^\\+", "")));
                FileManager.setFileArray(numbers);
                for (Component comp : getComponents(ListenerType.InputField, ListenerType.OutputField,
                        ListenerType.ResultField)) {
                    if (comp instanceof CTextPane) {
                        CTextPane textPane = CTextPane.class.cast(comp);
                        if (textPane.getListenerType() == ListenerType.InputField) {
                            textPane.setText(String.join(IOManager.displaySeparate,
                                    FileManager.getFileArray(IOManager.importFilters)));
                            textPane.setForeground(Setting.inputPaneTextColor);
                        } else {
                            textPane.setText("");
                            textPane.append(textPane.getInitialText(), Color.GRAY);
                        }
                    }
                }
                for (Component comp : getComponents(ListenerType.SaveAsFile)) {
                    comp.setEnabled(true);
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } catch (NullPointerException nrp) {
            JOptionPane.showMessageDialog(mainFrame, "Canceled!", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        mainFrame.requestFocus();
    }
}