import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import jdg.graph.DirectedGraph;
import jdg.graph.GridPoint_2;
import jdg.graph.GridSegment_2;
import jdg.graph.GridVector_2;
import jdg.graph.Node;
import jdg.graph.NodeSegment;
import jdg.io.GraphWriter_Json;

/**
 * Main class providing tools for computing an upward grid drawing of a directed graph
 * that minimizes the number of crossings
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class UpwardDrawing {
    /** input graph to draw */
    public DirectedGraph g = null;
    /** height of the grid (input parameter) */
    public int height;
    /** width of the grid (input parameter) */
    public int width;
    
    public static int progress = 0;
    public static int time;
    
    /**
     * initialize the input of the program
     */
    public UpwardDrawing(DirectedGraph g, int width, int height) {
    	this.g=g;
    	this.width=width;
    	this.height=height;
    }
    
    /**
     * Return the number of edge crossings
     */
    
    public int getCrossings() {
    	// COMPLETE THIS METHOD
    	int result = 0;
    	ArrayList<Node> list = this.g.vertices;
    	int ListSize = list.size();
    	for (int i = 0; i<ListSize-1; i++) {
    		Node nodeI = list.get(i);
    		if (nodeI.successorsList()==null) continue;
    		for (int j = i + 1 ; j<ListSize; j++) {
    			Node nodeJ = list.get(j);
    			if (nodeJ.successorsList()==null) continue;
    			
    			for( Node isuccNode : nodeI.successorsList()) {
    				for (Node jsuccNode : nodeJ.successorsList()) {
    					if (isuccNode == jsuccNode) continue;
    					GridSegment_2 seg1= new GridSegment_2(nodeI.getPoint(), isuccNode.getPoint());
    					GridSegment_2 seg2= new GridSegment_2(nodeJ.getPoint(), jsuccNode.getPoint());
    					if (GridSegment_2.isCrossing(seg1, seg2))
    						result++;
    				}
    			}
    		}
    	}
    	return result;
    }

    /**
     * Check whether the graph embedding is a valid upward drawing <br>
     * -) the drawing must be upward <br>
     * -) the integer coordinates of nodes must lie in the prescribed bounds: the drawing area is <em>[0..width] x [0..height]</em> <br>
     * -) non adjacent crossing edges must intersect at their interiors
     */
    public boolean checkCondition1() {
    	for (Node node: this.g.vertices) {
    		for (Node next: node.successorsList()) {
    			if (next.getPoint().getY()<=node.getPoint().getY())
    				return false;
    		}
    	}
    	return true;
    }
    
    public boolean checkDuplicated() {
    	GridPoint_2[] list = this.g.getPositions();
    	int ListSize = list.length;
    	for (int i = 0; i<ListSize - 1; i++) {
    		for (int j = i + 1 ; j<ListSize ; j++) {
    			if (list[i].equals(list[j]))
    				return false;
    		}
    	}
    	return true;
    }
    
    public boolean checkCondition2() {
    	for (Node node : this.g.vertices) {	
    		ArrayList<Node> list = node.successorsList();
    		if (list==null) return true;
    		int ListSize = list.size();
    		for (int i = 0;i < ListSize - 1 ; i++) {
    			for (int j = i + 1 ; j < ListSize; j++) {
    				int nodeX = node.getPoint().getX();
    				int nodeY = node.getPoint().getY();
    				int iX = list.get(i).getPoint().getX();
    				int iY = list.get(i).getPoint().getY();
    				int jX = list.get(j).getPoint().getX();
    				int jY = list.get(j).getPoint().getY();
    				if ((nodeX-jX)*(nodeY-iY)==(nodeY-jY)*(nodeX-iX))
    					return false;
    			}
    		}
    	}
    	return true;
    }
    
    public boolean checkCondition2Prime() {
    	for (Node node : this.g.vertices) {	
    		ArrayList<Node> list = node.predecessorsList();
    		if (list==null) return true;
    		int ListSize = list.size();
    		for (int i = 0;i < ListSize - 1  ; i++) {
    			for (int j = i + 1 ; j < ListSize; j++) {
    				int nodeX = node.getPoint().getX();
    				int nodeY = node.getPoint().getY();
    				int iX = list.get(i).getPoint().getX();
    				int iY = list.get(i).getPoint().getY();
    				int jX = list.get(j).getPoint().getX();
    				int jY = list.get(j).getPoint().getY();
    				if ((nodeX-jX)*(nodeY-iY)==(nodeY-jY)*(nodeX-iX))
    					return false;
    			}
    		}
    	}
    	return true;
    }
    public boolean checkHasOn() {
    	for(Node node : this.g.vertices) {
    		for (Node anotherNode : this.g.vertices) {
    			if (anotherNode == node) continue;
    			ArrayList<Node> list = anotherNode.successorsList();
    				for (Node nextNode : list) {
    					if (nextNode==node) continue;
    					GridSegment_2 segment = new GridSegment_2(anotherNode.getPoint(), nextNode.getPoint());
    					if (segment.hasOn(node.getPoint())) 
    						return false;
    				}
    		}
    	}
    	return true;
    }
    public boolean checkCondition3() { // 敲什么代码啊，晚上想好吃啥了没。。。。
    									//想喝早茶！奶茶！
    	GridPoint_2[] list = this.g.getPositions();
    	for (GridPoint_2 p : list) {
    		if (p.getX() < 0 || p.getX()>this.width) 
    			return false;
    		if (p.getY() < 0 || p.getY()>this.height) 
    			return false;
    	}
    	return true;
    }
    public boolean isValid() {// don't delete the comment
    	boolean flag1 = checkCondition1();
    	boolean flag2 = checkDuplicated();
    	boolean flag4 = checkCondition3();
    	boolean flag3 = checkHasOn();
    	if (flag1 && flag2 && flag3 && flag4)	return true;
    	return false;
    }

    /**
     * Compute a valid initial layout, satisfying the prescribed requirements according to the problem definition <br>
     * 
     * Remark: the vertex coordinates are stored in the class 'Node' (Point_2 'p' attribute)
     */
    public List<Node> topoSortDeepFirstSearch() {
    	for (Node node : g.vertices) {
    		node.tag = 0;
    		node.pi = null;
    	}
    	time = 0;
    	List<Node> topoList = new LinkedList<Node>();
    	for (Node node: g.vertices) {
    		if (node.tag == 0)
    			topoSortDeepFirstSearch(node,topoList);
    	}
    	return topoList;
    }
    public void topoSortDeepFirstSearch(Node u, List<Node> topoList) {
    	time++;
    	u.d = time;
    	u.tag = 1;
    	for (Node v : u.successorsList()) {
    		if (v.tag == 0) {
    			v.pi = u;
    			topoSortDeepFirstSearch(v, topoList);
    		}
    	}
    	u.tag = 2;
    	topoList.add(0, u);
    	time++;
    	u.f = time;
    }
    
    public void moveOnePlace(Node node) {
    	int xCoordinate = node.getPoint().getX();
		int yCoordinate = node.getPoint().getY();
		if(xCoordinate+1<=width)
			node.getPoint().setX(xCoordinate+1);
		else {
			if (yCoordinate==height) return;
			node.getPoint().setY(yCoordinate+1);
			node.getPoint().setX(0);
		}
	}
    
    public boolean checkAdd(DirectedGraph graph, Node node, List<Node> listOfPredecessors) {
    	GridPoint_2 nodePoint_2 = node.getPoint();
    	for (Node node2 : graph.vertices) {   //check duplicated
    		if (nodePoint_2.equals(node2.getPoint()))
    				return false;
    	}
    	for (Node node2 : graph.vertices) {
    			for (Node preNode: listOfPredecessors) {
    				if (preNode.getPoint().equals(node2.getPoint())) continue;
    				GridSegment_2 segment_2 = new GridSegment_2(preNode.getPoint(), node.getPoint());
    				if (segment_2.hasOn(node2.getPoint()))
    					return false;
    			}
    			for (Node nextNode: node2.successorsList()) {
    				GridSegment_2 segment_2 = new GridSegment_2(node2.getPoint(), nextNode.getPoint());
    				if (segment_2.hasOn(node.getPoint()))
    					return false;
    			}
    	}
    	return true;
    		
    }
	public void computeValidInitialLayout() {
		System.out.println("Start computeValidInitialLayout");
		if (isValid()) {
    		System.out.println("Already valid");
    		return;
    	}
    	DirectedGraph candiGraph = new DirectedGraph(height);
    	List<Node> topoList = topoSortDeepFirstSearch();
    	
    	for (Node node: topoList) {
    		node.getPoint().setX(0);
    		node.getPoint().setY(0);
    	}
    	
    	for(Node node: topoList) {
    		Node newNode = new Node(node.index, node.getPoint(), node.color, String.valueOf(node.index));
    		int maxDepth = -1;
    		for (Node preNode: node.predecessorsList()) {
    			if (maxDepth < preNode.getPoint().getY())
    				maxDepth = preNode.getPoint().getY();
    		}
    		newNode.getPoint().setY(maxDepth + 1);    		
    		while (!checkAdd(candiGraph, newNode, node.predecessorsList())) {
    			moveOnePlace(newNode);
    		}
    		candiGraph.addNode(newNode);
    		for (Node preNode: node.predecessorsList()) {
				candiGraph.addDirectedEdge(candiGraph.getNode(String.valueOf(preNode.index)), newNode);
    		}
    		
    	}
    	this.g = candiGraph;
    	System.out.println("Finish computeValidInitialLayout");
    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
	public boolean checkTargetNeighborHasOn(NodeSegment seg) {
		for(Node node : g.vertices) {
			if (seg.toGridSegment_2().hasOn(node.getPoint())) return false;
		}
		for (NodeSegment targetneigh: seg.targetNeighbors) {
			for(Node node : g.vertices) {
				if (node == seg.target()) continue;
				if (node == targetneigh.source()) continue;
				if (targetneigh.toGridSegment_2().hasOn(node.getPoint())) return false;
			}
		}
		return true;
	}
	public boolean checkSourceNeighborHasOn(NodeSegment seg) {
		for(Node node : g.vertices) {
			if (seg.toGridSegment_2().hasOn(node.getPoint())) return false;
		}
		for (NodeSegment targetneigh: seg.sorceNeighbors) {
			for(Node node : g.vertices) {
				if (node == seg.source()) continue;
				if (node == targetneigh.target()) continue;
				if (targetneigh.toGridSegment_2().hasOn(node.getPoint())) return false;
			}
		}
		return true;
	}
	
	public boolean localSearchOnlyTarget(NodeSegment seg, int Xrange , int Yrange, int Xstep, int Ystep) {
		//Node startNode = seg.source();
    	Node endNode = seg.target();
    	//int initialStartX = startNode.getPoint().getX();
    	//int initialStartY = startNode.getPoint().getY();
    	int initialEndX = endNode.getPoint().getX();
    	int initialEndY = endNode.getPoint().getY();
    	//System.out.println(startNode.toString());
		//System.out.println(endNode.toString());
		
    	int crossing = g.getAroundCrossings(seg);
		//int crossing = g.getCrossings(seg);
    	//System.out.println("old "+crossing);
    	//int crossing = getCrossings();
    	
    	/*int startLeftBound = Math.max(0, initialStartX-Xrange);
    	int startRightBound = Math.min(width, initialStartX+Xrange);
    	int startUpBound = Math.min(Math.min(startNode.getUpperBoundValidY(),height),initialStartY+Yrange);
    	int startLowBound = Math.max(Math.max(startNode.getLowerBoundValidY(),0),initialStartY-Yrange);
    	*/
    	int endLeftBound = Math.max(0, initialEndX-Xrange);
    	int endRightBound = Math.min(width, initialEndX+Xrange);
    	int endUpBound = Math.min(Math.min(endNode.getUpperBoundValidY(),height),initialEndY+Yrange);;
    	int endLowBound = Math.max(Math.max(endNode.getLowerBoundValidY(),0),initialEndY-Yrange);
    	boolean findBetterPlace = false;
    	
    			for (int endX = endLeftBound; endX <= endRightBound ; endX += Xstep) {
    				if (crossing == 0) break;
    				for (int endY = endLowBound;endY <= endUpBound ; endY += Ystep) {
    	    			if (crossing == 0) break;
    	    			endNode.getPoint().setX(endX);
    	    			endNode.getPoint().setY(endY);
    	    			if (checkDuplicated() && checkTargetNeighborHasOn(seg)) {
    	    				int nowCrossings = g.getAroundCrossings(seg);
    	    				if (nowCrossings < crossing) {
    	    					findBetterPlace = true;
    	    					initialEndX = endX;
    	    					initialEndY = endY;
    	    					crossing = nowCrossings;	
    	    				}    	    				
    	    			}	
    				}
    			}	
		endNode.getPoint().setX(initialEndX);	
		if (findBetterPlace) {
			g.updateCrossingInfo(seg);
			for (NodeSegment nextSeg:seg.targetNeighbors) {
				g.updateCrossingInfo(nextSeg);
			}
		}
		return findBetterPlace;
	}
	
	public boolean localSearch(NodeSegment seg, int Xrange , int Yrange, int Xstep, int Ystep) {
		Node startNode = seg.source();
    	Node endNode = seg.target();
    	int initialStartX = startNode.getPoint().getX();
    	int initialStartY = startNode.getPoint().getY();
    	int initialEndX = endNode.getPoint().getX();
    	int initialEndY = endNode.getPoint().getY();
		
    	int crossing = g.getAroundCrossings(seg);
    	
    	int startLeftBound = Math.max(0, initialStartX-Xrange);
    	int startRightBound = Math.min(width, initialStartX+Xrange);
    	int startUpBound = Math.min(Math.min(startNode.getUpperBoundValidY(),height),initialStartY+Yrange);
    	int startLowBound = Math.max(Math.max(startNode.getLowerBoundValidY(),0),initialStartY-Yrange);
    	int endLeftBound = Math.max(0, initialEndX-Xrange);
    	int endRightBound = Math.min(width, initialEndX+Xrange);
    	int endUpBound = Math.min(Math.min(endNode.getUpperBoundValidY(),height),initialEndY+Yrange);;
    	int endLowBound = Math.max(Math.max(endNode.getLowerBoundValidY(),0),initialEndY-Yrange);
    	boolean findBetterPlace = false;
    	
    	for (int startX = startLeftBound; startX <= startRightBound ; startX += Xstep) {
    		if (crossing == 0) break;
    		for (int startY = startLowBound;startY <= startUpBound ; startY += Ystep) {
    			if (crossing == 0) break;
    			for (int endX = endLeftBound; endX <= endRightBound ; endX += Xstep) {
    				if (crossing == 0) break;
    				for (int endY = endLowBound;endY <= endUpBound ; endY += Ystep) {
    	    			if (endY<=startY) continue;
    	    			if (crossing == 0) break;
    	    			startNode.getPoint().setX(startX);
    	    			startNode.getPoint().setY(startY);
    	    			endNode.getPoint().setX(endX);
    	    			endNode.getPoint().setY(endY);
    	    			if (checkDuplicated() && checkHasOn()) {
    	    				int nowCrossings = g.getAroundCrossings(seg);
    	    				if (nowCrossings < crossing) {
    	    					findBetterPlace = true;
    	    					initialStartX = startX;
    	    					initialStartY = startY;
    	    					initialEndX = endX;
    	    					initialEndY = endY;	
    	    					crossing = nowCrossings;	
    	    				}    	    				
    	    			}
    	    		}
    			}
    		}
    	}
    	startNode.getPoint().setX(initialStartX);
		startNode.getPoint().setY(initialStartY);
		endNode.getPoint().setX(initialEndX);
		endNode.getPoint().setY(initialEndY);
		if (findBetterPlace) {
			g.updateCrossingInfo(seg);
			for (NodeSegment nextSeg:seg.targetNeighbors) {
				g.updateCrossingInfo(nextSeg);
			}
			for (NodeSegment preSeg:seg.sorceNeighbors) {
				g.updateCrossingInfo(preSeg);
			}
		}
		return findBetterPlace;
	}
	
    public void localSearchHeuristic() {
    	computeValidInitialLayout();
    	preAffineTransform();
    	//forceDirectedHeuristic();
    	System.out.println("Start localSearchHeuristic");
    	g.initializeEdges();
    	g.initializeLinkedRelation();
    	g.calculateCrossingEdge();
    	int Xrange = Math.max(1, width/2);
    	int Yrange = Math.max(1, height/2);
    	System.out.println("start");
    	//int Xrange = width;
    	//int Yrange = height;
    	int initialXrange = Xrange;
    	int initialYrange = Yrange;
    	int crossing;
    	int newCrossing;
    	int iteration = 0;
    	int Xstep = Math.max(1, width/5);
    	int Ystep = Math.max(1, height/5);
    	do {
    	crossing = g.getCrossings();
    	while (!g.edgeQueue.isEmpty()) {
    		NodeSegment mostCrossingSeg = g.edgeQueue.poll();
    		boolean flag = localSearch(mostCrossingSeg, Xrange, Yrange,Xstep,Ystep);
    	}
    	g.calculateCrossingEdge();
    	newCrossing = g.getCrossings();
    	Xstep = Math.max(1,Xstep/2);
    	Ystep = Math.max(1,Ystep/2);
    	Xrange = Xrange/2;
    	Yrange = Yrange/2;
    	iteration++;
    	System.out.println("iteration: " + iteration + " crossings: "+newCrossing);
    	GraphWriter_Json.write(this.g, width,height, "test3-.txt");
    	} while (newCrossing<crossing);
    	
    	System.out.println("Finish localSearchHeuristic");
    }

    /**
     * Improve the current layout by performing a local search heuristic: nodes are moved
     * to a new location in order to reduce the number of crossings. The layout must remain valid at the end.
     */
    public int update_steplength(int step, double energy, double energy0) {
    	double t = 0.7;
    	if (energy<energy0) {
    		progress++;
    		if (progress>=5) {
    			progress = 0;
    			step = (int) Math.round(step/t);
    		}
    	}
    	else {
			progress = 0;
			step = (int) Math.round(t*step);
		}
    	return step;
    }
    
    public void calculateForce(Node node, double k, double c) {
    	node.force.add(0, (double) 0);
		node.force.add(1, (double) 0);
		for (Node preNode:node.predecessorsList()) {
			double distance = node.getPoint().distanceFrom(preNode.getPoint());
			node.force.set(0, node.force.get(0)+distance/k*(preNode.getPoint().getX()-node.getPoint().getX()));
			node.force.set(1, node.force.get(1)+distance/k*(preNode.getPoint().getY()-node.getPoint().getY()));
		}
		for (Node nextNode:node.successorsList()) {
			double distance = node.getPoint().distanceFrom(nextNode.getPoint());
			node.force.set(0, node.force.get(0)+distance/k*(nextNode.getPoint().getX()-node.getPoint().getX()));
			node.force.set(1, node.force.get(1)+distance/k*(nextNode.getPoint().getY()-node.getPoint().getY()));
		}
		
		for(Node anotherNode:g.vertices) {
			if (node==anotherNode) continue;
			double  squareDistance = node.getPoint().squareDistance(anotherNode.getPoint());
			node.force.set(0, node.force.get(0)-c*k*k/squareDistance*(anotherNode.getPoint().getX()-node.getPoint().getX()));
			node.force.set(1, node.force.get(1)-c*k*k/squareDistance*(anotherNode.getPoint().getY()-node.getPoint().getY()));
		}
		for (Node preNode:node.predecessorsList()) {
			for (Node anotherNode : preNode.successorsList()) {
				if (anotherNode == node) continue;
				double d1 = node.getPoint().distanceFrom(preNode.getPoint());
				double d2 = anotherNode.getPoint().distanceFrom(preNode.getPoint());
				GridVector_2 v1 = new GridVector_2(preNode.getPoint(), node.getPoint());
				GridVector_2 v2 = new GridVector_2(preNode.getPoint(), anotherNode.getPoint());	
				double angle = Math.acos(v1.innerProduct(v2)/(d1*d2));
				GridVector_2 u = new GridVector_2(anotherNode.getPoint(),node.getPoint());
				double force = Math.signum(u.getX())*(Math.max(1,height/100)*(Math.atan(d1/0.1)+Math.atan(d2/0.1))+Math.max(1,height/100)*Math.tan(angle/2));
				node.force.set(0, node.force.get(0)+force);
			}
		}
    }
    
    public double distanceOfPositionList(GridPoint_2[] x,GridPoint_2[] y) {
    	if (x.length!=y.length) throw new Error("length not equal");
    	double s = 0;
    	for (int i = 0; i<x.length; i++) {
    		s += x[i].squareDistance(y[i]);
    	}
    	return Math.sqrt(s);
    }
    
    
    public void affineTransform() {
    	int minX = 0,maxX=width,minY=0 ,maxY = height;
    	int count = 0;
    	do {
			GridPoint_2 nodePoint = g.getNode(count++).getPoint();
			if(nodePoint.getX()<minX) minX = nodePoint.getX();
    		if(nodePoint.getX()>maxX) maxX = nodePoint.getX();
    		if(nodePoint.getY()<minY) minY = nodePoint.getY();
    		if(nodePoint.getY()>maxY) maxY = nodePoint.getY();
		} while (count<g.sizeVertices());
    	int Xratio = (maxX-minX-1)/width+1;
    	int Yratio =  (maxY-minY-1)/height+1;
    	for(Node node:g.vertices) {
    		node.getPoint().setX(node.getPoint().getX()/Xratio);
    		node.getPoint().setY(node.getPoint().getY()/Yratio);
    	}
    }
    public void preAffineTransform() {
    	int minX = width,maxX=0,minY=height ,maxY = 0;
    	int count = 0;
    	do {
			GridPoint_2 nodePoint = g.getNode(count++).getPoint();
			if(nodePoint.getX()<minX) minX = nodePoint.getX();
    		if(nodePoint.getX()>maxX) maxX = nodePoint.getX();
    		if(nodePoint.getY()<minY) minY = nodePoint.getY();
    		if(nodePoint.getY()>maxY) maxY = nodePoint.getY();
		} while (count<g.sizeVertices());
    	
    	int Xratio = width/(maxX-minX);
    	int Yratio = height/(maxY-minY);
    	for(Node node:g.vertices) {
    		node.getPoint().setX(node.getPoint().getX()*Xratio);
    		node.getPoint().setY(node.getPoint().getY()*Yratio);
    	}
    }
    public GridPoint_2[] forDirectedAlgorithm(double tol, double c, double k) {
    	GridPoint_2[] x = g.getPositions();
    	boolean converged = false;
    	int step = Math.max(height/100,1);
    	double energy = Float.POSITIVE_INFINITY;
    	double energy0;
    	
    	while (converged == false) {
    		GridPoint_2[] x0 = g.getPositions();
    		energy0 = energy;
    		energy = 0;
    		for (int i = 0; i<g.sizeVertices();i++) {
        		Node node = g.vertices.get(i);
    			calculateForce(node, k, c);
    			double normForce = Math.sqrt(Math.pow(node.force.get(0), 2)+Math.pow(node.force.get(1), 2));
        		GridVector_2 translateGridVector_2 = new GridVector_2((int)(Math.round(step*node.force.get(0)/normForce)), (int)(Math.round(step*node.force.get(1)/normForce)));
        		
        		
        		if (node.force.get(1)>0)
        			for(Node aNode: g.vertices) {
        				if (aNode.getPoint().getY()>node.getPoint().getY())
        					aNode.getPoint().translateOf(translateGridVector_2);
        			}
        		else if (node.force.get(1)<0)
        			for(Node aNode: g.vertices) {
        				if (aNode.getPoint().getY()<node.getPoint().getY())
        					aNode.getPoint().translateOf(translateGridVector_2);
        			}
        		node.getPoint().translateOf(translateGridVector_2);
        		energy = energy + normForce;
        	}
    		x = g.getPositions();
    		step = update_steplength(step,energy,energy0);
    		if (distanceOfPositionList(x, x0)<k*tol) converged = true;
    	}
    	return x;
    }
    public void centralize() {
    	GridPoint_2[] position = g.getPositions();
    	long[] barycenter = g.barycenter();
    	GridPoint_2 center = new GridPoint_2(width/2, height/2);
    	GridPoint_2 barycenterGridPoint_2 = new GridPoint_2((int)barycenter[0], (int)barycenter[1]);
    	GridVector_2 vector_2 = new GridVector_2(barycenterGridPoint_2, center);
    	GridPoint_2[] newPosition = position.clone();
    	for (int i = 0; i<position.length;i++) {
    		newPosition[i] = position[i].sum(vector_2);
    	}
    	g.resetPosition(newPosition);
    }
    
    public void forceDirectedHeuristic() {
    	System.out.println("Start forceDirectedHeuristic");
    	computeValidInitialLayout();
    	preAffineTransform();
    	double tol = 0.001;
    	double c = 1;
    	double k = 1;
    	GridPoint_2[] newPosition = forDirectedAlgorithm(tol, c, k);
    	g.resetPosition(newPosition);
    	//affineTransform();
    	centralize();
    	affineTransform();
    	System.out.println("Finish forceDirectedHeuristic");
    	
    	
    }

    /**
     * Main function that computes a valid upward drawing that minimizes edge crossings. <br>
     * 
     * You are free to use and combine any algorithm/heuristic to obtain a good upward drawing.
     */
    public void computeUpwardDrawing() {
    	System.out.print("Compute a valid drawing with few crossings: ");
    	long startTime=System.nanoTime(), endTime; // for evaluating time performances
    	
    	computeValidInitialLayout();
    	UpwardDrawing drawing = new UpwardDrawing(this.g, this.height,this.width);
    	forceDirectedHeuristic();
    	if (!isValid()) {
    		this.g = drawing.g;
    	}
    	localSearchHeuristic();
    	//this.g.sortVertices();
    	//drawing.affineTransform();
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
    	System.out.println("Elapsed time: "+duration+" seconds");
    }

    /**
     * Check whether the current graph is provided with an embedding <br>
     * -) if all nodes are set to (0, 0): the graph has no embedding by definition <br>
     * -) otherwise, the graph has an embedding
     */
    public static boolean hasInitialLayout(DirectedGraph graph) {
    	for(Node v: graph.vertices) {
    		if(v.getPoint().getX()!=0 || v.getPoint().getY()!=0)
    			return true;
    	}
    	return false;
    }

}
