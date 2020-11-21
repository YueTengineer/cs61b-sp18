package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void addHexagon(TETile[][] w,int pos_x, int pos_y, int size, TETile tl) {
        drawHex(w,pos_x,pos_y,size,tl);
    }

    private static void drawHex(TETile[][] w, int pos_x, int pos_y, int size, TETile tl) {
        int cur_x = pos_x + 1;
        int cur_y = pos_y - 1;
        int cur_size = size - 2;

        for (int i = 0; i < size; i += 1) {
            cur_x -= 1;
            cur_y += 1;
            cur_size += 2;
            for (int x = cur_x; x < cur_x + cur_size; x += 1) {
                w[x][cur_y] = tl;
            }
        }
        cur_y += 1;

        for (int j = 0; j < size; j += 1) {
            for (int x = cur_x; x < cur_x + cur_size; x += 1) {
                w[x][cur_y] = tl;
            }
            cur_x += 1;
            cur_y += 1;
            cur_size -= 2;
        }
    }


    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(100, 100);
        // initialize tiles
        TETile[][] world = new TETile[100][100];
        for (int x = 0; x < 100; x += 1) {
            for (int y = 0; y < 100; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        HexWorld.addHexagon(world,40,50,3,Tileset.FLOWER);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
