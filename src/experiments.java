import api.Edge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class experiments {

    public static void main(String[] args){
        HashMap<String, Integer> myFirstHashMap = new HashMap<String, Integer>();
        myFirstHashMap.put("amir", 1);
        myFirstHashMap.put("benny", 2);
        myFirstHashMap.put("cidney", 3);
        System.out.println(myFirstHashMap);

        for (Integer i : myFirstHashMap.values()){
            System.out.println(i);
        }
        Iterator myFirstIterator = myFirstHashMap.entrySet().iterator();
        while (myFirstIterator.hasNext()){
            Map.Entry pair = (Map.Entry)myFirstIterator.next();
            System.out.println(pair.getKey() + " " + pair.getValue());
        }

        if (myFirstHashMap.get("amir") == 1){
            System.out.println("yas");
        }

        HashMap<Integer, HashMap<String, Edge>> amirMap = new HashMap<Integer, HashMap<String, Edge>>();
        amirMap.put(0, new HashMap<String, Edge>());
        amirMap.put(2, new HashMap<String, Edge>());
        amirMap.put(8, new HashMap<String, Edge>());

        amirMap.get(0).put("0,1", new Edge(0, 15, 1));
        amirMap.get(0).put("0,2", new Edge(0, 11, 2));

        amirMap.get(0).put("2,1", new Edge(2, 15, 1));
        amirMap.get(2).put("2,3", new Edge(2, 11, 3));

        amirMap.get(8).put("8,5", new Edge(8, 12, 5));
        amirMap.get(0).put("8,1", new Edge(8, 13, 1));

        Double e = Double.POSITIVE_INFINITY + Double.NEGATIVE_INFINITY;
        System.out.println(e < Double.MAX_VALUE);
//        Iterator scndIterator = amirMap.entrySet().iterator();
//        while (scndIterator.hasNext()){
//            Map.Entry pair2 = (Map.Entry)scndIterator.next();
//            System.out.println(pair2.getKey() +" "+ ((Edge)pair2.getValue()).getWeight());
//        }
//        System.out.println(amirMap.get(""+8+","+5).getDest());
        System.out.println(amirMap.size());
    }
}
