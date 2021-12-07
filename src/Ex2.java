import api.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import FileWorkout.*;
import graphics.Window;
import impGraph.Dwg;
import impGraph.DwgMagic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {

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
        Window w = new Window(alg);
    }

    public static void main(String[] args) {
        if(args.length > 0){
            try{
                runGUI(args[0]);
            }
            catch (Exception e){
                try{
                    runGUI("data\\" + args[0]);
                }
                catch (Exception e2){
                    System.out.println("File doesn't exist");
                }
            }
        }
        else{
            Scanner in = new Scanner(System.in);
            System.out.println("enter graph path:");
            String g = in.nextLine();
            g = g.replace("\\", "\\\\");
            try{
                runGUI(g);
            }
            catch (Exception e){
                try{
                    runGUI("data\\" + g);
                }
                catch (Exception e2){
                    System.out.println("File doesn't exist");
                }
            }
        }
    }
}
