package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    protected Maze maze;
    */
    private boolean cycleFound = false;
    private boolean cycleEnd = false;
    private int cyclepoint;


    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        distTo[0] = 0;
        dfs(0, 0);
    }

    private void dfs(int v, int n) {
        marked[v] = true;
        for (int w: maze.adj(v)) {
            if (cycleFound) {
                return;
            }
            if (!marked[w]) {
                marked[w] = true;
                distTo[w] = distTo[v] + 1;
                announce();
                dfs(w, v);
                if (cycleFound) {
                    if (w == cyclepoint) {
                        cycleEnd = true;
                        return;
                    }
                    if(!cycleEnd) {
                        edgeTo[w] = v;
                    }
                    announce();
                }
            } else if (w != n) {
                cycleFound = true;
                cyclepoint = w;
                edgeTo[w] = v;
                announce();
            }
        }
    }

}

