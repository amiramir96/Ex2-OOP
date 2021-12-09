package impGraph;

import api.NodeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

public class nodeIter implements Iterator<NodeData> {

    Dwg currGraph;
    int originModeCounter;
    Iterator<NodeData> nodeIterator; // regular iterator DO NOT REMOVE ANY ITEM FROM!
    NodeData tempN;

    // constructor for specific idx of the list (nodemaps) an iterator
    nodeIter(Dwg g, int node_id){
        this.currGraph = g;
        this.originModeCounter = g.getMC();
        // below is the main reason why we use this class and not "unnamed iterator"
        if (!this.currGraph.nodeMap.get(node_id%1000).isEmpty()){
            this.nodeIterator = this.currGraph.nodeMap.get(node_id%1000).values().iterator();
        }
        else {
            this.nodeIterator = new HashMap<Integer, NodeData>().values().iterator(); //will iterate over 0 items but not null!
        }
    }

    @Override
    public boolean hasNext() {
        if (this.currGraph.getMC() != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        return nodeIterator.hasNext();
    }

    @Override
    public NodeData next() {
        if (this.currGraph.getMC() != this.originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        this.tempN = nodeIterator.next();
        return this.tempN;
    }

    @Override
    public void remove() {
        if (this.currGraph.getMC() != this.originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        else {
            this.currGraph.removeNode(tempN.getKey());
            this.originModeCounter = this.currGraph.getMC();
        }
    }

    // https://stackoverflow.com/questions/42465871/whats-the-point-of-having-both-iterator-foreachremaining-and-iterable-foreach/42466144
    // comment #1, helped to understand how its worked
    @Override
    public void forEachRemaining(Consumer action) {
        while(nodeIterator.hasNext()){
            action.accept(next());
        }
    }
}
