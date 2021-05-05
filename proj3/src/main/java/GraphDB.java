import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    private Map<Long, ArrayList<Long>> adjNode = new HashMap<>();

    private Map<Long, ArrayList<Edge>> adjEdge = new HashMap<>();

    private Map<Long, Node> nodeList = new HashMap<>();

    private Map<Long, Node> nodeList_cleaned = new HashMap<>();


    private class Node {
        public Long id;
        public double lat;
        public double lon;
        private String nodeName = null;
        public Node (Long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }

        public void setNodeName (String nodeName) {
            this.nodeName = nodeName;
        }

        public String getNodeName () {
            return nodeName;
        }
    }

    private class Edge {
        private Long v;
        private Long w;
        private Long id;
        private double weight;
        public String name = null;
        public Edge(long v, long w, double weight, String name, Long id) {
            this.v = v;
            this.w = w;
            this.weight = weight;
            this.name = name;
            this.id = id;
        }

        public Long either() {
            return v;
        }

        public Long other(Long t) {
            return t.equals(v) ? w : v;
        }

        public double getWeight() {
            return weight;
        }

        public String getWayName () {
            return name;
        }


    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        /**  for (Long id : nodeList.keySet()) {
             if (adjNode.get(id).isEmpty()) nodeList_cleaned.remove(id);
        }*/

        Iterator<Map.Entry<Long, ArrayList<Long>>> it = adjNode.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, ArrayList<Long>> entry = it.next();
            if (entry.getValue().isEmpty()) {
                nodeList_cleaned.remove(entry.getKey());
            }
        }

    }

    public void addNode(long id, double lat, double lon)
    {
        nodeList.put(id, new Node(id,lat,lon));
        nodeList_cleaned.put(id, new Node(id,lat,lon));
        adjNode.put(id, new ArrayList<Long>());
        adjEdge.put(id, new ArrayList<Edge>());
    }

    public void addEdge(long v, long w, String name, long id) {
        validateVertex(v);
        validateVertex(w);
        Edge e = new Edge(v,w,distance(v,w),name, id);
        adjNode.get(w).add(v);
        adjNode.get(v).add(w);
        adjEdge.get(w).add(e);
        adjEdge.get(v).add(e);
    }

    public void addWay(ArrayList<Long> way, String name, long id) {
        for (int i = 1; i < way.size(); i += 1) {
            addEdge(way.get(i), way.get(i-1), name, id);
        }
    }

    public String getWayName(long v, long w) {

        for (Edge e : adjEdge.get(v)) {
            if (e.other(v).equals(w)) {
                return e.getWayName();
            }
        }
        return null;
    }

    public void setNodeName (Long id, String name) {
        nodeList_cleaned.get(id).setNodeName(name);
        nodeList.get(id).setNodeName(name);
    }

    public String getNodeName (Long id) {
        return nodeList.get(id).getNodeName();
    }

    private void validateVertex(long v) {
        if (!nodeList.containsKey(v)) {
            throw new IllegalArgumentException("Node" + v + "does not exist in the current graph.");
        }
    }


    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodeList_cleaned.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        validateVertex(v);
        return adjNode.get(v);
    }

    Iterable<Edge> adjacentEdge (long v) {
        validateVertex(v);
        return adjEdge.get(v);
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        validateVertex(v);
        validateVertex(w);
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        validateVertex(v);
        validateVertex(w);
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double closest = Double.MAX_VALUE;
        Long result = 0l;
        for (Long id : vertices()) {
            double distance = distance(lon(id), lat(id), lon, lat);
            if (distance < closest) {
                closest = distance;
                result = id;
            }
        }
        return result;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodeList.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodeList.get(v).lat;
    }

}
