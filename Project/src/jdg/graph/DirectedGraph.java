package jdg.graph;

import java.util.*;

/**
 * Pointer based implementation of an Adjacency List Representation of a directed graph
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class DirectedGraph {
	
	public ArrayList<Node> vertices; // list of vertices
	public HashMap<String,Node> labelMap; // map between vertices and their labels
	public ArrayList<NodeSegment> edges; //list of edges
	public Queue<NodeSegment> edgeQueue;
	public DirectedGraph() {
		this.vertices=new ArrayList<Node>();
		this.labelMap=null; // no labels defined
	}

	public DirectedGraph(int mapCapacity) {
		this.vertices=new ArrayList<Node>();
		this.labelMap=new HashMap<String,Node>(mapCapacity); // labels are defined
	}
	
	public void addNode(Node v) {
		String label=v.label;
		if(label==null) {
			this.vertices.add(v);
			return;
		}
		
		if(this.labelMap.containsKey(label)==false) {
			if (this.vertices.contains(v) == false) {
				this.vertices.add(v);
			}
			this.labelMap.put(label, v);
		}
	}
	
	public Node getNode(String label) {
		if(this.labelMap!=null && this.labelMap.containsKey(label)==true) {
			return this.labelMap.get(label);
		}
		return null;
	}
	public Node findNode(int nodeIndex) {
		for (Node node:vertices) {
			if (nodeIndex == node.index) return node;
		}
		return null;
	}
	public Node getNode(int index) {
		if(index>=0 && index<this.vertices.size()) {
			return this.vertices.get(index);
		}
		return null;
	}
	
	public void removeNode(Node v) {
		//throw new Error("To be updated/implemented");
		if(this.vertices.contains(v)==false)
			return;
		for(Node u: v.successorsList()) { // remove all edges between v and its neighbors
			u.predecessorsList().remove(v);
		}
		for(Node u: v.predecessorsList()) { // remove all edges between v and its neighbors
			u.successorsList().remove(v);
		}
		this.vertices.remove(v); // remove the vertex from the graph*/
		this.labelMap.remove(String.valueOf(v.index));
	}

	/** Add the (a, b) directed from 'a' to 'b' */
    public void addDirectedEdge(Node a, Node b) {
    	if(a==null || b==null)
    		return;
    	a.addSuccessor(b); // 'b' is a successor of 'b'
    	b.addPredecessor(a);
    }

    /** Remove both edges edges (a, b) and (b, a)*/
    public void removeEdge(Node a, Node b){
    	if(a==null || b==null)
    		return;
    	a.successorsList().remove(b);
    	b.successorsList().remove(a);
    	a.predecessorsList().remove(b);
    	b.predecessorsList().remove(a);
    }
    
    /** Check whether one of the two edges (a, b) and (b, a) does exist */
    public boolean adjacent(Node a, Node b) {
    	if(a==null || b==null)
    		throw new Error("Graph error: vertices not defined");
    	if(a.isPredecessorsOf(b)==true)
    		return true;
    	if(a.isSuccessorOf(b)==true)
    		return true;
    	return false;
    }
    
    public int degree(Node v) {
    	return v.degree();
    }
    
    
    public Collection<Node> getSuccessors(Node v) {
    	return v.successorsList();
    }
    
    public Collection<Node> getPredecessors(Node v) {
    	return v.predecessorsList();
    }
        
	/**
     * Return the number of nodes (it includes isolated nodes)
     */		
    public int sizeVertices() {
    	return this.vertices.size();
    }
    
	/**
     * Return the number of directed arcs
     * 
     * Remark: arcs are not counted twice
     */		
    public int sizeEdges() {
    	int result1=0, result2=0;
    	for(Node v: this.vertices) {
    		result1=result1+getSuccessors(v).size();
    		result2=result2+getPredecessors(v).size();
    	}
    	if(result1!=result2) {
    		System.out.println(result1+ " "+ result2);
    		throw new Error("Error: wrong number of edges");
    	}
    	//System.out.println(result1+ " "+ result2);
    	return result1;
    }

    /**
     * Return the number of non isolated nodes
     * 
     * @return  the number of non isolated nodes
     */		
    public int countNonIsolatedVertices() {
		int N=0; // count non isolated nodes
		for(Node u: this.vertices) {
			if(u.degree()>=0)
				N++;
		}
		return N;
    }
    
    /**
     * Return an array storing all vertex indices, according to the order of vertices
     */		   
    public int[] getIndices() {
    	int[] result=new int[this.vertices.size()];
    	
    	int count=0;
    	for(Node u: this.vertices) {
    		if(u!=null) {
    			result[count]=u.index;
    			count++;
    		}
    	}
    	return result;
    }
    
    /**
     * Return an array storing all vertex locations, according to the order of vertices
     */		   
    public GridPoint_2[] getPositions() {
    	GridPoint_2[] result=new GridPoint_2[this.vertices.size()];
    	
    	int count=0;
    	for(Node u: this.vertices) {
    		if(u!=null && u.getPoint()!=null) {
    			result[count]=u.getPoint();
    			count++;
    		}
    	}
    	return result;
    }
        
    /**
     * Compute the minimum vertex index of the graph (a non negative number)
     * 
     * Remark: vertices are allowed to have indices between 0..infty
     * This is required when graphs are dynamic: vertices can be removed
     */		   
    public int minVertexIndex() {
    	int result=Integer.MAX_VALUE;
    	for(Node v: this.vertices) {
    		if(v!=null)
    			result=Math.min(result, v.index); // compute min degree
    	}
    	return result;
    }

    /**
     * Compute the maximum vertex index of the graph (a non negative number)
     * 
     * Remark: vertices are allowed to have indices between 0..infty
     * This is required when graphs are dynamic: vertices can be removed
     */		   
    public int maxVertexIndex() {
    	int result=0;
    	for(Node v: this.vertices) {
    		if(v!=null)
    			result=Math.max(result, v.index); // compute max degree
    	}
    	return result;
    }
    
    public void resetPosition(GridPoint_2[] newPosition) {
    	for(int i = 0; i<this.sizeVertices();i++) {
    		this.vertices.get(i).setPoint(newPosition[i]);
    	}
    }
    
    public long[] barycenter() {
    	long count = 0;
    	long x = 0;
    	long y = 0;
    	for  (Node node:this.vertices) {
    		x += node.getPoint().getX();
    		y += node.getPoint().getY();
    		count++;
    	}
    	x = Math.round(((double)x/count));
    	y = Math.round(((double)y/count));
    	return new long[] {x,y};
    }
    
    public void initializeEdges() {
    	this.edges = new ArrayList<NodeSegment>();
    	for (Node node:this.vertices) {
    		for (Node nextNode:node.successorsList()) {
    			NodeSegment segment_2 = new NodeSegment(node, nextNode);
    			this.edges.add(segment_2);
    		}
    	}
    }
    
    public void calculateCrossingEdge() {
    	for (int i = 0 ; i < edges.size() - 1; i++) {
    		NodeSegment segI = edges.get(i);
    		for (int j = i + 1 ; j<edges.size() ; j++) {
    			NodeSegment segJ = edges.get(j);
    			if (GridSegment_2.isCrossing(segI.toGridSegment_2(), segJ.toGridSegment_2())) {
    				segI.crossing.add(segJ);
    				segJ.crossing.add(segI);
    			}
    		}
    	}
    	
    	edgeQueue = new PriorityQueue<NodeSegment>(edges.size(),
                new Comparator<NodeSegment>() {
                    public int compare(NodeSegment s1, NodeSegment s2) {
                        return s2.crossingNumber() - s1.crossingNumber();
                    }
                });
    	
    	for (NodeSegment seg : this.edges) {
    		edgeQueue.add(seg);
    	}
    }
    /*public void initializeLinkedRelation() {
    	for (NodeSegment seg:this.edges) {
    		for (NodeSegment anotherSeg : this.edges) {
    			if (anotherSeg == seg) continue;
    			if (seg.target()==anotherSeg.source()) {
    				seg.getNextEdges().add(anotherSeg);
    				anotherSeg.getPreEdges().add(seg);
    			}
    		}
    	}
    }*/
    public void initializeLinkedRelation() {
    	for (NodeSegment seg:this.edges) {
    		for (NodeSegment anotherSeg : this.edges) {
    			if (anotherSeg == seg) continue;
    			if (seg.target()==anotherSeg.source()) {
    				seg.targetNeighbors.add(anotherSeg);
    				anotherSeg.sorceNeighbors.add(seg);
    			}
    		}
    	}
    	for (int i = 0 ; i < edges.size() - 1; i++) {
    		NodeSegment segI = edges.get(i);
    		for (int j = i + 1 ; j<edges.size() ; j++) {
    			NodeSegment segJ = edges.get(j);
    			if (segI.source()==segJ.source()) {
    				segI.sorceNeighbors.add(segJ);
    				segJ.sorceNeighbors.add(segI);
    			}
    			if (segI.target()==segJ.target()) {
    				segI.targetNeighbors.add(segJ);
    				segJ.targetNeighbors.add(segI);
    			}
    		}
    	}
    	
    }
    public int getCrossings(NodeSegment seg) {
    	int result = 0;
    	for (NodeSegment anotherSeg : edges) {
    		if (anotherSeg == seg) continue;
    		if (seg.sorceNeighbors.contains(anotherSeg)) continue;
    		if (seg.targetNeighbors.contains(anotherSeg)) continue;
    		if (GridSegment_2.isCrossing(seg.toGridSegment_2(), anotherSeg.toGridSegment_2()))
    			result++;
    	}
    	return result;
    	
    }
    public int getAroundCrossings(NodeSegment seg) {
    	int result = getCrossings(seg);
    	
    	for (NodeSegment targetSegment : seg.targetNeighbors) {
    		result += getCrossings(targetSegment);
    	}
    	for (NodeSegment sorceSegment: seg.sorceNeighbors) {
    		result += getCrossings(sorceSegment);
    	}
    	
    	return result;
    }
    public void updateCrossingInfo(NodeSegment seg) {
    	Iterator<NodeSegment> itr = seg.crossing.iterator();
	    while (itr.hasNext()) {
	      NodeSegment crossSeg = itr.next();
	      if (!GridSegment_2.isCrossing(seg.toGridSegment_2(), crossSeg.toGridSegment_2())) {
				itr.remove();
				crossSeg.crossing.remove(seg);
			}
	    }
		for (NodeSegment anotherSeg : this.edges) {
			if (anotherSeg == seg || seg.crossing.contains(anotherSeg)) continue;
			if(GridSegment_2.isCrossing(seg.toGridSegment_2(), anotherSeg.toGridSegment_2())) {
				seg.crossing.add(anotherSeg);
				anotherSeg.crossing.add(seg);
			}
		}
    }
    public int getCrossings() {
    	int result = 0;
    	for (NodeSegment seg: edges) {
    		result += seg.crossingNumber();
    	}
    	return result/2;
    }
    
    public void sortVertices() {
    	Comparator<Node> comparator = new Comparator<Node>(){
    		   public int compare(Node s1, Node s2) {
    		     return s1.index-s2.index;
    		   }
    	};
    	Collections.sort(vertices,comparator);
    }
    
    
    /**
     * Return a string containing informations and parameters of the graph
     */		   
    public String info() {
    	String result=sizeVertices()+" vertices, "+sizeEdges()+" edges\n";
    	
    	int isolatedVertices=0;
    	int maxDegree=0;
    	for(Node v: this.vertices) {
    		if(v==null || v.degree()==0)
    			isolatedVertices++; // count isolated vertices
    		//if(v!=null && v.p!=null && v.p.distanceFrom(new Point_3()).doubleValue()>0.) // check geometric coordinates
    		//	geoCoordinates=true;
    		if(v!=null)
    			maxDegree=Math.max(maxDegree, v.degree()); // compute max degree
    	}
    	result=result+"isolated vertices: "+isolatedVertices+"\n";
    	result=result+"max vertex degree: "+maxDegree+"\n";
    	
    	result=result+"min and max vertex index: "+minVertexIndex();
    	result=result+"..."+maxVertexIndex()+"\n";
    	
    	return result;
    }
    
    public void printCoordinates() {
    	for(Node v: this.vertices) {
    		if(v!=null)
    			System.out.println(v.index+" "+v.p);
    	}
    }

}
