package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    /* Inherits public fields:
       public int[] distTo;
       public int[] edgeTo;
       public boolean[] marked;
       protected Maze maze;    */

    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<Node> pq = new PriorityQueue<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        edgeTo[s] = s;
        distTo[s] = 0;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    private class Node implements Comparable<Node> {
        int n;
        int weight;
        public Node(int n, int weight) {
            this.n = n;
            this.weight = weight;
        }

        @Override
        public int compareTo(Node node) {

            if (this.weight < node.weight) {
                return -1;
            } else if (this.weight < node.weight) {
                return 1;
            } else {
                return 0;
            }
        }

    }


    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        pq.add(new Node(s, h(s)));
        while (!pq.isEmpty()&&!targetFound) {
            int v = pq.remove().n;
            if (v == t) targetFound = true;
            for (int w : maze.adj(v)) {
                if (!marked[w]&&!targetFound) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    pq.add(new Node(w, h(w)));
                    announce();
                }
            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }

}

