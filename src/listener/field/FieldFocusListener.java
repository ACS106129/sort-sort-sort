package listener.field;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.DefaultCaret;

import graphic.component.CTextPane;

/**
 * Focus Listener to deal with field
 */
public interface FieldFocusListener extends FocusListener {

    /**
     * Only used for {@link OutputFieldListener}, {@link ResultFieldListener} or
     * {@link RankFieldListener}, other focus need to override
     * 
     * @throws ClassCastException if class is not {@code OutputFieldListener},
     *                            {@code ResultFieldListener} or
     *                            {@code RankFieldListener} and not override
     */
    @Override
    public default void focusGained(FocusEvent event) {
        if (!(this instanceof OutputFieldListener) && !(this instanceof ResultFieldListener)
                && !(this instanceof RankFieldListener)) {
            throw new ClassCastException("Function need to override!");
        }
        Object obj = event.getSource();
        if (obj instanceof CTextPane) {
            DefaultCaret.class.cast(CTextPane.class.cast(obj).getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        }
    }

    /**
     * Only used for {@link OutputFieldListener}, {@link ResultFieldListener} or
     * {@link RankFieldListener} other focus need to override
     * 
     * @throws ClassCastException if class is not {@code OutputFieldListener},
     *                            {@code ResultFieldListener} or
     *                            {@code RankFieldListener} and not override
     */
    @Override
    public default void focusLost(FocusEvent event) {
        if (!(this instanceof OutputFieldListener) && !(this instanceof ResultFieldListener)
                && !(this instanceof RankFieldListener)) {
            throw new ClassCastException("Function need to override!");
        }
        Object obj = event.getSource();
        if (obj instanceof CTextPane) {
            CTextPane textPane = CTextPane.class.cast(obj);
            textPane.setCaretPosition(textPane.getDocument().getLength());
            DefaultCaret.class.cast(textPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
    }

    /**
     * Focus gained for only input type field, when focused change the font color to
     * {@code color}
     * 
     * @param event focus event
     * @param color the origin color
     * @throws ClassCastException if class is {@code OutputFieldListener},
     *                            {@code ResultFieldListener} or
     *                            {@code RankFieldListener}
     */
    public default void focusGained(FocusEvent event, Color color) {
        if (this instanceof OutputFieldListener || this instanceof ResultFieldListener
                || this instanceof RankFieldListener) {
            throw new ClassCastException("Class is not compatible!");
        }
        Object obj = event.getSource();
        if (obj instanceof CTextPane) {
            CTextPane textPane = CTextPane.class.cast(obj);
            if (textPane.getForeground() == Color.GRAY && textPane.getText().equals(textPane.getInitialText())) {
                textPane.setText("");
            }
            textPane.setForeground(color);
        }
    }

    /**
     * Focus lost for only input type field, when lost change font color to
     * {@code Color.GRAY}
     * 
     * @param event focus event
     * @param color the origin color
     * @throws ClassCastException if class is {@code OutputFieldListener},
     *                            {@code ResultFieldListener} or
     *                            {@code RankFieldListener}
     */
    public default void focusLost(FocusEvent event, Color color) {
        if (this instanceof OutputFieldListener || this instanceof ResultFieldListener
                || this instanceof RankFieldListener) {
            throw new ClassCastException("Class is not compatible!");
        }
        Object obj = event.getSource();
        if (obj instanceof CTextPane) {
            CTextPane textPane = CTextPane.class.cast(obj);
            if (textPane.getForeground() == color && textPane.getText().equals("")) {
                textPane.setText(textPane.getInitialText());
                textPane.setForeground(Color.GRAY);
            }
        }
    }
}