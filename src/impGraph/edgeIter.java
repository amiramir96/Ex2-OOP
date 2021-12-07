package impGraph;

import api.EdgeData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * implements our own iterator class for iterEdge(node_id)
 * why? ez and ordered way to avoid from iteration on null (in place of iterating on hashmap size 0)
 */

public class edgeIter implements Iterator<EdgeData> {

    Dwg currGraph;
    int originModeCounter;
    Iterator<EdgeData> edgeIterator; // regular iterator DO NOT REMOVE ANY ITEM FROM!
    EdgeData tempE;

    edgeIter(Dwg g, int node_id){
        this.currGraph = g;
        this.originModeCounter = g.getMC();
        // below is the main reason why we use this class and not "unnamed iterator"
        if (this.currGraph.edgeOutMap.containsKey(node_id)){
            edgeIterator = this.currGraph.edgeOutMap.get(node_id).values().iterator();
        }
        else {
            edgeIterator = new HashMap<String, EdgeData>().values().iterator(); //will iterate over 0 items but not null!
        }
    }

    @Override
    public boolean hasNext() {
        if (this.currGraph.getMC() != originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        return edgeIterator.hasNext();
    }

    @Override
    public EdgeData next() {
        if (this.currGraph.getMC() != this.originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        tempE = edgeIterator.next();
        return tempE;
    }

    @Override
    public void remove() {
        if (this.currGraph.getMC() != this.originModeCounter) throw new RuntimeException("the graph isnt the same as it was") {
        }; // added the throw RunTimeException
        else {
            this.currGraph.removeEdge(tempE.getSrc(), tempE.getDest());
            this.originModeCounter = this.currGraph.getMC();
        }
    }

    // https://stackoverflow.com/questions/42465871/whats-the-point-of-having-both-iterator-foreachremaining-and-iterable-foreach/42466144
    // comment #1, helped to understand how its worked
    @Override
    public void forEachRemaining(Consumer<? super EdgeData> action) {
        while(edgeIterator.hasNext()){
            action.accept(next());
        }
    }
}
