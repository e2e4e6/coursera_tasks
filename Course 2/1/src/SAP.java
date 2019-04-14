import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.Collections;

public class SAP {
    private final Digraph digraph;

    private void checkVertex(Integer v) {
        if (v == null) {
            throw new java.lang.IllegalArgumentException("v == null");
        }

        if (v < 0 || v >= digraph.V()) {
            throw new java.lang.IllegalArgumentException("vertex is out of range");
        }
    }

    private void checkVertecies(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices == null");
        }

        for (Integer v : vertices) {
            checkVertex(v);
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException("G == null");
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
        Queue<Integer> q1 = new Queue<>();
        Queue<Integer> q2 = new Queue<>();
        boolean marked1[] = new boolean[digraph.V()];
        boolean marked2[] = new boolean[digraph.V()];
        int dist1[] = new int[digraph.V()];
        int dist2[] = new int[digraph.V()];

        for (Integer v : vert1) {
            q1.enqueue(v);
            marked1[v] = true;
            dist1[v] = 0;
        }

        for (Integer v : vert2) {
            q2.enqueue(v);
            marked2[v] = true;
            dist2[v] = 0;
        }

        int depth = 1;
        int result = -1;
        int minDist = Integer.MAX_VALUE;
        while (!q1.isEmpty() || !q2.isEmpty()) {
            Queue<Integer> q1_ = new Queue<>();
            while (!q1.isEmpty()) {
                int v1 = q1.dequeue();
                if (marked2[v1]) {
                    if (dist1[v1] + dist2[v1] < minDist) {
                        result = v1;
                        minDist = dist1[v1] + dist2[v1];
                    }
                }

                for (int w : digraph.adj(v1)) {
                    if (!marked1[w]) {
                        marked1[w] = true;
                        q1_.enqueue(w);
                        dist1[w] = depth;
                    }
                }
            }

            q1 = q1_;

            Queue<Integer> q2_ = new Queue<>();
            while (!q2.isEmpty()) {
                int v2 = q2.dequeue();
                if (marked1[v2]) {
                    if (dist1[v2] + dist2[v2] < minDist) {
                        result = v2;
                        minDist = dist1[v2] + dist2[v2];
                    }
                }

                for (int w : digraph.adj(v2)) {
                    if (!marked2[w]) {
                        marked2[w] = true;
                        q2_.enqueue(w);
                        dist2[w] = depth;
                    }
                }
            }

            q2 = q2_;

            depth++;
        }

        return result;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
