import FileWorkout.LoadGraph;
import FileWorkout.SaveGraph;
import api.DirectedWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SaveGraphTest {

    @Test
    void save() throws IOException {
        LoadGraph loader = new LoadGraph();
        DirectedWeightedGraph dwg = loader.loadGraph("test\\resources\\G1.json");
        SaveGraph.save("test\\resources\\saved_graph.json", dwg);
        DirectedWeightedGraph dwg2 = loader.loadGraph("test\\resources\\saved_graph.json");
        assertEquals(dwg.edgeSize(), dwg2. edgeSize());
        assertEquals(dwg.nodeSize(), dwg2.nodeSize());
    }

    @Test
    void edgeToObject() {
        // submethod of save, tested there
    }

    @Test
    void nodeToObject() {
        // submethod of save, tested there
    }
}