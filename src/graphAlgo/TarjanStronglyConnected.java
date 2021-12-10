package graphAlgo;

import api.DirectedWeightedGraph;
import api.EdgeData;
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
    // vars
    DirectedWeightedGraph currGraph;
    Stack<Integer> stack;
    ArrayList<HashMap<Integer, Integer>> indexMap; // index of node (when we found it)
    ArrayList<HashMap<Integer, Integer>> lowLinkMap; // lowlink
    ArrayList<HashMap<Integer, Boolean>> onStackMap; // visited?
    int idx; // idx control over which num to give a given node in the indexMap
    // construct maps
    public TarjanStronglyConnected(DirectedWeightedGraph g){
        this.currGraph = g;
        this.stack = new Stack<>();
        this.indexMap = new ArrayList<>();
        this.lowLinkMap = new ArrayList<>();
        this.onStackMap = new ArrayList<>();
        initMaps();
        this.idx = 0;
    }

    /**
     * tarjan algorithm
     * in origin, returns all the SCC (strongly connected components) of the graph
     * this tarjan is crafted to check if the graph is connected, so in place of searching for all SCC
     * this algo will check if all the nodes in the graph is able to get into the same SCC (which means the graph is connected)
     * @param n - start node
     */
    public void tarjanRecursive(NodeData n){
        // init vars
        int node_id = n.getKey();
        this.indexMap.get(KeyTransform(node_id)).replace(node_id, idx);
        this.lowLinkMap.get(KeyTransform(node_id)).replace(node_id, idx);
        idx++;
        this.stack.push(node_id);
        this.onStackMap.get(KeyTransform(node_id)).replace(node_id, true);

        int tempE_dest;

        // iteratte all over the edges of the given node
        Iterator<EdgeData> itEdge = this.currGraph.edgeIter(node_id);
        while (itEdge.hasNext()){
            tempE_dest = itEdge.next().getDest();
            if (this.indexMap.get(KeyTransform(tempE_dest)).get(tempE_dest) == -1){
                // start the recursion before the command!!! here is the magic
                // i cant xfer it to iterative :/
                tarjanRecursive(this.currGraph.getNode(tempE_dest));
                this.lowLinkMap.get(KeyTransform(node_id)).replace(node_id, Math.min(this.lowLinkMap.get(KeyTransform(node_id)).get(node_id), this.lowLinkMap.get(KeyTransform(tempE_dest)).get(tempE_dest)));
            }
            else if (this.onStackMap.get(KeyTransform(tempE_dest)).get(tempE_dest)){
                this.lowLinkMap.get(KeyTransform(node_id)).replace(node_id, Math.min(this.lowLinkMap.get(KeyTransform(node_id)).get(node_id), this.lowLinkMap.get(KeyTransform(tempE_dest)).get(tempE_dest)));
            }
        }
    }

    /**
     * check if there is unvisited node -> graph is not connected
     * @return boolean. graph is connected
     */
    public boolean tarjanConnected(){
        boolean ans = true;
        for (HashMap<Integer, Boolean> element : this.onStackMap){
            for (Boolean b : element.values()){
                if (!b){
                    ans = b;
                }
            }
        }
        return ans;
    }


    // help to mange keys of maps
    private int KeyTransform(int key){return key%1000;}

    /**
     * initialize maps while constructor
     */
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
