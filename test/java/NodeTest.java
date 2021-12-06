import api.Node;
import api.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Point3D p0 = new Point3D(0,0,0);
    Point3D p1 = new Point3D(3.0,4.0,0.0);
    Node n1 = new Node(p1, 1);
    Node n2 = new Node(p0, 2);
    Node n3 = new Node(p0, 3);

    @Test
    void getKey() {
        assertEquals(1, n1.getKey());
        assertEquals(2, n2.getKey());
    }

    @Test
    void getLocation() {
        assertNotEquals(p1, n1.getLocation());
        assertEquals(p1.x(), n1.getLocation().x());
        assertEquals(p0.y(), n3.getLocation().y());
        assertNotEquals(p0, new Node(n3).getLocation());// new points are independent
    }

    @Test
    void setLocation() {
        Node n4 = new Node(n1);
        n4.setLocation(p0);
        assertNotEquals(p0, n4.getLocation());// independent new location
        assertEquals(p0.x(), n4.getLocation().x());
    }

    @Test
    void getInfo() {
        n1.setInfo("node 1");
        assertEquals("node 1", n1.getInfo());
        assertEquals("", n3.getInfo());
    }

    @Test
    void setInfo() {
        n1.setInfo("node 1");
        assertEquals("node 1", n1.getInfo());
        n1.setInfo("");
        assertEquals("", n1.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0, n1.getTag());
        n1.setTag(4);
        assertEquals(4, n1.getTag());
    }

    @Test
    void setTag() {
        n1.setTag(0);
        assertEquals(0, n1.getTag());
        n1.setTag(4);
        assertEquals(4, n1.getTag());
    }

    @Test
    void getWeight() {
        // default is infinity
        assertEquals(Double.POSITIVE_INFINITY,n2.getWeight());
        n2.setWeight(2.2);
        assertEquals(2.2,n2.getWeight());
    }

    @Test
    void setWeight() {
        n2.setWeight(0);
        assertEquals(0,n2.getWeight());
        n2.setWeight(2.2);
        assertEquals(2.2,n2.getWeight());
    }
}