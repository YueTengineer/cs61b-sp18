import java.util.HashMap;
import java.util.Map;


/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public static final double ROOT_ULLAT = MapServer.ROOT_ULLAT, ROOT_ULLON = MapServer.ROOT_ULLON,
            ROOT_LRLAT = MapServer.ROOT_LRLAT, ROOT_LRLON = MapServer.ROOT_LRLON;
    public static final int TILE_SIZE = MapServer.TILE_SIZE;
    private double raster_ul_lon;
    private double raster_ul_lat;
    private double raster_lr_lon;
    private double raster_lr_lat;

    public Rasterer() {

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double ullat = params.get("ullat");
        double w = params.get("w");

        boolean query_success = true;

        // Make no sense.
        if (ullon >= lrlon || ullat <= lrlat) {
            query_success = false;
        }

        // No Coverage.
        if (ullon >= ROOT_LRLON || lrlon <= ROOT_ULLON || ullat <= ROOT_LRLAT || lrlat >= ROOT_ULLAT) {
            query_success = false;
        }

        // if query_success is false, return arbitrary results.
        if (!query_success) {
            Map<String, Object> results = new HashMap<>();

            results.put("render_grid",0);
            results.put("raster_ul_lon",0);
            results.put("raster_ul_lat",0);
            results.put("raster_lr_lon",0);
            results.put("raster_lr_lat",0);
            results.put("depth",0);
            results.put("query_success",query_success);

            return results;
        }

        double londpp = Math.abs(lrlon - ullon) / w;
        int depth = computeDepth(londpp);

        // Partial Coverage.

        if (lrlon > ROOT_LRLON) {
            lrlon = ROOT_LRLON;
        }
        if (lrlat < ROOT_LRLAT) {
            lrlat = ROOT_LRLAT;
        }
        if (ullon < ROOT_ULLON) {
            ullon = ROOT_ULLON;
        }
        if (ullat > ROOT_ULLAT) {
            ullat = ROOT_ULLAT;
        }


        int [] index = getIndex(depth, lrlon, lrlat, ullon, ullat);

        String [][] render_grid = new String[index[3] - index[2] + 1][index[1] - index[0] + 1];
        String depth_String = "d" + depth;

        for (int y = index[2]; y <= index[3]; y += 1) {
            for (int x = index[0]; x <= index[1]; x += 1) {
                render_grid[y - index[2]][x - index[0]] = depth_String + "_x" + x + "_y" + y +".png";
            }
        }

        Map<String, Object> results = new HashMap<>();

        results.put("render_grid",render_grid);
        results.put("raster_ul_lon",raster_ul_lon);
        results.put("raster_ul_lat",raster_ul_lat);
        results.put("raster_lr_lon",raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth",depth);
        results.put("query_success",query_success);

        return results;
    }

    private int computeDepth(double londpp) {
        double d0londpp = Math.abs(ROOT_ULLON - ROOT_LRLON) / TILE_SIZE;
        int depth = 0;
        while (d0londpp > londpp && depth < 7) {
            d0londpp = d0londpp / 2;
            depth += 1;
        }
        return depth;
    }


    private int[] getIndex(int depth, double lrlon, double lrlat, double ullon, double ullat) {

        int [] index = new int[4];

        double distperfile_long = Math.abs(ROOT_ULLON - ROOT_LRLON) / (Math.pow(2, depth));
        double distperfile_lat = Math.abs(ROOT_ULLAT - ROOT_LRLAT) / (Math.pow(2, depth));

        index[0] = (int) (Math.abs(ullon - ROOT_ULLON) / distperfile_long);
        index[1] = (int) (Math.ceil(Math.abs(lrlon - ROOT_ULLON) / distperfile_long) - 1);
        index[2] = (int) (Math.abs(ullat - ROOT_ULLAT) / distperfile_lat);
        index[3] = (int) (Math.ceil(Math.abs(lrlat - ROOT_ULLAT) / distperfile_lat) - 1);

        raster_ul_lon = ROOT_ULLON + index[0] * distperfile_long;
        raster_lr_lon = ROOT_ULLON + (index[1] + 1) * distperfile_long;
        raster_ul_lat = ROOT_ULLAT - index[2] * distperfile_lat;
        raster_lr_lat = ROOT_ULLAT - (index[3] + 1) * distperfile_lat;

        return index;
    }


}
