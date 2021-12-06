package api;

import FileWorkout.LoadGraph;
import FileWorkout.SaveGraph;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import graphAlgo.DFS;
import graphAlgo.Dijkstra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DwgMagic implements DirectedWeightedGraphAlgorithms{
    private DirectedWeightedGraph currGraph;
    private int isConnected;
    private int mc;
    private int center;

    public DwgMagic(DirectedWeightedGraph g) {
        this.currGraph = g;
        this.isConnected = -1;
        this.mc = g.getMC();
        this.center = -1;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.isConnected = -1;
        this.center = -1;
        this.mc = 0;
        this.currGraph = (Dwg) g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.currGraph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new Dwg(this.currGraph);
    }

    /**
     * choose randomally vertex v of the currGraph and use DFS algorithm twice (for more details on dfs algo: https://en.wikipedia.org/wiki/Depth-first_search)
     * 1- use DFS on v at currGraph
     * 2- use DFS on v at transpose(currGraph)
     * the assumption is - given v vertex of G, if possible to get to any other node in G from v and from every node to v - graph is connected
     * if both DFS finishingTime(v) is the highest -> means the assumption is true, else - false
     * credit to Doctor Nivash Gabriel for teaching us this algo and methods.
     * @return true if and only if the graph is connected
     */
    @Override
    public boolean isConnected() {
        boolean ans;
        if (this.currGraph.edgeSize() < this.currGraph.nodeSize()){
            return false;
        }
        if (this.mc == this.currGraph.getMC() && this.isConnected != -1){ // check if we already have updated data (can save O(|V|+|E|))
            ans = (1 == this.isConnected);
        }
        else {
            this.mc = this.currGraph.getMC(); // update mc
            DFS dfsObj = new DFS(this.currGraph); // use DFS algo
            Iterator <NodeData> it = this.currGraph.nodeIter();
            NodeData n = null;
            if (it.hasNext()){
                n = it.next();
            }
            else {
                return false;
            }
            ans = dfsObj.mainProcessIsConnected(n);
            // init obj isConnected var for future
            if (ans) {
                this.isConnected = 1;
            }
            else {
                this.isConnected = 0;
            }
        }
        return ans;
    }
    /**
     * ez kinda, just use once dijksta once for src to dest
     * O(|E|log|V| + |V|log|V|)
     * @param src - start node
     * @param dest - end (target) node
     * @return shortest distance from src to dest
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstra(this.currGraph.getNode(src));
        return dijObj.shortestToSpecificNode(this.currGraph.getNode(src), this.currGraph.getNode(dest));
    }

    /**
     * use once dijkstra but now we memorize parents of ea node
     * cost more memory but time run is same as dijkstra
     * more memory == 1 more hashmap that hold all the nodes of the graph (will be deleted in the end of the function)
     * O(|E|log|V| + |V|log|V|)
     * @param src - start node
     * @param dest - end (target) node
     * @return shortest path from src node to dest node
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        Dijkstra dijObj = new Dijkstra(this.currGraph, this.currGraph.getNode(src));
        dijObj.mapPathDijkstra(this.currGraph.getNode(src));
        return dijObj.shortestPathList(this.currGraph.getNode(src), this.currGraph.getNode(dest));
    }

    /**
     * first term is that the graph isConnected, so - will start "center" process if and only if isConnected is true
     * using dijkstra * |V| => O((|V|)*O(|E|log|V| + |V|log|V|)
     * |V| is the amount of nodes in the graph (V for vertex)
     * |E| is the amount of edges in the graph (E for edges)
     * for more details on dijkstra algorithm: https://www.youtube.com/watch?v=pSqmAO-m7Lk || https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * @return the Node which is the center (from that node, the sum of paths to all other nodes together is minimum compare to all other nodes)
     */
    @Override
    public NodeData center() {
        NodeData ansNode = null; // init ans
        // isConnect=-1 -> we dont know yet this data
        // this.mc != graph.mc -> graph as been changed since last time we could check "isConnected"
        if (this.isConnected == -1 || this.mc != this.currGraph.getMC()){
            this.isConnected();
        }
        if (this.isConnected == 1){ // if not connected - ans is null, go out
            this.mc = this.currGraph.getMC();
            // init vars
            double shortestFromLognests = Double.MAX_VALUE, tempLongest;
            NodeData tempN;
            List<Dijkstra> dijkstraList = new ArrayList<>();
            List<Thread> myFirstThreadList = new ArrayList<>();
            Iterator<NodeData> it = this.currGraph.nodeIter(); // ez to iterate all over the nodes one by one
            while (it.hasNext()){ // dijkstra on ea node as src one time
                tempN = it.next();
                Dijkstra dijObj = new Dijkstra(this.currGraph, tempN);
                dijkstraList.add(dijObj);
                dijObj.mapPathDijkstra(tempN);
                dijObj.longestPath();
//                Thread myFirstThread = new Thread(dijObj);
//                myFirstThreadList.add(myFirstThread);
//                myFirstThread.start();
//                tempLongest = dijObj.longestPath(tempN); // sum of all paths from the algo table
//                System.out.println("node "+tempN.getKey()+" longest road is: "+tempLongest);
//                if (shortestFromLognests > tempLongest){
//                    shortestFromLognests = tempLongest;
//                    ansNode = tempN;
//                }
            }
//            for (Thread t : myFirstThreadList){
//                try{
//                    t.join();
//                }
//                catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
            for (Dijkstra dijObj : dijkstraList){
//                System.out.println("node "+dijObj.src+ "longest path is: "+dijObj.longestPath);
                if (dijObj.longestPath < shortestFromLognests){
                    shortestFromLognests = dijObj.longestPath;
                    ansNode = dijObj.src;
                }
            }
        }
//        System.out.println("the center node is: "+ansNode.getKey());
        return ansNode;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        if (cities == null || cities.size() == 0){
            return null;
        }
        List<NodeData> orederedCitiesTravel = new ArrayList<>(); // output list
        List<NodeData> remainingCities = new LinkedList<>();
        // init map with all nodes keys as key and boolean false for visited
        NodeData tempCity;
        HashMap<Integer, Boolean> visitedMap = new HashMap<>();
        for (NodeData city : cities) {
            visitedMap.put(city.getKey(), false);
            remainingCities.add(city);
        }
        int tempDest;
        double weightOfTsp = 0;
        orederedCitiesTravel.add(remainingCities.get(0));
        while (!remainingCities.isEmpty() && weightOfTsp < Double.MAX_VALUE) {
            tempCity = orederedCitiesTravel.get(orederedCitiesTravel.size()-1);
            remainingCities.remove(tempCity);
            // for ea city
            // check if there is direct edges to the other nodes
            // if there is - take the minimal weigthed edge between the edges of the unvisited nodes
            // if there is not - use dijkstra on the curr node and take the shortest path to unvisited node
//            if (!visitedMap.get(tempCity.getKey())){ // not visited yet
                visitedMap.replace(tempCity.getKey(), true);
                tempDest = minimalDirectRoad(tempCity.getKey(), visitedMap, remainingCities);
                if (tempDest == -1){
                    Dijkstra dijObj = new Dijkstra(this.currGraph, tempCity);
                    dijObj.mapPathDijkstra(tempCity); // dijkstra algo
                    List<NodeData> unDirectedPath = minimalUndirectRoad(dijObj, tempCity, visitedMap, remainingCities);
                    weightOfTsp += dijObj.shortestToSpecificNode(tempCity, unDirectedPath.get(unDirectedPath.size()-1));
                    unDirectedPath.remove(0);
                    orederedCitiesTravel.addAll(unDirectedPath);
                    remainingCities.removeAll(unDirectedPath);
                }
                else {
//                    orederedCitiesTravel.add(tempCity);
                    orederedCitiesTravel.add(this.currGraph.getNode(tempDest));
                    remainingCities.remove(this.currGraph.getNode(tempDest));
                    weightOfTsp += this.currGraph.getEdge(tempCity.getKey(), tempDest).getWeight();
                }
            }
//        }
        int station = 0;
        for (NodeData element: orederedCitiesTravel){
            station++;
        }
        return orederedCitiesTravel;
    }

    private List<NodeData> minimalUndirectRoad(Dijkstra dijObj, NodeData src, HashMap<Integer, Boolean> visitedMap, List<NodeData> remainingCities) {
        double tempDist = Double.MAX_VALUE;
        int tempKey = -1;
        for (NodeData city : remainingCities){
            if (dijObj.shortestToSpecificNode(src, city) < tempDist && !visitedMap.get(city.getKey())){ // O(1) proccess - get val from map
                tempDist = dijObj.shortestToSpecificNode(src, city);
                tempKey = city.getKey();
            }
        }
        if (tempKey == -1){
            return null;
        }
        else {
            List<NodeData> output = dijObj.shortestPathList(src, this.currGraph.getNode(tempKey));
            return output;
        }
    }

    private int minimalDirectRoad(int src, HashMap<Integer, Boolean> visitedMap, List<NodeData> remainingCities) {
        double minRoad = Double.MAX_VALUE;
        int minDest = -1;
        for (NodeData city : remainingCities){
            if (!visitedMap.get(city.getKey())){
                if (this.currGraph.getEdge(src, city.getKey()) != null){
                    if (this.currGraph.getEdge(src, city.getKey()).getWeight() < minRoad){
                        minRoad = this.currGraph.getEdge(src, city.getKey()).getWeight();
                        minDest = city.getKey();
                    }
                }
            }
        }
        return minDest;
    }


    /**
     * if failed to save, the program will "explode"
     * @param file - the file name (may include a relative path).
     * @return boolean, succession of saving the graph data in the file
     */
    @Override
    public boolean save(String file) {
        try{
            SaveGraph.save(file, this.currGraph);
            return true;
        }
        catch (IOException err){
            err.printStackTrace();
            return false;
        }
    }

    /**
     * if failed to load, will return false and program keep working (wont "explode")
     * @param file - file name of JSON file
     * @return boolean, loaded/not
     */
    @Override
    public boolean load(String file) {
        try {
            File jsonGraphFile = new File(file); // get file
            DirectedWeightedGraph dwg = null;
            JsonElement graphElement = JsonParser.parseReader(new FileReader(jsonGraphFile)); // shall read, so have to handle exception
            JsonObject graphObject = graphElement.getAsJsonObject(); // convert to json object, then we can work on fields inside
            // jsonToNode return list of nodes, jsonToEdge return list of edges, both is the req to cons Dwg(directed weighted graph)
            LoadGraph g = new LoadGraph(graphObject);
            dwg = new Dwg(g.jsonToNode(), g.jsonToEdge());
            this.currGraph = dwg;
            this.isConnected = -1;
            this.mc = 0;
            return true;
        } catch (Exception e){ // dont print shit trace for gui
            return false;
        }// dont bomb the program

    }
}
