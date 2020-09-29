package setting;

/**
 * Theme of program decoration
 */
public enum Theme {
    Classic("Classic"), Plain("Plain"), Gradient("Gradient");

    private final String value;

    private Theme(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}