package graphic.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * <code>CImage</code> is an custom image class base on <code>JPanel</code>
 */
public class CImage extends JPanel {

    private static final long serialVersionUID = 1L;

    private File imageFile = null;

    private BufferedImage bufferedImage = null;

    private Dimension targetSize = null;

    /**
     * Constructor of empty image with certain size
     * 
     * @param size target size
     */
    public CImage(Dimension size) {
        setSize(size);
    }

    /**
     * Constructor of CImage with certain size
     * 
     * @param resource resource of image
     * @param size     certain size of image, if null use image original size
     */
    public CImage(File resource, Dimension size) {
        setImage(resource, size);
    }

    /**
     * Constructor of CImage with certain size
     * 
     * @param resourcePath resource path of image
     * @param size         certain size of image, if null use image original size
     */
    public CImage(String resourcePath, Dimension size) {
        this(new File(resourcePath), size);
    }

    /**
     * Set the image and scale the size
     * 
     * @param resource resource of image file
     * @param size     certain size of image, if null use image original size
     */
    public void setImage(File resource, Dimension size) {
        try {
            BufferedImage image = ImageIO.read(resource);
            if (size == null)
                setSize(new Dimension(image.getWidth(), image.getHeight()));
            else
                setSize(size);
            imageFile = resource;
            bufferedImage = image;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Change the image resource
     * 
     * @param resource resource of image
     */
    public void changeImage(BufferedImage resource) {
        if (resource == null) {
            return;
        }
        bufferedImage = resource;
    }

    @Override
    public void setSize(Dimension size) {
        super.setSize(size);
        targetSize = size;
    }

    @Override
    public Dimension getPreferredSize() {
        if (bufferedImage != null) {
            int w = bufferedImage.getWidth();
            int h = bufferedImage.getHeight();
            return new Dimension(w, h);
        } else {
            return super.getPreferredSize();
        }
    }

    /**
     * @return the image
     */
    public BufferedImage toImage() {
        return bufferedImage;
    }

    /**
     * @param size target size
     * @return image icon
     */
    public ImageIcon toImageIcon(Dimension size) {
        if (bufferedImage == null) {
            return null;
        }
        Image resizedImage = bufferedImage.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * @return image icon
     */
    public ImageIcon toImageIcon() {
        if (bufferedImage == null) {
            return null;
        }
        Image resizedImage = bufferedImage.getScaledInstance(targetSize.width, targetSize.height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * @return image file
     */
    public File getImageFile() {
        return imageFile;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bufferedImage != null) {
            g.drawImage(bufferedImage, 0, 0, targetSize.width, targetSize.height, this);
        }
    }
}