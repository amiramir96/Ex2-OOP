package graphAlgo;
import api.*;
import impGraph.Edge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * implementation of DFS - Depth First Search algorithm
 * will be used for "isConnected" function of DwgMagic
 * for more details: https://en.wikipedia.org/wiki/Depth-first_search || https://www.youtube.com/watch?v=7fujbpJ0LB4
 */

public class DFS {
    DirectedWeightedGraph currGraph;
    Double time;

    /**
     * constructor
     * @param g - graph we working on (directed weighted graph
     */
    public DFS(DirectedWeightedGraph g) {
        this.time = 0.0;
        this.currGraph = g;
    }

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
        HashMap<Integer, NodeData> predecessorMap = new HashMap<>();
        HashMap<Integer, Boolean> visitedMap = new HashMap<>();
        initMaps(visitedMap, predecessorMap);
        // phase 2
        iterativeDFS(start, visitedMap);
        // phase 3
        for (Boolean visited : visitedMap.values()) {
            if (!visited) {
                return false;
            }
        }
        // phase 4
        initMaps(visitedMap, predecessorMap);
        //phase 5
        HashMap<Integer, HashMap<String, EdgeData>> transposeMap;
        transposeMap = this.transpose();
        // phase 6
        iterativeDFSTranspose(start, visitedMap, transposeMap);
        //phase 7
        for (Boolean visited : visitedMap.values()) {
            if (!visited) {
                return false;
            }
        }
        // if we get to here the graph is connected!!!
        return true;
    }

    /**
     * the DFS main algo process, imp as iterative with Stack (avoid from recursive)
     * start from the input node and run depthly, each edge, till crossed all roads possibles for that node
     * @param start - input node to start DFS on
     * @param visitMap - key is node_id, boolean is "the node have been visited already?"
     * change to True all nodes that have been visited which means - all nodes that have a route from the input node
     */
    private void iterativeDFS(NodeData start, HashMap<Integer, Boolean> visitMap) {
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
                if (!visitMap.get(tempE.getDest())) {
                    stackNode.push(this.currGraph.getNode(tempE.getDest())); // only if that node wasnt yet in the stack
                    visitMap.replace(tempE.getDest(), true); // the node has been visited
                }
            }
        }
    }

    /**
     * same as above function - one change - the Edges of the graph have been transposed and stocked inside outed Map
     * @param start - input node to start DFS on
     * @param visitMap - key is node_id, boolean is "the node have been visited already?"
     * @param adjMap - transposeMap (can look at main process function for more data)
     */
    private void iterativeDFSTranspose(NodeData start, HashMap<Integer, Boolean> visitMap, HashMap<Integer, HashMap<String, EdgeData>> adjMap) {
        Stack<NodeData> stackNode = new Stack<>();
        stackNode.push(start);
        NodeData tempN;

        // as long as stack not empty, there is still edges/roads which we didnt visited yet from the given node
        while (!stackNode.isEmpty()) {
            tempN = stackNode.pop();

            // "DFS_VISIT" phase
            for (EdgeData edge : adjMap.get(tempN.getKey()).values()) {

                if (!visitMap.get(edge.getDest())) {
                    stackNode.push(this.currGraph.getNode(edge.getDest())); // only if that node wasnt yet in the stack
                    visitMap.replace(edge.getDest(), true); // the node has been visited
                }
            }
        }
    }

    /**
     * copy all the Edges of the this.currGraph to new Map and before that -
     *  - switch all edges "src" and "dest" nodes id
     * @return map arranged with all transposed edge while: first key is src node_id, second key is "src,dest"
     */
    private HashMap<Integer, HashMap<String, EdgeData>> transpose() {
        Iterator<EdgeData> it = this.currGraph.edgeIter();
        HashMap<Integer, HashMap<String, EdgeData>> outputMap = new HashMap<>();
        EdgeData tempE;
        while (it.hasNext()){
            tempE = it.next();
            if (!outputMap.containsKey(tempE.getDest())){
                outputMap.put(tempE.getDest(), new HashMap<>());
            }
            outputMap.get(tempE.getDest()).put("" + tempE.getDest() + "," + tempE.getSrc(), new Edge(tempE.getDest(), tempE.getWeight(), tempE.getSrc()));
        }
        return outputMap;
    }

    /**
     * initialize both maps to "zero status" -> boolean false, predeccesor node is null
     * integer keys is the node_id key
     * @param visitMap - node has been visited?
     * @param predecessorMap - predecessor node along the road
     */
    private void initMaps(HashMap<Integer, Boolean> visitMap, HashMap<Integer, NodeData> predecessorMap) {
        NodeData tempN;
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        if (visitMap.isEmpty()) {
            // case which visitedMap isnt usded yet which means we have to init the amount of object at the map (create key for ea node)
            while (itNode.hasNext()) {
                tempN = itNode.next();
                predecessorMap.put(tempN.getKey(), null);
                visitMap.put(tempN.getKey(), false);
            }
        }
        else {
            // case which visitedMap is already used once and now we start second DFS
            while (itNode.hasNext()) {
                tempN = itNode.next();
                predecessorMap.replace(tempN.getKey(), null);
                visitMap.replace(tempN.getKey(), false);
            }
        }
    }
}
