package impGraph;

import api.GeoLocation;

/**
 * via GeoLocation interface
 * represent points in 3D plane
 * in this programme we gonna use only x and y cordinates
 */

public class Point3D implements GeoLocation {
    double _x, _y, _z;

    public Point3D(double x, double y, double z) {
        this._x = x;
        this._y = y;
        this._z = z;
    }

    public Point3D(GeoLocation location) {
        this._x = location.x();
        this._y = location.y();
        this._z = location.z();
    }

    @Override
    public double x() {
        return this._x;
    }

    @Override
    public double y() {
        return this._y;
    }

    @Override
    public double z() {
        return this._z;
    }

    @Override
    public double distance(GeoLocation g) {
        double deltaX = this._x - g.x();
        double deltaY = this._y - g.y();
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }
}
