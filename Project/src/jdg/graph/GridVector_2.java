package jdg.graph;

public class GridVector_2{
  public int x,y;

  public GridVector_2() {}
  
  public GridVector_2(int x, int y) { 
  	this.x=x; 
  	this.y=y;
  }

  public GridVector_2(GridPoint_2 a, GridPoint_2 b) { 
	  	this.x=b.getX()-a.getX(); 
	  	this.y=b.getY()-a.getY(); 
	  }

  public int getX() {return x; }
  public int getY() {return y; }
  
  public void setX(int x) {this.x=x; }
  public void setY(int y) {this.y=y; }
  
  //should be in Object
  public boolean equals(GridVector_2 v) { 
  	return(this.x==v.x 
  		&& this.y==v.y); 
  }

  
  public String toString() {return "["+x+","+y+"]"; }
  public int dimension() { return 2;}
  
  public int getCartesian(int i) {
  	if(i==0) return x;
  	return y;
  } 
  
  public void setCartesian(int i, int value) {
  	if(i==0) this.x=value;
  	else this.y=value;
  }
    
  public GridVector_2 sum(GridVector_2 v) {
  	return new GridVector_2(this.x+v.x,
  						this.y+v.y);  	
  }
  
  public GridVector_2 difference(GridVector_2 v) {
  	return new GridVector_2(v.x-x,
  						v.y-y);  	
  }
  
  public GridVector_2 opposite() {
  	return new GridVector_2(-x,-y);  	
  }
  
  public int innerProduct(GridVector_2 v) {
  	return this.x*v.x+
  		   this.y*v.y;  	
  }

  /*public Vector_2 divisionByScalar(Number s) {
  	return new Vector_2(x/s.doubleValue(),y/s.doubleValue());  	
  }*/
  
  public GridVector_2 multiplyByScalar(int s) {
  	return new GridVector_2(x*s,y*s);  	
  }
  
  public Number squaredLength() {
  	return innerProduct(this);  	
  }
  
  public static long crossProduct(GridVector_2 vec1,GridVector_2 vec2) {
	  return (long)vec1.getX()*(long)vec2.getY()-(long)vec1.getY()*(long)vec2.getX();
  }
  
}




