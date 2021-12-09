package impGraph;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import java.util.*;
import java.util.function.Consumer;

public class Dwg implements DirectedWeightedGraph {
    HashMap<Integer, NodeData> nodeMap; // all nodes
    /**
     * for reduce sequent size block in the memory in huge graph cases, this strategy avoid from creating hashmaps of milions of items
     * ea of the maps, contains 3 shells:
     * 1- hasg all nodes to 1000 diffrent groups by KeyTransform function (simple modulo arithmetic cal)
     * 2- at ea group, there is map for every certain group of edges that represented via SRC_id node
     * 3- to get from ea group the exact edge, input the Dest_id node of the edge as key
     * ------------------in summary:----------------------
     * given edge within src and dest nodes which their id keys is: src.getKey():=x, dest.getKey():=y
     * hashMap keys: key1 := src_node_id%1000 , key2 := src_node_id key3 := dest_node_id
     * (key1, key2, key3)
     */
    HashMap<Integer, HashMap<Integer, HashMap<Integer, EdgeData>>> edgeOutMap; // order is via the node that the edge starting from
    HashMap<Integer, HashMap<Integer, HashMap<Integer, EdgeData>>> edgeInMap; // order is via the node that the edge coming into

    int nodeSize, edgeSize; // sizes
    int mc; // mode counter

    /**
     *  transform node_id num to "key1" (first key) for edge Hashmaps
     * @param id - node.getKey()
     * @return
     */
    private int KeyTransform(int id){
        return id%1000;
    }

    // constructor
    /**
     * construct empty graph
     */
    public Dwg(){
        this.nodeMap = new HashMap<>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.mc = 0;

        // construct hashmaps for edges via nodes
        // shall initialize all modulo groups
        this.edgeInMap = new HashMap<>();
        this.edgeOutMap = new HashMap<>();
        for (int i=0; i<1000; i++){
            this.edgeOutMap.put(i, new HashMap<>());
            this.edgeInMap.put(i, new HashMap<>());
        }

    }

    /**
     * construct three hashmaps:
     * map for nodes
     * map for edges ordered via "from" node
     * map for edges ordered via "to" node
     *                          Reminder!!!!
     * edgeMaps keys: key1 := src_node_id%1000 , key2 := src_node_id key3 := dest_node_id
     *                      (key1, key2, key3)
     *                          Reminder!!!!
     * @param nodeMap - hashmap of nodes, key is the node id
     * @param listOfEdges - list of edges
     */
    public Dwg(HashMap<Integer, NodeData> nodeMap, List<EdgeData> listOfEdges) {
        this.nodeMap = nodeMap;
        this.nodeSize = nodeMap.size();
        this.edgeSize = listOfEdges.size();
        this.mc = 0;

        // construct hashmaps for edges via nodes
        this.edgeInMap = new HashMap<>();
        this.edgeOutMap = new HashMap<>();
        // create modulo external maps
        for (int i=0; i<1000; i++){
            this.edgeOutMap.put(i, new HashMap<>());
            this.edgeInMap.put(i, new HashMap<>());
        }
        // init vars
        int tempSrc, tempDest, keyTrans1, keyTrans2;
        Iterator<EdgeData> iter = listOfEdges.iterator();
        EdgeData tempE;
        while(iter.hasNext()){
            // set at each hashmap of node i, the relevant edges
            tempE = iter.next();
            // get key1
            keyTrans1 = KeyTransform(tempE.getSrc());
            keyTrans2 = KeyTransform(tempE.getDest());
            tempSrc = tempE.getSrc();
            tempDest = tempE.getDest();
            // construct new map(category) for nodes that doesnt have edges till curr edge
            if (!this.edgeOutMap.get(keyTrans1).containsKey(tempSrc)) {
                this.edgeOutMap.get(keyTrans1).put(tempSrc, new HashMap<>());
            }
            if (!this.edgeInMap.get(keyTrans2).containsKey(tempDest)) {
                this.edgeInMap.get(keyTrans2).put(tempDest, new HashMap<>());
            }
            // add edges to the exact place
            this.edgeOutMap.get(keyTrans1).get(tempSrc).put(tempDest, tempE);
            this.edgeInMap.get(keyTrans2).get(tempDest).put(tempSrc, tempE);
        }
    }

    /**
     * deep copy - deep copy for maps, edges, node!!! everything is new.
     * doing the same proccess as above constructor
     * @param existingDwg - graph that already exists
     */
    public Dwg(DirectedWeightedGraph existingDwg){
        // init basic
        this.nodeSize = existingDwg.nodeSize();
        this.edgeSize = existingDwg.edgeSize();
        this.mc = 0;

        //construct hashmaps for edges via nodes

        this.edgeInMap = new HashMap<>();
        this.edgeOutMap = new HashMap<>();
        // create modulo external maps
        for (int i=0; i<1000; i++){
            this.edgeOutMap.put(i, new HashMap<>());
            this.edgeInMap.put(i, new HashMap<>());
        }

        // iterating over nodes and deep copy
        Iterator<NodeData> itNode = existingDwg.nodeIter();
        NodeData tempN;
        this.nodeMap = new HashMap<>();
        while(itNode.hasNext()){
            tempN = itNode.next();
            tempN = new Node(tempN);
            this.nodeMap.put(tempN.getKey(), new Node(tempN));
        }
        int keyTrans1, keyTrans2;
        // iterating over edges and deep copy
        Iterator<EdgeData> itEdge = existingDwg.edgeIter();
        EdgeData tempE;
        int tempSrc, tempDest;
        while(itEdge.hasNext()){
            tempE = itEdge.next();
            tempE = new Edge(tempE);
            // get key1
            keyTrans1 = KeyTransform(tempE.getSrc());
            keyTrans2 = KeyTransform(tempE.getDest());
            tempSrc = tempE.getSrc();
            tempDest = tempE.getDest();
            // construct new map(category) for nodes that doesnt have edges till curr edge
            if (!this.edgeOutMap.get(keyTrans1).containsKey(tempSrc)) {
                this.edgeOutMap.get(keyTrans1).put(tempSrc, new HashMap<>());
            }
            if (!this.edgeInMap.get(keyTrans2).containsKey(tempDest)) {
                this.edgeInMap.get(keyTrans2).put(tempDest, new HashMap<>());
            }
            // add edge
            this.edgeOutMap.get(keyTrans1).get(tempSrc).put(tempDest, new Edge(tempE));
            this.edgeInMap.get(keyTrans2).get(tempDest).put(tempSrc, new Edge (tempE));
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
        return this.edgeOutMap.get(KeyTransform(src)).get(src).get(dest);
    }

    /**
     * add node to the graph (without any edges)
     * @param n - node object
     */
    @Override
    public void addNode(NodeData n) {
        this.nodeMap.put(n.getKey(), n);
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
        // build edge
        Edge tempE = new Edge(src, w, dest);
        // construct new map(category) for nodes that doesnt have edges till curr edge
        if (!this.edgeOutMap.get(KeyTransform(src)).containsKey(src)){
            this.edgeOutMap.get(KeyTransform(src)).put(src, new HashMap<>());
        }
        if (!this.edgeInMap.get(KeyTransform(dest)).containsKey(dest)){
            this.edgeInMap.get(KeyTransform(dest)).put(dest, new HashMap<>());
        }
        // add edge to maps
        this.edgeOutMap.get(KeyTransform(src)).get(src).put(dest, tempE);
        this.edgeInMap.get(KeyTransform(dest)).get(dest).put(src, tempE);
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

            int originModeCounter = mc; //
            Iterator<NodeData> currIterator = nodeMap.values().iterator(); // regular iterator DO NOT REMOVE ANY ITEM FROM!
            NodeData tempN;

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
                tempN = currIterator.next();
                return tempN;
            }

            @Override
            public void remove() {
                if (mc != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
                };
                else {
                    removeNode(tempN.getKey());
                    originModeCounter = mc;
                }
            }

            // https://stackoverflow.com/questions/42465871/whats-the-point-of-having-both-iterator-foreachremaining-and-iterable-foreach/42466144
            // comment #1, helped to understand how its worked
            @Override
            public void forEachRemaining(Consumer<? super NodeData> action) {
                while(currIterator.hasNext()){
                    action.accept(next());
                }
            }
        };
    }

    /**
     * chain between iterators of ea node by:
     * loop over all nodes and add to ArrayList edgeIter(node_i_id)
     * use mergedIterators object to merge between them
     * for more details on mergedIterators, look at mergedIterators class
     * @return mergedIterators
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        ArrayList<Iterator<EdgeData>> linkedIterators = new ArrayList<>();
        for (NodeData n : this.nodeMap.values()){
            if (this.edgeOutMap.get(KeyTransform(n.getKey())).containsKey(n.getKey())){
                linkedIterators.add(new edgeIter(this, n.getKey()));
            }
        }
        return new mergedIterators(this, linkedIterators);
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
        edgeIter e = new edgeIter(this, node_id);
        return e;
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
        if (this.nodeSize == 1 || this.edgeSize == 0){
            NodeData n = this.getNode(key);
            this.nodeMap.remove(key);
            this.nodeSize--;
            this.mc++;
            return n;
        }
        this.nodeSize--;
        EdgeData tempE;
        ArrayList<EdgeData> eList;
        Iterator<Map.Entry<Integer, EdgeData>> edgeEntries;
        // used https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        // since for each loop can grant "unpredictable results" so we do it in old school style :-P
        if (this.edgeOutMap.get(KeyTransform(key)).get(key) != null && this.edgeOutMap.get(KeyTransform(key)).get(key).size() != 0){
            edgeEntries = this.edgeOutMap.get(KeyTransform(key)).get(key).entrySet().iterator();
            eList = new ArrayList<>();
            while (edgeEntries.hasNext()){
                // bullet "1-" of the removing process
                Map.Entry<Integer, EdgeData> entry = (Map.Entry<Integer, EdgeData>) (edgeEntries.next());
                tempE = entry.getValue();
                eList.add(tempE);
            }
            for (EdgeData element : eList){
                removeEdge(element.getSrc(), element.getDest());
            }
        }
        if (this.edgeInMap.get(KeyTransform(key)).get(key) != null && this.edgeInMap.get(KeyTransform(key)).get(key).size() != 0){
            eList = new ArrayList<>();
            edgeEntries = this.edgeInMap.get(KeyTransform(key)).get(key).entrySet().iterator();
            while (edgeEntries.hasNext()){
                // bullet "2-" of the removing process
                Map.Entry<Integer, EdgeData> entry = (Map.Entry<Integer, EdgeData>) (edgeEntries.next());
                tempE = entry.getValue();
                eList.add(tempE);
            }
            for (EdgeData element : eList){
                removeEdge(element.getSrc(), element.getDest());
            }
        }
        // bullet "3-" of the removing process
        this.edgeInMap.get(KeyTransform(key)).remove(key);
        this.edgeOutMap.get(KeyTransform(key)).remove(key);

        NodeData removedNode = this.nodeMap.get(key);
        // update vars
        this.nodeMap.remove(key);
        this.mc++;
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
        // save the removed edge
        EdgeData removedEdge = this.edgeOutMap.get(KeyTransform(src)).get(src).get(dest);
        // remove the edges from the edge maps
        this.edgeOutMap.get(KeyTransform(src)).get(src).remove(dest);
        this.edgeInMap.get(KeyTransform(dest)).get(dest).remove(src);
        // remove node_id maps if tehre is no more edge from/to him
        if (this.edgeOutMap.get(KeyTransform(src)).get(src).isEmpty()){
            this.edgeOutMap.get(KeyTransform(src)).remove(src);
        }
        if (this.edgeInMap.get(KeyTransform(dest)).get(dest).isEmpty()){
            this.edgeInMap.get(KeyTransform(dest)).remove(dest);
        }
        // update vars
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
     * @return amount of edges in the graph
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
