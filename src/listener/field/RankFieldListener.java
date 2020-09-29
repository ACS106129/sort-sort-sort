package listener.field;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.text.DefaultCaret;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import setting.Setting;

/**
 * Listener to deal with rank field
 */
public class RankFieldListener extends ListenerSystem implements FieldFocusListener {

    public RankFieldListener(MainFrame mainFrame, JComponent jComp) {
        this.mainFrame = mainFrame;
        jComp.setForeground(Color.GRAY);
        jComp.setBackground(Setting.rankPaneColor);
        jComp.setEnabled(ComponentState.isRankEnabled);
        if (jComp instanceof CTextPane) {
            DefaultCaret.class.cast(CTextPane.class.cast(jComp).getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
        comps.add(jComp);
    }
}