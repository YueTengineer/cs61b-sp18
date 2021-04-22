package lab11.graphs;
import java.util.LinkedList;
import java.util.Queue;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    protected Maze maze;
    */
    private int s; //source
    private int t; //target
    private boolean targetFound = false;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
       Queue<Integer> q = new LinkedList<>();
       marked[s] = true;
       q.add(s);
       while (!q.isEmpty()) {
           int v = q.remove();
           for (int w :maze.adj(v)) {

               if (!marked[w]) {
                   q.add(w);
                   marked[w] = true;
                   edgeTo[w] = v;
                   distTo[w] = distTo[v] + 1;
                   announce();
                   if (w == t) {
                       return;
                   }
               }
       }

       }

    }


    @Override
    public void solve() {
        bfs();
    }
}

