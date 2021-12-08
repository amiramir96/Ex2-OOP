import FileWorkout.LoadGraph;
import FileWorkout.SaveGraph;
import api.*;
import impGraph.*;
import graphAlgo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RandomGraphGenerator {


    /**
     * create random graph with node_size node in the "resources" directory
     * @param nodes_size size of graph
     * @return random graph
     */
    public static DirectedWeightedGraph createRndGraph(int nodes_size){
        DirectedWeightedGraph g = new Dwg();
        Random rnd = new Random();
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        //create nodes
        for (int i = 0; i < nodes_size; i++) {
            //range: -nodes_size/2 to nodes_size/2
            double x_coordinate= (-nodes_size/2.0)+(nodes_size* rnd.nextDouble());
            double y_coordinate= (-nodes_size/2.0)+(nodes_size* rnd.nextDouble());
            Point3D p = new Point3D(x_coordinate, y_coordinate, 0.0);
            nodes.put(i, new Node(p, i));// create new node
            //System.out.println(x_coordinate+","+ y_coordinate);
        }
        //create edges
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodes_size*20; i++) {
            int src = rnd.nextInt(nodes.size());
            int dest = rnd.nextInt(nodes.size());
            if(src != dest){
                double weight = rnd.nextDouble()*10;
                edges.add(new Edge(src, weight, dest));
            }
        }
        //save graph in data folder
        try {
            saveGraph(nodes, edges);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save random graph");
            System.exit(0);
        }
        try {
            g = LoadGraph.loadGraph("test\\resources\\random_graph.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load random graph");
            System.exit(0);

        }
        System.out.println(g.edgeSize());
        System.out.println(g.nodeSize());

        return g;
    }

    private static void saveGraph(HashMap<Integer, Node> nodes, ArrayList<Edge> edges) throws IOException {
        Iterator<Edge> edge_it = edges.iterator();
        Iterator<Node> node_it = nodes.values().iterator();
        // combine edges
        JsonArray json_edges = new JsonArray();
        while (edge_it.hasNext()){
            JsonObject edge = SaveGraph.EdgeToObject(edge_it.next());
            json_edges.add(edge);
        }
        // combine nodes
        JsonArray json_nodes = new JsonArray();
        while(node_it.hasNext()){
            JsonObject node = SaveGraph.NodeToObject(node_it.next());
            json_nodes.add(node);
        }
        // graph json
        JsonObject graph_json = new JsonObject();
        graph_json.add("Edges", json_edges);
        graph_json.add("Nodes", json_nodes);
        // write to file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(Paths.get("test\\resources\\random_graph.json"), gson.toJson(graph_json));
    }

    @Test
    void testGenerator(){
        Random rnd = new Random();
        createRndGraph(50 + rnd.nextInt(4500));

    }
}
