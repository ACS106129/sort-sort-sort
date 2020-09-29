package graphic.component;

import java.util.EventListener;

import javax.swing.JButton;

import graphic.MainFrame;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentSystem;
import listener.base.ListenerHelper;
import listener.base.ListenerType;

/**
 * A component system class of <code>JButton</code>
 */
public class CButton extends JButton implements ComponentSystem {

    private static final long serialVersionUID = 2L;

    private ListenerType type = null;

    private ComponentName name = null;

    /**
     * CButton constructor
     * 
     * @param compName  name of {@link ComponentName}
     * @param type      type of {@link ListenerType}
     * @param mainFrame main frame
     */
    public CButton(ComponentName compName, ListenerType type, MainFrame mainFrame) {
        this("", compName, type, mainFrame);
    }

    /**
     * CButton constructor
     * 
     * @param name      button name
     * @param compName  name of {@link ComponentName}
     * @param type      type of {@link ListenerType}
     * @param mainFrame main frame
     */
    public CButton(String name, ComponentName compName, ListenerType type, MainFrame mainFrame) {
        super(name);
        try {
            setComponentName(compName);
            processType(type, mainFrame);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    @Override
    public void setComponentName(ComponentName compName) {
        name = compName;
    }

    @Override
    public void addListener(EventListener listener) {
        ListenerHelper.addListener(this, listener);
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
        case OpenLoadFile:
        case SaveFile:
        case SaveAsFile:
        case VersionInfo:
        case ExitProgram:
        case ImportBackgroudImage:
        case Submit:
        case PauseResume:
        case Skip:
        case Stop:
            ListenerHelper.accept(this, mainFrame, type);
            break;
        default:
            throw new IllegalArgumentException("Type is not compatible!");
        }
        this.type = type;
    }
}