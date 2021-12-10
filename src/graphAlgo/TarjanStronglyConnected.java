package graphAlgo;

import api.DirectedWeightedGraph;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * this class purpose is to check if graph is connected or not
 * since we dont need all the strongly connected components, we shall use strong connect functions as mentioned in psuedo code:
 * https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
 * only once, if the whole graph after one iteration not all the nodes in the graph have been visited, that means the whole graph
 * cant represented as one strongly connected component => which means its not connected
 */
public class TarjanStronglyConnected {

    DirectedWeightedGraph currGraph;
    Stack<Integer> stack;
    ArrayList<HashMap<Integer, Integer>> indexMap;
    ArrayList<HashMap<Integer, Integer>> lowLinkMap;
    ArrayList<HashMap<Integer, Boolean>> onStackMap;

    public TarjanStronglyConnected(DirectedWeightedGraph g){
        this.currGraph = g;
        this.stack = new Stack<>();
        this.indexMap = new ArrayList<>();
        this.lowLinkMap = new ArrayList<>();
        this.onStackMap = new ArrayList<>();
    }

    public void strongConnect(NodeData n){
        initMaps();
        int idx = 0;
        int node_id = n.getKey();
        this.indexMap.get(KeyTransform(node_id)).replace(node_id, idx);
        this.lowLinkMap.get(KeyTransform(node_id)).replace(node_id, idx);
        idx++;
        this.stack.push(node_id);
        this.onStackMap.get(KeyTransform(node_id)).replace(node_id, true);

        while (!this.stack.isEmpty()){




        }

    }
    private int KeyTransform(int key){return key%1000;}

    private void initMaps() {
        for (int i=0; i<1000 ;i++){
            this.indexMap.add(new HashMap<>());
            this.lowLinkMap.add(new HashMap<>());
            this.onStackMap.add(new HashMap<>());
        }
        Iterator<NodeData> itN = this.currGraph.nodeIter();
        int node_id, trans_id;
        while (itN.hasNext()){
            node_id = itN.next().getKey();
            trans_id = KeyTransform(node_id);
            this.indexMap.get(trans_id).put(node_id, -1);
            this.lowLinkMap.get(trans_id).put(node_id, -1);
            this.onStackMap.get(trans_id).put(node_id, false);
        }
    }


}
