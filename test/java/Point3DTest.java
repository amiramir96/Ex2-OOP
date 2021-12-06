import api.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {

    Point3D p0 = new Point3D(0,0,0);
    Point3D p1 = new Point3D(3.0,4.0,0.0);

    @Test
    void x() {
        assertEquals(p1.x(), 3.0);
    }

    @Test
    void y() {
        assertEquals(p1.y(), 4.0);
    }

    @Test
    void z() {
        assertEquals(p1.z(), 0.0);
    }

    @Test
    void distance() {
        assertEquals(p1.distance(p0), 5.0);
        assertEquals(p1.distance(p0), p0.distance(p1));
    }
}