package graphAlgo;

import api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Dijkstra {
    Dwg currGraph;
    public Dijkstra(Dwg g) {
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
    public HashMap<Integer, Double> basicDijkstra(Node src){
        // initialize priority queue, visit - boolean, distance - double arrays
        HashMap<Integer, Boolean> visitMap = new HashMap<>();
        HashMap<Integer, Double> distMap = new HashMap<>();
        Node tempN;
        Iterator <NodeData> itNode = this.currGraph.nodeIter();
        while (itNode.hasNext()){
            tempN = (Node)itNode.next();
            distMap.put(tempN.getKey(), Double.POSITIVE_INFINITY);
            visitMap.put(tempN.getKey(), false);
        }
        PriorityQueue<Node> minHeap = new PriorityQueue<>(2 * distMap.size());

        // init the src node to be distance 0 and add it to our priority queue
        distMap.replace(src.getKey(), 0.0);
        src.setWeight(0);
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
            visitMap.replace(tempN.getKey(), true);

            // inner loop - move to all the neighbors of "x" via iterating all over the given node OUT edges
            Iterator<EdgeData> itEdge = this.currGraph.edgeIter(node_id);
            while (itEdge.hasNext()){
                tempE = (Edge)itEdge.next(); // Edge from "x" to "y"
                if (visitMap.get(tempE.getDest())){continue;} // if visited x, isnt relevant anymore
                newDist = distMap.get(node_id) + tempE.getWeight(); // path from src to "x" + path from "x" to "y"
                if (newDist < distMap.get(tempE.getDest())){ // switch only for better path
                    distMap.replace(tempE.getDest(), newDist);
                    this.currGraph.getNode(tempE.getDest()).setWeight(newDist);
                    minHeap.add((Node)this.currGraph.getNode(tempE.getDest()));
                }
            }
        }
        itNode = this.currGraph.nodeIter();
        while (itNode.hasNext()){
            tempN = (Node)itNode.next();
            tempN.setWeight(Double.POSITIVE_INFINITY);
        }
        return distMap;
    }

    public HashMap<Integer, Integer> mapPathDijkstra(Node src){
        // initialize priority queue, visit - boolean, distance - double arrays
        HashMap<Integer, Boolean> visitMap = new HashMap<>();
        HashMap<Integer, Integer> prevMap = new HashMap<>();
        HashMap<Integer, Double> distMap = new HashMap<>();
        Node tempN;
        Iterator <NodeData> itNode = this.currGraph.nodeIter();
        while (itNode.hasNext()){
            tempN = (Node)itNode.next();
            prevMap.put(tempN.getKey(), -1);
            distMap.put(tempN.getKey(), Double.POSITIVE_INFINITY);
            visitMap.put(tempN.getKey(), false);
        }
        PriorityQueue<Node> minHeap = new PriorityQueue<>(2 * distMap.size());

        // init the src node to be distance 0 and add it to our priority queue
        distMap.replace(src.getKey(), 0.0);
        src.setWeight(0);
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
            visitMap.replace(tempN.getKey(), true);
            // inner loop - move to all the neighbors of "x" via iterating all over the given node OUT edges
            Iterator<EdgeData> itEdge = this.currGraph.edgeIter(node_id);
            while (itEdge.hasNext()){
                tempE = (Edge)itEdge.next(); // Edge from "x" to "y"
                if (visitMap.get(tempE.getDest())){continue;} // if visited x, isnt relevant anymore
                newDist = distMap.get(node_id) + tempE.getWeight(); // path from src to "x" + path from "x" to "y"
                if (newDist < distMap.get(tempE.getDest())){ // switch only for better path
                    prevMap.replace(tempE.getDest(), node_id);
                    distMap.replace(tempE.getDest(), newDist);
                    this.currGraph.getNode(tempE.getDest()).setWeight(newDist);
                    minHeap.add((Node)this.currGraph.getNode(tempE.getDest()));
                }
            }
        }
        itNode = this.currGraph.nodeIter();
        while (itNode.hasNext()){
            tempN = (Node)itNode.next();
            tempN.setWeight(Double.POSITIVE_INFINITY);
        }
        return prevMap;
    }


    public double shortestToSpecificNode(Node src, Node dest){
        return basicDijkstra(src).get(dest.getKey());
    }

    public double summerizeAllShortestPaths(Node src){
        HashMap<Integer, Double> dijMap = basicDijkstra(src);
        double ans = 0;
        for (double value : dijMap.values()) {
            ans += value;
        }
        return ans;
    }

    public List<NodeData> shortestPathList(Node src, Node dest){
        HashMap<Integer, Integer> parentsMap = mapPathDijkstra(src); // get the parentsMap from the pathDijkstra algo
        LinkedList<NodeData> outputPath = new LinkedList<>(); // output list
        if (parentsMap.get(dest.getKey()) == -1) { return outputPath; } // -1 == not exist, both nodes is not connected
        for (NodeData tempN=dest; tempN != null; tempN = this.currGraph.getNode(parentsMap.get(tempN.getKey()))){ // reverse run from dest node to parents
            outputPath.addFirst(this.currGraph.getNode(parentsMap.get(tempN.getKey()))); // add first so we keep on order
        }
        return outputPath;
    }
}