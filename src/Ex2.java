import api.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import FileWorkout.*;
import graphics.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
//
//    /**
//     * key for each edge is : ""+edge.src()+","+edge.dest() -> for example: "0,5" is edge from node_id 0 to node_id 5
//     * @param graphObject an JsonObject that represent directed weighted graph
//     * @return hashMap<String, Edge> that represent all the graph edges
//     */
//     private static HashMap<String, Edge> jsonToEdge(JsonObject graphObject){
////        // declare our var that we gonna work with inside the loop
////        ArrayList<Edge> listOfEdges = new ArrayList<Edge>();
////
////        JsonArray arrOfEdges = graphObject.get("Edges").getAsJsonArray(); // get all the edge objects
////        JsonObject edgeJsonObj; // temporary var
////
////        for (JsonElement edge : arrOfEdges){
////            edgeJsonObj = edge.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
////            // construct the edge and add it to the output list
////            listOfEdges.add(new Edge(edgeJsonObj.get("src").getAsInt(), edgeJsonObj.get("w").getAsDouble()
////                    , edgeJsonObj.get("dest").getAsInt()));
////        }
////
////        return listOfEdges;
//
//
//         // declare our var that we gonna work with inside the loop
//         int tempSrc, tempDest;
//         HashMap<String, Edge> mapOfEdges = new HashMap<String, Edge>();
//
//         JsonArray arrOfEdges = graphObject.get("Edges").getAsJsonArray(); // get all the edge objects
//
//         JsonObject edgeJsonObj; // temporary var
//
//         for (JsonElement edge : arrOfEdges){
//             edgeJsonObj = edge.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
//             // construct the Node and add it to the output list
//             tempSrc = edgeJsonObj.get("src").getAsInt();
//             tempDest = edgeJsonObj.get("dest").getAsInt();
//             mapOfEdges.put(""+tempSrc+","+tempDest, new Edge(tempSrc, edgeJsonObj.get("w").getAsDouble()
//                     , tempDest));
//         }
//
//         return mapOfEdges;
//    }
//
//    /**
//     * @param graphObject - an JsonObject that represent directed weighted graph
//     * @return list of Nodes that represent all the graph Nodes
//     */
//    private static HashMap<Integer, Node> jsonToNode(JsonObject graphObject){
//        // declare our var that we gonna work with inside the loop
//        int tempId;
//        String[] pointsCor;  // x = idx 0, y = idx 1, z = idx 3
//        HashMap<Integer, Node> mapOfNodes = new HashMap<Integer, Node>();
//
//        JsonArray arrOfNodes = graphObject.get("Nodes").getAsJsonArray(); // get all the edge objects
//
//        JsonObject nodeJsonObj; // temporary var
//
//        for (JsonElement node : arrOfNodes){
//            nodeJsonObj = node.getAsJsonObject(); // convert from element to object, so we would be able to pull the data as strings names
//            // use split to discriminate between each cordinate inside the string
//            // idx 0 = x, idx 1 = y, idx 2 = z
//            pointsCor = nodeJsonObj.get("pos").getAsString().split(",");
//            // construct the Node and add it to the output list
//            tempId = nodeJsonObj.get("id").getAsInt();
//            mapOfNodes.put(tempId, new Node(new Point3D(Double.parseDouble(pointsCor[0]), Double.parseDouble(pointsCor[1]),
//                    Double.parseDouble(pointsCor[2])), tempId));
//        }
//
//        return mapOfNodes;
//    }


    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return directed weighted graph
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        File jsonGraphFile = new File(json_file); // get file
        DirectedWeightedGraph ans = null;

        try {
            JsonElement graphElement = JsonParser.parseReader(new FileReader(jsonGraphFile)); // shall read, so have to handle exception
            JsonObject graphObject = graphElement.getAsJsonObject(); // convert to json object, then we can work on fields inside
            // jsonToNode return list of nodes, jsonToEdge return list of edges, both is the req to cons Dwg(directed weighted graph)
            LoadGraph g = new LoadGraph(graphObject);
            ans = new Dwg(g.jsonToNode(), g.jsonToEdge());
        }
        catch (FileNotFoundException err){ // throw exception..
            err.printStackTrace();
        }

        return ans;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return dwg algorithms
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraph workGraph;
        DirectedWeightedGraphAlgorithms ans;
        workGraph = getGrapg(json_file); // const the Dwg via the above func :-)
        ans = new DwgMagic(workGraph);
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        // ****** Add your code here ******
        //
        // ********************************
    }

    //bdika bdika
    public static void main(String[] args) throws IOException {
        System.out.println("lets start");
        DwgMagic d = (DwgMagic) getGrapgAlgo("data\\G1.json");
        // saving graph
        SaveGraph.save("saved_graph.json", d.getGraph());
        Dwg g = (Dwg) getGrapg("saved_graph.json");
        System.out.println(g.getNode(2).getLocation().x());
//        System.out.println("ended const");
//        System.out.println(d.getGraph().nodeSize()+"  "+d.getGraph().edgeSize());
//        System.out.println("start dijkstra");
//        System.out.println(d.center().getKey());
//        System.out.println(d.shortestPathDist(2, 12));
//        System.out.println(d.shortestPath(2, 12));
//        System.out.println("end dijkstra");
//        System.out.println(d.isConnected());
//        System.out.println("end is connected");
//        d.load("C:\\Users\\Amir\\IdeaProjects\\Ex2-OOP\\data\\G1.jsonasda");
//        System.out.println("end is connected");
//        System.out.println("end is connected");
        Window w = new Window(d);

//        int bla = 0;
//        DirectedWeightedGraph asd =d.getGraph();
//        Iterator<EdgeData> iteraa = asd.edgeIter();
//        while (iteraa.hasNext()){
//            EdgeData edy = iteraa.next();
//            if (bla ==3){
//                asd.connect(0,4,1.6);
//            }
//            System.out.println(edy.getWeight());
//            bla+=1;
//        }

    }
}
