package graphic.dialog;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;

import graphic.component.CTextPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import setting.Setting;

public class SortProcessIntervalDialog extends JDialog {

    private static final long serialVersionUID = 1002L;

    private CTextPane textPane = new CTextPane();

    private final JFXPanel panel = new JFXPanel();

    private Button okButton = new Button("OK");

    private Button cancelButton = new Button("Cancel");

    private Button defaultButton = new Button("Default");

    private ScrollBar scrollBar = new ScrollBar();

    public SortProcessIntervalDialog(Frame owner) {
        super(owner, "Sort Process Interval", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(panel);
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setSize(400, 130);
        setResizable(false);
        setLocationRelativeTo(owner);
        initButton();
        initComps();
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            GridPane root = new GridPane();
            root.setHgap(10);
            root.setVgap(20);
            root.setPadding(new Insets(13, 10, 13, 10));

            root.add(scrollBar, 0, 0, 3, 1);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHalignment(HPos.RIGHT);
            cc.setPercentWidth(25);
            root.getColumnConstraints().add(cc);
            root.add(okButton, 0, 1);

            cc = new ColumnConstraints();
            cc.setHalignment(HPos.RIGHT);
            root.getColumnConstraints().add(cc);
            root.add(cancelButton, 1, 1);

            cc = new ColumnConstraints();
            cc.setHalignment(HPos.CENTER);
            cc.setPercentWidth(35);
            root.getColumnConstraints().add(cc);
            root.add(defaultButton, 2, 1);

            panel.setSize(getSize());
            panel.setScene(new Scene(root, getSize().width, getSize().height));
            latch.countDown();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.gc();
            }
        });
        add(textPane);
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
            Setting.sortProcessInterval = Integer.valueOf(textPane.getText().replace("ms", ""));
            SortProcessIntervalDialog.this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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
            SortProcessIntervalDialog.this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        cancelButton.setCancelButton(true);
        cancelButton.setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
        defaultButton.setOnMouseEntered(e -> {
            defaultButton.setEffect(new DropShadow());
        });
        defaultButton.setOnMouseExited(e -> {
            defaultButton.setEffect(null);
        });
        defaultButton.setOnAction(e -> {
            scrollBar.setValue(Setting.sortProcessIntervalDefault);
        });
        defaultButton.setStyle("-fx-font: 18 arial; -fx-base: #363535;");
    }

    private void initComps() {
        textPane.setText(String.valueOf(Setting.sortProcessInterval) + "ms");
        textPane.setOpaque(false);
        textPane.setEditable(false);
        scrollBar.setMin(0);
        scrollBar.setMax(Setting.sortProcessMax);
        scrollBar.setValue(Setting.sortProcessInterval);
        scrollBar.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            textPane.setText(String.valueOf(newvalue.intValue()) + "ms");
        });
    }
}