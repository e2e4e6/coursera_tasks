import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        // create n-by-n grid, with all sites blocked
        _n = n;
        _openList = new boolean[n][n];
        _quickUnion = new WeightedQuickUnionUF((n * n) + 2);
        _openCount = 0;
    }

    public void open(int row, int col) {
        checkRowAndCol(row, col);

        if (row == 1) {
            _quickUnion.union(topSite(), getIndex(row, col));
        }

        if (row == _n) {
            _quickUnion.union(bottomSite(), getIndex(row, col));
        }

        if (row > 1 && isOpen(row - 1, col)) {
            union(row, col, row - 1, col);
        }

        if (row < _n && isOpen(row + 1, col)) {
            union(row, col, row + 1, col);
        }

        if (col < _n && isOpen(row, col + 1)) {
            union(row, col, row, col + 1);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            union(row, col, row, col - 1);
        }

        if (!isOpen(row, col)) {
            _openCount++;
        }

        _openList[row - 1][col - 1] = true;
    }

    public boolean isOpen(int row, int col) {
        checkRowAndCol(row, col);

        return _openList[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        return _quickUnion.connected(topSite(), getIndex(row, col));
    }

    public int numberOfOpenSites() {
        return _openCount;
    }

    public boolean percolates() {
        return _quickUnion.connected(topSite(), bottomSite());
    }

    public static void main(String[] args) {
    }

    private int topSite() {
        return 0;
    }

    private int bottomSite() {
        return( _n * _n) + 1;
    }

    private int getIndex(int row, int col) {
        return (row - 1) * _n + col;
    }

    private void checkRowAndCol(int row, int col) {
        if (row < 1 || row > _n) {
            throw new java.lang.IllegalArgumentException("Invalid row " + row);
        }

        if (col < 1 || col > _n) {
            throw new java.lang.IllegalArgumentException("Invalid col " + col);
        }
    }

    private void union(int row1, int col1, int row2, int col2) {
        _quickUnion.union(getIndex(row1, col1), getIndex(row2, col2));
    }

    private WeightedQuickUnionUF _quickUnion;
    private boolean _openList[][];
    private int _n;
    private int _openCount;
}