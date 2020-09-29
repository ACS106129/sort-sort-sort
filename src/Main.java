import graphic.MainFrame;
import process.GUIProcess;
import process.InitProcess;

public class Main {
    public static void main(String[] args) {
        InitProcess.run();
        MainFrame mainFrame = new MainFrame(1000, 750, "Sort Program");
        GUIProcess.run(mainFrame);
        mainFrame.run();
    }
}
