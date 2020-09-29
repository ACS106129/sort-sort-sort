package listener.option;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import graphic.MainFrame;
import graphic.component.CMenuItem;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.IOManager;
import manager.ImageManager;

/**
 * Listener for change the data type of sort
 */
public class SortDataTypeListener extends ListenerSystem implements ActionListener {

    public SortDataTypeListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        if (button.getText().equals(IOManager.sortDataType)) {
            button.setIcon(ImageManager.getImageIcon("checked", new Dimension(16, 16)));
        } else {
            button.setIcon(null);
        }
        comps.add(button);
        button.setEnabled(ComponentState.isSortDataTypeEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object obj = event.getSource();
        if (obj instanceof CMenuItem) {
            CMenuItem menuItem = CMenuItem.class.cast(obj);
            String text = menuItem.getText();
            for (Component comp : getComponents(ListenerType.SortDataType)) {
                CMenuItem item = CMenuItem.class.cast(comp);
                if (item == menuItem || item.getIcon() == null) {
                    continue;
                }
                menuItem.setIcon(item.getIcon());
                item.setIcon(null);
            }
            IOManager.sortDataType = text;
        }
    }
}