package FileWorkout;

import api.DirectedWeightedGraph;

public class SaveGraph {

    DirectedWeightedGraph currGraph;
    String directory;

    public SaveGraph(String file, DirectedWeightedGraph g){
        this.directory = file;
        this.currGraph = g;
    }

    public boolean runSave(){
        return false;
    }
}
