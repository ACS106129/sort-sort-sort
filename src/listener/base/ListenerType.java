package listener.base;

/**
 * Type of listener
 */
public enum ListenerType {
    Arbitrary, OpenLoadFile, SaveFile, SaveAsFile, ClearOutput, ImportBackgroudImage, InputField, OutputField,
    ResultField, RankField, Redo, Reset, Undo, VersionInfo, Submit, PauseResume, Stop, Skip, ExitProgram, ThemeSelect,
    ColorSelect, SortOrder, SortDataType, SortProcessInterval, IOFilter, IOFieldAlpha, LookAndFeelSelect, Help, None
}