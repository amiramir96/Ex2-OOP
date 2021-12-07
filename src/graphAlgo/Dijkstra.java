package graphAlgo;

import api.*;
import impGraph.Edge;

import java.util.*;
import java.util.List;

/**
 * use details:
 * this is obj represent dijkstra algorithm
 * please use "mapPathDijkstra" function before u use any other kind of function
 * IMPLEMENTS Runnable means Dijkstra can be used to Thread object !
 * (can be used for Center func which asks for alot of dijkstra parallel on dif src node)
 */
public class Dijkstra{
    public NodeData src;
    DirectedWeightedGraph currGraph;
    // uses maps instead of arrays since the keys of the nodes not guranteed to be ordered one by one
    HashMap<Integer, Boolean> visitMap;
    HashMap<Integer, Integer> prevMap;
    HashMap<Integer, Double> distMap;
    public double longestPath;

    /**
     * @param g - relevant graph
     * @param src - starting src node
     */
    public Dijkstra(DirectedWeightedGraph g, NodeData src) {
        this.src = src;
        this.currGraph = g;
    }

    /**
     * given Directed Wieghted graph that isConnected with |V| nodes and |E| edges
     * dijkstra algo via: https://www.youtube.com/watch?v=pSqmAO-m7Lk || https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
     * using regular priority queue(binomal min heap)
     * running time O(|E|log|V| + |V|log|V|)
     * Use Nodes weight field to save the distance from src, resetting in the end of the algo run
     * @param src - the given node which we want to know all shortest path to every other node
     * @return array of all shortest path from the given node "src"
     */
    public void mapPathDijkstra(NodeData src){
        // initialize priority queue, visit - boolean, distance - double arrays
        this.visitMap = new HashMap<>();
        this.prevMap = new HashMap<>();
        this.distMap = new HashMap<>();
        NodeData tempN;
        Iterator <NodeData> itNode = this.currGraph.nodeIter();
        while (itNode.hasNext()){
            tempN = itNode.next();
            this.prevMap.put(tempN.getKey(), -1);
            this.distMap.put(tempN.getKey(), Double.POSITIVE_INFINITY);
            this.visitMap.put(tempN.getKey(), false);
        }
        // credit stack overflow https://stackoverflow.com/questions/2555284/java-priority-queue-with-a-custom-anonymous-comparator
        // compare through id ("serial number")
        PriorityQueue<NodeData> minHeap = new PriorityQueue<>(2 * distMap.size(), new Comparator<NodeData>() {
            @Override
            public int compare(NodeData o1, NodeData o2) { // credit to yuval bobnovsky to guide us to fix bug via using epsilon param
                if (Math.abs(distMap.get(o1.getKey()) - distMap.get(o2.getKey())) < 1e-32){
                    return 0;
                }
                else {
                    return distMap.get(o1.getKey()) - distMap.get(o2.getKey()) > 0 ? +1 : -1;
                }
            }
        });

        // init the src node to be distance 0 and add it to our priority queue
        this.distMap.replace(src.getKey(), 0.0);
        minHeap.add(src);

        // init vars
        int node_id;
        double newDist, minVal;
        Edge tempE;

        // "dijkstra" algorithm
        while(!minHeap.isEmpty()){
            // out loop, run over the heap till its empty (will be empty only after visited at all the nodes)
            tempN = minHeap.poll(); // given Node "x"
            node_id = tempN.getKey();
            this.visitMap.replace(tempN.getKey(), true);
            // inner loop - move to all the neighbors of "x" via iterating all over the given node OUT edges
            Iterator<EdgeData> itEdge = this.currGraph.edgeIter(node_id);
            while (itEdge.hasNext()){
                tempE = (Edge)itEdge.next(); // Edge from "x" to "y"
                if (this.visitMap.get(tempE.getDest())){continue;} // if visited x, isnt relevant anymore

                newDist = this.distMap.get(node_id) + tempE.getWeight(); // path from src to "x" + path from "x" to "y"
                if (newDist < this.distMap.get(tempE.getDest())){ // switch only for better path
                    this.prevMap.replace(tempE.getDest(), node_id);
                    this.distMap.replace(tempE.getDest(), newDist);
                    minHeap.add(this.currGraph.getNode(tempE.getDest()));
                }
            }
        }
    }


    /**
     *
     * @param src - starting node
     * @param dest - node
     * @return shortest via "mapPathDijkstra" functions updated maps
     */
    public double shortestToSpecificNode(NodeData src, NodeData dest){
        return this.distMap.get(dest.getKey());
    }

    /**
     * for use of Center from dwgMagic
     * returns longest path from the while distMap
     */
    public double longestPath(){
        this.longestPath = Double.MIN_VALUE;
        for (double value : this.distMap.values()){
            if (value > this.longestPath){
                this.longestPath = value;
            }
        }
        return this.longestPath;
    }

    /**
     * after main dijsktra func used (mapPathDijkstra) - ogenizing path from src to dest via parentsMap
     * @param src - start node
     * @param dest - last node in path
     * @return - shortestPath represented as List
     */
    public List<NodeData> shortestPathList(NodeData src, NodeData dest){
//        mapPathDijkstra(src); // get the prevMap from the pathDijkstra algo
        HashMap<Integer, Integer> parentsMap = this.prevMap;
        LinkedList<NodeData> outputPath = new LinkedList<>(); // output list
        if (parentsMap.get(dest.getKey()) == -1) { return outputPath; } // -1 == not exist, both nodes is not connected
        outputPath.addFirst(dest); // dest node is the last in the list
        for (NodeData tempN=dest; tempN != null; tempN = this.currGraph.getNode(parentsMap.get(tempN.getKey()))){ // reverse run from dest node to parents
            outputPath.addFirst(this.currGraph.getNode(parentsMap.get(tempN.getKey()))); // add first so we keep on order
        }
        outputPath.removeFirst(); // remove the parent of src which is null
        return outputPath;
    }

    /**
     * if we use dikstra thousends of times, its creates thousends of dijkstra objects which could be
     */
    public void cleanUp(){
        // uses maps instead of arrays since the keys of the nodes not guranteed to be ordered one by one
        this.visitMap = null;
        this.prevMap = null;
        this.distMap = null;
    }
}