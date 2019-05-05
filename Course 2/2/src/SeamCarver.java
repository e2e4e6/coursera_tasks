import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;


public class SeamCarver {
    private int data[][];
    private int width;
    private int height;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture == null");
        }

        this.data = new int[picture.width()][picture.height()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                data[x][y] = picture.getRGB(x, y);
            }
        }

        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        Picture result = new Picture(width(), height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                result.setRGB(x, y, data[x][y]);
            }
        }

        return result;
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
        if (x < 0 || x >= width()) {
            throw new IllegalArgumentException("x is not in range");
        }

        if (y < 0 || y >= height()) {
            throw new IllegalArgumentException("y is not in range");
        }

        if (x == 0 || x == width() - 1) {
            return 1000.0;
        }

        if (y == 0 || y == height() - 1) {
            return 1000.0;
        }

        int left  = data[x - 1][y];
        int right = data[x + 1][y];
        int up    = data[x][y - 1];
        int down  = data[x][y + 1];

        double delta_x = Math.pow(getRed(right) - getRed(left), 2) +
                Math.pow(getGreen(right) - getGreen(left), 2) +
                Math.pow(getBlue(right) - getBlue(left), 2);

        double delta_y = Math.pow(getRed(down) - getRed(up), 2) +
                Math.pow(getGreen(down) - getGreen(up), 2) +
                Math.pow(getBlue(down) - getBlue(up), 2);

        return Math.sqrt(delta_x + delta_y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(width() * height() + 2);

        for (int y = 0; y < height(); y++) {
            graph.addEdge(new DirectedEdge(beginVertex(), vertex(0, y), energy(0, y)));
        }

        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {
                if (y != 0) {
                    graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x + 1, y - 1), energy(x + 1, y - 1)));
                }

                graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x + 1, y), energy(x + 1, y)));

                if (y != height() - 1) {
                    graph.addEdge(new DirectedEdge(vertex(x, y), vertex(x + 1, y + 1), energy(x + 1, y + 1)));
                }
            }
        }

        for (int y = 0; y < height(); y++) {
            graph.addEdge(new DirectedEdge(vertex(width() - 1, y), endVertex(), 0.0));
        }

        int[] path = new int[width()];
        int i = 0;

        DijkstraSP shortPath = new DijkstraSP(graph, beginVertex());
        for (DirectedEdge edge : shortPath.pathTo(endVertex())) {
            if (edge.from() == beginVertex()) {
                continue;
            }

            path[i] = getY(edge.from(), i);
            i++;
        }

        return path;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(width() * height() + 2);

        for (int x = 0; x < width(); x++) {
            graph.addEdge(new DirectedEdge(beginVertex(), vertex(x, 0), energy(x, 0)));
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
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
            graph.addEdge(new DirectedEdge(vertex(x, height() - 1), endVertex(), 0.0));
        }

        int[] path = new int[height()];
        int i = 0;

        DijkstraSP shortPath = new DijkstraSP(graph, beginVertex());
        for (DirectedEdge edge : shortPath.pathTo(endVertex())) {
            if (edge.from() == beginVertex()) {
                continue;
            }

            path[i] = getX(edge.from(), i);
            i++;
        }

        return path;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width(), height());

        if (height() == 1) {
            throw new IllegalArgumentException("picture height is 1");
        }

        for (int x = 0; x < width(); x++) {
            for (int y = seam[x]; y < height() - 1; y++) {
                data[x][y] = data[x][y + 1];
            }
        }

        height = height - 1;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height(), width());

        if (width() == 1) {
            throw new IllegalArgumentException("picture width is 1");
        }

        for (int y = 0; y < height(); y++) {
            for (int x = seam[y]; x < width() - 1; x++) {
                data[x][y] = data[x + 1][y];
            }
        }

        width = width - 1;
    }

    static private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    static private int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    static private int getBlue(int rgb) {
        return (rgb) & 0xFF;
    }

    private int beginVertex() {
        return vertex(width() - 1, height() - 1) + 1;
    }

    private int endVertex() {
        return beginVertex() + 1;
    }

    private int vertex(int x, int y) {
        return y * width() + x;
    }

    private int getX(int vertex, int y) {
        return vertex - y * width();
    }

    private int getY(int vertex, int x) {
        return (vertex - x) / width();
    }

    static private void validateSeam(int[] seam, int expectedLength, int upperBound) {
        if (seam == null) {
            throw new IllegalArgumentException("seam == null");
        }

        if (seam.length != expectedLength) {
            throw new IllegalArgumentException("seam.length is incorrect");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0) {
                throw new IllegalArgumentException("seam item is negative");
            }

            if (seam[i] >= upperBound) {
                throw new IllegalArgumentException("seam item is higher than upper bound");
            }
        }

        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("seam is incorrect");
            }
        }
    }
}