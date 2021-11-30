package api;

import java.util.List;

public class DwgMagic implements DirectedWeightedGraphAlgorithms{
    private Dwg currGraph;

    public DwgMagic(Dwg dwg) {
        this.currGraph = dwg;
        
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.currGraph = (Dwg) g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.currGraph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return new Dwg(this.currGraph);
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
