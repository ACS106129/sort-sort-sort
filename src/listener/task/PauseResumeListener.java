package listener.task;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import graphic.MainFrame;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentState;
import graphic.component.base.ComponentSystem;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.ImageManager;
import setting.Setting;
import setting.Theme;

public class PauseResumeListener extends ListenerSystem implements ActionListener {

    public PauseResumeListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isPauseResumeEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (execWorker == null || (execWorker.isDone() && !execWorker.isPaused()))
            return;
        Object obj = event.getSource();
        if (obj instanceof ComponentSystem) {
            ComponentSystem button = ComponentSystem.class.cast(obj);
            if (execWorker.isPaused()) {
                button.setComponentName(ComponentName.Pause);
                changeAllState(button.getComponentName().toString());
                execWorker.resume();
            } else {
                button.setComponentName(ComponentName.Resume);
                changeAllState(button.getComponentName().toString());
                execWorker.pause();
            }
        }
    }

    private void changeAllState(String state) {
        final String themeSuffix = Setting.theme == Theme.Classic ? "" : "_" + Setting.theme.toString().toLowerCase();
        final String iconName = state + themeSuffix;
        final String pressedIconName = state + "_pressed" + themeSuffix;
        for (Component comp : getComponents(ListenerType.PauseResume)) {
            AbstractButton button = AbstractButton.class.cast(comp);
            if (button instanceof JMenuItem) {
                button.setText(state.substring(0, 1).toUpperCase() + state.substring(1));
                button.setIcon(ImageManager.getImageIcon(iconName, new Dimension(16, 16)));
                button.setPressedIcon(ImageManager.getImageIcon(pressedIconName, new Dimension(16, 16)));
            } else {
                Dimension iconSize = new Dimension(button.getIcon().getIconWidth(), button.getIcon().getIconHeight());
                button.setIcon(ImageManager.getImageIcon(iconName, iconSize));
                button.setPressedIcon(ImageManager.getImageIcon(pressedIconName, iconSize));
            }
        }
    }
}