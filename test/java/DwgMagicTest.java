import api.DirectedWeightedGraph;
import api.NodeData;
import impGraph.Dwg;
import impGraph.DwgMagic;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DwgMagicTest {
//
    @Test
    void init() {
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void center() {
    }

    @Test
    void tsp() {
        DwgMagic m = new DwgMagic(new Dwg());
        DirectedWeightedGraph g;
        m.load("test\\resources\\G1.json");
        g = m.getGraph();
        List<NodeData> l1 = new LinkedList<>();
        l1.add(g.getNode(9));
        l1.add(g.getNode(2));
        l1.add(g.getNode(7));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 9);
        assertEquals(l1.get(1).getKey(), 8);
        assertEquals(l1.get(2).getKey(), 7);
        assertEquals(l1.get(3).getKey(), 6);
        assertEquals(l1.get(4).getKey(), 2);
        assertEquals(l1.size(), 5);

        m.load("test\\resources\\G3.json");
        g = m.getGraph();
        l1 = new LinkedList<>();
        // 21,24,14,3,10,7
        l1.add(g.getNode(21));
        l1.add(g.getNode(24));
        l1.add(g.getNode(14));
        l1.add(g.getNode(3));
        l1.add(g.getNode(10));
        l1.add(g.getNode(7));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 21);
        assertEquals(l1.get(1).getKey(), 32);
        assertEquals(l1.get(2).getKey(), 31);
        assertEquals(l1.get(3).getKey(), 24);
        assertEquals(l1.get(4).getKey(), 31);
        assertEquals(l1.get(5).getKey(), 30);
        assertEquals(l1.get(6).getKey(), 29);
        assertEquals(l1.get(7).getKey(), 14);
        assertEquals(l1.get(8).getKey(), 13);
        assertEquals(l1.get(9).getKey(), 3);
        assertEquals(l1.get(10).getKey(), 12);
        assertEquals(l1.get(11).getKey(), 10);
        assertEquals(l1.get(12).getKey(), 7);
        assertEquals(l1.size(), 13);

        m.load("test\\resources\\G10K.json");
        // 9172,4255,8888,3835,808,8275,4404,478,4589
        g = m.getGraph();
        l1 = new LinkedList<>();
        l1.add(g.getNode(9172));
        l1.add(g.getNode(4404));
        l1.add(g.getNode(478));
        l1.add(g.getNode(1233));
        l1.add(g.getNode(4589));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 9172);
        assertEquals(l1.get(1).getKey(), 4404);
        assertEquals(l1.get(2).getKey(), 478);
        assertEquals(l1.get(3).getKey(), 4255);
        assertEquals(l1.get(4).getKey(), 8888);
        assertEquals(l1.get(5).getKey(), 3835);
        assertEquals(l1.get(6).getKey(), 7148);
        assertEquals(l1.get(7).getKey(), 4589);
        assertEquals(l1.get(8).getKey(), 1154);
        assertEquals(l1.get(9).getKey(), 808);
        assertEquals(l1.get(10).getKey(), 2316);
        assertEquals(l1.get(11).getKey(), 8275);
        assertEquals(l1.get(12).getKey(), 5668);
        assertEquals(l1.get(13).getKey(), 6856);
        assertEquals(l1.get(14).getKey(), 108);
        assertEquals(l1.get(15).getKey(), 4626);
        assertEquals(l1.get(16).getKey(), 4851);
        assertEquals(l1.get(17).getKey(), 1233);
        assertEquals(l1.size(), 18);

        // 9172,4255,8888,3835,808,8275,4404,478,4589
        l1 = new LinkedList<>();

        l1.add(g.getNode(9172));
        l1.add(g.getNode(4255));
        l1.add(g.getNode(8888));
        l1.add(g.getNode(3835));
        l1.add(g.getNode(808));
        l1.add(g.getNode(8275));
        l1.add(g.getNode(4404));
        l1.add(g.getNode(478));
        l1.add(g.getNode(4589));
        l1 = m.tsp(l1);
        assertEquals(l1.get(0).getKey(), 9172);
        assertEquals(l1.get(1).getKey(), 4404);
        assertEquals(l1.get(2).getKey(), 478);
        assertEquals(l1.get(3).getKey(), 4255);
        assertEquals(l1.get(4).getKey(), 8888);
        assertEquals(l1.get(5).getKey(), 3835);
        assertEquals(l1.get(6).getKey(), 7148);
        assertEquals(l1.get(7).getKey(), 4589);
        assertEquals(l1.get(8).getKey(), 1154);
        assertEquals(l1.get(9).getKey(), 808);
        assertEquals(l1.get(10).getKey(), 2316);
        assertEquals(l1.get(11).getKey(), 8275);
        assertEquals(l1.size(), 12);
    }

    @Test
    void save() {
    }
//
    @Test
    void load() {
    }
}