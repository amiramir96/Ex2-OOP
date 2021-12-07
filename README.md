# Ex2-OOP
Object Oriented Programming exercise 2

in this assigment we were required to implement Directed Weighted Garph and some choosen algorithms via our teacher Interfaces : https://github.com/amiramir96/Ex2-OOP/tree/main/src/api <br> 
the directed weigthed graph object shall implemented within the best time run as possible since its can hold alot of vertex and edges. <br>
in addition, we shall create a GUI programme that support every algorithm that implemented on the graph (for ex, load graph, isConnected, tsp etc..) <br>

## Program Overview
### structre of the project code

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
|    DrawGraph    |    inherite from JPanel and imp ActionListener, and Mouse interfaces, responsible to draw the given graph as it got commanded                            |    |      Menu       | inherite from MenuBar and imp ActionListener, control over the features, <br> decided which feature got clicked and arrange all the needed needs so that the DrawGarph object will be able to focus only via its got defined till the present time                                                          |


### Directed Weigthed Graph

## Directed Weighted Graph Algorithms
supporting some methods via interface https://github.com/amiramir96/Ex2-OOP/blob/main/src/api/DirectedWeightedGraphAlgorithms.java <br>
the class obj will construct by getting existing Directed Weigthed Graph (even an empty one). <br>
explanation on the algorithm methods:<br>
#### isConnected


#### ShortestPath


#### ShortestPathDist



#### center


#### tsp - traveler salesmen problem (crafted to be more realistic)



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
