package api;

public class Edge implements EdgeData{
    private int _src, _dest;
    private double weight;

    public Edge(int src, double weight, int dest) {
        this._src = src;
        this.weight = weight;
        this._dest = dest;
    }


    @Override
    public int getSrc() {
        return 0;
    }

    @Override
    public int getDest() {
        return 0;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setInfo(String s) {

    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public void setTag(int t) {

    }
}
