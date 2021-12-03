package FileWorkout;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SaveGraph {

    DirectedWeightedGraph currGraph;
    String directory;

    //Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SaveGraph(String file, DirectedWeightedGraph g){
        this.directory = file;
        this.currGraph = g;
    }

    /**
     * save graph as json string in given target file
     * @param target_file
     * @param g DirectedWeightedGraph
     * @throws IOException
     */
    public static void save(String target_file, DirectedWeightedGraph g) throws IOException {
        Iterator<EdgeData> edge_it = g.edgeIter();
        Iterator<NodeData> node_it = g.nodeIter();
        // combine edges
        JsonArray edges = new JsonArray();
        while (edge_it.hasNext()){
            JsonObject edge = EdgeToObject(edge_it.next());
            edges.add(edge);
        }
        // combine nodes
        JsonArray nodes = new JsonArray();
        while(node_it.hasNext()){
            JsonObject node = NodeToObject(node_it.next());
            nodes.add(node);
        }
        // graph json
        JsonObject graph_json = new JsonObject();
        graph_json.add("Edges", edges);
        graph_json.add("Nodes", nodes);
        // write to file
        Files.writeString(Paths.get(target_file), graph_json.getAsString());

    }

    /**
     * create json object from given edge
     * @param edge
     * @return json object
     */
    public static JsonObject EdgeToObject(EdgeData edge){//TODO implement
        JsonObject obj = new JsonObject();
        obj.addProperty("src",edge.getSrc());
        obj.addProperty("w",edge.getWeight());
        obj.addProperty("dest",edge.getDest());
        return obj;
    }

    public static JsonObject NodeToObject(NodeData node){
        double node_x= node.getLocation().x();
        double node_y= node.getLocation().y();
        double node_z= node.getLocation().z();
        JsonObject obj = new JsonObject();
        obj.addProperty("pos", node_x+","+node_y+","+node_z);
        obj.addProperty("id", node.getKey());
        return obj;
    }

    public boolean runSave(){
        return false;
    }
}
