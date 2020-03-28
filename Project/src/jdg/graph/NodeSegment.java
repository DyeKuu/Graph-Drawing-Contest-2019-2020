package jdg.graph;

import java.util.ArrayList;

public class NodeSegment {
	public Node p,q;
	public ArrayList<NodeSegment> crossing;
	public ArrayList<NodeSegment> nextLinkedEdge;
	public ArrayList<NodeSegment> preLinkedEdge;
	public ArrayList<NodeSegment> sorceNeighbors;
	public ArrayList<NodeSegment> targetNeighbors;
	public NodeSegment() {}
	  
	public NodeSegment(Node p, Node q) { 
		 this.p=p; 
		 this.q=q; 
		 this.crossing = new ArrayList<NodeSegment>();
		 this.nextLinkedEdge = new ArrayList<NodeSegment>();
		 this.preLinkedEdge = new ArrayList<NodeSegment>();
		 this.sorceNeighbors = new ArrayList<NodeSegment>();
		 this.targetNeighbors = new ArrayList<NodeSegment>();
	  }
	
	public int crossingNumber() {
		  return this.crossing.size();
	  }
	
	public Node source() {return this.p; }
	public Node target() {return this.q; }
	public Node vertex(int i) {
	  	if(i==0)return this.p; 
	  	else return this.q;	
	}
	public ArrayList<NodeSegment> getNextEdges(){
		return this.nextLinkedEdge;
	}
	public ArrayList<NodeSegment> getPreEdges(){
		return this.preLinkedEdge;
	}
	
	public GridVector_2 toVector() {
	  	return new GridVector_2 (this.p.getPoint(),this.q.getPoint());
	  }
	
	public GridSegment_2 toGridSegment_2() {
		return new GridSegment_2 (this.p.getPoint(),this.q.getPoint());
	}
	
}
