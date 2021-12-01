package api;

import graphAlgo.Dijkstra;

import java.util.Iterator;
import java.util.List;

public class DwgMagic implements DirectedWeightedGraphAlgorithms{
    private Dwg currGraph;

    public DwgMagic(Dwg dwg) {
        this.currGraph = dwg;
        
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

    @Override
    public boolean isConnected() {
        return false;
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
        return dijObj.shortestToSpecificNode((Node) this.currGraph.getNode(src), (Node) this.currGraph.getNode(dest));
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
        return dijObj.shortestPathList((Node)this.currGraph.getNode(src), (Node)this.currGraph.getNode(dest));
    }

    /**
     * using dijkstra * |V| => O((|V|)*O(|E|log|V| + |V|log|V|)
     * |V| is the amount of nodes in the graph (V for vertex)
     * |E| is the amount of edges in the graph (E for edges)
     * for more details on dijkstra algorithm: https://www.youtube.com/watch?v=pSqmAO-m7Lk || https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * @return the Node which is the center (from that node, the sum of paths to all other nodes together is minimum compare to all other nodes)
     */
    @Override
    public NodeData center() {
        Node ansNode = null; // init ans
        if (this.isConnected()){ // if not connected - ans is null, go out
            // init vars
            Dijkstra dijObj = new Dijkstra(this.currGraph);
            double lowestSum = Double.MAX_VALUE, tempSum;
            Node tempN;
            Iterator<NodeData> it = this.currGraph.nodeIter(); // ez to iterate all over the nodes one by one
            while (it.hasNext()){ // dijkstra on ea node as src one time
                tempN = (Node)it.next();
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

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
