import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture  = null");
        }

        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return picture;
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
        if (x < 0 || x >= picture.width()) {
            throw new IllegalArgumentException("x is not in range");
        }

        if (y < 0 || y >= picture.height()) {
            throw new IllegalArgumentException("y is not in range");
        }

        if (x == 0 || x == picture.width() - 1) {
            return 10000.0;
        }

        if (y == 0 || y == picture.height() - 1) {
            return 10000.0;
        }

        Color left  = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color up    = picture.get(x, y - 1);
        Color down  = picture.get(x, y + 1);

        double delta_x = Math.pow(right.getRed() - left.getRed(), 2) +
                Math.pow(right.getGreen() - left.getGreen(), 2) +
                Math.pow(right.getBlue() - left.getBlue(), 2);

        double delta_y = Math.pow(down.getRed() - up.getRed(), 2) +
                Math.pow(down.getGreen() - up.getGreen(), 2) +
                Math.pow(down.getBlue() - up.getBlue(), 2);

        return Math.sqrt(delta_x + delta_y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(width() * height() + 2);

        for (int x = 0; x < width(); x++) {
            graph.addEdge(new DirectedEdge(topVertex(), vertex(x, 0), 0.0));
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height() - 1; y++) {
                if (x != 0) {
                    graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x - 1, y + 1), energy(x - 1, y + 1)));
                }

                graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x, y + 1), energy(x, y + 1)));

                if (x != width() - 1) {
                    graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x + 1, y + 1), energy(x + 1, y + 1)));
                }
            }
        }

        for (int x = 0; x < width(); x++) {
            graph.addEdge(new DirectedEdge(vertex(x, height() - 1), bottomVertex(), 0.0));
        }

        int[] path = new int[picture.height()];
        int i = 0;

        DijkstraSP shortPath = new DijkstraSP(graph, topVertex());
        for (DirectedEdge edge : shortPath.pathTo(bottomVertex())) {
            path[i] = getX(edge.to(), i);
            i++;
        }

        return path;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, picture.width());
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, picture.height());
    }

    private int topVertex() {
        return picture.height() * picture.width();
    }

    private int bottomVertex() {
        return picture.height() * picture.width() + 1;
    }

    private int leftVertex() {
        return picture.height() * picture.width() + 2;
    }

    private int rightVertex() {
        return picture.height() * picture.width() + 3;
    }

    private int vertex(int x, int y) {
        return y * picture.width() + x;
    }

    private int getX(int vertex, int y) {
        return vertex - y * picture.width();
    }

    private int getY(int vertex, int x) {
        return (vertex - x) / picture.width();
    }

    static private void validateSeam(int[] seam, int currentLength) {
        if (seam == null) {
            throw new IllegalArgumentException("seam == null");
        }

        if (currentLength == 1) {
            throw new IllegalArgumentException("picture size is 1");
        }

        if (seam.length != currentLength) {
            throw new IllegalArgumentException("seam.length is incorrect");
        }

        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("seam is incorrect");
            }
        }
    }
}