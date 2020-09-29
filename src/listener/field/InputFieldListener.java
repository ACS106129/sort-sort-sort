package listener.field;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.FileManager;
import manager.IOManager;
import setting.Setting;

/**
 * Listener to deal with input field
 */
public class InputFieldListener extends ListenerSystem implements FieldFocusListener, KeyListener {

    private String oriString = "";

    private boolean isStartDel = true;

    private int deleteCount = 0;

    public InputFieldListener(MainFrame mainFrame, JComponent jComp) {
        this.mainFrame = mainFrame;
        jComp.setForeground(Color.GRAY);
        jComp.setBackground(Setting.inputPaneColor);
        jComp.setEnabled(ComponentState.isInputEnabled);
        comps.add(jComp);
    }

    @Override
    public void focusGained(FocusEvent event) {
        FieldFocusListener.super.focusGained(event, Setting.inputPaneTextColor);
        if (!FileManager.isCurrentFileExist()) {
            for (Component comp : getComponents(ListenerType.SaveAsFile)) {
                comp.setEnabled(true);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent event) {
        FieldFocusListener.super.focusLost(event, Setting.inputPaneTextColor);
        Object obj = event.getSource();
        if (!(obj instanceof CTextPane)) {
            return;
        }
        CTextPane textPane = CTextPane.class.cast(obj);
        if (!FileManager.isCurrentFileExist() && textPane.getForeground() == Color.GRAY) {
            for (Component comp : getComponents(ListenerType.SaveAsFile)) {
                comp.setEnabled(false);
            }
        }
    }

    public void keyTyped(KeyEvent event) {
    }

    public void keyPressed(KeyEvent event) {
        Object obj = event.getSource();
        if (obj instanceof CTextPane) {
            CTextPane textPane = CTextPane.class.cast(obj);
            oriString = textPane.getText();
        }
    }

    public void keyReleased(KeyEvent event) {
        Object obj = event.getSource();
        switch (event.getModifiersEx()) {
        case KeyEvent.ALT_DOWN_MASK:
        case KeyEvent.CTRL_DOWN_MASK:
        case KeyEvent.META_DOWN_MASK:
        case KeyEvent.SHIFT_DOWN_MASK:
            return;
        }
        if (obj instanceof CTextPane) {
            CTextPane textPane = CTextPane.class.cast(obj);
            if (oriString.length() == textPane.getText().length()) {
                return;
            }
            // if input is del text, check delete count to add undo text
            if (oriString.length() > textPane.getText().length()) {
                if (!isStartDel) {
                    IOManager.traceBackStrings.push(oriString);
                    isStartDel = true;
                }
                if (++deleteCount >= IOManager.deleteCountsPerUndo) {
                    IOManager.traceBackStrings.push(textPane.getText());
                    // del if over the max times
                    int size = IOManager.traceBackStrings.size();
                    for (int i = IOManager.maxTraceBackTimes; i < size; ++i) {
                        // remove index from 0 to overed Indexes
                        IOManager.traceBackStrings.remove(i - IOManager.maxTraceBackTimes);
                    }
                    deleteCount = 0;
                }
                // if input is add text, clear the redo, check enter or space to add undo text
            } else {
                if (isStartDel) {
                    IOManager.traceBackStrings.push(oriString);
                    isStartDel = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    IOManager.traceBackStrings.push(textPane.getText());
                    // del if over the max times
                    int size = IOManager.traceBackStrings.size();
                    for (int i = IOManager.maxTraceBackTimes; i < size; ++i) {
                        // remove index from 0 to overed Indexes
                        IOManager.traceBackStrings.remove(i - IOManager.maxTraceBackTimes);
                    }
                }
                IOManager.traceForwardStrings.clear();
                deleteCount = 0;
            }
            for (Component undo : getComponents(ListenerType.Undo)) {
                if (IOManager.traceBackStrings.empty()) {
                    undo.setEnabled(false);
                } else {
                    undo.setEnabled(true);
                }
            }
            for (Component redo : getComponents(ListenerType.Redo)) {
                if (IOManager.traceForwardStrings.empty()) {
                    redo.setEnabled(false);
                } else {
                    redo.setEnabled(true);
                }
            }
        }
    }
}