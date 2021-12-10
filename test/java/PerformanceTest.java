import FileWorkout.LoadGraph;
import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import correctness.RandomGraphGenerator;
import impGraph.Dwg;
import impGraph.DwgMagic;
import impGraph.Node;
import impGraph.Point3D;

import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PerformanceTest {

    
    public static void main(String[] args) {
        int param;
        if (args.length > 0){
            param = Integer.parseInt(args[0]);
        }
        else{
            System.out.println("set number of nodes:");
            Scanner sc = new Scanner(System.in);
            param = sc.nextInt();
        }
        long start = System.nanoTime();
        DirectedWeightedGraph g = new Dwg();
        for (int i=0; i < param; i++){
            g.addNode(new Node(new Point3D(Math.random()*100, Math.random()*100,0.0), i));
        }
        for (int i=0; i < param; i++){
            for (int j=0; j < 20; j++){
                g.connect(i, (int)(Math.random()*(param)), Math.random()*50);
            }
        }
        DwgMagic dm = new DwgMagic(g);
        if (dm.getGraph().nodeSize() > 800000){
            Iterator<NodeData> n = dm.getGraph().nodeIter();
            ArrayList<NodeData> p = new ArrayList<>();
            for (int i=0; n.hasNext() && i<100000; i++){
                n.next();
                p.add(n.next());
            }
            NodeData ni = n.next();
            dm.getGraph().removeNode(ni.getKey());
            dm.getGraph().addNode(ni);
            int j=0;
            for (int i=dm.getGraph().edgeSize(); i<dm.getGraph().nodeSize()*20; i++){
                dm.getGraph().connect(p.get(j).getKey(),p.get(j+1).getKey(), Math.random()*1000);
                j++;
            }
        }
        long finish = System.nanoTime();
        System.out.println("create and load random graph time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");

        System.out.println("Number of nodes: "+ g.nodeSize());
        System.out.println("Number of edges: "+ g.edgeSize());

        //is connected
        start = System.nanoTime();
        dm.isConnected();
        finish = System.nanoTime();
        System.out.println("isConnected time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //center
        start = System.nanoTime();
        dm.center();
        finish = System.nanoTime();
        System.out.println("center time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //shortest path
        start = System.nanoTime();
        dm.shortestPathDist(0, g.nodeSize()-1);
        finish = System.nanoTime();
        System.out.println("shortestPath time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
         //tsp
        int num_of_nodes = g.nodeSize();
        LinkedList<NodeData> l1 = new LinkedList<>();
        // add 15 nodes to the list
        for (int i = 2; i < 17; i++) {
            l1.add(g.getNode(num_of_nodes/i));
        }
        start = System.nanoTime();
        dm.tsp(l1);
        finish = System.nanoTime();
        System.out.println("tsp for 15 cities time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //copy graph
        start = System.nanoTime();
        dm.copy();
        finish = System.nanoTime();
        System.out.println("copy time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
    }
}
