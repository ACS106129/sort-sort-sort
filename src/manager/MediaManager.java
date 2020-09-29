package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manager for dealing with media
 */
public class MediaManager {

    // use to wrap add mediaPlayers
    private static Map<Path, MediaPlayer> mediasMap = new LinkedHashMap<>();

    private static File currentStayedDir = null;

    private MediaManager() {
    };

    /**
     * Initialize the media file(s)
     * 
     * @param mediaDirPath media directory path
     */
    public static void initialize(String mediaDirPath) {
        File mediaFileDir = new File(mediaDirPath);
        if (!mediaFileDir.isDirectory()) {
            throw new IllegalArgumentException("Invalid media file directory path!");
        }
        List<File> mediaFiles = new ArrayList<>();
        listMediaFiles(mediaDirPath, mediaFiles, true);
        for (File media : mediaFiles) {
            try {
                loadMediaFromFile(media);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            }
        }
    }

    /**
     * Load media from File class
     * 
     * @param source media file source
     * @throws FileNotFoundException if media file is not found
     * @return true if load success
     */
    public static boolean loadMediaFromFile(File source) throws FileNotFoundException {
        if (!source.exists()) {
            throw new FileNotFoundException("Source is not found!");
        }
        File absFile = source.getAbsoluteFile();
        if (!mediasMap.containsKey(absFile.toPath())) {
            mediasMap.put(absFile.toPath(), new MediaPlayer(new Media(source.toURI().toString())));
            return true;
        }
        return false;
    }

    /**
     * Load media from File class
     * 
     * @param source media file source
     * @throws FileNotFoundException if media file is not found
     * @return true if load success
     */
    public static boolean loadMediaFromFile(String sourcePath) throws FileNotFoundException {
        return loadMediaFromFile(new File(sourcePath));
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
     * Remove media from mediaMap
     * 
     * @param path targetPath
     */
    public static boolean removeMedia(Path path) {
        return mediasMap.remove(path.toAbsolutePath()) == null ? false : true;
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
     * Get the media by path
     * 
     * @param path media path
     */
    public static File getFile(Path path) {
        try {
            URI uri = new URI(mediasMap.get(path.toAbsolutePath()).getMedia().getSource());
            return Paths.get(uri).toFile();
        } catch (URISyntaxException urise) {
            urise.printStackTrace();
        }
        return null;
    }

    /**
     * Get the media by path
     * 
     * @param fileName media file name
     */
    public static File getFile(String fileName) throws NullPointerException {
        Iterator<Path> it = mediasMap.keySet().iterator();
        while (it.hasNext()) {
            Path path = it.next();
            String pathName = path.toString();
            int backslashIndex = pathName.lastIndexOf(File.separatorChar);
            String targetFileName = pathName.substring(backslashIndex + 1);
            if (targetFileName.endsWith(fileName)
                    || targetFileName.replaceAll("\\..*$", "").equals(fileName.replaceAll("\\..*$", ""))) {
                return getFile(path);
            }
        }
        throw new NullPointerException("Media file not found!");
    }

    /**
     * Get the mediaPlayer from media manager
     * 
     * @param mediaName media name
     * @return mediaPlayer
     */
    public static MediaPlayer getMediaPlayer(String mediaName) {
        return mediasMap.get(getFile(mediaName).toPath());
    }

    /**
     * Get the mediaPlayer from media manager
     * 
     * @param path media path
     * @return mediaPlayer
     */
    public static MediaPlayer getMediaPlayer(Path path) {
        return mediasMap.get(path.toAbsolutePath());
    }

    private static void listMediaFiles(String dirName, List<File> files, boolean isFindSubDir) {
        File dir = new File(dirName);
        File[] targets = dir.listFiles();
        if (targets != null) {
            for (File file : targets) {
                if (file.isFile()) {
                    String[] suffixes = { ".mp3", ".wav", ".mp4", ".aif", ".aiff", ".m4a", ".m4v", ".fxm", ".flv",
                            ".m3u8" };
                    for (String suffix : suffixes) {
                        if (file.getPath().endsWith(suffix)) {
                            files.add(file);
                            break;
                        }
                    }
                } else if (file.isDirectory() && isFindSubDir) {
                    listMediaFiles(file.getAbsolutePath(), files, isFindSubDir);
                }
            }
        }
    }
}