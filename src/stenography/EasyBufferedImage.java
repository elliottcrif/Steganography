//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package stenography;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EasyBufferedImage extends BufferedImage {
    public static final int GRAY = 4;
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    public static final int ALPHA = 3;
    public static final int JPEG = 0;
    public static final int GIF = 1;
    public static final int PNG = 2;
    private static final String[] TYPES = new String[]{"JPEG", "GIF", "PNG"};
    private static int windowCount = 0;

    public String[] getSupportedFormats() {
        return TYPES;
    }

    public static EasyBufferedImage createImage(BufferedImage image) {
        return toBufferedImage(image);
    }

    public static EasyBufferedImage createImage(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException(filename);
        } else {
            return toBufferedImage(Toolkit.getDefaultToolkit().createImage(filename));
        }
    }

    public static EasyBufferedImage createImage(File file) throws IOException, FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getName());
        } else {
            return toBufferedImage(ImageIO.read(file));
        }
    }

    public static EasyBufferedImage createImage(URL url) {
        return toBufferedImage(Toolkit.getDefaultToolkit().createImage(url));
    }

    public static EasyBufferedImage createImage(int[][][] pixels) {
        if (pixels == null) {
            throw new IllegalArgumentException("null pixels array");
        } else {
            EasyBufferedImage result = new EasyBufferedImage(pixels[0].length, pixels.length, 1);
            result.setPixels(pixels);
            return result;
        }
    }

    public static EasyBufferedImage createImage(int[][] pixels) {
        if (pixels == null) {
            throw new IllegalArgumentException("null pixels array");
        } else {
            EasyBufferedImage result = new EasyBufferedImage(pixels[0].length, pixels.length, 1);
            result.setPixels((int[][])pixels, 0);
            result.setPixels((int[][])pixels, 1);
            result.setPixels((int[][])pixels, 2);
            return result;
        }
    }

    public static EasyBufferedImage createImage(int[] pixels, int width, int height) {
        if (pixels == null) {
            throw new IllegalArgumentException("null pixels array");
        } else if (width * height != pixels.length) {
            throw new IllegalArgumentException("pixels dimensions doesn't match width/height parameters");
        } else if (pixels == null) {
            throw new IllegalArgumentException("null pixels array");
        } else {
            EasyBufferedImage result = new EasyBufferedImage(width, height, 1);
            result.setPixels((int[])pixels, 0);
            result.setPixels((int[])pixels, 1);
            result.setPixels((int[])pixels, 2);
            return result;
        }
    }

    private EasyBufferedImage(int w, int h, int type) {
        super(w, h, type);
    }

    public static int clamp(double v) {
        if (v < 0.0D) {
            return 0;
        } else {
            return v > 255.0D ? 255 : (int)v;
        }
    }

    public EasyBufferedImage copyToGrayScale() {
        EasyBufferedImage result = new EasyBufferedImage(this.getWidth(), this.getHeight(), 10);
        WritableRaster input = this.getRaster();
        WritableRaster output = result.getRaster();

        for(int row = 0; row < input.getHeight(); ++row) {
            for(int col = 0; col < input.getWidth(); ++col) {
                int red = input.getSample(col, row, 0);
                int green = input.getSample(col, row, 1);
                int blue = input.getSample(col, row, 2);
                output.setSample(col, row, 0, clamp((double)(red + green + blue) / 3.0D));
            }
        }

        return result;
    }

    private static EasyBufferedImage toBufferedImage(Image image) {
        image = (new ImageIcon(image)).getImage();
        EasyBufferedImage result = new EasyBufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 1);
        Graphics g = result.createGraphics();
        g.drawImage(image, 0, 0, (ImageObserver)null);
        g.dispose();
        return result;
    }

    public int[][][] getPixels3D() {
        int width = this.getWidth();
        int height = this.getHeight();
        int bands = this.getSampleModel().getNumBands();
        int[][][] pixels = new int[height][width][bands];
        WritableRaster raster = this.getRaster();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                for(int k = 0; k < bands; ++k) {
                    pixels[i][j][k] = raster.getSample(j, i, k);
                }
            }
        }

        return pixels;
    }

    public int[][] getPixels2D(int band) {
        int width = this.getWidth();
        int height = this.getHeight();
        int[][] pixels = new int[height][width];
        WritableRaster raster = this.getRaster();
        int j;
        if (band == 4 && this.isColor()) {
            float[] hsb = new float[3];

            for(j = 0; j < height; ++j) {
                for(int i = 0; i < width; ++i) {
                    this.getRGB(j, i);
                    Color.RGBtoHSB(raster.getSample(j, i, 0), raster.getSample(j, i, 1), raster.getSample(j, i, 2), hsb);
                    pixels[j][j] = clamp((double)(hsb[2] * 255.0F));
                }
            }
        } else {
            if (band == 4) {
                band = 0;
            }

            for(int i = 0; i < height; ++i) {
                for(j = 0; j < width; ++j) {
                    pixels[i][j] = raster.getSample(j, i, band);
                }
            }
        }

        return pixels;
    }

    public int[] getPixels1D(int band) {
        if (band == 4) {
            band = 0;
        }

        int width = this.getWidth();
        int height = this.getHeight();
        int[] pixels = new int[height * width];
        WritableRaster raster = this.getRaster();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                pixels[i * width + j] = raster.getSample(j, i, band);
            }
        }

        return pixels;
    }

    public int[] getPixels1D() {
        int width = this.getWidth();
        int height = this.getHeight();
        int bands = this.getRaster().getNumBands();
        int[] pixels = new int[height * width * bands];
        WritableRaster raster = this.getRaster();

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                for(int k = 0; k < bands; ++k) {
                    pixels[i * width * bands + j * bands + k] = raster.getSample(j, i, k);
                }
            }
        }

        return pixels;
    }

    public void setPixels(int[] pixels, int band) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (pixels != null && width * height == pixels.length) {
            WritableRaster raster = this.getRaster();
            int i;
            if (band == 4 && this.isColor()) {
                float[] hsb = new float[3];

                for(i = 0; i < height; ++i) {
                    for(int j = 0; j < width; ++j) {
                        Color.RGBtoHSB(raster.getSample(j, i, 0), raster.getSample(j, i, 1), raster.getSample(j, i, 2), hsb);
                        hsb[2] = (float)((double)pixels[i * width + j] / 255.0D);
                        int pixel = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                        this.setRGB(j, i, pixel);
                    }
                }
            } else {
                for(int j = 0; j < height; ++j) {
                    for(i = 0; i < width; ++i) {
                        raster.setSample(i, j, band, pixels[i * width + j]);
                    }
                }
            }

        } else {
            throw new IllegalArgumentException("pixel array doesn't match the image size");
        }
    }

    public void setPixels(int[][] pixels, int band) {
        int width = this.getWidth();
        int height = this.getHeight();
        if (pixels != null && pixels[0] != null && width == pixels[0].length && height == pixels.length) {
            WritableRaster raster = this.getRaster();
            int i;
            if (band == 4 && this.isColor()) {
                float[] hsb = new float[3];

                for(i = 0; i < height; ++i) {
                    for(int j = 0; j < width; ++j) {
                        Color.RGBtoHSB(raster.getSample(j, i, 0), raster.getSample(j, i, 1), raster.getSample(j, i, 2), hsb);
                        hsb[2] = (float)((double)pixels[i][j] / 255.0D);
                        int pixel = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                        this.setRGB(j, i, pixel);
                    }
                }
            } else {
                for(int j = 0; j < height; ++j) {
                    for(i = 0; i < width; ++i) {
                        raster.setSample(i, j, band, pixels[i][i]);
                    }
                }
            }

        } else {
            throw new IllegalArgumentException("pixel array doesn't match the image size");
        }
    }

    public void setPixels(int[][][] pixels) {
        int width = this.getWidth();
        int height = this.getHeight();
        int bands = this.getSampleModel().getNumBands();
        if (pixels != null && pixels[0] != null && pixels[0][0] != null && width == pixels[0].length && height == pixels.length && bands == pixels[0][0].length) {
            WritableRaster raster = this.getRaster();

            for(int i = 0; i < height; ++i) {
                for(int j = 0; j < width; ++j) {
                    for(int k = 0; k < bands; ++k) {
                        raster.setSample(j, i, k, pixels[i][j][k]);
                    }
                }
            }

        } else {
            throw new IllegalArgumentException("pixel array doesn't match the image size");
        }
    }

    public boolean isColor() {
        return this.getSampleModel().getNumBands() >= 3;
    }

    public EasyBufferedImage copy() {
        return toBufferedImage(this);
    }

    public void show(String title) {
        JFrame window = new JFrame(title);
        window.getContentPane().add(new EasyBufferedImage.ImagePanel(this.copy()));
        window.setSize(this.getWidth(), this.getHeight());
        ++windowCount;
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (--EasyBufferedImage.windowCount == 0) {
                    System.exit(0);
                } else {
                    e.getWindow().dispose();
                }

            }
        });
        window.setVisible(true);
    }

    public void save(String filename, int format) throws IOException {
        if (format != 0 && format != 2 && format != 1) {
            throw new IllegalArgumentException("Type must be either GIF, PNG, or JPEG");
        } else {
            ImageIO.write(this, TYPES[format], new File(filename));
        }
    }

    private static class ImagePanel extends JPanel {
        BufferedImage image;
        private static final long serialVersionUID = 42L;

        ImagePanel(BufferedImage im) {
            this.image = im;
            this.setMinimumSize(new Dimension(im.getWidth(), im.getHeight()));
        }

        public void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            int dx = (this.getWidth() - this.image.getWidth()) / 2;
            if (dx < 0) {
                dx = 0;
            }

            int dy = (this.getHeight() - this.image.getHeight()) / 2;
            if (dy < 0) {
                dy = 0;
            }

            g.drawImage(this.image, dx, dy, this);
        }
    }
}
