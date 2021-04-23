package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;


public class Solver {

    /*
    Solver(initial): Constructor which solves the puzzle, computing  everything necessary
                     for moves() and solution() to not have to solve the problem again.
                     Solves the puzzle using the A* algorithm. Assumes a solution exists.
            moves(): Returns the minimum number of moves to solve the puzzle starting
                     at the initial WorldState.
         solution(): Returns a sequence of WorldStates from the initial WorldState to the solution.
    */

    private int shortestmoves;
    private SearchNode goal;
    private Stack<WorldState> solution = new Stack<>();


    private class SearchNode implements WorldState,Comparable<SearchNode>  {
        WorldState ws;
        int moves;
        SearchNode prev;
        int priority;
        int estimatedDistance;
        public SearchNode(WorldState ws, int moves, SearchNode prev) {
            this.ws = ws;
            this.moves = moves;
            this.prev = prev;
            this.priority = moves + estimatedDistanceToGoal();
        }

        @Override
        public int estimatedDistanceToGoal() {
            // Optimization: caching
            if (estimatedDistance == 0) {
                estimatedDistance = ws.estimatedDistanceToGoal();
            }
            return estimatedDistance;
        }

        @Override
        public Iterable<WorldState> neighbors() {
            return ws.neighbors();
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.priority < o.priority) {
                return -1;
            } else if (this.priority > o.priority) {
                return 1;
            } else {
                return 0;
            }
        }

    }
    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial,0,null));
        boolean goalFound = false;

        if (pq.min().isGoal()) {
            goalFound = true;
            shortestmoves = 0;
            solution.push(pq.min());
        }

        while (!pq.isEmpty()&&!goalFound) {
            SearchNode cur = pq.delMin();

            for (WorldState ws : cur.neighbors()) {
                // Optimiaztion:reduce unnecessary exploration of useless search nodes.
                if (cur.prev != null) {
                    if (ws.equals(cur.prev.ws)) continue;
                }

                if(goalFound == true) break;
                if (ws.isGoal()) {
                    goalFound = true;
                    shortestmoves = cur.moves + 1;
                    goal = new SearchNode(ws,cur.moves + 1,cur);
                }
                pq.insert(new SearchNode(ws,cur.moves + 1,cur));
            }

        if (shortestmoves != 0) {
            // solution
            solution.push(goal.ws);
            SearchNode prev = goal.prev;
            while (prev != null) {
                solution.push(prev.ws);
                prev = prev.prev;
            }
        }

    }

    }

    public int moves() {
        return shortestmoves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }

}
