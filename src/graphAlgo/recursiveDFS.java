package graphAlgo;

import api.Dwg;
import api.Edge;
import api.Node;
import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;

public class recursiveDFS {

    Dwg currGraph;
    public recursiveDFS(Dwg g) {
        this.currGraph = g;
    }

//    public boolean mainProcessIsConnected(Node start) {
//        HashMap<Integer, Node> predecessortMap = new HashMap<>();
//        HashMap<Integer, Boolean> visitedMap = new HashMap<>();
//        long time = 0;
//
//        initMaps(visitedMap, predecessortMap);
//        recursiveDFS(start, visitedMap, this.currGraph.edgeOutMap);
//        for (Boolean visited : visitedMap.values()) { if (!visited) {return false;}}
//
//        initMaps(visitedMap, predecessortMap);
//        recursiveDFSTranspose(start, visitedMap, this.currGraph.edgeInMap);
//        for (Boolean visited : visitedMap.values()) { if (!visited) {return false; } }
//
//        return true;
//    }

    private void initMaps(HashMap<Integer, Boolean> visitMap, HashMap<Integer, Node> predecessortMap){
        Node tempN;
        Iterator<NodeData> itNode = this.currGraph.nodeIter();
        if (visitMap.isEmpty()){
            while (itNode.hasNext()){
                tempN = (Node)itNode.next();
                predecessortMap.put(tempN.getKey(), null);
                visitMap.put(tempN.getKey(), false);
            }
        }
        else {
            while (itNode.hasNext()) {
                tempN = (Node) itNode.next();
                predecessortMap.replace(tempN.getKey(), null);
                visitMap.replace(tempN.getKey(), false);
            }
        }
    }

    void recursiveDFS(Node start, HashMap<Integer, Boolean> visitMap, HashMap<Integer, HashMap<String, Edge>> adjMap){
        if (visitMap.get(start.getKey())){return;}
        visitMap.replace(start.getKey(), true);

        Node tempN;
        for (Edge edge : adjMap.get(start.getKey()).values()) {
            tempN = (Node) this.currGraph.getNode(edge.getDest());
            recursiveDFS(tempN, visitMap, adjMap);
        }
    }

    void recursiveDFSTranspose(Node start, HashMap<Integer, Boolean> visitMap, HashMap<Integer, HashMap<String, Edge>> adjMap){
        if (visitMap.get(start.getKey())){return;}
        visitMap.replace(start.getKey(), true);

        Node tempN;
        for (Edge edge : adjMap.get(start.getKey()).values()) {
            tempN = (Node) this.currGraph.getNode(edge.getSrc());
            recursiveDFSTranspose(tempN, visitMap, adjMap);
        }
    }
}
