
![](https://raw.github.com/DyeKuu/Graph-Drawing-Contest-2019-2020/master/report/icon.jpg)
# Java project for Graph Drawing Contest 2020
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)
### Author: Yuyang ZHAO, Kunhao ZHENG, Luca Castelli Aleardi
### This project has ranked 5th place in the Graph Drawing Contest 2020 during the 28th International Symposium on Graph Drawing and Network Visualization


## Table of Contents
- [How to run](#how-to-run)
- [Hierarchical organization](#hierarchical-organization)
- [UpwardDrawing presentation](#upwarddrawing-presentation)
- [Node and NodeSegment presentation](#node-and-nodesegment-presentation)
- [DirectedGraph presentation](#directedgraph-presentation)
- [License](#license)


# How to run

The folder contains 
- README.md 
- folder result (where the result are stored)
- folder report (where the latex files and figures are stored)
- folder Project (where the eclipse project are stored)



# Hierarchical organization

## Defaut Class
- ComputeUpwardGridDrawing
    - Test (We add) 
    - UpwardDrawing
    - GraphRenderer

## Package jdg-graph
- DirectedGraph
    - NodeSegment (We add)
    - Node 
        - GridSegment_2
        - GridVector_2
            - GridPoint_2

## Package jdg-io
- GraphLoader
    - GraphWriter_Json
- GraphReader
    - GraphReader_Json

## Package jdg-utils
- Print

# UpwardDrawing presentation

This is our main class where we develop different algorithms and methods.

- isValid()
    - checkCondition1()
    - checkHasOn()
    - checkCondition3()

- computValidInitialLayout()
    - topoSortDeepFirstSearch()
        - topoSortDeepFirstSearch(Node u, List topoList)
    - moveOnePlace(Node node)
    - checkAdd(DirectedGraph graph, Node node, List listOfPredecessors)

- forceDirectedHeuristic() 
    - forceDirectedHeuristic(double tol, double c, double k)
        - calculateForce(Node node, double k, double c)
        - update_steplength(int step, double energy, double energy0)
        - distanceOfPositionList(GridPoint_2[] x,GridPoint_2[] y)

- localSearchHeuristic()
    - localSearch(NodeSegment seg, int Xrange , int Yrange, int Xstep, int Ystep) 

Other pre-processing methods of the graph.
- affineTransform()
- centralize()
- preAffineTransform()

# Node and NodeSegment presentation

Node
----
We have added several instances in this class namely these.
```
public int d; //time when the Node turns grey in DFS
public int f; //time when the Node turns black in DFS
public Node pi; //predecessor in the DFS tree
public Vector<Double> force;
```

The instance force is to storage the force it receives when in force-directed heuristic algorithm.

The d, f, pi are for noting the status during the DFS of the graph. The color can be either white, grey or black during the graph. More information can be found in our report.

We have added two methods here.

- getUpperBoundValidY

This method calculate the upper bound it can move to while it maintains the upward property.
```
public int getUpperBoundValidY() {
	    	if (this.successors.isEmpty()) return Integer.MAX_VALUE;
	    	int Ymax = this.successors.get(0).getPoint().getY();
	    	for (Node nextNode : this.successors) {
	    		if (nextNode.getPoint().getY()<Ymax)
	    			Ymax = nextNode.getPoint().getY();
	    	}
	    	return Ymax-1;
	    }
``` 

- getLowerBoundValidY

This method is largely the same as this one above.

NodeSegment
-----
We have created this class mainly to maintain the same property of GridSegment_2. Meanwhile this class has two ends of type Node but not GridPoint_2.

We have also created the instances for the sake of local search heuristic.
```
public Node p,q;
public ArrayList<NodeSegment> crossing;
public ArrayList<NodeSegment> nextLinkedEdge;
public ArrayList<NodeSegment> preLinkedEdge;
public ArrayList<NodeSegment> sorceNeighbors;
public ArrayList<NodeSegment> targetNeighbors;
```

- ArrayList crossing
    
    this is to store the other edges which cross with this edge.

- ArrayList nextLinkedEdge

    this is to store the edges whose source is the same as this edge's target.

- ArrayList preLinkedEdge

    this is to store the edges whose target is the same as this edge's source.

- ArrayList sorceNeighbors

    this is to store the edges whose target or source is the same as this edge's source.

- ArrayList targetNeighbors

    this is to store the edges whose target or source is the same as this edge's target.

GridVector_2
----

- crossProduct

The static method crossProduct() return a long int (this can prevent error for large input) of the cross product of two instances of GridVector_2. 
```
public static long crossProduct(GridVector_2 vec1,GridVector_2 vec2) {
	  return (long)vec1.getX()*(long)vec2.getY()-(long)vec1.getY()*(long)vec2.getX();
  }
```

GridSegment_2
----

- hasOn

The method hasOn() is to use judge whether a point (GridPoint_2) is lying on a segment including the source and the target.

Firstly we judge whether the point lies in the rectangle whose diagonal is this segment, only the sign of the equation counts to avoid out of range of int.

Secondly we judge whether the slopes are equal, in other words whether they are parallel.

```
    public boolean hasOn(GridPoint_2 p) {
	  if (p.equals(this.p)||p.equals(this.q))
		  return true;
	  
	  if( Math.signum((p.getX()-this.p.getX()))*Math.signum((p.getX()-this.q.getX()))<=0 && 
			  Math.signum((p.getY()-this.p.getY()))* Math.signum((p.getY()-this.q.getY()))<=0) {
		  if( (long)(this.q.getX()-this.p.getX())*(long)(p.getY()-this.q.getY())==(long)(this.q.getY()-this.p.getY())*(long)(p.getX()-this.q.getX()) )
			  return true;
	  }
	  return false;
  }
```

- isCrossing

This method mainly use 4 cross product to judge whether two ends of one segment lie on the same side of the other segment. Only the sign of the result counts here.

```
    public static boolean isCrossing(GridSegment_2 seg1, GridSegment_2 seg2) {
  	GridVector_2 P1P2 = new GridVector_2(seg1.source(),seg1.target());
  	GridVector_2 P1Q1 = new GridVector_2(seg1.source(),seg2.source());
  	GridVector_2 P1Q2 = new GridVector_2(seg1.source(),seg2.target());
  	GridVector_2 Q1Q2 = new GridVector_2(seg2.source(),seg2.target());
  	GridVector_2 Q1P1 = P1Q1.opposite();
  	GridVector_2 Q1P2 = new GridVector_2(seg2.source(),seg1.target());
  	long d1 = GridVector_2.crossProduct(P1P2, P1Q1);
  	long d2 = GridVector_2.crossProduct(P1P2, P1Q2);
  	long d3 = GridVector_2.crossProduct(Q1Q2, Q1P1);
  	long d4 = GridVector_2.crossProduct(Q1Q2, Q1P2);
  	if (Math.signum(d1) * Math.signum(d2)  < 0 && Math.signum(d3) * Math.signum(d4) < 0) 
  		return true;
  	return false;  	
  }
}
```

# DirectedGraph presentation
We have added the instance to store directly the edges of the graph.

```
public ArrayList<Node> vertices; // list of vertices
public HashMap<String,Node> labelMap; // map between vertices and their labels
public ArrayList<NodeSegment> edges; //list of edges
public Queue<NodeSegment> edgeQueue;
```

The labelMap is a hash map between vertex and label.
Here the edgeQueue is a priority queue comparing the crossing number of each edge.

```
edgeQueue = new PriorityQueue<NodeSegment>(edges.size(),
                new Comparator<NodeSegment>() {
                    public int compare(NodeSegment s1, NodeSegment s2) {
                        return s2.crossingNumber() - s1.crossingNumber();
                    }
                });
```

We have added several methods as below.

- resetPosition(GridPoint_2[] newPosition)

    This method is to reset positions of vertex to a set of new positions.


- barycenter()

    This method is to calculate the barycenter of all the vertex.

- initializeEdges()

    Initially the graph only have the information of all the vertex and their successors / predecessors. This method is to initialize the instance Edges.

- calculateCrossingEdge()

    This method is to calculate the edges crossing with each other, storing the information in their instance.

- initializeLinkedRelation()

    This method is to calculate and initialize the instance sorceNeighbors and targetNeighbors.

- getCrossings(NodeSegment seg)

    This method returns the crossing number of NodeSegment seg.

- get AroundCrossings(NodeSegment seg)

    This method returns the crossing number of this seg plusing the number of its source segments and target segments.

# License

[GNU Lesser General Public License v3.0](https://raw.githubusercontent.com/DyeKuu/Graph-Drawing-Contest-2019-2020/master/LICENSE)
