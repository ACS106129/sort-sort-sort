package graphic.component.base;

import java.util.EventListener;

import listener.base.ListenerType;

/**
 * System of component kernel
 */
public interface ComponentSystem {
    /**
     * AddListener's common function, according event type to add certain listener
     * 
     * @param listener listener to be add
     */
    public void addListener(EventListener listener);

    /**
     * @return listener type
     */
    public ListenerType getListenerType();

    /**
     * Set the component name
     * 
     * @param name name of component
     */
    public void setComponentName(ComponentName name);

    /**
     * @return component name
     */
    public ComponentName getComponentName();
}