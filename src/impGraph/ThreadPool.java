package impGraph;

import graphAlgo.Dijkstra;

import java.util.List;

public class ThreadPool extends Thread{
    List<Dijkstra> dijList;
    int id;
    ThreadPool(List<Dijkstra> dijList, int serialnum){
        this.dijList = dijList;
        this.id = serialnum;
    }

    @Override
    public void run() {
        for (Dijkstra d : dijList){
            d.mapPathDijkstra(d.src);
            d.longestPath();

//            System.out.println("pool: "+this.id+" dijObj of node: "+ d.src.getKey());
        }
    }
}
