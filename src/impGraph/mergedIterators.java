package impGraph;

import api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Cycle through iterators
 */
public class mergedIterators<O> implements Iterator<O> {

    /**
     * this object purpose is to merge between Iterators type EdgeData
     * used the above instruction to build this class
     * https://stackoverflow.com/questions/9200080/join-multiple-iterators-in-java?lq=1 comment 0
     */

    ArrayList<Iterator<O>> mergedIter; // iterators list (will loop over them)
    DirectedWeightedGraph currGraph; // our graph
    int currectIdx; // represent which iterator in use now
    int MC;

    /**
     * basic constructor
     * @param g - relevant graph
     * @param itList - list of iterators
     */
    mergedIterators(DirectedWeightedGraph g, ArrayList<Iterator<O>> itList){
        this.mergedIter = itList;
        this.currGraph = g;
        this.currectIdx = 0;
        this.MC = g.getMC();
    }

    /**
     * monitoring 2 things:
     * 1- there is at least 1 more iterator which we didnt iterated completly over
     * 2- the curr iterator has next
     * @return boolean, if there is next object in the iterator
     */
    @Override
    public boolean hasNext() {
        // check if we shall forward to the next iterator in the list
        while (this.currectIdx < this.mergedIter.size() && !this.mergedIter.get(currectIdx).hasNext()){
            this.currectIdx++;
        }
        // return has next
        return (this.mergedIter.size() > this.currectIdx) && (this.mergedIter.get(currectIdx).hasNext());
    }

    /**
     * 1- there is at least 1 more iterator which we didnt iterated completly over
     * 2- the curr iterator has next
     * @return next Edge (null if there isnt next)
     */
    @Override
    public O next() {
        // check if we shall forward to the next iterator in the list
        while (this.currectIdx < this.mergedIter.size() && !this.mergedIter.get(currectIdx).hasNext()){
            this.currectIdx++;
        }
        return this.mergedIter.get(currectIdx).next();

    }


    @Override
    public void remove() {
        // check if we shall forward to the next iterator in the list
//        while (this.currectIdx < this.mergedIter.size() && !this.mergedIter.get(currectIdx).hasNext()){
//            this.currectIdx++;
//        }
        this.mergedIter.get(currectIdx).remove();
        MC++;
    }

    @Override
    public void forEachRemaining(Consumer<? super O> action) {
        Iterator.super.forEachRemaining(action);
    }
}
