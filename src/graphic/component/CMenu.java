package graphic.component;

import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.JMenu;

import graphic.MainFrame;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentSystem;
import listener.base.ListenerHelper;
import listener.base.ListenerType;

/**
 * A component system class of <code>JMenu</code>
 */
public class CMenu extends JMenu implements ComponentSystem {

    private static final long serialVersionUID = 5L;

    private ListenerType type = null;

    private ComponentName name = null;

    /**
     * CMenu constructor
     * 
     * @param name      menu item name
     * @param compName  name of {@link ComponentName}
     * @param mainFrame main frame
     */
    public CMenu(String name, ComponentName compName, MainFrame mainFrame) {
        this(name, compName, ListenerType.None, mainFrame);
    }

    /**
     * CMenu constructor
     * 
     * @param name      menu name
     * @param compName  name of {@link ComponentName}
     * @param type      type of {@link ListenerType}
     * @param mainFrame main frame
     */
    public CMenu(String name, ComponentName compName, ListenerType type, MainFrame mainFrame) {
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
        if (compName == ComponentName.File) {
            setMnemonic(KeyEvent.VK_F);
        } else if (compName == ComponentName.Edit) {
            setMnemonic(KeyEvent.VK_E);
        } else if (compName == ComponentName.Task) {
            setMnemonic(KeyEvent.VK_T);
        } else if (compName == ComponentName.Option) {
            setMnemonic(KeyEvent.VK_O);
        } else if (compName == ComponentName.About) {
            setMnemonic(KeyEvent.VK_A);
        } else if (compName == ComponentName.Algorithm) {
            setMnemonic(KeyEvent.VK_A);
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
        case None:
            ListenerHelper.accept(this, mainFrame, type);
            break;
        default:
            throw new IllegalArgumentException("Type is not compatible!");
        }
        this.type = type;
    }
}