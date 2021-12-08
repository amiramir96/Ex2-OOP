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
|    EdgeIter     |   iterator for Edges of curr node, crafted to the needs of our graph (throw exception if graph has been changed since iterator is created)               |
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
graph is connected if and only if there is path between every node to every node. <br>
the above term is equivlent to say: given vertex v1, if there is path from v1 to every other vertex, and from any v_i that belongs to V there is path to v1 => is connected! <br>
because of that, we use DFS algorithm twice - once regulary on random vertex (name it v1), use transpose on all edges of G, then use DFS again on v1. this way, via using "finish time" of ea node, the only thing we have to check is -> if f_time[v1] is the highest from all f_time[v_i]. 

#### ShortestPath O(|E|log(|V|))
return list of nodes that represent the shortest path between pair of given nodes. <br>
via using dijkstra algorithm we can find the shortest path between pair of vertexes, and that what we did.
<br>

#### ShortestPathDist O(|E|log(|V|))
same as ShortestPath - just return instead the distance between the pair of nodes (that value can be taken within O(1) in after using dijkstra algo on the relevant node). <br>

#### center O(|V|\*|E|log(|V|))
(a.k.a shortest from longests) returns the node within the longest path that is the shortest between all the other longest paths of the other nodes <br>
use dijkstra algorithm on ea node and save his longest value => then loop over all the nodes once and take the shortest from all the longests values (and returns the adjust node)<br>
for getting better time run results we split all the nodes of G to 3 equal sizes lists and use ThreadPool (class extends Thread) to run all this dijkstra calculation in seprate 3 threads

#### tsp - traveler salesmen problem (crafted to be more realistic) O(n*|E|log(|V|)), while n is the amount of the cities
problem description: <br>
have to find as short as possible path between all list of nodes that we got as input (can move more than once over one node and no need to return to the start node) <br>
algo solution: <br>
iterate on ea node (name it v_i) from the input list of city and: <br>
1. if there is direct edge from v_i to any other cities which we didnt visited yet -> choose the minimal direct edge from all the edge to unvisited cities. (and add to output list) <br>
2. otherwise, use dijkstra on v_i, and loop over all the cities which we didnt visited yet -> take the minimal path from all the unvisited cities and add the whole path to the output list.<br>
pros of this algorithm: <br>
- running time is polynomial, bound by center time run for worst case, and ea direct edge between pair of nodes, cut by |E|log(|V|) the upper bound time run <br>
- even if G is unconnected, sub graph cities is unconnected which is the worst, the algo will return solution as long as all cities is part of the same connected component <br>
cons: <br>
- wont return the Optimal running time as dynamic programming (but, dynamic programming running time is O(n^2 * 2^n) which is exponential - the worst)
<br>

### graphics - GUI 
#### logic system




#### how to use / tutorial

<br>


### Running The Simulation

<br>

### Tests

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



<br>

special thanks to Daniel Rosenberg our class mate which helped with some hints and guidence along the gui implementation
