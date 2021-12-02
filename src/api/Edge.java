package api;

/**
 * implemented as writen in the interface EdgeData
 */
public class Edge implements EdgeData{
    private int _src, _dest, tag;
    private double _weight;
    private String metaData;

    public Edge(int src, double weight, int dest) {
        this._src = src;
        this._weight = weight;
        this._dest = dest;
        this.tag = 0;
        this.metaData = "";
    }

    public Edge(EdgeData e){
        this._src = e.getSrc();
        this._weight = e.getWeight();
        this._dest = e.getDest();
        this.tag = e.getTag();
        this.metaData = e.getInfo();
    }

    @Override
    public int getSrc() {
        return this._src;
    }

    @Override
    public int getDest() {
        return this._dest;
    }

    @Override
    public double getWeight() {
        return this._weight;
    }

    @Override
    public String getInfo() {
        return this.metaData;
    }

    @Override
    public void setInfo(String s) {
        this.metaData = "" + s;
    }

    @Override
    public int getTag() { return this.tag; }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public String toString(){
        return "from: "+this._src+" to: "+this._dest+" weight: "+this._weight;
    }
}
