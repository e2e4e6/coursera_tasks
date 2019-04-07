import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

public class Solver {
    private boolean isSolvable = false;
    private SolutionNode solutionStep = null;

    private class SolutionNode {
        private Board board;
        private Board predecessor;
        private LinkedList<Board> solution;
        private int stepNumber;
        private int priority;

        public SolutionNode(Board board, Board predecessor, LinkedList<Board> solution, int stepNumber) {
            this.board = board;
            this.predecessor = predecessor;
            this.stepNumber = stepNumber;
            if (solution != null) {
                this.solution = new LinkedList<>(solution);
            }
            if (board != null) {
                priority = stepNumber + board.manhattan();
            }
        }

        public Board getBoard() {
            return board;
        }

        public LinkedList<Board> getSolution() {
            return solution;
        }

        public void addToSolution(Board newBoard) {
            if (solution != null) {
                solution.add(newBoard);
            }
        }

        public int getPriority() {
            return priority;
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public Board getPredecessor() {
            return predecessor;
        }
    }

    private SolutionNode processSolutionTree(MinPQ<SolutionNode> pq) {
        SolutionNode node;
        node = pq.min();
        pq.delMin();

        Board board = node.getBoard();
        node.addToSolution(board);
        if (board.isGoal()) {
            return new SolutionNode(null, board, node.getSolution(), node.getStepNumber() + 1);
        }

        for (Board neighbor : board.neighbors()) {
            if (neighbor.isGoal()) {
                node.addToSolution(neighbor);
                return new SolutionNode(null, neighbor, node.getSolution(), node.getStepNumber() + 1);
            }
            if (!neighbor.equals(node.getPredecessor())) {
                pq.insert(new SolutionNode(neighbor, board, node.getSolution(), node.getStepNumber() + 1));
            }
        }

        return null;
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial == null");
        }

        MinPQ<SolutionNode> pq1 = new MinPQ<>(Comparator.comparingInt(SolutionNode::getPriority));
        MinPQ<SolutionNode> pq2 = new MinPQ<>(Comparator.comparingInt(SolutionNode::getPriority));

        pq1.insert(new SolutionNode(initial, null, new LinkedList<>(), 0));
        pq2.insert(new SolutionNode(initial.twin(), null, null, 0));

        while (!pq1.isEmpty() && !pq2.isEmpty()) {
            SolutionNode node = processSolutionTree(pq1);
            if (node != null) {
                isSolvable = true;
                solutionStep = node;
                break;
            }

            node = processSolutionTree(pq2);
            if (node != null) {
                break;
            }
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (solutionStep == null) {
            return -1;
        }

        return solutionStep.getSolution().size() - 1;
    }

    public Iterable<Board> solution() {
        if (solutionStep == null) {
            return null;
        }
        return solutionStep.getSolution();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}