# Ex2-OOP
Object Oriented Programming exercise 2

in this assigment we were required to implement Directed Weighted Garph and some choosen algorithms via our teacher Interfaces : https://github.com/amiramir96/Ex2-OOP/tree/main/src/api <br> 
the directed weigthed graph object shall implemented within the best time run as possible since its can hold alot of vertex and edges. <br>
in addition, we shall create a GUI programme that support every algorithm that implemented on the graph (for ex, load graph, isConnected, tsp etc..) <br>

## Program Overview
### structre of the project code
the project splits to packges: api (interfaces of our teacher, as explained above), FileWorkout, impGraph, graphAlgo, graphics, tests (will be explained below, not in this topic)

 |   package name: |                                                     **FileWorkout**                                                                                      |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **file name**   |      **description**                                                                                                                                     |   
| LoadGraph       |             Load a Json file and construct <br> Directed Weighted Graph via the json file                                                                   |
|    SaveGraph    |             Save an existing Directed Weighted Graph - <br> nodes and edges as json file, at any directory in the computer                                    |

|   package name: |                                                     **graphAlgo**                                                                                        |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **file name**   |      **description**                                                                                                                                     |   
| DFS             |     implement DFS algorithm, for more details on DFS <br> please look at "Review of the Literature" at the bottom of the readme                               |
|    Dijkstra     |     implement Dijkstra algorithm, for more details on Dijksta <br> please look at "Review of the Literature" at the bottom of the readme                      |


|   package name: |                                                     **impGraph**                                                                                         |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **file name**   |      **description**                                                                                                                                     |   
| Point3D         |  represents point on the plane, imp GeoLocation                                                                                                          |
|    Node         |  is vertex in the graph, imp NodeData                                                                                                                    |
|    Edge         |   edge in graph, imp EdgeData                                                                                                                            |
|    Dwg          |  1. imp DirectedWeightedGraph, hold iterators for all nodes, edges, edges of specific nodes, <br> 2. support add node and remove/add edge within O(1), <br> 3. support remove key within O(k) while k is the amount of in+out edges of the given node                                                                                                     |
|merged & EdgeIter|   iterator for Edges of curr node, crafted to the needs of our graph (throw exception if graph has been changed since iterator is created)               |
|    DwgMagic     |    imp DirectedWeigthedGraphAlgorithms, provide solutions for several "known" and "unkown" problem                                                       |
|    ThreadPool   |    this class exists to support using Dijkstra algorithm on diff nodes in parralel (will be explained more in "center" algorithm)                        |


|   package name: |                                                     **graphics**                                                                                         |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **file name**   |      **description**                                                                                                                                     |   
|       Window    |    inherite from JFrame, represent our JFrame (the visualized window)                                                                                    |
|    DrawGraph    |    inherite from JPanel and imp ActionListener, and Mouse interfaces, responsible to draw the given graph as it got commanded                            |   |    hartabepita  |  1                                                                                       
|      Menu       | inherite from MenuBar and imp ActionListener, control over the features, <br> decided which feature got clicked and arrange all the needed needs so that <br> the DrawGarph object will be able to focus only via its got defined till the present time | 


### Directed Weigthed Graph
אורי אין לי מה לכתוב פה.. אני מתלבט אם להסיר את הבולט הזה
אם ישלך מה לרשום פה אז בכבוד..


### Directed Weighted Graph Algorithms
supporting some methods via interface https://github.com/amiramir96/Ex2-OOP/blob/main/src/api/DirectedWeightedGraphAlgorithms.java <br>
the class obj will construct by getting existing Directed Weigthed Graph (even an empty one). <br>
explanation on the algorithm methods:<br>

given G=(V,E) directed weighted graph, while V-set of all vertexes, E-set of all edge:

#### isConnected O(|V|+|E|)
definition: graph is connected if and only if there is path from every node to every node <br>
solution: used DFS algorithm twice on same node, the second time on the "transposed" edges <br>

#### ShortestPath O(|E|log(|V|))
return list of nodes that represent the shortest path between pair of given nodes. <br>
via using dijkstra algorithm we can find the shortest path between pair of vertexes, and that what we did.
<br>

#### ShortestPathDist O(|E|log(|V|))
same pattern as ShortestPath - just return instead the distance between the pair of nodes (that value can be taken within O(1) after using dijkstra algo on the relevant node). <br>

#### center O(|V|\*|E|log(|V|))
(a.k.a shortest from longests) returns the node that has the shortest path out of all the longest paths of all the other nodes <br>
use dijkstra algorithm on ea node and save his longest value => then loop over all the nodes once and take the shortest from all the longests values (and returns the adjust node)<br>
for getting better time run results we split all the nodes of G to 3 equal sizes lists and use ThreadPool (class extends Thread) to run all this dijkstra calculation in seprate 3 threads

#### tsp - traveler salesmen problem (crafted to be more realistic) O(n*|E|log(|V|)), while n is the amount of the cities
problem description: <br>
find the shortest path posible that move over all the nodes in the input list (can move more than once over one node and doesn't need to return to the start node) <br>
algo solution: is bases on nearest neighbour algorithm with little twist - if there is no direct edge to any other nodes in the list, use dijkstra on that node and choose the shortest path <br>

### Tests

<br>

### graphics - GUI 
via the GUI, the use can do every action he could from the code via using DirectedWeightedGraphAlgorithms interface commands <br>
supports loading, saving graph from json files <br>
supports add, delete commands to node, edges and create new Directed Weigthed Graph <br>

#### logic system
each class hold a role: <br>
1. window - hold the Frame that we draw on <br>
2. drawGarph (a.k.a drawer) - responsible to paint and update himself via outside inputs (menubar - user methods commands, mouse moveing) <br>
3. menu - get the user orders via clicking on toolbar features, and transform the command click on the bar to: <br>
     a. actions on the graph and the graph algo objects. <br>
     b. execute the relevant proccess with the drawer (init the currect details/params and use repaint command)


#### how to use / tutorial
MenuBar - the user can execute all the features of the algorithms, editting the graph nodes/edges and load/save graph <br>
For any INVALID input, a popup msg will appear and will explain what the currect format that require to be sent next time <br>
the gui support mouse inputs as moving screen and zooming in/out the picture<br>
defualt colors is BLACK for node, BLUE for edge,
any other kind of color to the graph will represent occure of one of the algorithms command!<br>
![default](https://user-images.githubusercontent.com/89981387/145303879-a5766b8d-9666-4977-a5fb-9b76d228b37f.png)
<br>File category <br>
load graph - choose json file from directory and load it while creating new graph. <br>
save graph - choose folder at directory to extract to the graph to json file. <br>
<br>
Algo_Command category<br>
isConnected - true = all nodes drawn to GREEN, false - RED <br>
![isConnected](https://user-images.githubusercontent.com/89981387/145305314-39acf3aa-3420-47bd-bcf9-9f4a77953499.png)

center - the center node drawn to bright yellow, if garph is not connected - popup a msg to the user. <br>
![center](https://user-images.githubusercontent.com/89981387/145305342-a9a194a5-9528-440b-baf7-7e3517b12cc9.png)

shortestPathDist - ask from user input via format "node_id,node_id" (int,int) and draw green for nodes, pink for edges(if there is no path, nothing will happen)  and popup msg with the distance of the path <br>
shortestPath - same as above, diff colors (pink for nodes, red for edges). <br> 
![ShortestPath](https://user-images.githubusercontent.com/89981387/145305331-87ef9cb4-f644-4a68-b353-b0aa8a73b135.png)


tsp - ask from user input via format "node_id,node_id,....,node_id" (int,int,...,int), if there is solution -> draw nodes in green and add string to the node_id with 
the station idx along the road, edges with pink \ if there is no solution - draw all nodes of the input to red. <br>
![tsp](https://user-images.githubusercontent.com/89981387/145305351-79742b51-c79d-4412-bb46-154762dfdb70.png)

<br>
Graph_Management category <br>
add node - ask for user input via "node_id,x,y" (int,float,float) and add node to the currect graph <br>
add edge - same as above, valid input is: "node_id_src,node_id_dest,weight" ("int,int,float") <br>
del node - input is "node_id" (int), the node will be deleted from graph and screen <br>
del edge - as above, valid input is: "node_id_src,node_id_dest" (int,int)
create new graph - remove the currect graph and start a clean new graph (zero nodes and edges)
<br>


### Running The Simulation

<br>


## Results
<br>

| **Case name**   |**Node_size**|**Edge_size**|     **isConnected**     |     **shortestPath**     |   **shortestPathDist**     |      **center**      |      **tsp**      |
|-----------------|-------------|-------------|-------------------------|--------------------------|----------------------------|----------------------|-------------------|   
|                 |             |             |                         |       **description**    |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |
|                 |             |             |                         |                          |                            |                      |                   |



<br>

## Assigment instructions



## Review of the Literature
DFS - https://en.wikipedia.org/wiki/Depth-first_search , https://www.youtube.com/watch?v=7fujbpJ0LB4&t=10s <br>
Dijksta - https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm , https://www.youtube.com/watch?v=pSqmAO-m7Lk&t=776s <br>
GUI via Swing - https://www.youtube.com/watch?v=Kmgo00avvEw&t=7097s <br>
tsp algorithms (which helped to get ideas) - https://www.youtube.com/watch?v=M5UggIrAOME , https://en.wikipedia.org/wiki/Travelling_salesman_problem <br>
mathematical way to draw arrow - https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java comment 19 <br>
thread usage - https://www.youtube.com/watch?v=r_MbozD32eo&t=517s <br>
iterator usage - https://stackoverflow.com/questions/9200080/join-multiple-iterators-in-java?lq=1 comment 0 <br>
special material - https://github.com/ShaiAharon/OOP_19 credit to our teacher Shai.Aharon, helped alot with GUI and threads as well <br>
