package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 70;
    public static final int HEIGHT = 50;
    private static long SEED;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        //To start a game with ‘N’ or 'n' and end with S : N####S, digits ### is seed.
        String regex = "[^0-9]+";
        String seed = input.replaceAll(regex,"");
        SEED = Long.parseLong(seed);
        // initialize tiles
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        mapGenerator(finalWorldFrame);

        return finalWorldFrame;
    }
    
    public void mapGenerator(TETile[][] world) {
        List<Room> existingrooms = new ArrayList<>();
        int times = 300;
        Random random = new Random(SEED);
        for (int t = 0; t < times; t += 1) {
            Room r = new Room(RandomUtils.uniform(random, WIDTH - 10), RandomUtils.uniform(random, HEIGHT - 10), RandomUtils.uniform(random, 4, (int) (WIDTH / 4)), RandomUtils.uniform(random, 4, (int) (HEIGHT / 4)));
            if (!checkExistence(existingrooms, r) && !r.isOutOfBounds()) {
                existingrooms.add(r);
                r.addTo(world);
            }
        }
        hallwayGenerator(world);
        connect(existingrooms,random,world);
        wallPlaster(world);
        unCarving(world,existingrooms);
    }

    // fill the rest of the world with walls!
    private void wallPlaster(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1){
            for (int y = 0; y < HEIGHT; y += 1) {
                if (world[x][y] == Tileset.NOTHING) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    // turn the surplus walls back to nothing
    private void unCarving (TETile[][] world,List<Room> er) {

        for (int y = 1; y < HEIGHT - 1; y += 1){
            for (int x = 1; x < WIDTH - 1; x += 1) {
                if (checkExistence(er,new Position(x,y)) || world[x][y] == Tileset.FLOOR ) {
                    continue;
                }
                if (checkEightSurrondings(x,y,world,Tileset.WALL,4)) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
        }
    }

    private void connect(List<Room> er, Random random, TETile [][] world) {
        for (Room rm : er) {
            Map<String,List<Position>> sidemap = rm.checkhallways(world);
            randomSelectConnection(sidemap,random,world);
        }
    }

    private void randomSelectConnection(Map<String,List<Position>> sp,Random random, TETile [][] world) {

      /*  if (sp.size() == 0) {
            throw new RuntimeException("A room without any connection occured!");
        } */
        // change a wall tile of the room to door tile
        if (sp.size() == 1) {
            openWall(1,random,world,sp);
        } else if (sp.size() == 2) {
            openWall(2,random,world,sp);
        } else if (sp.size() >= 3) {
            openWall(3,random,world,sp);
        }

    }

    private void openWall(int cnt,Random random,TETile [][] world,Map<String,List<Position>> sp) {
        for (int i = 0; i < cnt; i += 1) {
            String [] keyls = (String []) sp.keySet().toArray(new String[0]);
            String key = keyls[(int)RandomUtils.uniform(random,keyls.length)];
            List<Position> list_chosen = sp.get(key);
            Position pos_chosen = list_chosen.get((int)RandomUtils.uniform(random,list_chosen.size()));
            world[pos_chosen.pos_x][pos_chosen.pos_y] = Tileset.UNLOCKED_DOOR;
        }

    }

    private boolean checkExistence(List<Room> er, Room r) {
        if (er.isEmpty()) {
            return false;
        }
        for (Room rm : er) {
            if (rm.isOverlapped(r)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkExistence(List<Room> er, Position p) {
        if (er.isEmpty()) {
            return false;
        }
        for (Room rm : er) {
            if (rm.isOverlapped(p)) {
                return true;
            }
        }
        return false;
    }

    private class Room {
        // width > 4 , height > 4.
        private int loc_x;
        private int loc_y;
        private int width;
        private int height;

        Room(int lx, int ly, int w, int h) {
            loc_x = lx;
            loc_y = ly;
            width = w;
            height = h;
        }

        private void addTo(TETile[][] world) {
            addFloors(world);
            addWalls(world);
        }

        private void addFloors(TETile[][] world) {
            for (int x = loc_x + 1; x < loc_x + width - 1; x += 1) {
                for (int y = loc_y + 1; y < loc_y + height - 1; y += 1) {
                    world[x][y] = Tileset.FLOOR;
                }
            }

        }

        // return a map consisting of Position list which records the sides of every room connected to hallways.
        public Map<String, List<Position>> checkhallways(TETile [][] world) {
            List <Position> left = new ArrayList<>();
            List <Position> right = new ArrayList<>();
            List <Position> top = new ArrayList<>();
            List <Position> bottom = new ArrayList<>();
            // the room is on the left border of the world.
            if (loc_x == 0) {
                // the room is on the bottom-left corner.
                if (loc_y == 0) {
                    top = checkSide(world,"top");
                    right = checkSide(world, "right");

                } else if (loc_y + height == HEIGHT) { // the room is on the top-left corner.
                    bottom = checkSide(world,"bottom");
                    right = checkSide(world,"right");

                } else {
                    top = checkSide(world,"top");
                    right = checkSide(world, "right");
                    bottom = checkSide(world,"bottom");
                }
            } else if (loc_x + width == WIDTH) {
                // the room is on the bottom-right corner.
                if (loc_y == 0){
                    top = checkSide(world,"top");
                    left = checkSide(world,"left");
                } else if (loc_y + height == HEIGHT) { // the room is on the top-right corner.
                    left = checkSide(world,"left");
                    bottom = checkSide(world,"bottom");
                } else {
                    top = checkSide(world,"top");
                    left = checkSide(world,"left");
                    bottom = checkSide(world,"bottom");
                }
            } else {
                if (loc_y == 0) {
                    top = checkSide(world,"top");
                    left = checkSide(world,"left");
                    right = checkSide(world, "right");
                } else if (loc_y + height == HEIGHT) {
                    left = checkSide(world,"left");
                    right = checkSide(world, "right");
                    bottom = checkSide(world,"bottom");
                } else {
                    top = checkSide(world,"top");
                    left = checkSide(world,"left");
                    right = checkSide(world, "right");
                    bottom = checkSide(world,"bottom");
                }
            }
            Map<String,List<Position>> sidemap = new HashMap<>();
            if (!top.isEmpty()) {
                sidemap.put("top",top);
            }
            if (!bottom.isEmpty()) {
                sidemap.put("bottom",bottom);
            }
            if (!left.isEmpty()) {
                sidemap.put("left",left);
            }
            if (!right.isEmpty()) {
                sidemap.put("right",right);
            }
            return sidemap;
        }


        private List <Position> checkSide (TETile[][] world,String s) {
            switch (s) {
                case "left":
                    List <Position> left = new ArrayList<>();
                    for (int y = loc_y + 1; y < loc_y + height - 1; y += 1) {
                        if (world[loc_x - 1][y] == Tileset.FLOOR) {
                            left.add(new Position(loc_x, y));
                        }
                    }
                    return left;

                case "right":
                    List <Position> right = new ArrayList<>();
                    for (int y = loc_y + 1; y < loc_y + height - 1; y += 1) {
                        if (world[loc_x + width][y] == Tileset.FLOOR) {
                            right.add(new Position(loc_x + width - 1, y));
                        }
                    }
                    return right;

                case "top":
                    List <Position> top = new ArrayList<>();
                    for (int x = loc_x + 1; x < loc_x + width - 1; x += 1) {
                        if (world[x][loc_y + height] == Tileset.FLOOR) {
                            top.add(new Position(x, loc_y + height - 1));
                        }
                    }
                    return top;

                case "bottom":
                    List <Position> bottom = new ArrayList<>();
                    for (int x = loc_x + 1; x < loc_x + width - 1; x += 1) {
                        if (world[x][loc_y - 1] == Tileset.FLOOR) {
                            bottom.add(new Position(x, loc_y));
                        }
                    }
                    return bottom;

                default:
                    throw new RuntimeException("Please enter the right String: left/right/top/bottom");
            }

        }

        private void addWalls(TETile[][] world) {
            /* for (int x = loc_x; x < loc_x + width; x += 1) {
                world[x][loc_y] = Tileset.WALL;
                world[x][loc_y + height - 1] = Tileset.WALL;
            }
            for (int y = loc_y + 1; y < loc_y + height - 1; y += 1) {
                world[loc_x][y] = Tileset.WALL;
                world[loc_x + width - 1][y] = Tileset.WALL;
            } */
            addSquareRing(world, Tileset.WALL, 0);
        }

        private void addSquareRing(TETile[][] world, TETile tl, int layer) {
            for (int x = loc_x + layer; x < loc_x + width - layer; x += 1) {
                world[x][loc_y + layer] = tl;
                world[x][loc_y + height - 1 - layer] = tl;
            }
            for (int y = loc_y + 1 + layer; y < loc_y + height - 1 - layer; y += 1) {
                world[loc_x + layer][y] = tl;
                world[loc_x + width - 1 - layer][y] = tl;
            }
        }

        private boolean isOutOfBounds() {
            if (loc_x + width > WIDTH || loc_y + height > HEIGHT) {
                return true;
            }
            return false;
        }

        private boolean checkIfOverlapped(Room r0, Room r1) {
            boolean x0_true = r1.loc_x >= r0.loc_x - 1 && r1.loc_x <= r0.loc_x + r0.width;
            boolean y0_true = r1.loc_y >= r0.loc_y - 1 && r1.loc_y <= r0.loc_y + r0.height;
            boolean x1_true = r1.loc_x + r1.width - 1 >= r0.loc_x - 1 && r1.loc_x + r1.width - 1 <= r0.loc_x + r0.width;
            boolean y1_true = r1.loc_y + r1.height - 1 >= r0.loc_y - 1 && r1.loc_y + r1.height - 1 <= r0.loc_y + r0.height;

            return (x0_true && y0_true) || (x1_true && y1_true) || (x0_true && y1_true) || (x1_true && y0_true);
        }

        private boolean isOverlapped(Room r1) {
            return checkIfOverlapped(r1, this) || checkIfOverlapped(this, r1);
        }

        private boolean isOverlapped(Position p) {
            boolean x_true = loc_x <= p.pos_x && p.pos_x <= loc_x + width -1;
            boolean y_true = loc_y <= p.pos_y && p.pos_y <= loc_y + height -1;
            return x_true && y_true;
        }
    }

    private class Position {
        int pos_x;
        int pos_y;
        Position (int px,int py) {
            pos_x = px;
            pos_y = py;
        }
    }

    public void hallwayGenerator(TETile[][] world) {
        Position start = new Position(0,0);
        MainLoop:
        for (int x = 1; x < WIDTH; x += 1) {
            for (int y = 1; y < HEIGHT; y += 1) {
                if (checkFourSurrondings(x,y,world,Tileset.NOTHING,4)) {
                    start = new Position(x, y);
                    break MainLoop;
                }
            }
        }
        floodFill(start.pos_x,start.pos_y,Tileset.NOTHING,Tileset.FLOOR,world);
    }


    public void floodFill(int x,int y,TETile target, TETile replacement, TETile [][] world) {
        if (x < 1 || y < 1 || x > WIDTH - 2 || y > HEIGHT -2) {
            return;
        }
        if (world[x][y] != target ) {
            return;
        }
        if (world[x][y] == replacement ) {
            return;
        }
        if (checkFourSurrondings(x,y,world,replacement,2)) {
            return;
        }
        world[x][y] = replacement;
        floodFill(x + 1,y,target,replacement,world);
        floodFill(x,y - 1,target,replacement,world);
        floodFill(x - 1,y,target,replacement,world);
        floodFill(x, y + 1, target, replacement, world);
        }


    // To prevent the hallways from forming a loop.
    public boolean checkFourSurrondings(int x, int y, TETile [][] world, TETile tl_checked, int num) {
        int cnt = 0;

        if (world[x - 1][y - 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x - 1][y + 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x + 1][y - 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x + 1][y + 1] == tl_checked) {
            cnt += 1;
        }
        if (cnt >= num) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkEightSurrondings(int x, int y, TETile [][] world, TETile tl_checked, int num) {
        int cnt = 0;
        if (world[x][y - 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x][y + 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x - 1][y] == tl_checked) {
            cnt += 1;
        }
        if (world[x + 1][y] == tl_checked) {
            cnt += 1;
        }
        if (world[x - 1][y - 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x - 1][y + 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x + 1][y - 1] == tl_checked) {
            cnt += 1;
        }
        if (world[x + 1][y + 1] == tl_checked) {
            cnt += 1;
        }
        if (cnt >= num) {
            return true;
        } else {
            return false;
        }
    }
    @Test
    public void testmapGenerator() {
        ter.initialize(WIDTH, HEIGHT);
        // initialize tiles
        TETile[][] testworld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                testworld[x][y] = Tileset.NOTHING;
            }
        }
        mapGenerator(testworld);
        ter.renderFrame(testworld);
    }

    @Test
    public void testRoom() {
        ter.initialize(WIDTH, HEIGHT);
        Room r1 = new Room(1, 1, 5, 5);

        TETile[][] testtiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                testtiles[x][y] = Tileset.NOTHING;
            }
        }
        r1.addTo(testtiles);
        ter.renderFrame(testtiles);
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.testmapGenerator();
    }
}

