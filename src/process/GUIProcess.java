package process;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import graphic.MainFrame;
import graphic.component.CButton;
import graphic.component.CMenu;
import graphic.component.CMenuItem;
import graphic.component.CTextPane;
import graphic.component.base.ComponentName;
import listener.base.ListenerType;
import manager.ImageManager;
import setting.Setting;
import sort.base.SortDataType;
import sort.base.SortOrder;
import sort.base.SortType;

/**
 * Process of user interface
 */
public class GUIProcess {
    private static MainFrame mainFrame = null;
    private static JMenuBar menubar = null;
    // menus of menubar
    private static CMenu filemenu = null;
    private static CMenu editmenu = null;
    private static CMenu taskmenu = null;
    private static CMenu optionmenu = null;
    private static CMenu aboutmenu = null;
    // items of file component
    private static CMenuItem olfile = null;
    private static CMenuItem sfile = null;
    private static CMenuItem sAfile = null;
    private static CMenuItem arbitrary = null;
    private static CMenuItem ibg = null;
    private static CMenuItem theme = null;
    private static CMenuItem lookAndFeel = null;
    private static CMenuItem exit = null;
    // items of edit component
    private static CMenuItem undo = null;
    private static CMenuItem redo = null;
    private static CMenuItem reset_input = null;
    private static CMenuItem clear_output = null;
    // items of task component
    private static CMenuItem submit = null;
    private static CMenuItem pause_resume = null;
    private static CMenuItem skip = null;
    private static CMenuItem stop = null;
    // items of option component
    private static CMenuItem ioFilter = null;
    private static CMenuItem sortProcess = null;
    private static CMenuItem ioFieldAlpha = null;
    private static CMenu algorithm = null;
    private static CMenuItem number = null;
    private static CMenuItem string = null;
    private static CMenuItem ascending = null;
    private static CMenuItem descending = null;
    private static CMenuItem changeColor = null;
    // items of about component
    private static CMenuItem help;
    private static CMenuItem version;

    private GUIProcess() {
    }

    public static void run(MainFrame window) {
        mainFrame = window;
        initialize(mainFrame);
        CButton submit_button = new CButton("", ComponentName.Submit, ListenerType.Submit, mainFrame);
        CButton pause_resume_button = new CButton("", ComponentName.Pause, ListenerType.PauseResume, mainFrame);
        CButton stop_button = new CButton("", ComponentName.Stop, ListenerType.Stop, mainFrame);
        CButton skip_button = new CButton("", ComponentName.Skip, ListenerType.Skip, mainFrame);
        // common set of button
        Arrays.asList(submit_button, pause_resume_button, stop_button, skip_button).forEach(button -> {
            String compName = button.getComponentName().toString();
            button.setIcon(ImageManager.getImageIcon(compName, new Dimension(70, 70)));
            button.setPressedIcon(ImageManager.getImageIcon(compName + "_pressed", new Dimension(70, 70)));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        });

        JTabbedPane sortTP = new JTabbedPane(JTabbedPane.LEFT);
        sortTP.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 20));
        // linked map of sort pane
        Map<String, JScrollPane> sortPaneMaps = new LinkedHashMap<>();
        sortPaneMaps.put(SortType.Insertion, null);
        sortPaneMaps.put(SortType.SelectionSort, null);
        sortPaneMaps.put(SortType.BubbleSort, null);
        sortPaneMaps.put(SortType.QuickSort, null);
        sortPaneMaps.put(SortType.MergeSort, null);
        sortPaneMaps.put(SortType.CocktailSort, null);
        sortPaneMaps.put(SortType.ExchangeSort, null);
        sortPaneMaps.replaceAll((sortType, sortScrollPane) -> {
            CTextPane sortPane = new CTextPane("This area will display the " + sortType.toLowerCase() + ":",
                    ComponentName.Undefined, ListenerType.OutputField, sortType, mainFrame);
            sortPane.setPreferredSize(new Dimension(400, 350));
            sortPane.setOpacity(Setting.IOFieldAlpha);
            sortPane.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 16));
            sortPane.setEditable(false);
            sortScrollPane = new JScrollPane(sortPane);
            sortScrollPane.getViewport().setOpaque(false);
            sortScrollPane.setOpaque(false);
            // add pane to tab
            sortTP.addTab(sortPane.getSortType().replace(" sort", ""), sortScrollPane);
            return sortScrollPane;
        });

        Map<CTextPane, JScrollPane> IOMaps = new LinkedHashMap<>();
        CTextPane input_pane = new CTextPane("Please input the data that you need to arrange", ComponentName.Undefined,
                ListenerType.InputField, mainFrame);
        CTextPane result_pane = new CTextPane("Result will show here:", ComponentName.Undefined,
                ListenerType.ResultField, mainFrame);
        result_pane.setEditable(false);
        CTextPane rank_pane = new CTextPane("Rank will show here:", ComponentName.Undefined, ListenerType.RankField,
                mainFrame);
        rank_pane.setEditable(false);
        IOMaps.put(input_pane, null);
        IOMaps.put(result_pane, null);
        IOMaps.put(rank_pane, null);
        // common set of input, result and rank pane
        IOMaps.replaceAll((IOPane, scroll) -> {
            IOPane.setOpacity(Setting.IOFieldAlpha);
            IOPane.setFont(new Font("Microsoft Tai Le", Font.PLAIN, 16));
            scroll = new JScrollPane(IOPane);
            scroll.getViewport().setOpaque(false);
            scroll.setOpaque(false);
            return scroll;
        });

        JPanel wholePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel leftSidePanel = new JPanel();
        JPanel rightSidePanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel, BoxLayout.Y_AXIS));
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel, BoxLayout.Y_AXIS));

        IOMaps.get(input_pane).setMaximumSize(new Dimension(400, 200));
        IOMaps.get(input_pane).setPreferredSize(new Dimension(400, 200));
        sortTP.setPreferredSize(sortTP.getPreferredSize());
        IOMaps.get(rank_pane).setMaximumSize(new Dimension(215, 200));
        IOMaps.get(rank_pane).setPreferredSize(new Dimension(215, 200));
        IOMaps.get(result_pane).setMaximumSize(new Dimension(215, 320));
        IOMaps.get(result_pane).setPreferredSize(new Dimension(215, 320));

        wholePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        leftSidePanel.setOpaque(false);
        rightSidePanel.setOpaque(false);

        buttonPanel.add(submit_button);
        buttonPanel.add(pause_resume_button);
        buttonPanel.add(skip_button);
        buttonPanel.add(stop_button);
        leftSidePanel.add(IOMaps.get(input_pane));
        leftSidePanel.add(buttonPanel);
        leftSidePanel.add(sortTP);
        rightSidePanel.add(IOMaps.get(rank_pane));
        rightSidePanel.add(Box.createRigidArea(new Dimension(0, 70)));
        rightSidePanel.add(IOMaps.get(result_pane));
        wholePanel.add(leftSidePanel);
        wholePanel.add(Box.createRigidArea(new Dimension(80, 0)));
        wholePanel.add(rightSidePanel);
        for (Component comp : leftSidePanel.getComponents()) {
            if (comp instanceof JComponent) {
                JComponent.class.cast(comp).setAlignmentX(Component.RIGHT_ALIGNMENT);
            }
        }
        mainFrame.add(wholePanel);
    }

    private static void initialize(MainFrame mainFrame) {
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setBackground(ImageManager.getFile("background"));
        mainFrame.setIconImage(ImageManager.getCImage("sorticon", null).toImage());
        menubar = new JMenuBar();
        menubar.setBackground(Color.WHITE);
        mainFrame.setJMenuBar(menubar);
        MenuComponentAppend();
    }

    private static void MenuComponentAppend() {
        filemenu = new CMenu("(F)ile", ComponentName.File, mainFrame);
        FileItemsAppend();
        editmenu = new CMenu("(E)dit", ComponentName.Edit, mainFrame);
        EditItemsAppend();
        taskmenu = new CMenu("(T)ask", ComponentName.Task, mainFrame);
        TaskItemAppend();
        optionmenu = new CMenu("(O)ption", ComponentName.Option, mainFrame);
        OptionItemsAppend();
        aboutmenu = new CMenu("(A)bout", ComponentName.About, mainFrame);
        AboutItemsAppend();
        // common set of menu
        Arrays.asList(filemenu, editmenu, taskmenu, optionmenu, aboutmenu).forEach(menu -> {
            menu.setFont(new Font("TimesRoman", Font.PLAIN, 16));
            menu.setMargin(new Insets(0, 3, 0, 3));
            menubar.add(menu);
        });
    }

    private static void AboutItemsAppend() {
        help = new CMenuItem("Help", ComponentName.Help, ListenerType.Help, mainFrame);
        version = new CMenuItem("Version", ComponentName.Version, ListenerType.VersionInfo, mainFrame);
        // common set of about menu items
        Arrays.asList(help, version).forEach(aboutItem -> {
            ComponentName compName = aboutItem.getComponentName();
            aboutItem.setIcon(ImageManager.getImageIcon(compName.toString(), new Dimension(16, 16)));
            aboutItem.setMinimumSize(new Dimension(125, 25));
            aboutmenu.add(aboutItem);
        });
    }

    private static void FileItemsAppend() {
        olfile = new CMenuItem("Open/Load file", ComponentName.Open, ListenerType.OpenLoadFile, mainFrame);
        sfile = new CMenuItem("Save file", ComponentName.Save, ListenerType.SaveFile, mainFrame);
        sAfile = new CMenuItem("Save As...", ComponentName.SaveAs, ListenerType.SaveAsFile, mainFrame);
        arbitrary = new CMenuItem("Arbitrary generate", ComponentName.Arbitrary, ListenerType.Arbitrary, mainFrame);
        ibg = new CMenuItem("Import Background Image", ComponentName.ImportImage, ListenerType.ImportBackgroudImage,
                mainFrame);
        theme = new CMenuItem("Select Theme", ComponentName.Theme, ListenerType.ThemeSelect, mainFrame);
        lookAndFeel = new CMenuItem("Select Look & Feel", ComponentName.LookAndFeel, ListenerType.LookAndFeelSelect,
                mainFrame);
        exit = new CMenuItem("Exit", ComponentName.Exit, ListenerType.ExitProgram, mainFrame);
        // common set of file menu items
        Arrays.asList(olfile, sfile, sAfile, arbitrary, ibg, theme, lookAndFeel, exit).forEach(fileItem -> {
            ComponentName compName = fileItem.getComponentName();
            if (compName == ComponentName.Exit)
                filemenu.addSeparator();
            fileItem.setIcon(ImageManager.getImageIcon(compName.toString(), new Dimension(16, 16)));
            fileItem.setMinimumSize(new Dimension(225, 25));
            filemenu.add(fileItem);
        });
    }

    private static void TaskItemAppend() {
        submit = new CMenuItem("Submit", ComponentName.Submit, ListenerType.Submit, mainFrame);
        pause_resume = new CMenuItem("Pause", ComponentName.Pause, ListenerType.PauseResume, mainFrame);
        skip = new CMenuItem("Skip", ComponentName.Skip, ListenerType.Skip, mainFrame);
        stop = new CMenuItem("Stop", ComponentName.Stop, ListenerType.Stop, mainFrame);
        // common set of task menu items
        Arrays.asList(submit, pause_resume, skip, stop).forEach(taskItem -> {
            ComponentName compName = taskItem.getComponentName();
            taskItem.setIcon(ImageManager.getImageIcon(compName.toString(), new Dimension(16, 16)));
            taskItem.setMinimumSize(new Dimension(200, 25));
            taskmenu.add(taskItem);
        });
    }

    private static void EditItemsAppend() {
        undo = new CMenuItem("Undo", ComponentName.Undo, ListenerType.Undo, mainFrame);
        redo = new CMenuItem("Redo", ComponentName.Redo, ListenerType.Redo, mainFrame);
        reset_input = new CMenuItem("Reset input", ComponentName.Reset, ListenerType.Reset, mainFrame);
        clear_output = new CMenuItem("Clear output", ComponentName.ClearOutput, ListenerType.ClearOutput, mainFrame);
        // common set of edit menu items
        Arrays.asList(undo, redo, reset_input, clear_output).forEach(editItem -> {
            ComponentName compName = editItem.getComponentName();
            if (compName == ComponentName.ClearOutput)
                editmenu.addSeparator();
            editItem.setIcon(ImageManager.getImageIcon(compName.toString(), new Dimension(16, 16)));
            editItem.setMinimumSize(new Dimension(150, 25));
            editmenu.add(editItem);
        });
    }

    private static void AlgorithmItemsAppend() {
        number = new CMenuItem(SortDataType.Number, ComponentName.Checked, ListenerType.SortDataType, mainFrame);
        string = new CMenuItem(SortDataType.String, ComponentName.Checked, ListenerType.SortDataType, mainFrame);
        ascending = new CMenuItem(SortOrder.Ascending, ComponentName.Checked, ListenerType.SortOrder, mainFrame);
        descending = new CMenuItem(SortOrder.Descending, ComponentName.Checked, ListenerType.SortOrder, mainFrame);
        // common set of algorithm menu items
        Arrays.asList(number, string, ascending, descending).forEach(algoItem -> {
            if (algoItem.getText() == SortOrder.Ascending)
                algorithm.addSeparator();
            algoItem.setMinimumSize(new Dimension(125, 25));
            algorithm.add(algoItem);
        });
    }

    private static void OptionItemsAppend() {
        ioFilter = new CMenuItem("I/O Filter", ComponentName.Filter, ListenerType.IOFilter, mainFrame);
        ioFieldAlpha = new CMenuItem("I/O Field Alpha", ComponentName.FieldAlpha, ListenerType.IOFieldAlpha, mainFrame);
        sortProcess = new CMenuItem("Sort Process Interval", ComponentName.SortProcessInterval,
                ListenerType.SortProcessInterval, mainFrame);
        algorithm = new CMenu("Algorithm", ComponentName.Algorithm, ListenerType.None, mainFrame);
        AlgorithmItemsAppend();
        changeColor = new CMenuItem("Change color style", ComponentName.ColorStyle, ListenerType.ColorSelect,
                mainFrame);
        // common set of option menu items
        Arrays.asList(ioFilter, ioFieldAlpha, sortProcess, algorithm, changeColor).forEach(optionItem -> {
            ComponentName compName = optionItem.getComponentName();
            if (compName == ComponentName.ColorStyle)
                optionmenu.addSeparator();
            optionItem.setIcon(ImageManager.getImageIcon(compName.toString(), new Dimension(16, 16)));
            optionItem.setMinimumSize(new Dimension(200, 25));
            optionmenu.add(optionItem);
        });
    }
}