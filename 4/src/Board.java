import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private static final int EMPTY_BLOCK = 0;

    private final int[][] blockNumber;
    private final int n;
    private final int hamming;
    private final int manhattan;

    private class Position {
        int i;
        int j;

        Position(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private class NeighborsIterator implements Iterator<Board> {
        private final Position emptyBlock;
        private final Stack<Position> exchangePosition;

        public NeighborsIterator() {
            emptyBlock = findEmptyBlock();
            exchangePosition = new Stack<>();

            int i = emptyBlock.i;
            int j = emptyBlock.j;

            if (i < n - 1) {
                exchangePosition.push(new Position(i + 1, j));
            }
            if (j < n - 1) {
                exchangePosition.push(new Position(i, j + 1));
            }
            if (j > 0) {
                exchangePosition.push(new Position(i, j - 1));
            }
            if (i > 0) {
                exchangePosition.push(new Position(i - 1, j));
            }
        }

        private Position findEmptyBlock() {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (blockNumber[i][j] == EMPTY_BLOCK) {
                        return new Position(i, j);
                    }
                }
            }

            throw new RuntimeException("Array doesn't contain an empty block.");
        }

        @Override
        public boolean hasNext() {
            return !exchangePosition.isEmpty();
        }

        @Override
        public Board next() {
            if (exchangePosition.isEmpty()) {
                throw new NoSuchElementException("No more.");
            }

            Position exchange = exchangePosition.pop();
            int[][] tempBlockNumber = new int[n][];
            for (int i = 0; i < n; ++i) {
                tempBlockNumber[i] = blockNumber[i].clone();
            }

            int value = tempBlockNumber[exchange.i][exchange.j];
            tempBlockNumber[exchange.i][exchange.j] = EMPTY_BLOCK;
            tempBlockNumber[emptyBlock.i][emptyBlock.j] = value;

            return new Board(tempBlockNumber);
        }
    }

    private class Neighbors implements Iterable<Board> {
        @Override
        public Iterator<Board> iterator() {
            return new NeighborsIterator();
        }
    }

    private int calcHamming() {
        int expectNumber = 1;
        int result = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blockNumber[i][j] != expectNumber && expectNumber != n * n) {
                    result++;
                }
                expectNumber++;
            }
        }

        return result;
    }

    private int calcManhattan() {
        int result = 0;
        int expectNumber = 1;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blockNumber[i][j] != expectNumber && blockNumber[i][j] != EMPTY_BLOCK) {
                    int number = blockNumber[i][j];

                    int expectI = (number - 1) / n;
                    int expectJ = (number - 1) % n;

                    result = result + Math.abs(i - expectI) + Math.abs(j - expectJ);
                }

                expectNumber++;
            }
        }

        return result;
    }

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("blocks == null");
        }

        n = blocks.length;
        blockNumber = new int[n][n];
        for (int i = 0; i < n; ++i) {
            blockNumber[i] = blocks[i].clone();
        }

        hamming = calcHamming();
        manhattan = calcManhattan();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        int[][] tempBlockNumber = new int[n][];
        for (int i = 0; i < n; ++i) {
            tempBlockNumber[i] = blockNumber[i].clone();
        }

        Position pos1;
        Position pos2;
        if (tempBlockNumber[0][0] != EMPTY_BLOCK) {
            pos1 = new Position(0, 0);
            if (tempBlockNumber[0][1] != EMPTY_BLOCK) {
                pos2 = new Position(0, 1);
            } else {
                pos2 = new Position(1, 0);
            }
        } else {
            pos1 = new Position(0, 1);
            pos2 = new Position(1, 0);
        }

        int value = tempBlockNumber[pos1.i][pos1.j];
        tempBlockNumber[pos1.i][pos1.j] = tempBlockNumber[pos2.i][pos2.j];
        tempBlockNumber[pos2.i][pos2.j] = value;

        return new Board(tempBlockNumber);
    }

    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }

        if (this == that) {
            return true;
        }

        if (this.getClass() != that.getClass()) {
            return false;
        }

        Board thatBoard = (Board) that;
        if (this.n != thatBoard.n) {
            return false;
        }

        if (this.manhattan != thatBoard.manhattan) {
            return false;
        }

        if (this.hamming != thatBoard.hamming) {
            return false;
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (this.blockNumber[i][j] != thatBoard.blockNumber[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        return new Neighbors();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n);
        builder.append("\n");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                builder.append(String.format("%2d ", blockNumber[i][j]));
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        int [][] blocks = new int[3][3];
        int value = 1;
        for (int i = 0; i < 3; ++i) {
            for (int j = 2; j >= 0; --j) {
                if (value == 9) {
                    blocks[j][i] = 0;
                } else {
                    blocks[j][i] = value;
                }
                value++;
            }
        }

        Board board = new Board(blocks);

        StdOut.print(board.toString());
        StdOut.printf("Haming = %d", board.hamming());
        StdOut.println();
        StdOut.printf("Manhattan = %d", board.manhattan());
        StdOut.println();

        for (Board b : board.neighbors()) {
            StdOut.print(b.toString());
        }
    }
}