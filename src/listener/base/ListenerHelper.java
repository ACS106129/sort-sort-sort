package listener.base;

import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.EventListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeListener;

import graphic.MainFrame;
import graphic.component.base.ComponentSystem;
import listener.NoneListener;
import listener.edit.ClearOutputListener;
import listener.edit.RedoListener;
import listener.edit.ResetListener;
import listener.edit.UndoListener;
import listener.field.InputFieldListener;
import listener.field.OutputFieldListener;
import listener.field.RankFieldListener;
import listener.field.ResultFieldListener;
import listener.file.ArbitraryListener;
import listener.file.ExitProgramListener;
import listener.file.ImportBackgroundImageListener;
import listener.file.LookAndFeelSelectListener;
import listener.file.OpenLoadFileListener;
import listener.file.SaveAsFileListener;
import listener.file.SaveFileListener;
import listener.file.ThemeSelectListener;
import listener.help.HelpListener;
import listener.help.VersionInfoListener;
import listener.option.ColorSelectListener;
import listener.option.IOFieldAlphaListener;
import listener.option.IOFilterListener;
import listener.option.SortDataTypeListener;
import listener.option.SortOrderListener;
import listener.option.SortProcessIntervalListener;
import listener.task.PauseResumeListener;
import listener.task.SkipListener;
import listener.task.StopListener;
import listener.task.SubmitListener;

/**
 * Helper to interact with listener
 */
public class ListenerHelper {
    /**
     * Helper listener to be add to proper component
     * 
     * @param button   the component of button
     * @param listener event listener
     */
    public static void addListener(AbstractButton button, EventListener listener) {
        if (listener instanceof ActionListener) {
            button.addActionListener(ActionListener.class.cast(listener));
        }
        if (listener instanceof MouseListener) {
            button.addMouseListener(MouseListener.class.cast(listener));
        }
        if (listener instanceof KeyListener) {
            button.addKeyListener(KeyListener.class.cast(listener));
        }
        if (listener instanceof ItemListener) {
            button.addItemListener(ItemListener.class.cast(listener));
        }
        if (listener instanceof FocusListener) {
            button.addFocusListener(FocusListener.class.cast(listener));
        }
        if (listener instanceof ChangeListener) {
            button.addChangeListener(ChangeListener.class.cast(listener));
        }
        if (listener instanceof AncestorListener) {
            button.addAncestorListener(AncestorListener.class.cast(listener));
        }
        if (listener instanceof ComponentListener) {
            button.addComponentListener(ComponentListener.class.cast(listener));
        }
        if (listener instanceof ContainerListener) {
            button.addContainerListener(ContainerListener.class.cast(listener));
        }
        if (listener instanceof HierarchyListener) {
            button.addHierarchyListener(HierarchyListener.class.cast(listener));
        }
        if (listener instanceof MouseWheelListener) {
            button.addMouseWheelListener(MouseWheelListener.class.cast(listener));
        }
        if (listener instanceof InputMethodListener) {
            button.addInputMethodListener(InputMethodListener.class.cast(listener));
        }
        if (listener instanceof MouseMotionListener) {
            button.addMouseMotionListener(MouseMotionListener.class.cast(listener));
        }
        if (listener instanceof VetoableChangeListener) {
            button.addVetoableChangeListener(VetoableChangeListener.class.cast(listener));
        }
        if (listener instanceof PropertyChangeListener) {
            button.addPropertyChangeListener(PropertyChangeListener.class.cast(listener));
        }
    }

    /**
     * Helper listener to be add to proper component
     * 
     * @param component the component of JComponent
     * @param listener  event listener
     */
    public static void addListener(JComponent component, EventListener listener) {
        if (listener instanceof MouseListener) {
            component.addMouseListener(MouseListener.class.cast(listener));
        }
        if (listener instanceof KeyListener) {
            component.addKeyListener(KeyListener.class.cast(listener));
        }
        if (listener instanceof FocusListener) {
            component.addFocusListener(FocusListener.class.cast(listener));
        }
        if (listener instanceof AncestorListener) {
            component.addAncestorListener(AncestorListener.class.cast(listener));
        }
        if (listener instanceof ComponentListener) {
            component.addComponentListener(ComponentListener.class.cast(listener));
        }
        if (listener instanceof ContainerListener) {
            component.addContainerListener(ContainerListener.class.cast(listener));
        }
        if (listener instanceof HierarchyListener) {
            component.addHierarchyListener(HierarchyListener.class.cast(listener));
        }
        if (listener instanceof MouseWheelListener) {
            component.addMouseWheelListener(MouseWheelListener.class.cast(listener));
        }
        if (listener instanceof InputMethodListener) {
            component.addInputMethodListener(InputMethodListener.class.cast(listener));
        }
        if (listener instanceof MouseMotionListener) {
            component.addMouseMotionListener(MouseMotionListener.class.cast(listener));
        }
        if (listener instanceof VetoableChangeListener) {
            component.addVetoableChangeListener(VetoableChangeListener.class.cast(listener));
        }
        if (listener instanceof PropertyChangeListener) {
            component.addPropertyChangeListener(PropertyChangeListener.class.cast(listener));
        }
        if (listener instanceof HierarchyBoundsListener) {
            component.addHierarchyBoundsListener(HierarchyBoundsListener.class.cast(listener));
        }
    }

    /**
     * Help listener type to be add to component
     * 
     * @param comp      component
     * @param mainFrame main frame
     * @param type      listener type
     */
    public static void accept(ComponentSystem comp, MainFrame mainFrame, ListenerType type)
            throws IllegalArgumentException {
        if (comp instanceof AbstractButton) {
            AbstractButton button = AbstractButton.class.cast(comp);
            if (type == ListenerType.Arbitrary) {
                comp.addListener(new ArbitraryListener(mainFrame, button));
            } else if (type == ListenerType.ClearOutput) {
                comp.addListener(new ClearOutputListener(mainFrame, button));
            } else if (type == ListenerType.ExitProgram) {
                comp.addListener(new ExitProgramListener(mainFrame, button));
            } else if (type == ListenerType.ImportBackgroudImage) {
                comp.addListener(new ImportBackgroundImageListener(mainFrame, button));
            } else if (type == ListenerType.None) {
                comp.addListener(new NoneListener(mainFrame, button));
            } else if (type == ListenerType.OpenLoadFile) {
                comp.addListener(new OpenLoadFileListener(mainFrame, button));
            } else if (type == ListenerType.Redo) {
                comp.addListener(new RedoListener(mainFrame, button));
            } else if (type == ListenerType.Undo) {
                comp.addListener(new UndoListener(mainFrame, button));
            } else if (type == ListenerType.Reset) {
                comp.addListener(new ResetListener(mainFrame, button));
            } else if (type == ListenerType.SaveAsFile) {
                comp.addListener(new SaveAsFileListener(mainFrame, button));
            } else if (type == ListenerType.SaveFile) {
                comp.addListener(new SaveFileListener(mainFrame, button));
            } else if (type == ListenerType.SortOrder) {
                comp.addListener(new SortOrderListener(mainFrame, button));
            } else if (type == ListenerType.SortDataType) {
                comp.addListener(new SortDataTypeListener(mainFrame, button));
            } else if (type == ListenerType.SortProcessInterval) {
                comp.addListener(new SortProcessIntervalListener(mainFrame, button));
            } else if (type == ListenerType.IOFilter) {
                comp.addListener(new IOFilterListener(mainFrame, button));
            } else if (type == ListenerType.IOFieldAlpha) {
                comp.addListener(new IOFieldAlphaListener(mainFrame, button));
            } else if (type == ListenerType.ColorSelect) {
                comp.addListener(new ColorSelectListener(mainFrame, button));
            } else if (type == ListenerType.Submit) {
                comp.addListener(new SubmitListener(mainFrame, button));
            } else if (type == ListenerType.PauseResume) {
                comp.addListener(new PauseResumeListener(mainFrame, button));
            } else if (type == ListenerType.Skip) {
                comp.addListener(new SkipListener(mainFrame, button));
            } else if (type == ListenerType.Stop) {
                comp.addListener(new StopListener(mainFrame, button));
            } else if (type == ListenerType.ThemeSelect) {
                comp.addListener(new ThemeSelectListener(mainFrame, button));
            } else if (type == ListenerType.LookAndFeelSelect) {
                comp.addListener(new LookAndFeelSelectListener(mainFrame, button));
            } else if (type == ListenerType.Help) {
                comp.addListener(new HelpListener(mainFrame, button));
            } else if (type == ListenerType.VersionInfo) {
                comp.addListener(new VersionInfoListener(mainFrame, button));
            } else {
                throw new IllegalArgumentException("Invalid listener type of instanceof AbstractButton!");
            }
        } else if (comp instanceof JComponent) {
            JComponent jComp = JComponent.class.cast(comp);
            if (type == ListenerType.InputField) {
                comp.addListener(new InputFieldListener(mainFrame, jComp));
            } else if (type == ListenerType.OutputField) {
                comp.addListener(new OutputFieldListener(mainFrame, jComp));
            } else if (type == ListenerType.ResultField) {
                comp.addListener(new ResultFieldListener(mainFrame, jComp));
            } else if (type == ListenerType.RankField) {
                comp.addListener(new RankFieldListener(mainFrame, jComp));
            } else {
                throw new IllegalArgumentException("Invalid listener type of instanceof JComponent!");
            }
        }
    }
}