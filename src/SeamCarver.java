import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        energy = new double[picture.width()][picture.height()];
    }

    /**
     * @return The current picture
     */
    public Picture picture() {
        return picture;
    }

    /**
     * @return The width of the current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * @return The height of the current picture
     */
    public int height() {
        return picture.height();
    }

    /**
     * The energy of pixel (x, y) is Δx^2(x, y) + Δy^2(x, y)
     * 
     * @param x
     *            The pixel at column x
     * @param y
     *            The pixel at column y
     * @return The energy of pixel at column x and row y in current picture
     */
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            return 195075.0;
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 195075.0;
        }
        double xEnergy = xEnergy(x, y);
        double yEnergy = yEnergy(x, y);
        return xEnergy + yEnergy;
    }

    /**
     * @return A sequence of indices for horizontal seam in current picture
     */
    public int[] findHorizontalSeam() {
        return null;
    }

    /**
     * @return A sequence of indices for vertical seam in current picture
     */
    public int[] findVerticalSeam() {
        return null;
    }

    /**
     * Removes horizontal seam from the current picture
     * 
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length <= 1) {
            throw new IllegalArgumentException("The horizontal size must be greater than 1.");
        }

    }

    /**
     * Removes vertical seam from the current picture
     * 
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam.length <= 1) {
            throw new IllegalArgumentException("The vertical size must be greater than 1.");
        }
    }

    /**
     * Square of the x-gradient Δx^2(x, y) = Rx(x, y)^2 + Gx(x, y)^2 + Bx(x, y)^2
     * 
     * @param x
     * @param y
     * @return The square of the x-gradient at column x and row y.
     */
    private double xEnergy(int x, int y) {
        Color leftPixel = picture.get(x - 1, y);
        Color rightPixel = picture.get(x + 1, y);
        int red = Math.abs(leftPixel.getRed() - rightPixel.getRed());
        int green = Math.abs(leftPixel.getGreen() - rightPixel.getGreen());
        int blue = Math.abs(leftPixel.getBlue() - rightPixel.getBlue());

        return red * red + green * green + blue * blue;
    }

    /**
     * Square of the y-gradient Δy^2(x, y) = Ry(x, y)^2 + Gy(x, y)^2 + By(x, y)^2
     * 
     * @param x
     * @param y
     * @return The square of the x-gradient at column x and row y.
     */
    private double yEnergy(int x, int y) {
        Color upperPixel = picture.get(x, y - 1);
        Color lowerPixel = picture.get(x, y + 1);
        int red = Math.abs(upperPixel.getRed() - lowerPixel.getRed());
        int green = Math.abs(upperPixel.getGreen() - lowerPixel.getGreen());
        int blue = Math.abs(upperPixel.getBlue() - lowerPixel.getBlue());

        return red * red + green * green + blue * blue;
    }
}