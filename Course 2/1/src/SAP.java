import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Queue;

import java.util.Collections;

public class SAP {
    private Digraph digraph;

    private void checkVertex(Integer v) {
        if (v == null) {
            throw new java.lang.IllegalArgumentException("v == null");
        }

        if (v < 0 || v > digraph.V()) {
            throw new java.lang.IllegalArgumentException("vertex is out of range");
        }
    }

    private void checkVertecies(Iterable<Integer> vertecies) {
        for (Integer v : vertecies) {
            checkVertex(v);
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException("G == null");
        }

        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException("Digraph has a cycle.");
        }

        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertecies(v);
        checkVertecies(w);

        int ancestor = findLCA(v, w);
        if (ancestor == -1) {
            return -1;
        }

        BreadthFirstDirectedPaths pathFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathFromW = new BreadthFirstDirectedPaths(digraph, w);

        return pathFromV.distTo(ancestor) + pathFromW.distTo(ancestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkVertecies(v);
        checkVertecies(w);

        return findLCA(v, w);
    }

    private int findLCA(Iterable<Integer> vert1, Iterable<Integer> vert2) {
        Queue<Integer> q = new Queue<>();
        boolean marked1[] = new boolean[digraph.V()];
        for (Integer v : vert1) {
            q.enqueue(v);
            marked1[v] = true;
        }

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : digraph.adj(v)) {
                if (!marked1[w]) {
                    marked1[w] = true;
                    q.enqueue(w);
                }
            }
        }

        boolean marked2[] = new boolean[digraph.V()];
        for (Integer v : vert2) {
            q.enqueue(v);
            marked2[v] = true;
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : digraph.adj(v)) {
                if (marked1[w]) {
                    return w;
                }

                if (!marked2[w]) {
                    marked2[w] = true;
                    q.enqueue(w);
                }
            }
        }

        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
