package manager;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import graphic.component.CImage;

/**
 * Manager for dealing with image
 */
public class ImageManager {

    // use to wrap add images
    private static Map<Path, CImage> imagesMap = new LinkedHashMap<>();

    private static File currentStayedDir = null;

    private ImageManager() {
    };

    /**
     * Initialize the image
     * 
     * @param imageFileDirPath the dir path
     */
    public static void initialize(String imageDirPath) {
        File imageFileDir = new File(imageDirPath);
        if (!imageFileDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid image file directory path!");
        }
        List<File> imageFiles = new ArrayList<>();
        listImageFiles(imageDirPath, imageFiles, true);
        for (File image : imageFiles) {
            try {
                loadImageFromFile(image);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
        }
    }

    /**
     * Load image from File class
     * 
     * @param source image file source
     * @throws FileNotFoundException if image file is not found
     * @return true if load success
     */
    public static boolean loadImageFromFile(File source) throws FileNotFoundException {
        if (!source.exists()) {
            throw new FileNotFoundException("Source is not found!");
        }
        File absFile = source.getAbsoluteFile();
        if (!imagesMap.containsKey(absFile.toPath())) {
            imagesMap.put(absFile.toPath(), new CImage(source, null));
            return true;
        }
        return false;
    }

    /**
     * Load image from File class
     * 
     * @param source image file source
     * @throws FileNotFoundException if image file is not found
     * @return true if load success
     */
    public static boolean loadImageFromFile(String sourcePath) throws FileNotFoundException {
        return loadImageFromFile(new File(sourcePath));
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
     * Remove image from imageMap
     * 
     * @param path targetPath
     */
    public static boolean removeImage(Path path) {
        return imagesMap.remove(path.toAbsolutePath()) == null ? false : true;
    }

    /**
     * Get the current file path
     * 
     * @return null if file is null
     */
    public static File getCurrentStayedDir() {
        return currentStayedDir;
    }

    /**
     * Get the image by path
     * 
     * @param path image path
     */
    public static File getFile(Path path) {
        return imagesMap.get(path.toAbsolutePath()).getImageFile();
    }

    /**
     * Get the image by path
     * 
     * @param fileName image file name
     */
    public static File getFile(String fileName) throws NullPointerException {
        Iterator<Path> it = imagesMap.keySet().iterator();
        while (it.hasNext()) {
            Path path = it.next();
            String pathName = path.toString();
            int backslashIndex = pathName.lastIndexOf(File.separatorChar);
            String targetFileName = pathName.substring(backslashIndex + 1);
            if (targetFileName.endsWith(fileName)
                    || targetFileName.replaceAll("\\..*$", "").equals(fileName.replaceAll("\\..*$", ""))) {
                return imagesMap.get(path).getImageFile();
            }
        }
        throw new NullPointerException("Image file not found!");
    }

    /**
     * Get image from image manager
     * 
     * @param path the image path
     * @param size the certain size if image, if null use original image size
     * @return <code>CImage</code> file
     */
    public static CImage getCImage(Path path, Dimension size) {
        CImage image = imagesMap.get(path.toAbsolutePath());
        image.setImage(getFile(path.toAbsolutePath()), size);
        return image;
    }

    /**
     * Get image from image manager
     * 
     * @param fileName the image file name
     * @param size     the certain size if image, if null use original image size
     * @return <code>CImage</code> file
     */
    public static CImage getCImage(String fileName, Dimension size) {
        return getCImage(getFile(fileName).toPath(), size);
    }

    /**
     * Get image icon
     * 
     * @param fileName the image file name
     * @param size     the certain size if image, if null use original image size
     * @return <code>ImageIcon</code>
     */
    public static ImageIcon getImageIcon(Path path, Dimension size) {
        return getCImage(path, size).toImageIcon();
    }

    /**
     * Get image icon
     * 
     * @param fileName the image file name
     * @param size     the certain size if image, if null use original image size
     * @return <code>ImageIcon</code>
     */
    public static ImageIcon getImageIcon(String fileName, Dimension size) {
        return getCImage(fileName, size).toImageIcon();
    }

    private static void listImageFiles(String dirName, List<File> files, boolean isFindSubDir) {
        File dir = new File(dirName);
        File[] targets = dir.listFiles();

        if (targets != null) {
            for (File file : targets) {
                if (file.isFile()) {
                    String[] suffixes = ImageIO.getReaderFileSuffixes();
                    for (String suffix : suffixes) {
                        if (file.getPath().endsWith(suffix)) {
                            files.add(file);
                            break;
                        }
                    }
                } else if (file.isDirectory() && isFindSubDir) {
                    listImageFiles(file.getAbsolutePath(), files, isFindSubDir);
                }
            }
        }
    }
}