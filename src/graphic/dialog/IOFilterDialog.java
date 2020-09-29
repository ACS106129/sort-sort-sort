package graphic.dialog;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import filter.Filter;
import filter.FilterType;
import graphic.component.CTextPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import listener.field.FieldFocusListener;
import manager.IOManager;
import manager.ImageManager;
import setting.Setting;
import utility.Utility;

public class IOFilterDialog extends JDialog {

    private static final long serialVersionUID = 1003L;

    private static final String ImportFilter = "Import Filter";

    private static final String SortFilter = "Sort Filter";

    private static final String ExportFilter = "Export Filter";

    private static final String Option = "Option";

    private static final String[] optionName = { "Save Inconsistent Element", "Use scientific notation" };

    // panel name, check order id
    private static final Map<String, Set<Integer>> defaultRecordsCheckMaps = new LinkedHashMap<>();

    // panel name, check order id
    private static Map<String, Set<Integer>> recordsCheckMaps = new LinkedHashMap<>();

    // view the last combo box position
    private static Map<String, FilterType> lastViewedComboBoxMaps = new LinkedHashMap<>();

    // code content in text pane
    private static Map<Pair<String, Integer>, String> recordsCodeMaps = new LinkedHashMap<>();

    static {
        defaultRecordsCheckMaps.put(ImportFilter, new LinkedHashSet<>());
        defaultRecordsCheckMaps.put(SortFilter, new LinkedHashSet<>());
        defaultRecordsCheckMaps.put(ExportFilter, new LinkedHashSet<>());
        defaultRecordsCheckMaps.put(Option, new LinkedHashSet<>(Arrays.asList(1)));

        recordsCheckMaps.put(ImportFilter, new LinkedHashSet<>());
        recordsCheckMaps.put(SortFilter, new LinkedHashSet<>());
        recordsCheckMaps.put(ExportFilter, new LinkedHashSet<>());
        recordsCheckMaps.put(Option, new LinkedHashSet<>(Arrays.asList(1)));

        lastViewedComboBoxMaps.put(ImportFilter, FilterType.Number);
        lastViewedComboBoxMaps.put(SortFilter, FilterType.Number);
        lastViewedComboBoxMaps.put(ExportFilter, FilterType.Number);

        // initialize the code content
        for (String key : lastViewedComboBoxMaps.keySet()) {
            for (FilterType type : FilterType.values()) {
                recordsCodeMaps.put(new Pair<String, Integer>(key, type.getOrder()), "");
            }
        }
    }

    private Map<String, Set<Integer>> tempRecordsCheckMaps = ((Supplier<Map<String, Set<Integer>>>) () -> {
        Map<String, Set<Integer>> maps = new LinkedHashMap<>();
        maps.put(ImportFilter, new LinkedHashSet<>(recordsCheckMaps.get(ImportFilter)));
        maps.put(SortFilter, new LinkedHashSet<>(recordsCheckMaps.get(SortFilter)));
        maps.put(ExportFilter, new LinkedHashSet<>(recordsCheckMaps.get(ExportFilter)));
        maps.put(Option, new LinkedHashSet<>(recordsCheckMaps.get(Option)));
        return maps;
    }).get();

    private final JFXPanel mainPanel = new JFXPanel();

    private class CardPanel extends JFXPanel {
        private static final long serialVersionUID = 10030L;

        public FilterType filterType = null;

        public String panelType = null;
    }

    // panel name, panel
    private Map<String, JFXPanel> panelsMap = new LinkedHashMap<>();

    // panel name, comboBox
    private Map<String, JComboBox<FilterType>> comboBoxesMap = new LinkedHashMap<>();

    // panel name, data type + card content
    private Map<String, List<CardPanel>> cardsMap = new LinkedHashMap<>();

    private Button okButton = new Button("OK");

    private Button cancelButton = new Button("Cancel");

    private Button defaultButton = new Button("Default");

    public IOFilterDialog(Frame owner) {
        super(owner, "IO Filter", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setContentPane(mainPanel);
        setLayout(new FlowLayout());
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(owner);
        initButton();
        initComps();
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            BackgroundSize size = new BackgroundSize(getSize().width, getSize().height, false, false, false, false);
            BackgroundImage background = new BackgroundImage(
                    new Image(ImageManager.getFile("bg_06").toURI().toString()), BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, size);
            StackPane root = new StackPane();
            HBox hBox = new HBox();
            hBox.setPadding(new Insets(10));
            hBox.setSpacing(50);
            hBox.setAlignment(Pos.BOTTOM_CENTER);
            hBox.getChildren().addAll(okButton, cancelButton, defaultButton);
            root.getChildren().add(hBox);
            root.setBackground(new Background(background));
            mainPanel.setScene(new Scene(root));
            latch.countDown();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelButton.fire();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.gc();
            }
        });
        for (JFXPanel panel : panelsMap.values()) {
            add(panel);
        }
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setVisible(true);
    }

    private void initButton() {
        okButton.setOnMouseEntered(e -> {
            okButton.setEffect(new DropShadow());
        });
        okButton.setOnMouseExited(e -> {
            okButton.setEffect(null);
        });
        okButton.setOnAction(e -> {
            // import, sort, export filter, option panel comboBox
            panelsMap.forEach((key, value) -> {
                for (Component comp : value.getComponents()) {
                    if (comp instanceof JComboBox) {
                        if (!lastViewedComboBoxMaps.containsKey(key)) {
                            continue;
                        }
                        lastViewedComboBoxMaps.put(key, (FilterType) JComboBox.class.cast(comp).getSelectedItem());
                    }
                }
            });
            // import, sort, export filter, option card panel component
            for (String key : recordsCheckMaps.keySet()) {
                // option component
                if (key.equals(Option)) {
                    for (Component comp : panelsMap.get(key).getComponents()) {
                        if (comp instanceof JCheckBox) {
                            JCheckBox checkBox = JCheckBox.class.cast(comp);
                            String choose = checkBox.getText();
                            boolean isSelected = checkBox.isSelected();
                            if (choose.equals(optionName[0])) {
                                IOManager.isSaveInconsistentElement = isSelected;
                            } else if (choose.equals(optionName[1])) {
                                IOManager.isUseScientificNotation = isSelected;
                            }
                        }
                    }
                    continue;
                }
                for (CardPanel cardPanel : cardsMap.get(key)) {
                    Map<Pair<String, Integer>, String> tempCodeMaps = new LinkedHashMap<>();
                    // check for syntax
                    for (Component comp : cardPanel.getComponents()) {
                        if (comp instanceof JScrollPane) {
                            Component view = JScrollPane.class.cast(comp).getViewport().getView();
                            if (view instanceof CTextPane) {
                                CTextPane codePane = CTextPane.class.cast(view);
                                // if code is initial text, set code to empty
                                String code = codePane.getText().equals(codePane.getInitialText()) ? ""
                                        : codePane.getText();
                                try {
                                    checkSyntax(cardPanel, code);
                                } catch (IllegalArgumentException iae) {
                                    // stop ok process because code pane syntax wrong
                                    JOptionPane.showMessageDialog(this, iae.getMessage(), "Syntax Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                tempCodeMaps.put(new Pair<String, Integer>(key, cardPanel.filterType.getOrder()), code);
                            }
                        }
                    }
                    // put to records
                    recordsCodeMaps.putAll(tempCodeMaps);
                    // change the enable state
                    for (Component comp : cardPanel.getComponents()) {
                        if (comp instanceof JCheckBox) {
                            JCheckBox checkBox = JCheckBox.class.cast(comp);
                            if (key == ImportFilter) {
                                changeFilter(IOManager.importFilters, cardPanel, checkBox.isSelected());
                            } else if (key == SortFilter) {
                                changeFilter(IOManager.sortFilters, cardPanel, checkBox.isSelected());
                            } else if (key == ExportFilter) {
                                changeFilter(IOManager.exportFilters, cardPanel, checkBox.isSelected());
                            }
                        }
                    }
                }
            }
            dispose();
        });
        okButton.setDefaultButton(true);
        okButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        cancelButton.setOnMouseEntered(e -> {
            cancelButton.setEffect(new DropShadow());
        });
        cancelButton.setOnMouseExited(e -> {
            cancelButton.setEffect(null);
        });
        cancelButton.setOnAction(e -> {
            recordsCheckMaps = tempRecordsCheckMaps;
            dispose();
        });
        cancelButton.setCancelButton(true);
        cancelButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        defaultButton.setOnMouseEntered(e -> {
            defaultButton.setEffect(new DropShadow());
        });
        defaultButton.setOnMouseExited(e -> {
            defaultButton.setEffect(null);
        });
        // refresh the component
        defaultButton.setOnAction(e -> {
            defaultRecordsCheckMaps.forEach((key, value) -> {
                // option component
                if (key.equals(Option)) {
                    for (Component comp : panelsMap.get(Option).getComponents()) {
                        if (comp instanceof JCheckBox) {
                            JCheckBox checkBox = JCheckBox.class.cast(comp);
                            String choose = checkBox.getText();
                            boolean saveIEOK = value.contains(0) && choose.equals(optionName[0]);
                            boolean useSNOK = value.contains(1) && choose.equals(optionName[1]);
                            if (saveIEOK || useSNOK) {
                                checkBox.setSelected(true);
                            } else {
                                checkBox.setSelected(false);
                            }
                        }
                    }
                    return;
                }
                // import, sort, export filter component
                for (CardPanel cardPanel : cardsMap.get(key)) {
                    for (Component comp : cardPanel.getComponents()) {
                        if (comp instanceof JCheckBox) {
                            JCheckBox checkBox = JCheckBox.class.cast(comp);
                            if (value.contains(cardPanel.filterType.getOrder())) {
                                checkBox.setSelected(true);
                            } else {
                                checkBox.setSelected(false);
                            }
                        }
                    }
                }
            });
        });
        defaultButton.setStyle("-fx-font: 18 arial; -fx-base: #363535;");
    }

    private void initComps() {
        panelsMap.put(ImportFilter, null);
        panelsMap.put(SortFilter, null);
        panelsMap.put(ExportFilter, null);
        panelsMap.put(Option, null);
        comboBoxesMap.put(ImportFilter, null);
        comboBoxesMap.put(SortFilter, null);
        comboBoxesMap.put(ExportFilter, null);
        cardsMap.put(ImportFilter, null);
        cardsMap.put(SortFilter, null);
        cardsMap.put(ExportFilter, null);
        // init combo boxes map
        for (String key : comboBoxesMap.keySet()) {
            JComboBox<FilterType> comboBox = new JComboBox<>(FilterType.values());
            comboBox.setFont(new Font("arial", Font.PLAIN, 16));
            comboBox.setEditable(false);
            comboBoxesMap.put(key, comboBox);
        }
        // init cards map
        for (String key : cardsMap.keySet()) {
            List<CardPanel> panels = new ArrayList<>();
            for (FilterType dataType : FilterType.values()) {
                CardPanel panel = new CardPanel();
                panel.setLayout(new FlowLayout(FlowLayout.CENTER));
                panel.filterType = dataType;
                panel.panelType = key;
                panels.add(panel);
            }
            cardsMap.put(key, panels);
        }
        // init panels map
        for (String key : panelsMap.keySet()) {
            JFXPanel panel = new JFXPanel();
            TitledBorder border = new TitledBorder(key);
            border.setTitleFont(new Font("arial", Font.BOLD, 20));
            border.setTitleColor(new Color(182, 255, 110));
            panel.setBorder(border);
            if (key != Option) {
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setPreferredSize(new Dimension((int) (getSize().width * 0.3), 230));
                JFXPanel cardsPanel = new JFXPanel();
                cardsPanel.setLayout(new CardLayout(10, 10));
                comboBoxesMap.get(key)
                        .setPreferredSize(new Dimension((int) (panel.getPreferredSize().width * 0.9), 30));
                comboBoxesMap.get(key).addItemListener(event -> {
                    if (!(event.getSource() instanceof JComboBox)) {
                        return;
                    }
                    CardLayout cl = CardLayout.class.cast(cardsPanel.getLayout());
                    cl.show(cardsPanel, event.getItem().toString());
                });
                initCards(key, panel);
                // add cards to card panel and set type
                for (CardPanel cardPanel : cardsMap.get(key)) {
                    cardsPanel.add(cardPanel, cardPanel.filterType.toString());
                }
                comboBoxesMap.get(key).setSelectedItem(lastViewedComboBoxMaps.get(key));
                // add comboBox
                panel.add(comboBoxesMap.get(key));
                // add content card
                panel.add(cardsPanel);
            } else {
                // save inconsistent element
                JCheckBox saveIEBox = new JCheckBox(optionName[0]);
                saveIEBox.setOpaque(false);
                saveIEBox.setFont(new Font("arial", Font.PLAIN, 16));
                saveIEBox.setForeground(Color.WHITE);
                if (recordsCheckMaps.get(Option).contains(0)) {
                    saveIEBox.setSelected(true);
                }
                saveIEBox.addItemListener(event -> {
                    if (event.getStateChange() == ItemEvent.SELECTED) {
                        recordsCheckMaps.get(Option).add(0);
                    } else if (event.getStateChange() == ItemEvent.DESELECTED) {
                        recordsCheckMaps.get(Option).remove(0);
                    }
                });
                // use scientific notation
                JCheckBox useSNBox = new JCheckBox(optionName[1]);
                useSNBox.setOpaque(false);
                useSNBox.setFont(new Font("arial", Font.PLAIN, 16));
                useSNBox.setForeground(Color.WHITE);
                if (recordsCheckMaps.get(Option).contains(1)) {
                    useSNBox.setSelected(true);
                }
                useSNBox.addItemListener(event -> {
                    if (event.getStateChange() == ItemEvent.SELECTED) {
                        recordsCheckMaps.get(Option).add(1);
                    } else if (event.getStateChange() == ItemEvent.DESELECTED) {
                        recordsCheckMaps.get(Option).remove(1);
                    }
                });
                panel.setLayout(new FlowLayout());
                panel.setPreferredSize(new Dimension((int) (getSize().width * 0.9), 80));
                panel.add(saveIEBox);
                panel.add(useSNBox);
            }
            panelsMap.put(key, panel);
        }
    }

    private void initCards(String key, JFXPanel panel) {
        for (FilterType type : FilterType.values()) {
            JCheckBox enableBox = new JCheckBox("Enable");
            enableBox.setOpaque(false);
            enableBox.setFont(new Font("arial", Font.PLAIN, 16));
            CTextPane codePane = new CTextPane();
            codePane.setPreferredSize(new Dimension((int) (panel.getPreferredSize().width * 0.8), 110));
            codePane.setFont(new Font("arial", Font.PLAIN, 13));
            codePane.setOpacity(Setting.IOFieldAlpha);
            codePane.addListener(new FieldFocusListener() {
                @Override
                public void focusGained(FocusEvent event) {
                    focusGained(event, Color.BLACK);
                }

                @Override
                public void focusLost(FocusEvent event) {
                    focusLost(event, Color.BLACK);
                }
            });
            JScrollPane scrollPane = new JScrollPane(codePane);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setOpaque(false);
            codePane.setInitialText("Please Enter Condition:\r\n(Parameter use \"target\")");
            codePane.setText(recordsCodeMaps.get(new Pair<String, Integer>(key, type.getOrder())));
            // give initial select
            if (recordsCheckMaps.get(key).contains(type.getOrder())) {
                if (codePane.getText().equals(codePane.getInitialText())) {
                    codePane.setForeground(Color.GRAY);
                }
                enableBox.setSelected(true);
            } else {
                codePane.setEnabled(false);
                if (codePane.getText().equals(codePane.getInitialText())) {
                    codePane.setText("");
                }
            }
            // add listener
            enableBox.addItemListener(event -> {
                Object object = enableBox.getParent();
                if (!(object instanceof CardPanel)) {
                    return;
                }
                CardPanel cardPanel = CardPanel.class.cast(object);
                String ini = codePane.getInitialText();
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    recordsCheckMaps.get(cardPanel.panelType).add(cardPanel.filterType.getOrder());
                    codePane.setEnabled(true);
                    if (codePane.getText().isEmpty()) {
                        codePane.setText(ini);
                        codePane.setForeground(Color.GRAY);
                    }
                } else if (event.getStateChange() == ItemEvent.DESELECTED) {
                    recordsCheckMaps.get(cardPanel.panelType).remove(cardPanel.filterType.getOrder());
                    if (codePane.getText().equals(ini) || codePane.getText().matches("^\\s+$")) {
                        codePane.setText("");
                    }
                    codePane.setEnabled(false);
                }
            });
            // add to card panel
            for (CardPanel cardPanel : cardsMap.get(key)) {
                if (cardPanel.filterType == type) {
                    cardPanel.add(enableBox);
                    cardPanel.add(scrollPane);
                    break;
                }
            }
        }
    }

    private void checkSyntax(CardPanel cardPanel, String syntax) throws IllegalArgumentException {
        if (syntax.isEmpty())
            return;
    }

    private void changeFilter(List<Filter<?>> filters, CardPanel cardPanel, boolean isEnable) {
        // remove already exist filter
        filters.removeIf(filter -> {
            return filter.genericType == cardPanel.filterType.getValue();
        });
        if (!isEnable) {
            return;
        }
        String code = recordsCodeMaps
                .get(new Pair<String, Integer>(cardPanel.panelType, cardPanel.filterType.getOrder()));
        filters.add(Utility.getFilterInstance(cardPanel.filterType, code));
    }
}