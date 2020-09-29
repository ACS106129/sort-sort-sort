package manager;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

import javafx.util.Pair;
import utility.Regex;

/**
 * Manager for custom object
 */
public class CObjectManager {

    private static final File iniFile = new File("object.ini");

    private static final String iniDescription = "// Initial file for object declaration\r\n"
            + "// Object declaration:\r\n" + "// \r\n" + "// \t'Object [ObjectName]'\r\n" + "// \r\n"
            + "// Object data member declaration:\r\n" + "// \r\n" + "// For number declaration:\r\n"
            + "// \t'[DataName] := number'\r\n" + "// For number declaration with initial value:\r\n"
            + "// \t'[DataName] := number(value)'\r\n" + "// \r\n" + "// For string declaration:\r\n"
            + "// \t'[DataName] := string'\r\n" + "// For string declaration with initial value:\r\n"
            + "// \t'[DataName] := string(value)'\r\n" + "// \r\n" + "// Comment line use \"//\"\r\n" + "// \r\n";

    // object kind name, object attributes / object data type(Number, String)
    private static Map<String, Map<String, Object>> objectKindMaps = new ConcurrentHashMap<>();

    // object attribute / object name, object
    private static Map<String, Map<String, String>> objectDataMaps = new ConcurrentHashMap<>();

    private static String objectKind = "";

    private static enum Type {
        Object, Data
    }

    private CObjectManager() {
    }

    /**
     * Initialize the object ini file
     */
    public static void initialize(Component parent) {
        try {
            if (!iniFile.exists())
                iniFile.createNewFile();
            else
                checkIniFile(parent);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        updateIniFile();
    }

    /**
     * Create an new object
     * 
     * @return new object name
     * @throws NullPointerException when cancel input dialog
     */
    public static String createNewObject(Component parentComponent) throws NullPointerException {
        while (true) {
            String object = JOptionPane.showInputDialog(parentComponent, "Enter Object Name: ", "New Object",
                    JOptionPane.INFORMATION_MESSAGE);
            if (!object.matches("^[a-zA-Z0-9\\_\\-]+$"))
                JOptionPane.showMessageDialog(parentComponent, "Error Format!\r\n(Only accept letters/numbers/'_'/'-')",
                        "Invalid Object", JOptionPane.ERROR_MESSAGE);
            else if (objectKindMaps.containsKey(object))
                JOptionPane.showMessageDialog(parentComponent, "This Object already exist!", "Object conflict",
                        JOptionPane.ERROR_MESSAGE);
            else
                return object;

        }
    }

    /*
     * public static String createNewData(Component parentComponent) {
     * 
     * }
     */

    public static String getObjectKind() {
        return objectKind;
    }

    public static File getIniFile() {
        return iniFile;
    }

    private static void checkIniFile(Component parent) throws FileNotFoundException {
        Scanner sc = new Scanner(iniFile);
        String currentObject = "";
        for (int lineCount = 1; sc.hasNextLine(); ++lineCount) {
            try {
                String line = sc.nextLine();
                // comment line
                if (line.matches("^\\s*[\\/]{2}.*"))
                    continue;
                Type type = recognizeSyntax(lineCount, line, parent);
                while (type == null) {
                    line = repairUnrecognizedLine(lineCount, line, parent);
                    type = recognizeSyntax(lineCount, line, parent);
                }
                if (type == Type.Object) {
                    String object = verifyObjectLine(lineCount, line);
                    if (!object.isEmpty()) {
                        currentObject = object;
                        if (!objectKindMaps.containsKey(currentObject)) {
                            objectKindMaps.put(currentObject, new LinkedHashMap<>());
                        }
                        continue;
                    }
                }
                if (currentObject.isEmpty()) {
                    while (true) {
                        try {
                            int choosed = JOptionPane.showConfirmDialog(parent,
                                    "Object is not exist, error on line '" + lineCount + "':\r\n" + line
                                            + "\r\nNeed to build an Object? (No will deprecate this line)",
                                    "Object undefined", JOptionPane.YES_NO_OPTION);
                            if (choosed == JOptionPane.YES_OPTION) {
                                currentObject = createNewObject(parent);
                                objectKindMaps.put(currentObject, new LinkedHashMap<>());
                                break;
                            } else if (choosed == JOptionPane.NO_OPTION) {
                                break;
                            }
                        } catch (NullPointerException nrp) {
                        }
                    }
                }
                if (!currentObject.isEmpty()) {
                    Pair<String, ?> pair = verifyAttributeLine(lineCount, line);
                    objectKindMaps.get(currentObject).put(pair.getKey(), pair.getValue());
                }
            } catch (NullPointerException nrp) {
                System.out.println(nrp.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        sc.close();
    }

    private static void updateIniFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(iniFile), StandardCharsets.UTF_8))) {
            bw.write(iniDescription);
            for (Entry<String, Map<String, Object>> entry : objectKindMaps.entrySet()) {
                bw.write("Object " + entry.getKey() + "\r\n");
                for (Entry<String, Object> en : entry.getValue().entrySet()) {
                    bw.write(en.getKey() + " := ");
                    Object val = en.getValue();
                    if (val instanceof Number) {
                        bw.write("number(" + val + ")\r\n");
                    } else {
                        bw.write("string(" + val + ")\r\n");
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static Type recognizeSyntax(int lineNumber, String line, Component parent) {
        if (line.matches("^\\s*[Oo]bject.*")) {
            // if ambiguity is like ':='
            if (line.matches("\\:\\s*\\=")) {
                while (true) {
                    int choosed = JOptionPane.showConfirmDialog(parent,
                            "Detected operator ':=' in line '" + lineNumber + "':\r\n" + line
                                    + "\r\nNeed to consider as an 'Object'?",
                            "Ambiguity syntax", JOptionPane.QUESTION_MESSAGE);
                    if (choosed == JOptionPane.YES_OPTION)
                        break;
                    else if (choosed == JOptionPane.NO_OPTION)
                        return Type.Data;
                }
            }
            return Type.Object;
        } else if (line.matches("^.*\\:\\s*\\=\\s*([Ss]tring|[Nn]umber)\\s*(\\(.*\\))?\\s*$"))
            return Type.Data;
        return null;
    }

    /**
     * @throws NullPointerException if user give up the repairment of this line
     */
    private static String verifyObjectLine(int lineCount, String line) throws NullPointerException {
        boolean isNeedRepair = false;
        String object = null;
        // wrong expression
        if (!line.matches("^\\s*Object .*"))
            isNeedRepair = true;
        object = line.replaceFirst("^\\s*[Oo]bject", "").trim();
        // wrong object name matched
        if (!object.matches("^[a-zA-Z0-9\\_\\-]+$"))
            isNeedRepair = true;
        if (isNeedRepair)
            object = repairSyntaxError(lineCount, line, "Object",
                    "Object " + object.replaceAll("[^a-zA-Z0-9\\_\\-]", "_"));
        return object;
    }

    /**
     * @throws NullPointerException if user give up the repairment of this line
     */
    private static Pair<String, ?> verifyAttributeLine(int lineCount, String line)
            throws NullPointerException, ParseException {
        boolean isNeedRepair = false;
        String attribute = null;
        String data = null;
        // revise declare operator if wrong operator or wrong data type case
        if (!line.matches(".*\\:\\=\\s*([Ss]tring|[Nn]umber)(\\(.*\\))?\\s*"))
            isNeedRepair = true;
        data = line.replaceAll(".*\\:\\s*\\=\\s*([Ss]tring|[Nn]umber)", "").trim();
        // remove ( and )
        if (!data.isEmpty())
            data = data.substring(1, data.length() - 1);
        attribute = line.replaceAll("\\:\\s*\\=\\s*([Ss]tring|[Nn]umber).*", "").trim();
        // number type check
        if (line.matches(".*\\=\\s*[Nn]umber.*")) {
            data = data.replaceAll("^\\+", "");
            if (data.isEmpty())
                data = "0";
            if (!Regex.isNumber(data))
                data = repairSyntaxError(lineCount, line, "number", attribute + " := number(0)");
            else if (isNeedRepair)
                data = repairSyntaxError(lineCount, line, "number", attribute + " := number(" + data + ")");
            return new Pair<String, Number>(attribute, new DecimalFormat().parse(data));
            // string type need to repair
        } else {
            if (isNeedRepair)
                data = repairSyntaxError(lineCount, line, "string", attribute + " := string(" + data + ")");
            return new Pair<String, String>(attribute, data);
        }
    }

    /**
     * @param lineNumber        error happened line number
     * @param errLine           error line string
     * @param predictType       try to predict type
     * @param predictExpression try to predict the expression
     * @return if {@code predictType} is object return object name, else return data
     *         name
     * @throws NullPointerException if give up repairment
     */
    private static String repairSyntaxError(int lineNumber, String errLine, String predictType,
            String predictExpression) throws NullPointerException {
        String[] options = { "Adopt hint", "Edit yourself", "Deprecate" };
        while (true) {
            int choosed = JOptionPane.showOptionDialog(null,
                    "Syntax error on line '" + lineNumber + "':\r\n" + errLine + "\r\nDo you want to express "
                            + predictExpression + "?",
                    "SyntaxError Hint", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
                    null);
            // adopt hint
            if (choosed == JOptionPane.YES_OPTION) {
                if (predictType.equals("Object"))
                    return predictExpression.split(" ")[1];
                String data = predictExpression.split(":=")[1];
                return data.substring(data.indexOf('(') + 1, data.lastIndexOf(')'));
                // edit yourself
            } else if (choosed == JOptionPane.NO_OPTION) {
                try {
                    if (predictType.equals("Object")) {
                        return createNewObject(null);
                    } else if (predictType.equals("number")) {
                        while (true) {
                            String data = JOptionPane.showInputDialog(null,
                                    "Origin line:\r\n" + errLine + "\r\nEnter new number: ", "Edit data",
                                    JOptionPane.INFORMATION_MESSAGE);
                            if (Regex.isNumber(data))
                                return data.replaceAll("^\\+", "");
                            JOptionPane.showMessageDialog(null, "Not a number!", "Error number",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (predictType.equals("string")) {
                        return JOptionPane.showInputDialog(null,
                                "Origin line:\r\n" + errLine + "\r\nEnter new string: ", "Edit data",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new IllegalArgumentException("Unknown type in prediction!");
                    }
                } catch (NullPointerException e) {
                }
            } else if (choosed == JOptionPane.CANCEL_OPTION) {
                throw new NullPointerException("Give up repairing syntax!");
            }
        }
    }

    /**
     * @throws NullPointerException if give up repairment
     */
    private static String repairUnrecognizedLine(int lineNumber, String errLine, Component parent)
            throws NullPointerException {
        String[] options = { "Repair", "Deprecate" };
        while (true) {
            int choosed = JOptionPane.showOptionDialog(parent,
                    "Unrecognized on line '" + lineNumber + "':\r\n" + errLine + "\r\nDo you want to repair?",
                    "Unrecognized Hint", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
            if (choosed == JOptionPane.YES_OPTION) {
                return JOptionPane.showInputDialog(parent, "Origin line:\r\n" + errLine + "\r\nEnter new line: ",
                        "Edit", JOptionPane.INFORMATION_MESSAGE);
            } else if (choosed == JOptionPane.NO_OPTION) {
                throw new NullPointerException("Give up repairing unrecognized line!");
            }
        }
    }
}