import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        List<Long> sp = new ArrayList<>();

        Map<Long,Long> edgeTo = new HashMap<>();
        Map<Long,Double> distTo = new HashMap<>();
        Map<Long,Boolean> marked = new HashMap<>();

        Long sourceId = g.closest(stlon,stlat);
        Long destId = g.closest(destlon,destlat);

        PriorityQueue<Node> pq = new PriorityQueue<>();

        //Initialize source node.
        Router r = new Router();
        Node source = r.new Node(sourceId,g.distance(sourceId,destId));
        edgeTo.put(sourceId, null);
        distTo.put(sourceId,0.0);
        pq.add(source);

        //java utils 自带的 PriorityQueue 不含有 changePriority, 故允许PQ中含有重复的Node.

        while (!pq.isEmpty()) {

            Node curNode = pq.remove();
            Long curId = curNode.getId();
            if (marked.containsKey(curId)) continue;

            marked.put(curId,true);

            if (curId.equals(destId)) break;

            for (Long id : g.adjacent(curId)) {
                //Undirected Graph. A* algorithm.
                if (!marked.containsKey(id)) {

                    if (!distTo.containsKey(id)) {
                        distTo.put(id,Double.MAX_VALUE);
                    }

                    double newDistance = distTo.get(curId) + g.distance(curId, id);

                    if (newDistance < distTo.get(id)) {
                        distTo.put(id,newDistance);
                        edgeTo.put(id,curId);
                        Node newNode = r.new Node(id, newDistance + g.distance(id,destId));
                        pq.add(newNode);
                    }
                }
            }
        }

        Long Id = destId;
        while (Id != null) {
            sp.add(Id);
            Id = edgeTo.get(Id);
        }

        return r.reverse(sp);

    }

    private ArrayList<Long> reverse(List<Long> ls) {
        ArrayList reverse = new ArrayList();
        for (int i = ls.size() - 1; i >= 0 ; i-= 1) {
            reverse.add(ls.get(i));
        }
        return reverse;
    }

    private class Node implements Comparable<Node> {
        private Long id;
        private double priority;
        public Node(Long id, double priority) {
            this.id = id;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node o) {
            if (this.priority < o.priority) {
                return -1;
            } else if (this.priority > o.priority) {
                return 1;
            } else {
                return 0;
            }
        }

        public Long getId() {
            return id;
        }

    }



    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        if (route.size() < 2) {
            throw new IllegalArgumentException("Route size is not sufficient!");
        }

        if (route.size() == 3) {

        }

        List<NavigationDirection> result = new ArrayList<>();

        int direction = 0;
        double distance = 0.0;

        for (int i = 1; i < route.size() - 1; i += 1) {

            Long prevNode = route.get(i - 1);
            Long curNode = route.get(i);
            Long nextNode = route.get(i + 1);

            distance += g.distance(prevNode, curNode);


            String curwayName = g.getWayName(prevNode, curNode);
            String nextwayName = g.getWayName(curNode, nextNode);

            boolean isFinal = i + 1 == route.size() - 1;

            // When wayName gets changed, it suggests a turning point. Then the
            if (!curwayName.equals(nextwayName)) {

                NavigationDirection nd = new NavigationDirection(direction,curwayName,distance);
                result.add(nd);


                double curAngle = g.bearing(prevNode, curNode);
                double nextAngle = g.bearing(curNode, nextNode);
                direction = relativeDirection(nextAngle - curAngle);

                distance = 0;

            }

            // Calculate the last part of the route.
            if (isFinal) {

                if (!curwayName.equals(nextwayName)) {
                    distance = g.distance(curNode,nextNode);
                } else {
                    distance += g.distance(curNode,nextNode);
                }

                NavigationDirection nd = new NavigationDirection(direction,nextwayName,distance);
                result.add(nd);
            }

        }
        return result;
    }

    private static int relativeDirection(double angle) {
        if (angle > 180) {
            angle = angle - 360;
        }
        if (angle < -180) {
            angle = 360 + angle;
        }

        if (angle >= -15 && angle <= 15) {
            return 1;
        } else if (angle < -15 && angle >= -30) {
            return 2;
        } else if (angle > 15 && angle <= 30) {
            return 3;
        } else if (angle < -30 && angle >= -100) {
            return 5;
        } else if (angle > 30 && angle <= 100) {
            return 4;
        } else if (angle < -100) {
            return 6;
        } else if (angle > 100) {
            return 7;
        } else {
            return 0;
        }
    }



    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public NavigationDirection(int direction, String way, double distance) {
            if (way == null) {
                way = UNKNOWN_ROAD;
            }
            this.direction = direction;
            this.way = way;
            this.distance = distance;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
