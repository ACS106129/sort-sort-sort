package utility;

public class Regex {
    public static boolean isInteger(String str) {
        return isInteger(str, 10);
    }

    public static boolean isInteger(String str, int radix) {
        if (str.isEmpty())
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1)
                    return false;
                else
                    continue;
            }
            if (Character.digit(str.charAt(i), radix) < 0)
                return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        return str.matches("^[+-]?[0-9]+\\.[0-9]+$");
    }

    public static boolean isNumber(String str) {
        return str.matches("^[+-]?[0-9]+(\\.[0-9]+)?$");
    }

    public static boolean isScientificNotation(String str) {
        return str.matches("^[+-]?(?:0|[1-9]\\d*)(?:\\.\\d*)?(?:E[+-]?\\d+)?$");
    }
}