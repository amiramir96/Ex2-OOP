package api;

import java.util.Iterator;

public class Node implements NodeData{
    private Point3D cord;
    private int _id;
    private int tag;
    private String metaData;

    public Node(Point3D cord, int id) {
        this.cord = cord;
        this._id = id;
        this.tag = 0;
        this.metaData = "";
    }

    public Node(NodeData n){
        this.cord = new Point3D(n.getLocation());
        this._id = n.getKey();
        this.tag = n.getTag();
        this.metaData = n.getInfo();
    }

    @Override
    public int getKey() {
        return this._id;
    }

    @Override
    public GeoLocation getLocation() {
        return this.cord;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.cord._x = p.x();
        this.cord._y = p.y();
        this.cord._z = p.z();
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
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }


    // irrelevant for this exercise
    @Override
    public double getWeight() {
        return 0;
    }
    // irrelevant for this exercise
    @Override
    public void setWeight(double w) {}
}
