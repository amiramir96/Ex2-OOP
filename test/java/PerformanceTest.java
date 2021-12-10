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

//        if (args.length > 0){
//            g = RandomGraphGenerator.createRndGraph(Integer.parseInt(args[0]));
//        }
//        else{
//            System.out.println("set number of nodes:");
//            Scanner sc = new Scanner(System.in);
//            g = RandomGraphGenerator.createRndGraph(sc.nextInt());
//        }
        System.out.println("set number of nodes:");
        Scanner sc = new Scanner(System.in);
        int param = sc.nextInt();
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
        System.out.println("Number of nodes: "+ g.nodeSize());
        System.out.println("Number of edges: "+ g.edgeSize());
        //is connected
        long start = System.nanoTime();
        dm.isConnected();
        long finish = System.nanoTime();
        System.out.println("isConnected time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //shortest path
        start = System.nanoTime();
        dm.shortestPathDist(0, g.nodeSize()-1);
        finish = System.nanoTime();
        System.out.println("shortestPath time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //center
//        start = System.nanoTime();
//        dm.center();
//        finish = System.nanoTime();
//        System.out.println("center time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");
        //tsp
        int num_of_nodes = g.nodeSize();
        LinkedList<NodeData> l1 = new LinkedList<>();
        l1.add(g.getNode(0));
        l1.add(g.getNode(num_of_nodes/2));
        l1.add(g.getNode(num_of_nodes/3));
        l1.add(g.getNode(num_of_nodes/4));
        l1.add(g.getNode(num_of_nodes/5));
        start = System.nanoTime();
        dm.tsp(l1);
        finish = System.nanoTime();
        System.out.println("tsp for 5 cities time: " + TimeUnit.SECONDS.convert((finish - start), TimeUnit.NANOSECONDS) + " seconds");


    }
}
