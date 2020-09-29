package listener.task;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import graphic.MainFrame;
import graphic.component.CTextPane;
import graphic.component.base.ComponentName;
import graphic.component.base.ComponentState;
import graphic.component.base.ComponentSystem;
import javafx.util.Pair;
import listener.base.ListenerSystem;
import listener.base.ListenerType;
import manager.FileManager;
import manager.IOManager;
import setting.Setting;
import sort.base.SortSystem;
import utility.ExecuteWorker;
import utility.Utility;

/**
 * Listener to submit the file array
 */
public class SubmitListener extends ListenerSystem implements ActionListener {

    private static String result = "";

    public SubmitListener(MainFrame mainFrame, AbstractButton button) {
        this.mainFrame = mainFrame;
        comps.add(button);
        button.setEnabled(ComponentState.isSubmitEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int choosed = JOptionPane.showConfirmDialog(mainFrame, "Are you sure to submit?", "Submit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choosed == JOptionPane.YES_OPTION) {
            boolean isOutput = false;
            // get the input field text
            for (Component comp : getComponents(ListenerType.InputField)) {
                if (comp instanceof CTextPane) {
                    CTextPane inputPane = CTextPane.class.cast(comp);
                    String input = inputPane.getText();
                    // if input is null continue
                    if (input.isEmpty()
                            || (inputPane.getForeground() == Color.GRAY && input.equals(inputPane.getInitialText()))) {
                        JOptionPane.showMessageDialog(mainFrame, "Input is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    // enable the button during output
                    for (Component component : getComponents(ListenerType.PauseResume, ListenerType.Skip,
                            ListenerType.Stop)) {
                        component.setEnabled(true);
                    }
                    // disable the button during output
                    for (Component component : getComponents(ListenerType.Arbitrary, ListenerType.OpenLoadFile,
                            ListenerType.IOFilter, ListenerType.Submit, ListenerType.ColorSelect, ListenerType.Reset,
                            ListenerType.ClearOutput)) {
                        component.setEnabled(false);
                    }
                    // disable the menu during output
                    for (Component component : getComponents(ListenerType.None)) {
                        if (component instanceof ComponentSystem && ComponentSystem.class.cast(component)
                                .getComponentName() == ComponentName.Algorithm) {
                            component.setEnabled(false);
                        }
                    }
                    // reset the result pane
                    for (Component component : getComponents(ListenerType.ResultField, ListenerType.RankField)) {
                        if (!(component instanceof CTextPane))
                            continue;
                        CTextPane pane = CTextPane.class.cast(component);
                        if (pane.getListenerType() == ListenerType.ResultField)
                            pane.setForeground(Setting.resultPaneTextColor);
                        else
                            pane.setForeground(Setting.rankPaneTextColor);
                        pane.setText(pane.getInitialText());
                    }
                    IOManager.traceBackStrings.push(input);
                    // del if over the max times
                    int size = IOManager.traceBackStrings.size();
                    for (int i = IOManager.maxTraceBackTimes; i < size; ++i) {
                        // remove index from 0 to overed Indexes
                        IOManager.traceBackStrings.remove(i - IOManager.maxTraceBackTimes);
                    }
                    FileManager.setFileArray(input.trim().split(IOManager.splitRegex));
                    isOutput = true;
                    break;
                }
            }
            if (!isOutput) {
                return;
            }
            execWorker = new ExecuteWorker<>() {

                private final List<Callable<Void>> callables = new ArrayList<>();

                private final List<Pair<Integer, String>> ranks = Collections.synchronizedList(new ArrayList<>());

                @Override
                protected Void doInBackground() throws Exception {
                    // start sort
                    for (Component comp : getComponents(ListenerType.OutputField)) {
                        if (!(comp instanceof CTextPane) || CTextPane.class.cast(comp).getSortType() == null) {
                            continue;
                        }
                        callables.add(new Callable<Void>() {

                            private int step = 1;

                            private long computeTime = 0;

                            @Override
                            public Void call() throws Exception {
                                addPropertyChangeListener(e -> {
                                    new Thread(() -> {
                                        synchronized (this) {
                                            if (e.getPropertyName().equals("paused")) {
                                                if (Boolean.class.cast(e.getNewValue()))
                                                    return;
                                                this.notifyAll();
                                            }
                                        }
                                    }).start();
                                });
                                synchronized (this) {
                                    final CTextPane outputPane = CTextPane.class.cast(comp);
                                    try {
                                        final long start = System.currentTimeMillis();
                                        final SortSystem sortSystem = IOManager.getSortSystem(outputPane.getSortType());
                                        result = String.join(IOManager.displaySeparate,
                                                IOManager.makeSort(FileManager.getFileArray(), sortSystem));
                                        outputPane.setForeground(Setting.outputPaneTextColor);
                                        outputPane.setText(
                                                IOManager.startMessage + "( " + outputPane.getSortType() + " )\r\n");
                                        final Map<String, int[]> stepMaps = sortSystem.getArrayStepMaps();
                                        // iterate the entry of sorting maps
                                        for (final Entry<String, int[]> entry : stepMaps.entrySet()) {
                                            if (isPaused()) {
                                                this.wait();
                                            }
                                            if (isSkipped())
                                                break;
                                            else if (isCancelled())
                                                throw new InterruptedException("Sort stopped!");
                                            System.out.println(Runtime.getRuntime().totalMemory()
                                                    - Runtime.getRuntime().freeMemory());
                                            final String key = entry.getKey();
                                            final long perStart = System.currentTimeMillis();
                                            final String[] arrays = key.split(IOManager.displaySeparate);
                                            final int[] coloredIndice = entry.getValue();
                                            EventQueue.invokeAndWait(() -> {
                                                if (key.equals(result)) {
                                                    outputPane.append(IOManager.finishMessage + "\r\n");
                                                    --step;
                                                } else {
                                                    outputPane.append("Step " + step++ + ":=>");
                                                }
                                            });
                                            int i = 0;
                                            if (coloredIndice != null) {
                                                // sort coloredIndice in ascending
                                                Arrays.sort(coloredIndice);
                                                for (int index : coloredIndice) {
                                                    final String uncoloredStr = Utility.join(arrays,
                                                            IOManager.displaySeparate, i, index);
                                                    if (!uncoloredStr.isEmpty()) {
                                                        outputPane.append((i == 0 ? "" : IOManager.displaySeparate)
                                                                + uncoloredStr);
                                                    }
                                                    outputPane.append(index == 0 ? "" : IOManager.displaySeparate);
                                                    outputPane.append(arrays[index], Setting.changedIndexColor);
                                                    i = index + 1;
                                                }
                                            }
                                            outputPane.append(
                                                    (i == 0 || i == arrays.length ? "" : IOManager.displaySeparate)
                                                            + Utility.join(arrays, IOManager.displaySeparate, i,
                                                                    arrays.length));
                                            try {
                                                final long elapsed = System.currentTimeMillis() - perStart;
                                                computeTime += elapsed;
                                                if (!key.equals(result)) {
                                                    outputPane.append(" (Finished in " + elapsed + "ms)\r\n");
                                                }
                                                final long sleepTime = Setting.sortProcessInterval - elapsed;
                                                Thread.sleep(sleepTime < 0 ? 0 : sleepTime);
                                            } catch (InterruptedException ie) {
                                                ie.printStackTrace();
                                            }
                                            if (step % 500 == 0) {
                                                System.gc();
                                            }
                                        }
                                        final long totalTime = System.currentTimeMillis() - start;
                                        final String additionWord = isSkipped() ? "(Skipped)" : "";
                                        outputPane.append("\r\nTotal sort time: "
                                                + new DecimalFormat("#.###").format(totalTime / 1000.0) + " (sec)"
                                                + additionWord + ".\r\nTotal compute time: " + computeTime + " (ms)"
                                                + additionWord + ".\r\nTotal steps: " + stepMaps.size() + "(times).");
                                        ranks.add(new Pair<>(stepMaps.size(), outputPane.getSortType()));
                                    } catch (InterruptedException ie) {
                                        outputPane.append(ie.getMessage());
                                    } catch (Exception ex) {
                                        outputPane.setText("");
                                        outputPane.append("Nothing to sort!!!");
                                    }
                                }
                                return null;
                            }
                        });
                    }
                    final ExecutorService executor = Executors.newFixedThreadPool(callables.size());
                    executor.invokeAll(callables);
                    System.gc();
                    return null;
                }

                @Override
                protected void done() {
                    // disable the all component at end of output
                    for (Component component : getComponents(ListenerType.PauseResume, ListenerType.Skip,
                            ListenerType.Stop)) {
                        component.setEnabled(false);
                    }
                    // if pause is still exist, back to resume
                    for (Component component : getComponents(ListenerType.PauseResume)) {
                        if (isPaused() && component instanceof ComponentSystem) {
                            if (ComponentSystem.class.cast(component).getListenerType() != ListenerType.PauseResume)
                                continue;
                            for (ActionListener a : component.getListeners(ActionListener.class)) {
                                if (!(a instanceof PauseResumeListener))
                                    continue;
                                a.actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, ""));
                                break;
                            }
                        }
                        break;
                    }
                    // enable the all component at end of output
                    for (Component component : getComponents(ListenerType.None, ListenerType.Arbitrary,
                            ListenerType.OpenLoadFile, ListenerType.IOFilter, ListenerType.Submit,
                            ListenerType.ColorSelect, ListenerType.ClearOutput, ListenerType.Reset)) {
                        component.setEnabled(true);
                    }
                    if (!isCancelled()) {
                        // print the result to result pane
                        for (Component comp : getComponents(ListenerType.ResultField, ListenerType.RankField)) {
                            if (!(comp instanceof CTextPane))
                                continue;
                            CTextPane pane = CTextPane.class.cast(comp);
                            if (pane.getListenerType() == ListenerType.ResultField) {
                                pane.setForeground(Setting.resultPaneTextColor);
                                pane.setText(IOManager.finishMessage + "(" + IOManager.sortDataType + " sort in "
                                        + IOManager.sortOrder + " order)\r\n" + result);
                                continue;
                            }
                            String rankResult = "Ranking(" + IOManager.sortDataType + " sort in " + IOManager.sortOrder
                                    + " order):\r\n";
                            ranks.sort((left, right) -> {
                                final int leftInt = left.getKey();
                                final int rightInt = right.getKey();
                                if (leftInt > rightInt)
                                    return 1;
                                else if (leftInt < rightInt)
                                    return -1;
                                return 0;
                            });
                            int preSteps = -1;
                            int rank = 1;
                            for (int i = 0; i < ranks.size(); ++i) {
                                int steps = ranks.get(i).getKey();
                                if (preSteps != steps)
                                    rank = i + 1;
                                rankResult += rank + ": " + ranks.get(i).getValue() + "(" + steps + " steps)\r\n";
                                preSteps = steps;
                            }
                            pane.setForeground(Setting.rankPaneTextColor);
                            pane.setText(rankResult);
                            JOptionPane.showMessageDialog(mainFrame, rankResult, "Message", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                    callables.clear();
                    System.gc();
                }
            };
            execWorker.execute();
        }
        mainFrame.requestFocus();
    }
}