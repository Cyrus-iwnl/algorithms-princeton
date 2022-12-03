import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] rgb;
    private double[][] energy;
    private int height, width;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("picture is null");
        height = picture.height();
        width = picture.width();
        rgb = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rgb[i][j] = picture.getRGB(j, i);
            }
        }
        setEnergy();
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pic.setRGB(j, i, rgb[i][j]);
            }
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (y < 0 || y >= height || x < 0 || x >= width) {
            throw new IllegalArgumentException();
        }
        return energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int n = height * width;
        // initialize
        double[] distTo = new double[n + 1];
        int[] edgeTo = new int[n + 1];
        for (int v = 0; v < n + 1; v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[0] = 0;
        // topological order start
        for (int v = 1; v <= height; v++) { // from vertex = 0 (virtual vertex)
            distTo[v] = 1000;
            edgeTo[v] = 0;
        }
        for (int v = 1; v <= n - height; v++) { // from vertex
            int row = rowH(v) - 2, col = colH(v) + 1; // adjacent vertices
            for (int adj = 0; adj < 3; adj++) {
                row++;
                if (row < 0 || row >= height) continue;
                int w = col * height + row + 1; // to vertex
                if (distTo[w] > distTo[v] + energy[row][col]) {
                    distTo[w] = distTo[v] + energy[row][col];
                    edgeTo[w] = v;
                }
            }
        } // topological order end
        int index = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = n - height + 1; i <= n; i++) {
            if (distTo[i] < min) {
                index = i;
                min = distTo[i];
            }
        }
        int[] seam = new int[width];
        seam[width - 1] = rowH(index);
        for (int i = width - 2; i >= 0; i--) {
            seam[i] = rowH(edgeTo[index]);
            index = edgeTo[index];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int n = height * width;
        // initialize
        double[] distTo = new double[n + 1];
        int[] edgeTo = new int[n + 1];
        for (int v = 0; v < n + 1; v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[0] = 0;
        // topological order start
        for (int v = 1; v <= width; v++) { // from vertex = 0 (virtual vertex)
            distTo[v] = 1000;
            edgeTo[v] = 0;
        }
        for (int v = 1; v <= n - width; v++) { // from vertex
            int row = rowV(v) + 1, col = colV(v) - 2; // adjacent vertices
            for (int adj = 0; adj < 3; adj++) {
                col++;
                if (col < 0 || col >= width) continue;
                int w = row * width + col + 1; // to vertex
                if (distTo[w] > distTo[v] + energy[row][col]) {
                    distTo[w] = distTo[v] + energy[row][col];
                    edgeTo[w] = v;
                }
            }
        } // topological order end
        int index = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = n - width + 1; i <= n; i++) {
            if (distTo[i] < min) {
                index = i;
                min = distTo[i];
            }
        }
        int[] seam = new int[height];
        seam[height - 1] = colV(index);
        for (int i = height - 2; i >= 0; i--) {
            seam[i] = colV(edgeTo[index]);
            index = edgeTo[index];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) { // row = seam[col]
        validate(seam, false);
        int[][] rgbNew = new int[height - 1][width];
        for (int j = 0; j < width; j++) {
            int k = 0;
            for (int i = 0; i < height; i++) {
                if (i == seam[j]) continue;
                rgbNew[k][j] = rgb[i][j];
                k++;
            }
        }
        rgb = rgbNew;
        height--;
        setEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validate(seam, true);
        int[][] rgbNew = new int[height][width - 1];
        for (int i = 0; i < height; i++) {
            int k = 0;
            for (int j = 0; j < width; j++) {
                if (j == seam[i]) continue;
                rgbNew[i][k] = rgb[i][j];
                k++;
            }
        }
        rgb = rgbNew;
        width--;
        setEnergy();
    }

    private void validate(int[] seam, boolean isVertical) {
        if (seam == null) throw new IllegalArgumentException("seam is null");
        int len = isVertical ? height : width;
        int range = isVertical ? width : height;
        if (range <= 1)
            throw new IllegalArgumentException((isVertical ? "width" : "height") + "<=1");
        if (seam.length != len) throw new IllegalArgumentException("seam length wrong");
        for (int i : seam) {
            if (i < 0 || i >= range) throw new IllegalArgumentException("index out of range");
        }
        for (int i = 1; i < len; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException("two adjacent entries differ by more than 1");
        }
    }

    private void setEnergy() {
        energy = new double[height][width];
        // get R,G,B respectively
        int[][][] color = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int argb = rgb[i][j];
                color[i][j][0] = (argb >> 16) & 0xFF;
                color[i][j][1] = (argb >> 8) & 0xFF;
                color[i][j][2] = (argb) & 0xFF;
            }
        }
        // set border to be 1000
        for (int i = 0; i < width; i++) {
            energy[0][i] = 1000;
            energy[height - 1][i] = 1000;
        }
        for (int i = 1; i < height - 1; i++) {
            energy[i][0] = 1000;
            energy[i][width - 1] = 1000;
        }
        // calculate others
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int[] left = color[i][j - 1];
                int[] right = color[i][j + 1];
                int[] up = color[i - 1][j];
                int[] down = color[i + 1][j];
                double dxSquare = Math.pow(left[0] - right[0], 2) +
                        Math.pow(left[1] - right[1], 2) +
                        Math.pow(left[2] - right[2], 2);
                double dySquare = Math.pow(up[0] - down[0], 2) +
                        Math.pow(up[1] - down[1], 2) +
                        Math.pow(up[2] - down[2], 2);
                energy[i][j] = Math.sqrt(dxSquare + dySquare);
            }
        }
    }

    // for horizontal seam
    private int rowH(int index) {
        return (index % height == 0) ? (height - 1) : (index % height - 1);
    }

    private int colH(int index) {
        return (index % height == 0) ? (index / height - 1) : (index / height);
    }

    // for vertical seam
    private int rowV(int index) {
        return (index % width == 0) ? (index / width - 1) : (index / width);
    }

    private int colV(int index) {
        return (index % width == 0) ? (width - 1) : (index % width - 1);
    }
}