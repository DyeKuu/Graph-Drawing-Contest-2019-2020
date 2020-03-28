
import processing.core.*;

import jdg.graph.DirectedGraph;
import jdg.graph.GridPoint_2;
import jdg.graph.GridVector_2;
import jdg.graph.Node;

import java.awt.Color;

/**
 * A class for drawing (dynamic) graphs (using Processing 1.5.1)
 *
 * @author Luca Castelli Aleardi (Ecole Polytechnique, 2019)
 */
public class GraphRenderer extends PApplet {
    // coordinates of the bounding box
    protected double xmin=Double.MAX_VALUE, xmax=Double.MIN_VALUE, ymin=Double.MAX_VALUE, ymax=Double.MIN_VALUE;

    // parameters for edge rendering
    double boundaryThickness=0.5;
    private int backgroundColor=255;
    private int edgeColor=50;
    private int edgeOpacity=200;
        
    /** node selected with mouse click (to show)  */
    public Node selectedNode=null; 
    public GridPoint_2 current; // coordinates of the selected point
        
    /** input graph to render */
    static public DirectedGraph inputGraph=null;
    /** height of the grid (input parameter) */
    static public int drawingHeight;
    /** width of the grid (input parameter) */
    static public int drawingWidth;
        	
   	// parameters of the 2d frame/canvas
    /** horizontal size of the canvas (pizels) */
    public static int sizeX;
    /** vertical size of the canvas (pixels) */
    public static int sizeY; // 
    
    /** range of the window (left bottom and right top corners) */
    public static GridPoint_2 a,b; // 
	  
	  /**
	   * Initialize the frame
	   */
	  public void setup() {
		  if(sizeX<=0 || sizeY<=0) {
			  System.out.println("Error: the size of the canvas is not defined");
			  System.out.println("WARNING: please do NOT run the class GraphRenderer");
			  System.out.println("to launch the viewer, run the class ComputeUpwardGridDrawing");
			  System.exit(0);
		  }
		  System.out.println("Setting Canvas size: "+sizeX+" x "+sizeY);
		  this.size(sizeX,sizeY); // set the size of the Java Processing frame
		  
		  // set drawing parameters (size and range of the drawing layout)
		  int deltaX=2*drawingWidth/10;
		  int deltaY=2*drawingHeight/10;
		  this.a=new GridPoint_2(-deltaX, -deltaY); // left bottom corner (the drawing region is centered at the origin)
		  this.b=new GridPoint_2(drawingWidth+deltaX, drawingHeight+deltaY); // top right corner of the drawing region
	  }

	  /**
	   * Main method for drawing the applet
	   */
	  public void draw() {
	    this.background(this.backgroundColor); // set the color of background
	    this.drawGrid();
	    
	    this.display2D(); // draw all edges in gray

	    if(this.selectedNode!=null) {
	    	this.drawVertexLabel(this.selectedNode);
	    }
	    
	    this.drawOptions();
	  }
	  
	  /**
	   * Deal with keyboard events
	   */
	  public void keyPressed(){
		  switch(key) {
		  case('-'):this.zoom(1.2); break;
		  case('+'):this.zoom(0.8); break;
		  case('r'):this.translateBox(-2, 0);; break;
		  case('l'):this.translateBox(2, 0);; break;
		  case('u'):this.translateBox(0, -2);; break;
		  case('d'):this.translateBox(0, 2);; break;
		  }
	  }
	  
	  public void translateBox(int deltaX, int deltaY) {
			  GridVector_2 v=new GridVector_2(deltaX, deltaY);
			  a.translateOf(v);
			  b.translateOf(v);
			  System.out.println("translating window");
	  }
	  
	  public void zoom(double factor) {
		  //System.out.print("a="+a+", b="+b+"\t");
		  if(factor>1.0) {
			  GridVector_2 vA=new GridVector_2(-2, -2);
			  //System.out.println("vA="+vA);
			  GridVector_2 vB=new GridVector_2(2, 2);
			  a.translateOf(vA);
			  b.translateOf(vB);
			  System.out.println("zoom out");
			  //System.out.println("a="+a+", b="+b);
		  }
		  if(factor<1.0 && a.x+6<b.x) {
			  GridVector_2 vA=new GridVector_2(2, 2);
			  GridVector_2 vB=new GridVector_2(-2, -2);
			  a.translateOf(vA);
			  b.translateOf(vB);
			  System.out.println("zoom in");
			  //System.out.println("a="+a+", b="+b);
		  }
	  }
	  	  
	  public void mouseClicked() {
		  if(mouseButton==LEFT) { // select a vertex (given its 2D position)
			  this.selectedNode=this.selectNode(mouseX, mouseY);
			  if(selectedNode!=null)
				  System.out.println("vertex "+selectedNode.index);
		  }
	  }

	  public void mousePressed() {
		  this.current=new GridPoint_2(mouseX, mouseY);
	  }
	  
	  public void mouseReleased() {
	  }
	  
	  public void mouseDragged() {
		  /*if(mouseButton==RIGHT) { // translate the window
			  double deltaAB=b.x-a.x;
			  double cellSize=sizeX/deltaAB;
			  
			  double deltaX=(mouseX-current.getX())/cellSize;
			  double deltaY=(current.getY()-mouseY)/cellSize;
			  
			  System.out.println("delta x="+(mouseX-current.getX())+" pixels");
			  System.out.println("delta y="+(current.getY()-mouseY)+" pixels");
			  System.out.println("cell size="+cellSize);
			  
			  // update the left bottom and right top vertices
			  double factor=8;
			  this.a.x=a.x-(int)(deltaX/factor);
			  this.a.y=a.y-(int)(deltaY/factor); 
			  this.b.x=b.x-(int)(deltaX/factor);
			  this.b.y=b.y-(int)(deltaY/factor); 
			  
			  System.out.println("a="+a+", b="+b);
			  //this.current=new Point_2(mouseX, mouseY);
		  }*/
	  }
		
	    /**
	     * Update of the bounding box
	     */    
	    protected void update(double x, double y) {
	    	if (x<xmin)
	    		xmin = x-boundaryThickness;
	    	if (x>xmax)
	    		xmax = x+boundaryThickness;
	    	if (y<ymin)
	    		ymin = y-boundaryThickness;
	    	if (y>ymax)
	    		ymax = y+boundaryThickness;
	    }

	    /**
	     * Update the range of the drawing region (defined by corners points 'a' and 'b')
	     */    
	    public void updateBoundingBox() {
	    	/*
	    	for(Node u: inputGraph.vertices) {
	    		Point_3 p=u.getPoint();
	    		update(p.getX().doubleValue(), p.getY().doubleValue());
	    	}
	    	a=new Point_2(xmin, ymin);
	    	b=new Point_2(xmax, ymax);
	    	*/
	    }
	    
	    /**
	     * Return the current coordinates of the bounding box
	     */    
	    public double[] boundingBox() {
	    	return new double[] {xmin, xmax, ymin, ymax};
	    }

		/**
		 * Return the integer coordinates of a pixel (px, py) corresponding to a given point 'v'. <br>
		 * <br>
		 * Warning: we must take care of the following parameters: <br>
		 * -) the size of the canvas <br>
		 * -) the size of bottom and left panels <br>
		 * -) the negative direction of y-coordinates (in java drawing) <br>
		 * 
		 * @return res[] an array storing the 'x' (stored in res[0]) and 'y' coordinates of the pixel on the screen
		 */
		public int[] getCoordinates(GridPoint_2 v) {
			double x=v.getX(); // coordinates of point v
			double y=v.getY();
			double xRange=b.getX()-a.getX(); // width and height of the drawing area
			double yRange=b.getY()-a.getY();
			int i= (int) (this.sizeX*( (x-a.getX()) / xRange )); // scale with respect to the canvas dimension
			int j= (int) (this.sizeY*( (y-a.getY()) / yRange ));
			j=this.sizeY-j; // y = H - py;
			
			int[] res=new int[]{i, j};
			return res;
		}
		
		  /**
		   * Draw a gray edge (u, v)
		   */
		  public void drawSegment(GridPoint_2 u, GridPoint_2 v) {		  
			int[] min=getCoordinates(u);
			int[] max=getCoordinates(v);
		    
			this.stroke(edgeColor, edgeOpacity);
		    this.line(	(float)min[0], (float)min[1], 
		    			(float)max[0], (float)max[1]);
		  }

		  /**
		   * Draw a colored edge (u, v)
		   */
		  public void drawColoredSegment(GridPoint_2 u, GridPoint_2 v, int r, int g, int b) {		  
			int[] min=getCoordinates(u);
			int[] max=getCoordinates(v);
		    
			this.stroke(r, g, b, edgeOpacity);
		    this.line(	(float)min[0], (float)min[1], 
		    			(float)max[0], (float)max[1]);
		  }

		  /**
		   * Draw the grid
		   */
		  public void drawGrid() {
			  int r=180, g=180, b=180; // gray

			  if(drawingWidth>300 || drawingHeight>300) // do not show the grid (to many segments)
				  return;
			  
			  for(int i=0;i<=drawingWidth;i++) {
				  this.drawColoredSegment(new GridPoint_2(i, 0), new GridPoint_2(i, drawingHeight), r, g, b);
			  }
			  for(int j=0;j<=drawingHeight;j++) {
				  this.drawColoredSegment(new GridPoint_2(0, j), new GridPoint_2(drawingWidth, j), r, g, b);
			  }

		  }

		  /**
		   * Draw a vertex label on the canvas (close to the node location)
		   */
		  public void drawVertexLabel(Node u) {
			int[] min=getCoordinates(u.getPoint()); // pixel coordinates of the point in the frame
		    			
			String label=this.getVertexLabel(u); // retrieve the vertex label to show
			
			//this.stroke(edgeColor, edgeOpacity);
			this.fill(220);
			this.rect((float)min[0], (float)min[1], 80, 20); // fill a gray rectangle
			this.fill(0);
			this.text(label, (float)min[0]+4, (float)min[1]+14); // draw the vertex label
		  }

		  /**
		   * Show options on the screen
		   */
		  public void drawOptions() {
			String label="press '-' or '+' buttons for zooming\n"; // text to show
			label=label+"press 'r' or 'l' to move the layout horizontally\n";
			label=label+"press 'u' or 'd' to move the layout vertically\n";
			label=label+"use 'left mouse click' to show vertex indices and coordinates";
			
			int posX=2;
			int posY=2;
			int textHeight=64;
			
			//this.stroke(edgeColor, edgeOpacity);
			this.fill(240);
			this.rect((float)posX, (float)posY, 389, textHeight); // fill a gray rectangle
			this.fill(0);
			this.text(label, (float)posX+2, (float)posY+10); // draw the text
		  }

		  /**
		   * Select the vertex whose 2d projection is the closest to pixel (i, j)
		   */
		  public Node selectNode(int i, int j) {			  
			  Node result=null;
			  
			  double minDist=40.;
			  for(Node u: inputGraph.vertices) { // iterate over the vertices of g
				  GridPoint_2 p=new GridPoint_2(u.getPoint().getX(), u.getPoint().getY());
				  int[] q=this.getCoordinates(p);
				  
				  double dist=Math.sqrt((q[0]-i)*(q[0]-i)+(q[1]-j)*(q[1]-j));
				  if(dist<minDist) {
					  minDist=dist;
					  result=u;
				  }
			  }
			  
			  this.selectedNode=result;
			  return result;
		  }
		  
		  /**
		   * Draw the skeleton of a graph in 2D using a Processing frame
		   */
		  public void display2D() {
			  if(this.inputGraph==null)
				  return;
			  DirectedGraph graph=this.inputGraph; // current graph to draw
			  if(graph==null) // if the graph is not defined exit
				  return;
			  
			  this.fill(255,100);
				for(Node u: graph.vertices) { // draw the edges of g
					GridPoint_2 p=u.getPoint();
					for(Node v: u.successors) {
						if(v!=null) { // draw only directed edges (u, v) such that u<v
							GridPoint_2 q=v.getPoint();
								this.drawSegment(p, q); // draw a gray edge
						}
					}
				}
				
				for(Node u: graph.vertices) { // finally draw the vertices of g
					this.drawVertex(u); // color map is not computed
				}

		  }
		  
		  /**
		   * Compute the label of a vertex, from its index, spectral distortion and vertex age
		   */
		  public static String getVertexLabel(Node u) {
		    if (u.getLabel() == null) {
	        String label="v"+u.index+" ("+u.getPoint().getX()+", "+u.getPoint().getY()+")";
	        u.setLabel(label);
	        return label;
		    } else {
		      return u.getLabel();}
		  }
		  
		  /**
		   * Draw a vertex u on the canvas
		   */
		  public void drawVertex(Node u) {
			  double maxValue;
			  
			int[] min=getCoordinates(u.getPoint()); // pixel coordinates of the point in the frame
		    
			//System.out.println("v"+u.index+" dist: "+distortion+" max: "+maxDistortion);
			
			this.stroke(50, 255); // border color
			this.fill(50, 50, 50, 255); // node color
			
			int vertexSize=8; // basic vertex size
			//double growingFactor=1.+(distortion*10.);
			//vertexSize=(int)(3+vertexSize*growingFactor);
			this.ellipse((float)min[0], (float)min[1], vertexSize, vertexSize);
		  }

			/**
			 * Return an "approximation" (as String) of a given real number (with a given numeric precision)
			 */
			private static String approxNumber(double a, int precision) {
				String format="%."+precision+"f";
				String s=String.format(format,a);
				return s;
			}

}
