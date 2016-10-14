import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] M;
    private int[][] prevX;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }
    // current picture
    public Picture picture() {
        return new Picture(picture);
    }
    // width of current picture
    public int width() {
        return picture.width();
    }
    // height of current picture
    public int height() {
        return picture.height();
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        return (xGradient(x, y) + yGradient(x, y));
    }
    // Calculate xGradient
    private double xGradient(int x, int y) {
        double energyX;
        double redDiff;
        double greenDiff;
        double blueDiff;
        if (width() == 1) {
            return 0;
        }
        if (x == width() - 1) {
            redDiff = Math.abs(picture.get(0, y).getRed() 
                - picture.get(x - 1, y).getRed());
            greenDiff = Math.abs(picture.get(0, y).getGreen() 
                - picture.get(x - 1, y).getGreen());
            blueDiff = Math.abs(picture.get(0, y).getBlue() 
                - picture.get(x - 1, y).getBlue());
        } else if (x == 0) {
            redDiff = Math.abs(picture.get(x + 1, y).getRed() 
                - picture.get(width() - 1, y).getRed());
            greenDiff = Math.abs(picture.get(x + 1, y).getGreen() 
                - picture.get(width() - 1, y).getGreen());
            blueDiff = Math.abs(picture.get(x + 1, y).getBlue() 
                - picture.get(width() - 1, y).getBlue());
        } else {
            redDiff = Math.abs(picture.get(x + 1, y).getRed() 
                - picture.get(x - 1, y).getRed());
            greenDiff = Math.abs(picture.get(x + 1, y).getGreen() 
                - picture.get(x - 1, y).getGreen());
            blueDiff = Math.abs(picture.get(x + 1, y).getBlue() 
                - picture.get(x - 1, y).getBlue());
        }
        energyX = redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
        return energyX;
    }

    // Calculate yGradient
    private double yGradient(int x, int y) {
        double energyY;
        double redDiff;
        double greenDiff;
        double blueDiff;
        if (height() == 1) {
            return 0;
        }
        if (y == height() - 1) {
            redDiff = Math.abs(picture.get(x, 0).getRed() 
                - picture.get(x, y - 1).getRed());
            greenDiff = Math.abs(picture.get(x, 0).getGreen() 
                - picture.get(x, y - 1).getGreen());
            blueDiff = Math.abs(picture.get(x, 0).getBlue() 
                - picture.get(x, y - 1).getBlue());
        } else if (y == 0) {
            redDiff = Math.abs(picture.get(x, y + 1).getRed() 
                - picture.get(x, height() - 1).getRed());
            greenDiff = Math.abs(picture.get(x, y + 1).getGreen() 
                - picture.get(x, height() - 1).getGreen());
            blueDiff = Math.abs(picture.get(x, y + 1).getBlue() 
                - picture.get(x, height() - 1).getBlue());
        } else {
            redDiff = Math.abs(picture.get(x, y + 1).getRed() 
                - picture.get(x, y - 1).getRed());
            greenDiff = Math.abs(picture.get(x, y + 1).getGreen() 
                - picture.get(x, y - 1).getGreen());
            blueDiff = Math.abs(picture.get(x, y + 1).getBlue() 
                - picture.get(x, y - 1).getBlue());
        }
        energyY = redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
        return energyY;
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture copy = picture;
        Picture transpose = new Picture(copy.height(), copy.width());
        for (int x = 0; x < transpose.width(); x += 1) {
            for (int y = 0; y < transpose.height(); y += 1) {
                transpose.set(x, y, copy.get(y, x));
            }
        }

        this.picture = transpose;

        // call findVerticalSeam
        int[] seam = findVerticalSeam();

        // Transpose back.
        this.picture = copy;

        return seam;
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        M = new double[width()][height()];
        prevX = new int[width()][height()];

        for (int i = 0; i < width(); i += 1) {
            this.M[i][0] = energy(i, 0);
        }

        // Calculate M(i, j)
        for (int y = 0; y < height() - 1; y += 1) {
            for (int x = 0; x < width(); x += 1) {
                if (width() == 1) {
                    M[x][y + 1] = M[x][y] + energy(x, y + 1);
                    prevX[x][y + 1] = x;
                } else {
                    double min;
                    int minIdx;
                    if (x == 0) {
                        if (M[x][y] > M[x + 1][y]) {
                            min = M[x + 1][y];
                            minIdx = x + 1;
                        } else {
                            min = M[x][y];
                            minIdx = x;
                        }
                    } else if (x == width() - 1) {
                        if (M[x - 1][y] > M[x][y]) {
                            min = M[x][y];
                            minIdx = x;
                        } else {
                            min = M[x - 1][y];
                            minIdx = x - 1;
                        }
                    } else {
                        min = M[x - 1][y];
                        minIdx = x - 1;
                        double min2;
                        int minIdx2;
                        if (M[x][y] > M[x + 1][y]) {
                            min2 = M[x + 1][y];
                            minIdx2 = x + 1;
                        } else {
                            min2 = M[x][y];
                            minIdx2 = x;
                        }
                        if (min > min2) {
                            min = min2;
                            minIdx = minIdx2;
                        }
                    }
                    M[x][y + 1] = min + energy(x, y + 1);
                    prevX[x][y + 1] = minIdx;
                }
            }
        }

        // Find the minimum energy of the last row and its x-value
        double minEnergy = Double.POSITIVE_INFINITY;
        int xValue = -1;
        for (int x = 0; x < width(); x += 1) {
            if (M[x][height() - 1] < minEnergy) {
                minEnergy = M[x][height() - 1];
                xValue = x;
            }
        }
        int[] seamX = new int[height()];
//        seamX[height() - 1] = xValue;
//        xValue = prevX[xValue][height() - 1];
        for (int i = height() - 1; i > -1; i -= 1) {
            seamX[i] = xValue;
            xValue = prevX[xValue][i];
        }
        return seamX;
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width()) {
            throw new IllegalArgumentException("Invalid Input");
        }
        for (int i = 1; i < seam.length; i += 1) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        SeamRemover.removeHorizontalSeam(picture(), seam);
    }
    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new IllegalArgumentException("Invalid Input");
        }
        for (int j = 1; j < seam.length; j += 1) {
            if (Math.abs(seam[j] - seam[j - 1]) > 1) {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        SeamRemover.removeVerticalSeam(picture(), seam);
    }

//    public static void main(String[] args) {
//        Picture p = new Picture("images/6x5.png");
//        SeamCarver sc = new SeamCarver(p);
//
//        int[] seam = sc.findVerticalSeam();
//        for (int g = 0; g < seam.length; g ++) {
//            System.out.println(seam[g]);
//        }
//    }
}
