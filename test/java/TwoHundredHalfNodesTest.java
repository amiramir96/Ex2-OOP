import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.NodeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impGraph.Dwg;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TwoHundredHalfNodesTest {
    /**
     * function I "borrowed" from Ex2
     * @param json_file
     * @return
     */
    private Dwg getGraph(String json_file) {
        File jsonGraphFile = new File(json_file); // get file
        Dwg ans = null;

        try {
            JsonElement graphElement = JsonParser.parseReader(new FileReader(jsonGraphFile)); // shall read, so have to handle exception
            JsonObject graphObject = graphElement.getAsJsonObject(); // convert to json object, then we can work on fields inside
            // jsonToNode return list of nodes, jsonToEdge return list of edges, both is the req to cons Dwg(directed weighted graph)
            LoadGraph g = new LoadGraph();
            ans = (Dwg) g.loadGraph(json_file);
        }
        catch (FileNotFoundException err){ // throw exception..
            err.printStackTrace();
        }

        return ans;
    }

    DirectedWeightedGraph d = getGraph("test\\resources\\G250K.json"); //initialize graph
    DwgMagic thousend = new DwgMagic(d);
    int size = 250000;

    @Test
    void load() {
        thousend.load("test\\resources\\G250K.json");
        assertEquals(thousend.getGraph().nodeSize(), size);
    }
    @Test
    void init() {
        this.thousend.init(this.thousend.getGraph());
        assertEquals(thousend.getGraph().nodeSize(), size);
    }

    @Test
    void getGraph() {
        d = this.thousend.getGraph();
        assertEquals(thousend.getGraph().nodeSize(), size);
    }

    @Test
    void copy() {
        this.d = this.thousend.copy();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void isConnected() {
        this.thousend.isConnected();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPathDist() {
        this.thousend.shortestPathDist(1,505);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void shortestPath() {
        this.thousend.shortestPathDist(8,730);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void center() {
        this.thousend.center();
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }

    @Test
    void tsp() {
        LinkedList<NodeData> l1 = new LinkedList<>();
        l1.add(this.d.getNode(1171));
        l1.add(this.d.getNode(73023));
        l1.add(this.d.getNode(58455));
        l1.add(this.d.getNode(22844));
        l1.add(this.d.getNode(3213));
        l1.add(this.d.getNode(177161));
        l1.add(this.d.getNode(8172));
        l1.add(this.d.getNode(113665));
        l1.add(this.d.getNode(9752));
        l1.add(this.d.getNode(78));
        l1.add(this.d.getNode(931));

        this.thousend.tsp(l1);
        assertEquals(thousend.getGraph().nodeSize(), this.size);
    }
}