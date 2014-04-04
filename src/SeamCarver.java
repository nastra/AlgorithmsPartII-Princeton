import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private int[][] parent;
    private static final double MAX_ENERGY = 195075.0;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        energy = new double[picture.width()][picture.height()];
        parent = new int[picture.width()][picture.height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
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
            throw new IndexOutOfBoundsException();
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return MAX_ENERGY;
        }
        double xDiff = gradient(picture.get(x - 1, y), picture.get(x + 1, y));
        double yDiff = gradient(picture.get(x, y - 1), picture.get(x, y + 1));
        return xDiff + yDiff;
    }

    /**
     * @return A sequence of indices for horizontal seam in current picture
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    /**
     * @return A sequence of indices for vertical seam in current picture
     */
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        double[] distTo = new double[width()];
        double[] oldDistTo = new double[width()];

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                relaxVertically(x, y, distTo, oldDistTo);
            }
            System.arraycopy(distTo, 0, oldDistTo, 0, width());
        }

        double min = oldDistTo[0];
        int best = 0;
        for (int index = 0; index < oldDistTo.length; index++) {
            if (oldDistTo[index] < min) {
                min = oldDistTo[index];
                best = index;
            }
        }

        seam[height() - 1] = best;
        for (int i = height() - 2; i >= 0; i--) {
            seam[i] = parent[best][i + 1];
            best = parent[best][i + 1];
        }
        return seam;
    }

    /**
     * Removes horizontal seam from the current picture
     * 
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        checkValidity(seam);
        if (seam.length > width()) {
            throw new IllegalArgumentException("The seam must not be greater than the image width!");
        }

        this.picture = removeSeam(seam, false);
        double[][] oldEnergy = energy;
        energy = new double[width()][height()];
        // TODO: possible improvement would be to recalculate only the energy that has actually changed
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    private void checkValidity(int[] seam) {
        if (width() <= 1 || height() <= 1) {
            throw new IllegalArgumentException("The width and height of the picture must be greatern than 1");
        }
        if (seam.length <= 1) {
            throw new IllegalArgumentException("The seam size must be greater than 1.");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Removes vertical seam from the current picture
     * 
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        checkValidity(seam);
        if (seam.length > height()) {
            throw new IllegalArgumentException("The seam must not be greater than the image height!");
        }

        this.picture = removeSeam(seam, true);
        double[][] oldEnergy = energy;
        energy = new double[width()][height()];

        // TODO: possible improvement would be to recalculate only the energy that has actually changed
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    /**
     * Removes one entire seam from the picture
     * 
     * @param seam
     * @param vertical
     *            Defines if the seam is vertical or horizontal
     * @return A new picture with the seam removed
     */
    private Picture removeSeam(int[] seam, boolean vertical) {
        if (vertical) {
            Picture p = new Picture(width() - 1, height());
            for (int y = 0; y < height(); y++) {
                int k = 0;
                for (int x = 0; x < width(); x++) {
                    if (x != seam[y]) {
                        p.set(k, y, picture.get(x, y));
                        k++;
                    }
                }
            }
            return p;
        }

        Picture p = new Picture(width(), height() - 1);
        for (int y = 0; y < width(); y++) {
            int k = 0;
            for (int x = 0; x < height(); x++) {
                if (x != seam[y]) {
                    p.set(y, k, picture.get(y, x));
                    k++;
                }
            }
        }
        return p;
    }

    /**
     * Square of the gradient Δ^2(x, y) = R(x, y)^2 + G(x, y)^2 + B(x, y)^2
     * 
     * @param a
     * @param b
     * @return The square of the gradient
     */
    private double gradient(Color a, Color b) {
        int red = a.getRed() - b.getRed();
        int green = a.getGreen() - b.getGreen();
        int blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }

    private void relaxVertically(int col, int row, double[] distTo, double[] oldDistTo) {
        if (row == 0) {
            distTo[col] = MAX_ENERGY;
            parent[col][row] = -1;
            return;
        }

        if (col == 0) {
            // we have only 2 edges
            double a = oldDistTo[col];
            double b = oldDistTo[col + 1];
            double min = Math.min(a, b);
            distTo[col] = min + energy[col][row];
            if (a < min) {
                parent[col][row] = col;
            } else {
                parent[col][row] = col + 1;
            }
            return;
        }

        if (col == width() - 1) {
            // we have only 2 edges
            double a = oldDistTo[col];
            double b = oldDistTo[col - 1];
            double min = Math.min(a, b);
            distTo[col] = min + energy[col][row];
            if (a < min) {
                parent[col][row] = col;
            } else {
                parent[col][row] = col - 1;
            }
            return;
        }

        // for 3 edges
        double left = oldDistTo[col - 1];
        double mid = oldDistTo[col];
        double right = oldDistTo[col + 1];

        double min = Math.min(Math.min(left, mid), right);

        distTo[col] = min + energy[col][row];
        if (min == left) {
            parent[col][row] = col - 1;
        } else if (min == mid) {
            parent[col][row] = col;
        } else {
            parent[col][row] = col + 1;
        }
    }

    /**
     * Transposes the picture <br>
     * IMPROVEMENT: transpose only the energy matrix when needed. Instead of transposing it back again, check if the energy matrix was actually
     * transposed
     */
    private void transpose() {
        Picture transposedPicture = new Picture(picture.height(), picture.width());
        double[][] newEnergy = new double[picture.height()][picture.width()];
        for (int i = 0; i < picture.width(); i++)
            for (int k = 0; k < picture.height(); k++) {
                transposedPicture.set(k, i, picture.get(i, k));
                newEnergy[k][i] = energy[i][k];
            }
        energy = newEnergy;
        picture = transposedPicture;
        parent = new int[picture.width()][picture.height()];
    }

}