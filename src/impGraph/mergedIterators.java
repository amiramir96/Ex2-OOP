package impGraph;

import api.DirectedWeightedGraph;
import api.EdgeData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Cycle through iterators
 */
public class mergedIterators implements Iterator<EdgeData> {

    ArrayList<Iterator<EdgeData>> mergedIter;
    DirectedWeightedGraph currGraph;
    int currectIdx;

    mergedIterators(DirectedWeightedGraph g, ArrayList<Iterator<EdgeData>> itList){
        this.mergedIter = itList;
        this.currGraph = g;
        this.currectIdx = 0;
    }

    @Override
    public boolean hasNext() {
        if (this.currectIdx < this.mergedIter.size() && !this.mergedIter.get(currectIdx).hasNext()){
            this.currectIdx++;
        }
        return (this.mergedIter.size() > this.currectIdx) && (this.mergedIter.get(currectIdx).hasNext());
    }

    @Override
    public EdgeData next() {
        if (this.currectIdx < this.mergedIter.size() && !this.mergedIter.get(currectIdx).hasNext()){
            this.currectIdx++;
        }
        return this.mergedIter.get(currectIdx).next();

    }
}
