package listener.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import graphic.MainFrame;
import graphic.component.base.ComponentState;
import listener.base.ListenerSystem;
import setting.Setting;

/**
 * Listener to show version infomation
 */
public class VersionInfoListener extends ListenerSystem implements ActionListener {

    private static JEditorPane url = ((Supplier<JEditorPane>) () -> {
        JEditorPane url = new JEditorPane("text/html",
                "<span style=\"font-family:TimesRoman; font-weight:bold; font-size:150%\">" + Setting.versionInfo
                        + "</span><br/>"
                        + "<span style=\"font-family:TimesRoman;\">Development site: <a href=\"https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort\">Gitlab</a></span>");
        // handle link events
        url.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    return;
                }
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        url.setEditable(false);
        url.setOpaque(false);
        return url;
    }).get();

    public VersionInfoListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isVersionInfoEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(mainFrame, url, "VersionInfo", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.requestFocus();
    }
}