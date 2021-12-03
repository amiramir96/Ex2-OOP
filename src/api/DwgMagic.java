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
import java.util.Iterator;
import java.util.List;

public class DwgMagic implements DirectedWeightedGraphAlgorithms{
    private DirectedWeightedGraph currGraph;
    private int isConnected;
    private int mc;

    public DwgMagic(DirectedWeightedGraph g) {
        this.currGraph = g;
        this.isConnected = -1;
        this.mc = g.getMC();
    }

    @Override
    public void init(DirectedWeightedGraph g) {
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
            NodeData n = it.next();
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
        Dijkstra dijObj = new Dijkstra(this.currGraph);
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
        Dijkstra dijObj = new Dijkstra(this.currGraph);
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
            Dijkstra dijObj = new Dijkstra(this.currGraph);
            double lowestSum = Double.MAX_VALUE, tempSum;
            NodeData tempN;
            Iterator<NodeData> it = this.currGraph.nodeIter(); // ez to iterate all over the nodes one by one
            while (it.hasNext()){ // dijkstra on ea node as src one time
                tempN = it.next();
                tempSum = dijObj.summerizeAllShortestPaths(tempN); // sum of all paths from the algo table
                if (lowestSum > tempSum){
                    lowestSum = tempSum;
                    ansNode = tempN;
                }
            }
        }
        return ansNode;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    /**
     * if failed to save, the program will "explode"
     * @param file - the file name (may include a relative path).
     * @return boolean, succession of saving the graph data in the file
     */
    @Override
    public boolean save(String file) {
        SaveGraph saveObj = new SaveGraph(file, this.currGraph);
        return saveObj.runSave();
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
        }
        catch (NullPointerException e){ // dont print shit trace for gui
            return false;
        }
        catch (Exception e){ // dont bomb the program
            return false;
        }
    }
}
