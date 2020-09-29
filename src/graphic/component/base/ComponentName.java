package graphic.component.base;

/**
 * Name of component
 */
public enum ComponentName {
    About("about"), Algorithm("algorithm"), Arbitrary("arbitrary"), Background("background"), Checked("checked"),
    ClearOutput("clear_output"), Color("color"), ColorStyle("color_style"), Edit("edit"), Exit("exit"), File("file"),
    Filter("filter"), Help("help"), ImportImage("import_image"), FieldAlpha("field_alpha"), Open("open"),
    Option("option"), Pause("pause"), Resume("resume"), Redo("redo"), Reset("reset"), Save("save"), SaveAs("saveas"),
    Skip("skip"), SortProcessInterval("sort_process_interval"), Stop("stop"), Submit("submit"), Task("task"),
    Theme("theme"), LookAndFeel("look_and_feel"), Undo("undo"), Version("version"), Undefined("undefined");

    private final String value;

    private ComponentName(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}