package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import filter.Filter;
import setting.Setting;
import utility.Utility;

/**
 * Manager for dealing with file
 */
public class FileManager {

    // Be mutable and interact with user during execution
    private static File currentFile = null;

    private static File currentStayedDir = null;

    private static List<String> inputArr = Collections.synchronizedList(new ArrayList<>());

    private FileManager() {
    };

    /**
     * Set the file array from user interface
     * 
     * @param inArr input array from outside
     */
    public static void setFileArray(String[] inArr) {
        inputArr.clear();
        inputArr.addAll(Arrays.asList(inArr));
    }

    /**
     * Load file from File class
     * 
     * @param source file source
     * @throws FileNotFoundException if file is not a file
     */
    public static void loadFile(File source) throws FileNotFoundException {
        if (!source.isFile()) {
            throw new FileNotFoundException("Source is not a file!");
        }
        currentFile = source;
        reloadFileArray();
    }

    /**
     * Load file from File class
     * 
     * @param source  source file
     * @param filters filter the unnecessary data with DataType
     * @throws NullPointerException  if filter is null
     * @throws FileNotFoundException
     */
    public static void loadFile(File source, List<Filter<?>> filters)
            throws NullPointerException, FileNotFoundException {
        if (!source.isFile()) {
            throw new FileNotFoundException("Source is not a file!");
        } else if (filters == null) {
            throw new NullPointerException("Filter is null!");
        }
        currentFile = source;
        reloadFileArray();
        filterFileArray(filters);
    }

    /**
     * Load file from file path
     * 
     * @param sourcePath source path
     * @throws FileNotFoundException if file is not a file
     */
    public static void loadFile(String sourcePath) throws FileNotFoundException {
        loadFile(new File(sourcePath));
    }

    /**
     * Load file from file path
     * 
     * @param sourcePath source path
     * @param filters    filter the unnecessary data with DataType
     * @throws FileNotFoundException
     */
    public static void loadFile(String sourcePath, List<Filter<?>> filters)
            throws NullPointerException, FileNotFoundException {
        loadFile(new File(sourcePath), filters);
    }

    /**
     * Save file in current file
     * 
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveFile() throws FileNotFoundException {
        if (currentFile == null) {
            throw new NullPointerException("Current file is null!");
        }
        if (!currentFile.exists()) {
            throw new FileNotFoundException("File is not exist!");
        }
        try {
            saveAsFile(currentFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Save file in current file
     * 
     * @param filters filter the unnecessary data with DataType
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveFile(List<Filter<?>> filters) throws FileNotFoundException {
        if (currentFile == null) {
            throw new NullPointerException("Current file is null!");
        }
        if (!currentFile.exists()) {
            throw new FileNotFoundException("File is not exist!");
        }
        try {
            saveAsFile(currentFile, filters);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Save as file in target file
     * 
     * @param targetFile target file to save
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveAsFile(File targetFile) throws FileNotFoundException, IOException {
        if (!targetFile.exists()) {
            File targetDir = targetFile.getParentFile();
            if (targetDir != null) {
                targetDir.mkdirs();
            }
            targetFile.createNewFile();
        }
        currentFile = targetFile;
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8))) {
            for (int i = 0; i < inputArr.size(); ++i) {
                bw.write(inputArr.get(i) + IOManager.displaySeparate);
                if ((i + 1) % Setting.outputFileElementPerLine == 0) {
                    bw.write("\r\n");
                }
            }
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Save as file in target file
     * 
     * @param targetFile target file to save
     * @param filter     filter the unnecessary data with DataType
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveAsFile(File targetFile, List<Filter<?>> filters) throws FileNotFoundException, IOException {
        if (currentFile == null) {
            throw new NullPointerException("Current file is null!");
        }
        if (!targetFile.exists()) {
            File targetDir = targetFile.getParentFile();
            if (targetDir != null) {
                targetDir.mkdirs();
            }
            targetFile.createNewFile();
        }
        List<String> filteredArr = new ArrayList<>();
        for (String element : inputArr) {
            boolean isAccept = filters.parallelStream().anyMatch(filter -> {
                return Utility.isFilterSuccess(element, filter);
            });
            if (isAccept || filters.isEmpty()) {
                filteredArr.add(element);
            }
        }
        currentFile = targetFile;
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8))) {
            for (int i = 0; i < filteredArr.size(); ++i) {
                bw.write(filteredArr.get(i) + IOManager.displaySeparate);
                if ((i + 1) % Setting.outputFileElementPerLine == 0) {
                    bw.write("\r\n");
                }
            }
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Save as file in target file by path
     * 
     * @param targetFilePath target path to save file
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveAsFile(String targetFilePath) throws FileNotFoundException, IOException {
        saveAsFile(new File(targetFilePath));
    }

    /**
     * Save as file in target file by path
     * 
     * @param targetFilePath target path to save file
     * @param filters        filter the unnecessary data with DataType
     * @throws FileNotFoundException if file is not exist
     */
    public static void saveAsFile(String targetFilePath, List<Filter<?>> filters)
            throws FileNotFoundException, IOException {
        saveAsFile(new File(targetFilePath), filters);
    }

    /**
     * Set current stayed Dir
     * 
     * @param source source file
     */
    public static void setCurrentStayedDir(File source) {
        if (source.isDirectory() && source.exists()) {
            currentStayedDir = source;
        } else {
            source = source.getParentFile();
            if (source != null && source.exists() && source.isDirectory()) {
                currentStayedDir = source;
            }
        }
    }

    /**
     * @return true if exist
     */
    public static boolean isCurrentFileExist() {
        return (currentFile == null || !currentFile.exists()) ? false : true;
    }

    /**
     * Get the file array
     * 
     * @return array of file
     */
    public static String[] getFileArray() {
        return inputArr.toArray(new String[0]);
    }

    /**
     * Get the file array with filter
     * 
     * @param filters filter the unnecessary data in array
     * @return string array of file
     */
    public static String[] getFileArray(List<Filter<?>> filters) {
        List<String> filteredFileArr = new ArrayList<>();
        String[] unfilteredFileArr = getFileArray();
        for (String element : unfilteredFileArr) {
            boolean isAccept = filters.parallelStream().anyMatch(filter -> {
                return Utility.isFilterSuccess(element, filter);
            });
            if (isAccept || filters.isEmpty()) {
                filteredFileArr.add(element);
            }
        }
        return filteredFileArr.toArray(new String[0]);
    }

    /**
     * Filter and change the file array
     * 
     * @param filters filter the unnecessary data in array
     */
    public static void filterFileArray(List<Filter<?>> filters) {
        inputArr = Collections.synchronizedList(Arrays.asList(getFileArray(filters)));
    }

    /**
     * Reload the file array
     */
    public static void reloadFileArray() throws FileNotFoundException {
        if (!currentFile.exists()) {
            throw new FileNotFoundException("File is not exist!");
        }
        inputArr.clear();
        Scanner sc = new Scanner(currentFile);
        while (sc.hasNext()) {
            String token = sc.next();
            inputArr.add(token);
        }
        sc.close();
    }

    /**
     * Get the current stayed directory
     * 
     * @return null if file is null
     */
    public static File getCurrentStayedDir() {
        return currentStayedDir;
    }

    /**
     * Get the current file path
     * 
     * @return file path, if null return empty string
     */
    public static String getCurrentFilePath() {
        if (currentFile == null) {
            return "";
        }
        return currentFile.getAbsolutePath();
    }
}