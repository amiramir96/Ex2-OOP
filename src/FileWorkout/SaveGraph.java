package FileWorkout;

import api.DirectedWeightedGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
