package jdg.graph;

import java.util.ArrayList;

public class GridSegment_2{
  public GridPoint_2 p, q;
  public ArrayList<GridSegment_2> crossing;

  public GridSegment_2() {}
  
  public GridSegment_2(GridPoint_2 p, GridPoint_2 q) { 
	 this.p=p; 
	 this.q=q; 
	 this.crossing = new ArrayList<GridSegment_2>();
  }

  public GridPoint_2 source() {return this.p; }
  public GridPoint_2 target() {return this.q; }
  public GridPoint_2 vertex(int i) {
  	if(i==0)return this.p; 
  	else return this.q;	
  }
  
/**
 * returns the vector s.target() - s.source()
 */
  public GridVector_2 toVector() {
  	return new GridVector_2 (this.p,this.q);
  }

/**
 * returns a segment with source and target interchanged
 */
  public GridVector_2 opposite() {
  	return new GridVector_2 (this.q,this.p);
  }

/**
 * A point is on s, 
 * iff it is equal to the source or target of s, 
 * or if it is in the interior of s
 */  
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

  public String toString() {return "["+p+","+q+"]"; }
  public int dimension() { return 2;}
  public int crossingNumber() {
	  return this.crossing.size();
  }
  
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




