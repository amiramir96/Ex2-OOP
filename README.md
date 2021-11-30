# Ex2-OOP
exercise two of object oriented programming of Ariel university

## lets use this readme to module our needs for the excersiece
### topic of tasks:
0. load json file input <br>
1. simple Objects: Node (imp NodeData), Edge (imp NodeData), Point(imp GeoLocation) <br>
2. Object Dwg (imp DirectedWeightedGraph) <br>
3. Object DwgMagic (imp DirectedWeightedGraphAlgotithms) <br>
3.1 algorithms that we need for bullet 3
4. GUI Dwg, DwgMagic <br>

### 0. load json file input
what is done: <br>
loading json files<br>
<br>
what have to be done: <br>
maybe xfer the functions from Ex2 class to another class that will hold all the functions for load/saving json files (cuz we have at DwgMagic same task + save func)

### 1. simple Objects: Node (imp NodeData), Edge (imp NodeData), Point(imp GeoLocation) 
what is done: <br>
kinda everything <br>
<br>
what have to be done: <br>
to adjust objects (if neccessary) for GUI needes <br>
that means we probable have to xfer the geoLocation data of nodes to something that will always match to the window sizes <br>

### 2. Object Dwg (imp DirectedWeightedGraph) 
what is done: <br>
kinda everything
<br>
what have to be done: <br>
in theory: the minimum of objects that we shall "save" in memory is: once all the nodes, once all the edges <br>
in my imp (which is very rough and i didnt thought more than few mins on that) there is once all nodes, and 3 times all the edges (its only pointers to the obj but still..) <br>
also, if we stay with this memory architecture, i have to edit the constructors to be more efficient (will take me prob like 1h +-) <br>

another issue, we shall adjust this obj to work with GUI as neccessary (if there is a need for inner functions)


### 3. Object DwgMagic (imp DirectedWeightedGraphAlgotithms) 
what is done: <br>
basic funcs (1 line code xD)
center function (base on dijkstra)
<br>
what have to be done: <br>
a. isConnected <br>
b. shortestPath <br>
c. shortestPathDist <br>
d. tsp <br>
e. save the Dwg object that class worken on to json file <br>
f. load from Dwg from json file (same as "0. load json file input" <br>

#### algorithms that we need for bullet 3
 is connected: <br>
<\t>    1. DFS for <br>
<\t>    2. transpose edges for is connected (we can not impliment that if we stay with same data structres cuz we have there hashmap of all transpose edges) <br>
 shortestPath / Dist: <br>
BFS, and save there the nodes for the path in another func/same func <br>
 tsp: <br>
 Option1. kruskel or prim MST algos + DFS <br> 
 Option2. kruskel/prim MST + christofides <br> 
  and decide if to imp dynamic prog then we can switch in small cases (10~15 nodes) to brute forces with optimal solution
  
  
### 4. GUI Dwg, DwgMagic
its like a one whole exercise by itself, we shall talk about it deeply <br>
have alot to implement <br>
window <br>
adjust points of nodes in plain to the window of the GUI <br>
toolbar: save, load <br>
functions buttons: add/delete node and edge, create new graph <br>
visualisation of the graph <br>
visualisation of the algorithms on the graph (who is center, color shortest path and etc depend on the user clicks)
