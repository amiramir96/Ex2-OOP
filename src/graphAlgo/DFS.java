package graphAlgo;
import api.*;
import impGraph.Edge;

import java.util.*;

/**
 * implementation of DFS - Depth First Search algorithm
 * will be used for "isConnected" function of DwgMagic
 * for more details: https://en.wikipedia.org/wiki/Depth-first_search || https://www.youtube.com/watch?v=7fujbpJ0LB4
 * initialize visited,predeccesor maps as list of HashMaps for being able to handle huge sizes of graph (milions of items) and wont get into heap out of memory
 * for more details look at DWG object notes and the project readme: https://github.com/amiramir96/Ex2-OOP#readme
 */

public class DFS {
    DirectedWeightedGraph currGraph;
    Double time;
//    ArrayList<HashMap<Integer, Integer>> predecessorMap;
    ArrayList<HashMap<Integer, Boolean>> visitedMap;
    /**
     * constructor
     * @param g - graph we working on (directed weighted graph
     */
    public DFS(DirectedWeightedGraph g) {
        this.time = 0.0;
        this.currGraph = g;
    }

    private int KeyTransform(int id) {return id%1000;}


    /**
     * kinda "main" of the algorithm process
     * stages:
     * 1- init visited and predecessor Maps with the initMaps function.
     * 2- DFS(given node) - "iterativeDFS"
     * 3- check if there is a node which not visited -> if there is, the graph is not connection by definition
     * 4- initMaps again
     * 5- create "transposeMap" which is structure that hold all Edges via Nodes_id key as TRANSPOSED!!!
     *          5.1 - use transpose function on the graph to copy the edges as transposed tothe transMap
     * 6- DFS(given node) - same as above but now on the graph edges is from the transMap
     * 7- check if there is node which not visited -> graph isnt connected (else, we end with true)
     * @param start - node to start DFS on (which is not relevant in this process since we check "isConnected")
     * @return - boolean, graph isConnected?
     */
    public boolean mainProcessIsConnected(NodeData start) {
        // phase 1
        initMaps();
        // phase 2
        iterativeDFS(start);
        // phase 3
        for (HashMap<Integer, Boolean> element : this.visitedMap){
            for (Boolean visited : element.values()){
                if (!visited){
                    return false;
                }
            }
        }
        // phase 4
        initMaps();
        //phase 5
        ArrayList<HashMap<Integer, HashMap<Integer, EdgeData>>> transposeMaps;
        transposeMaps = this.transpose();
        // phase 6
        iterativeDFSTranspose(start, transposeMaps);
        //phase 7
        for (HashMap<Integer, Boolean> element : this.visitedMap){
            for (Boolean visited : element.values()){
                if (!visited){
                    return false;
                }
            }
        }
        // if we get to here the graph is connected!!!
        return true;
    }

    /**
     * the DFS main algo process, imp as iterative with Stack (avoid from recursive)
     * start from the input node and run depthly, each edge, till crossed all roads possibles for that node
     * @param start - input node to start DFS on
     */
    private void iterativeDFS(NodeData start) {
        // init vars and structres
        Stack<NodeData> stackNode = new Stack<>();
        stackNode.push(start);
        NodeData tempN;
        EdgeData tempE;
        Iterator<EdgeData> it;
        // as long as stack not empty, there is still edges/roads which we didnt visited yet from the given node
        while (!stackNode.isEmpty()) {
            tempN = stackNode.pop();
            // "DFS_VISIT" phase
            it = this.currGraph.edgeIter(tempN.getKey());
            while (it!=null && it.hasNext()){
                tempE = it.next();
                if (!this.visitedMap.get(KeyTransform(tempE.getDest())).get(tempE.getDest())) {
                    stackNode.push(this.currGraph.getNode(tempE.getDest())); // only if that node wasnt yet in the stack
                    this.visitedMap.get(KeyTransform(tempE.getDest())).replace(tempE.getDest(), true); // the node has been visited
                }
            }
        }
    }

    /**
     * same as above function - one change - the Edges of the graph have been transposed and stocked inside outed Map
     * @param start - input node to start DFS on
     * @param adjMap - transposeMap (can look at main process function for more data)
     */
    private void iterativeDFSTranspose(NodeData start, ArrayList<HashMap<Integer, HashMap<Integer, EdgeData>>> adjMap) {
        Stack<NodeData> stackNode = new Stack<>();
        stackNode.push(start);
        NodeData tempN;

        // as long as stack not empty, there is still edges/roads which we didnt visited yet from the given node
        while (!stackNode.isEmpty()) {
            tempN = stackNode.pop();
            // "DFS_VISIT" phase
            for (EdgeData edge : adjMap.get(KeyTransform(tempN.getKey())).get(tempN.getKey()).values()) {

                if (!this.visitedMap.get(KeyTransform(edge.getSrc())).get(edge.getSrc())) {
                    stackNode.push(this.currGraph.getNode(edge.getSrc())); // only if that node wasnt yet in the stack
                    this.visitedMap.get(KeyTransform(edge.getSrc())).replace(edge.getSrc(), true); // the node has been visited
                }
            }
        }
    }
    /**
     * copy all the Edges of the this.currGraph to new Map and before that -
     *  - switch all edges "src" and "dest" nodes id
     * @return map arranged with all transposed edge while: first key is src node_id, second key is "src,dest"
     */
    private ArrayList<HashMap<Integer, HashMap<Integer, EdgeData>>> transpose() {
        ArrayList<HashMap<Integer, HashMap<Integer, EdgeData>>> outMaps = new ArrayList<>();
        for (int i=0; i<1000; i++){
            outMaps.add(new HashMap<>());
        }
        Iterator<EdgeData> it = this.currGraph.edgeIter();
        EdgeData tempE;
        while (it.hasNext()){
            tempE = it.next();
            if (!outMaps.get(KeyTransform(tempE.getDest())).containsKey(tempE.getDest())){
                outMaps.get(KeyTransform(tempE.getDest())).put(tempE.getDest(), new HashMap<>());
            }
            outMaps.get(KeyTransform(tempE.getDest())).get(tempE.getDest()).put(tempE.getSrc(), tempE);
        }
        return outMaps;
    }

    /**
     * initialize both maps to "zero status" -> boolean false, predeccesor node is null
     * integer keys is the node_id key
     */
    private void initMaps() {
        this.visitedMap = new ArrayList<>();
//        this.predecessorMap = new ArrayList<>();
        for (int i=0; i<1000; i++){
            this.visitedMap.add(new HashMap<>());
//            this.predecessorMap.add(new HashMap<>());
        }
        NodeData tempN;
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        // case which visitedMap isnt usded yet which means we have to init the amount of object at the map (create key for ea node)
        while (itNode.hasNext()) {
            tempN = itNode.next();
//            this.predecessorMap.get(KeyTransform(tempN.getKey())).put(tempN.getKey(), -1);
            this.visitedMap.get(KeyTransform(tempN.getKey())).put(tempN.getKey(), false);
        }
    }
}
