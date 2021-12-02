package FileWorkout;

import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class LoadGraph {

    JsonObject currGraphObject;
    public LoadGraph(JsonObject graphObject){
        this.currGraphObject = graphObject;
    }

    /**
     * key for each edge is : ""+edge.src()+","+edge.dest() -> for example: "0,5" is edge from node_id 0 to node_id 5
     * @return hashMap<String, Edge> that represent all the graph edges
     */
    public HashMap<String, EdgeData> jsonToEdge(){
        // declare our var that we gonna work with inside the loop
        int tempSrc, tempDest;
        HashMap<String, EdgeData> mapOfEdges = new HashMap<>();

        JsonArray arrOfEdges = this.currGraphObject.get("Edges").getAsJsonArray(); // get all the edge objects

        JsonObject edgeJsonObj; // temporary var

        for (JsonElement edge : arrOfEdges){
            edgeJsonObj = edge.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
            // construct the Node and add it to the output list
            tempSrc = edgeJsonObj.get("src").getAsInt();
            tempDest = edgeJsonObj.get("dest").getAsInt();
            mapOfEdges.put(""+tempSrc+","+tempDest, new Edge(tempSrc, edgeJsonObj.get("w").getAsDouble()
                    , tempDest));
        }

        return mapOfEdges;
    }

    /**
     * @return HashMap of Nodes that represent all the graph Nodes (key is node_id)
     */
    public HashMap<Integer, NodeData> jsonToNode(){
        // declare our var that we gonna work with inside the loop
        int tempId;
        String[] pointsCor;  // x = idx 0, y = idx 1, z = idx 3
        HashMap<Integer, NodeData> mapOfNodes = new HashMap<>();

        JsonArray arrOfNodes = this.currGraphObject.get("Nodes").getAsJsonArray(); // get all the edge objects

        JsonObject nodeJsonObj; // temporary var

        for (JsonElement node : arrOfNodes){
            nodeJsonObj = node.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
            // use split to discriminate between each cordinate inside the string
            // idx 0 = x, idx 1 = y, idx 2 = z
            pointsCor = nodeJsonObj.get("pos").getAsString().split(",");
            // construct the Node and add it to the output list
            tempId = nodeJsonObj.get("id").getAsInt();
            mapOfNodes.put(tempId, new Node(new Point3D(Double.parseDouble(pointsCor[0]), Double.parseDouble(pointsCor[1]),
                    Double.parseDouble(pointsCor[2])), tempId));
        }

        return mapOfNodes;
    }

}