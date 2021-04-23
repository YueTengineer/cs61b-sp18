package hw4.puzzle;

/*  Board(tiles): Constructs a board from an N-by-N array of tiles where
    tiles[i][j] = tile at row i, column j
    tileAt(i, j): Returns value of tile at row i, column j (or 0 if blank)
    size():       Returns the board size N
    neighbors():  Returns the neighbors of the current board
    hamming():    Hamming estimate described below
    manhattan():  Manhattan estimate described below
    estimatedDistanceToGoal(): Estimated distance to goal. This method should
                               simply return the results of manhattan()
                               when submitted to Gradescope.
    equals(y):    Returns true if this board's tile values are the same
                  position as y's
    toString():   Returns the string representation of the board. This
                  method is provided in the skeleton */

import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState{

    int [][] tiles;
    int size;

    public Board(int[][] tiles) {
        /*  Mutable board class : If you just copy the reference in the Board constructor,
        someone can change the state of your Board by changing the array.
        this.tiles = tiles;
        */

        //Immutable board class.
        size = tiles[0].length;
        this.tiles = new int[size][size];
        for (int y = 0; y < size; y += 1) {
            for (int x = 0; x < size; x += 1) {
                this.tiles[y][x] = tiles[y][x];
            }
        }

    }
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }
    public int size() {
        return size;
    }

    @Override
    public Iterable<WorldState> neighbors() {

        Queue<WorldState> neighbors = new Queue<>();
        int N = size();
        int xblank = 0;
        int yblank = 0;

        int[][] world = new int[N][N];
        for (int a = 0; a < N; a++) {
            for (int b = 0; b < N; b++) {
                world[a][b] = tileAt(a, b);
                if (tileAt(a,b) == 0) {
                    xblank = a;
                    yblank = b;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (Math.abs(-xblank + i) + Math.abs(j - yblank) - 1 == 0) {
                    world[xblank][yblank] = world[i][j];
                    world[i][j] = 0;
                    Board neighbor = new Board(world);
                    neighbors.enqueue(neighbor);
                    world[i][j] = world[xblank][yblank];
                    world[xblank][yblank] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int N = size;
        int sum = 0;
        int correctNum = 0;
        int cnt = 0;
        for (int y = 0; y < N; y += 1) {
            for (int x = 0; x < N; x +=1) {
                cnt += 1;
                if (cnt == size) continue;
                if (tileAt(y,x) == ++correctNum) {
                    sum += 1;
                }
            }
        }
        return sum;
    }
    public int manhattan() {
        int N = size;
        int sum = 0;
        for (int y = 0; y < N; y += 1) {
            for (int x = 0; x < N; x +=1) {
                if (tileAt(y,x) != 0) {
                    sum += getmanhattan(tileAt(y,x),x,y);
                }
            }
        }
        return sum;
    }

    private int getX(int value) {
        return (value - 1) % size ;
    }

    private int getY(int value) {
        return (value - 1) / size ;
    }

    private int getmanhattan(int v1, int x, int y) {
        return Math.abs(getX(v1)-x) + Math.abs(getY(v1)-y);
    }


    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object o) {
        int N = size;
        Board other = (Board) o;
        for (int y = 0; y < N; y += 1) {
            for (int x = 0; x < N; x +=1) {
                if (tileAt(y,x) != other.tileAt(y,x)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
