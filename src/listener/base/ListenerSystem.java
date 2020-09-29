package listener.base;

import java.awt.Component;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import graphic.MainFrame;
import graphic.component.base.ComponentSystem;
import utility.ExecuteWorker;

/**
 * Listener system for custom listener
 */
public abstract class ListenerSystem {

    protected MainFrame mainFrame = null;

    protected static ExecuteWorker<Void, Void> execWorker = null;

    protected static List<Component> comps = new CopyOnWriteArrayList<>();

    /**
     * @return list of components
     */
    public final List<Component> getComponents() {
        return comps;
    }

    /**
     * @param types listener types
     * @return list of components with listener types
     */
    public final List<Component> getComponents(ListenerType... types) {
        List<Component> targetComps = new CopyOnWriteArrayList<>();
        comps.parallelStream().forEach(comp -> {
            if (comp instanceof ComponentSystem) {
                for (ListenerType type : types) {
                    if (type == ComponentSystem.class.cast(comp).getListenerType()) {
                        targetComps.add(comp);
                    }
                }
            }
        });
        return targetComps;
    }
}