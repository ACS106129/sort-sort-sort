package graphic.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EventListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import graphic.MainFrame;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentSystem;
import listener.base.ListenerHelper;
import listener.base.ListenerType;
import sort.base.SortType;

/**
 * A component system class of <code>JTextPane</code>
 */
public class CTextPane extends JTextPane implements ComponentSystem {

    private static final long serialVersionUID = 6L;

    private int alpha = 255;

    private String iniText = null;

    private ListenerType type = null;

    private ComponentName name = null;

    private String sortType = null;

    /**
     * Empty constructor
     */
    public CTextPane() {
    };

    /**
     * Constructor of <code>CTextPane</code>
     * 
     * @param initialText initialText of area
     * @param compName    name of {@link ComponentName}
     * @param type        type of {@link ListenerType}
     * @param mainFrame   main frame
     */
    public CTextPane(String initialText, ComponentName compName, ListenerType type, MainFrame mainFrame) {
        this(initialText, compName, type, null, mainFrame);
    }

    /**
     * Constructor of <code>CTextPane</code>
     * 
     * @param initialText initialText of area
     * @param compName    name of {@link ComponentName}
     * @param type        type of {@link ListenerType}
     * @param sortType    type of {@link SortType}
     * @param mainFrame   main frame
     */
    public CTextPane(String initialText, ComponentName compName, ListenerType type, String sortType,
            MainFrame mainFrame) {
        setText(initialText);
        setInitialText(initialText);
        setComponentName(compName);
        processType(type, sortType, mainFrame);
    }

    /**
     * Set initial text, will not call and is different from {@code setText(String)}
     * 
     * @param initialText initial text
     */
    public void setInitialText(String initialText) {
        iniText = initialText;
    }

    /**
     * Set the opacity of {@code CTextPane}, it will also invoke
     * {@code setOpaque(false)}
     * 
     * @param alpha the alpha of opacity from 0 ~ 255
     */
    public void setOpacity(int alpha) {
        if (alpha > 255 || alpha < 0) {
            throw new IllegalArgumentException("Invalid alpha for setting opacity, must be 0 ~ 255!");
        }
        setOpaque(false);
        this.alpha = alpha;
    }

    /**
     * Append text to text pane
     * 
     * @param message the string message
     * @param font    text font
     * @param color   message color
     */
    public void append(final String message, final Font font, final Color color) {
        final StyledDocument sdoc = getStyledDocument();
        final SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, color);
        if (font != null) {
            StyleConstants.setFontSize(keyword, font.getSize());
            StyleConstants.setBold(keyword, font.isBold());
            StyleConstants.setFontFamily(keyword, font.getFamily());
        }
        try {
            sdoc.insertString(sdoc.getLength(), message, keyword);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
    }

    /**
     * Append text to text pane
     * 
     * @param message the string message
     * @param color   message color
     */
    public void append(final String message, final Color color) {
        append(message, null, color);
    }

    /**
     * Append text to text pane
     * 
     * @param message the string message
     */
    public void append(final String message) {
        append(message, getForeground() == null ? Color.BLACK : getForeground());
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(isOpaque);
        alpha = 0;
    }

    @Override
    public void addListener(EventListener listener) {
        ListenerHelper.addListener(this, listener);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        StyledDocument sdoc = getStyledDocument();
        MutableAttributeSet attrs = getInputAttributes();
        if (sdoc != null) {
            StyleConstants.setForeground(attrs, c);
            sdoc.setCharacterAttributes(0, sdoc.getLength() + 1, attrs, false);
        }
    }

    @Override
    public void setComponentName(ComponentName compName) {
        name = compName;
    }

    /**
     * @return the initial text
     */
    public String getInitialText() {
        return iniText;
    }

    /**
     * @return {@link SortType}
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * Get the alpha of opacity
     * 
     * @return alpha
     */
    public int getOpacity() {
        return alpha;
    }

    @Override
    public ComponentName getComponentName() {
        return name;
    }

    @Override
    public ListenerType getListenerType() {
        return type;
    }

    private void processType(ListenerType type, String sortType, MainFrame mainFrame) throws IllegalArgumentException {
        switch (type) {
        case InputField:
        case OutputField:
        case ResultField:
        case RankField:
            ListenerHelper.accept(this, mainFrame, type);
            break;
        default:
            throw new IllegalArgumentException("Type is not compatible!");
        }
        if (type == ListenerType.OutputField && sortType == null) {
            throw new IllegalArgumentException("Sort type is not exist!");
        }
        this.sortType = sortType;
        this.type = type;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (!isOpaque()) {
            Color color = getBackground();
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}