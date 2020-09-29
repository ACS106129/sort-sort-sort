package graphic.component;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import graphic.MainFrame;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentSystem;
import listener.base.ListenerHelper;
import listener.base.ListenerType;

/**
 * A component system class of <code>JMenuItem</code>
 */
public class CMenuItem extends JMenuItem implements ComponentSystem {

    private static final long serialVersionUID = 3L;

    private ListenerType type = null;

    private ComponentName name = null;

    /**
     * CMenuItem constructor
     * 
     * @param name      menu item name
     * @param compName  name of {@link ComponentName}
     * @param type      type of {@link ListenerType}
     * @param mainFrame main frame
     */
    public CMenuItem(String name, ComponentName compName, ListenerType type, MainFrame mainFrame) {
        super(name);
        try {
            setComponentName(compName);
            processType(type, mainFrame);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    @Override
    public void addListener(EventListener listener) {
        ListenerHelper.addListener(this, listener);
    }

    @Override
    public void setComponentName(ComponentName compName) {
        if (compName == ComponentName.Save) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        } else if (compName == ComponentName.SaveAs) {
            setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        } else if (compName == ComponentName.Open) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        } else if (compName == ComponentName.Exit) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        } else if (compName == ComponentName.Undo) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        } else if (compName == ComponentName.Redo) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        } else if (compName == ComponentName.Help) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        } else if (compName == ComponentName.Reset) {
            setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        } else if (compName == ComponentName.ClearOutput) {
            setAccelerator(
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        } else if (compName == ComponentName.Filter) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.SHIFT_DOWN_MASK));
        } else if (compName == ComponentName.FieldAlpha) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.SHIFT_DOWN_MASK));
        } else if (compName == ComponentName.SortProcessInterval) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK));
        }
        name = compName;
    }

    @Override
    public ComponentName getComponentName() {
        return name;
    }

    @Override
    public ListenerType getListenerType() {
        return type;
    }

    private void processType(ListenerType type, MainFrame mainFrame) throws IllegalArgumentException {
        switch (type) {
        case Arbitrary:
        case OpenLoadFile:
        case SaveFile:
        case SaveAsFile:
        case ClearOutput:
        case VersionInfo:
        case ExitProgram:
        case ImportBackgroudImage:
        case IOFilter:
        case IOFieldAlpha:
        case ThemeSelect:
        case ColorSelect:
        case LookAndFeelSelect:
        case SortOrder:
        case SortDataType:
        case SortProcessInterval:
        case Submit:
        case PauseResume:
        case Stop:
        case Skip:
        case Redo:
        case Reset:
        case Undo:
        case Help:
            ListenerHelper.accept(this, mainFrame, type);
            break;
        default:
            throw new IllegalArgumentException("Type is not compatible!");

        }
        this.type = type;
    }
}