package correctness;

import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.NodeData;
import graphAlgo.DFS;
import impGraph.Node;
import impGraph.Point3D;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DFSTest {

    @Test
    void mainProcessIsConnected() {
        DirectedWeightedGraph g1 = null;
        try {
            g1 = LoadGraph.loadGraph("test\\resources\\GShfiut.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DFS dfs = new DFS(g1);
        NodeData n1 = g1.nodeIter().next();
        assertTrue(dfs.mainProcessIsConnected(n1));
        // disconnect the graph
        Point3D p1 = new Point3D(0,0,0);
        Node n2 = new Node(p1,20);
        g1.addNode(n2);
        assertFalse(dfs.mainProcessIsConnected(n1));
    }
}