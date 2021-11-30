package api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dwg implements DirectedWeightedGraph{

    HashMap<Integer, Node> nodeMap;
    HashMap<String, Edge> edgeMap;
    HashMap<Integer, HashMap<String, Edge>> edgeOutMap; // order is via the node that the edge starting from
    HashMap<Integer, HashMap<String, Edge>> edgeInMap; // order is via the node that the edge coming into

    int nodeSize, edgeSize;
    int mc; // mode counter

    /**
     * construct three hashmaps:
     * 1- all the nodes in, via integer id (just copy pointer, not deep copy)
     *
     * struct of the next bullets is: HashMap<Integer, HashMap<String, Edge>>
     * 2- edgeOutMap - first use node id key to get relevant hashmap that hold all the edges that the curr node is starting from
     * 3- edgeInMap - first use node id key to get relevant hashmap that hold all the edges that the curr node is coming into
     *
     *                          IMPORTANT, DONT MISS!!!!
     * bullets 2,3 inner HashMap key is via the formula: (String type) edge key = ""+src_node_id+","+dest_node_id
     *                          IMPORTANT, DONT MISS!!!!
     *
     * @param nodeMap - hashmap of nodes, key is the node id
     * @param edgeMap - hashmap of edges, key is ""+src_node_id+","+dest_node_id (for example "0,5", edge from 0 to 5)
     */
    public Dwg(HashMap<Integer, Node> nodeMap, HashMap<String, Edge> edgeMap) {
        this.nodeMap = nodeMap;
        this.edgeMap = edgeMap;
        this.nodeSize = nodeMap.size();
        this.edgeSize = edgeMap.size();
        this.mc = 0;

        //construct hashmaps for edges via nodes
        this.edgeInMap = new HashMap<>();
        this.edgeOutMap = new HashMap<>();
        for (int i=0; i < this.nodeSize; i++){
            // const hashmap for each node
            this.edgeOutMap.put(i, new HashMap<>());
            this.edgeInMap.put(i, new HashMap<>());
        }

        int tempSrc, tempDest;
        Iterator<Edge> iter = this.edgeMap.values().iterator();
        Edge tempE;
        while(iter.hasNext()){ // is stable since we dont remove any item from the map
            // set at each hashmap of node i, the relevant edges
            tempE = iter.next();
            tempSrc = tempE.getSrc();
            tempDest = tempE.getDest();
            this.edgeOutMap.get(tempSrc).put(""+tempSrc+","+tempDest, tempE);
            this.edgeInMap.get(tempDest).put(""+tempDest+","+tempSrc, tempE);
        }

    }

    /**
     * deep copy - deep copy for maps, edges, node!!! everything is new.
     * doing the same proccess as above constructor
     * @param existingDwg
     */
    public Dwg(Dwg existingDwg){
        this.nodeSize = existingDwg.nodeSize();
        this.edgeSize = existingDwg.edgeSize();
        this.mc = 0;

        Iterator<NodeData> itNode = existingDwg.nodeIter();
        Node tempN;
        this.nodeMap = new HashMap<>();
        while(itNode.hasNext()){
            tempN = (Node)itNode.next();
            this.nodeMap.put(tempN.getKey(), new Node(tempN));
        }
        this.edgeMap = new HashMap<>();
        this.edgeInMap = new HashMap<>();
        this.edgeOutMap = new HashMap<>();
        for (int i=0; i < this.nodeSize; i++){
            // const hashmap for each node
            this.edgeOutMap.put(i, new HashMap<>());
            this.edgeInMap.put(i, new HashMap<>());
        }

        Iterator<EdgeData> itEdge = existingDwg.edgeIter();
        Edge tempE;
        int tempSrc, tempDest;
        double tempWeight;
        while(itEdge.hasNext()){
            tempE = (Edge)itEdge.next();
            tempSrc = tempE.getSrc();
            tempDest = tempE.getDest();
            this.edgeMap.put(""+tempSrc+","+tempDest, new Edge(tempE));
            this.edgeOutMap.get(tempSrc).put(""+tempSrc+","+tempDest, new Edge (tempE));
            this.edgeInMap.get(tempDest).put(""+tempDest+","+tempSrc, new Edge (tempE));
        }
    }

    /**
     * @param key - the node_id
     * @return node via its id
     */
    @Override
    public NodeData getNode(int key) {
        return this.nodeMap.get(key);
    }

    /**
     * @param src - node id
     * @param dest - node id
     * @return edge via its src, dest nodes id
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        return this.edgeMap.get(""+src+","+dest);
    }

    /**
     * add node to the graph (without any edges)
     * @param n - node object
     */
    @Override
    public void addNode(NodeData n) {
        this.nodeMap.put(n.getKey(), new Node(n));
        this.mc++;
        this.nodeSize++;
    }

    /**
     * add an edge to the graph - which means, connection between to nodes
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        Edge tempE = new Edge(src, w, dest);
        this.edgeMap.put(""+src+","+dest, tempE);
        this.edgeOutMap.get(src).put(""+src+","+dest, tempE);
        this.edgeInMap.get(dest).put(""+dest+","+src, tempE);
        this.mc++;
        this.edgeSize++;
    }

    /**
     * we had to make our own iterator since the interface claims is to throw an exception if the mode counter(mc) has been changed
     * look for https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html#forEachRemaining-java.util.function.Consumer-
     * category - abstract method
     * implemented only the absract (what we must to..) which is hasNext and next funcs
     * the other functions can be used from the super class Iterator
     * @return ourNodeIterator - iterator that hold all the Node objects of the graph
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        // create our own iterator
        return new Iterator<>() {

            final int originModeCounter = getMC(); //
            final Iterator<Node> currIterator = nodeMap.values().iterator(); // regular iterator DO NOT REMOVE ANY ITEM FROM!

            @Override
            public boolean hasNext() { // added the throw RunTimeException
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                };
                return currIterator.hasNext();
            }

            @Override
            public NodeData next() { // added the throw RunTimeException
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                };
                return currIterator.next();
            }
        };
    }
    /**
     * we had to make our own iterator since the interface claims is to throw an exception if the mode counter(mc) has been changed
     * look for https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html#forEachRemaining-java.util.function.Consumer-
     * category - abstract method
     * implemented only the absract (what we must to..) which is hasNext and next funcs
     * the other functions can be used from the super class Iterator
     * @return ourNodeIterator - iterator that hold all the Edge objects of the graph
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<>() {
            final int originModeCounter = getMC(); //
            final Iterator<Edge> edgeIterator = edgeMap.values().iterator(); // regular iterator DO NOT REMOVE ANY ITEM FROM!

            @Override
            public boolean hasNext() {
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                }; // added the throw RunTimeException
                return edgeIterator.hasNext();
            }

            @Override
            public EdgeData next() {
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                }; // added the throw RunTimeException
                return edgeIterator.next();
            }
        };
    }
    /**
     * we had to make our own iterator since the interface claims is to throw an exception if the mode counter(mc) has been changed
     * look for https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html#forEachRemaining-java.util.function.Consumer-
     * category - abstract method
     * implemented only the absract (what we must to..) which is hasNext and next funcs
     * the other functions can be used from the super class Iterator
     * @return ourNodeIterator - iterator that hold all the Edge objects going out from SPECIFIC node
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new Iterator<>() {
            final int originModeCounter = getMC();
            final Iterator<Edge> edgeIterator = edgeOutMap.get(node_id).values().iterator(); // regular iterator DO NOT REMOVE ANY ITEM FROM!

            @Override
            public boolean hasNext() {
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                }; // added the throw RunTimeException
                return edgeIterator.hasNext();
            }

            @Override
            public EdgeData next() {
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                }; // added the throw RunTimeException
                return edgeIterator.next();
            }
        };
    }

    /**
     * update mc for all the edges that gonna be deleted with that node + the curr node
     * removing via the next process:
     * 1- run over all the edges that going outside the node -> remove the edges from edgeMapViaInNode, then remove the curr edge from outMap
     * 2- run over all the edges that going inside the node -> remove the edges from edgeMapViaOutNode, then remove the curr edge from inMap
     * 3- remove the maps of that nodes from each of the three maps of the graph
     * @param key - id of removing node
     * @return the node that we removed
     */
    @Override
    public NodeData removeNode(int key) {
        this.mc += this.edgeInMap.get(key).size() + this.edgeOutMap.get(key).size() + 1;
        this.nodeSize--;
        this.edgeSize = this.edgeSize - this.edgeInMap.get(key).size() - this.edgeOutMap.get(key).size();

        // used https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        // since for each loop can grant "unpredictable results" so we do it in old school style :-P
        int tempSrc, tempDest;
        Iterator<Map.Entry<String, Edge>> edgeEntries = this.edgeOutMap.get(key).entrySet().iterator();
        while (edgeEntries.hasNext()){
            // bullet "1-" of the removing process
            Map.Entry<String, Edge> edgeData = edgeEntries.next();
            tempSrc = ((Edge)edgeData).getSrc();
            tempDest = ((Edge)edgeData).getDest();
            this.edgeMap.remove(""+tempDest+","+tempSrc);
            this.edgeInMap.get(key).remove(""+tempDest+","+tempSrc);
            this.edgeOutMap.get(key).remove(""+tempSrc+","+tempDest);
        }
        edgeEntries = this.edgeOutMap.get(key).entrySet().iterator();
        while (edgeEntries.hasNext()){
            // bullet "2-" of the removing process
            Map.Entry<String, Edge> edgeData = edgeEntries.next();
            tempSrc = ((Edge)edgeData).getSrc();
            tempDest = ((Edge)edgeData).getDest();
            this.edgeMap.remove(""+tempSrc+","+tempDest);
            this.edgeOutMap.get(key).remove(""+tempSrc+","+tempDest);
            this.edgeInMap.get(key).remove(""+tempDest+","+tempSrc);
        }
        // bullet "3-" of the removing process
        this.edgeInMap.remove(key);
        this.edgeOutMap.remove(key);

        Node removedNode = this.nodeMap.get(key);
        this.nodeMap.remove(key);

        return removedNode;
    }

    /**
     * remove specific edge from the graph
     * @param src - id of node start
     * @param dest - id of node "to"
     * @return the removed edge
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        Edge removedEdge = this.edgeOutMap.get(src).get(""+src+","+dest);
        this.edgeMap.remove(""+src+","+dest);
        this.edgeOutMap.get(src).remove(""+src+","+dest);
        this.edgeInMap.get(dest).remove(""+dest+","+src);
        this.edgeSize--;
        this.mc++;
        return removedEdge;
    }

    /**
     * @return amount of nodes in the graph
     */
    @Override
    public int nodeSize() { return this.nodeSize; }

    /**
     * @return amounf of edges in the graph
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * @return how many edites have been done since the graph has been constructed
     */
    @Override
    public int getMC() {
        return this.mc;
    }
}
