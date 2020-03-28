package jdg.io;

import java.awt.Color;

import jdg.graph.DirectedGraph;
import jdg.graph.Node;
import jdg.graph.GridPoint_2;
import tc.TC;

/**
 * Provides methods for dealing with networks stored as edge lists (.edges file).
 */		   
public class GraphReader_Json extends GraphReader {

    /**
     * Construct a graph from an edge list representation
     * Remarks: 
     * 	-) vertex indices range from 0..n-1
     *  -) the graph is assumed to undirected
     * 
     */		   
    public DirectedGraph read(String filename) { 
    	System.out.println("Reading (undirected) graph in JSON format: "+filename);
    	int n, e; // number of vertices and edges
    	int[] drawingBounds = readDrawingAreaBounds(filename);
    	DirectedGraph g=new DirectedGraph(drawingBounds[0]);	
    	n = this.readNodes(filename, g); // first pass: read the nodes of the graph
    	e = this.readEdges(filename, g); // second pass: read the edges of the graph
    	System.out.println("Input graph loaded in main memory ("+g.vertices.size()+" vertices, "+g.sizeEdges()+" edges)");
    	
    	return g;
    }

    /**
     * Read the nodes of the graph<br>
     * Remarks: <br>
     * 	-) vertex indices range from 0..n-1
     * 
     * @return the number of nodes in the graph
     */		   
    private int readNodes(String filename, DirectedGraph g) { 
    	System.out.print("\tReading nodes...");
		int n; // number of vertices
		String l, text[];    	
		
		TC.lectureDansFichier(filename);
		
		l=TC.lireLigne();
		while(l.contains("\"nodes\"")==false) {
			//System.out.println("\t "+l);
			l=TC.lireLigne();
		}
		
		l=TC.lireLigne();
		if(l.contains("{")==false)
			throw new Error("Error: the line "+l+" does not contain symbol '{'");
    	int i=0;
    	while(l.contains("]")==false) { // set nodes
    		l=TC.lireLigne(); // line "id": 0,
    		//System.out.println("\t line id: "+l);
    		text=TC.motsDeChaine(l);
    		if(l.contains("id")==false)
    			throw new Error("Error: the line "+l+" does not contain id");
    		int index=Integer.parseInt(text[1].replace(",", ""));
    		l=TC.lireLigne();
    		//System.out.println("\t line x: "+l);
    		text=TC.motsDeChaine(l);
    		if(l.contains("x")==false)
    			throw new Error("Error: the line "+l+" does not contain x");
    		int x=Integer.parseInt(text[1].replace(",", ""));
    		
    		l=TC.lireLigne();
    		//System.out.println("\t line y: "+l);
    		text=TC.motsDeChaine(l);
    		if(l.contains("y")==false)
    			throw new Error("Error: the line "+l+" does not contain y");
    		int y=Integer.parseInt(text[1].replace(",", ""));
    		
    		GridPoint_2 p=new GridPoint_2(x, y);
    		Color color=null;
    		g.addNode(new Node(index, p, color ,String.valueOf(index)));
    		i++;
    		
    		l=TC.lireLigne(); // reading "},"
    		//System.out.println("\t line }: "+l);
    		l=TC.lireLigne(); // reading either "{" or "],"
    	}
    	n=i; // number of nodes
    	    	
    	System.out.println("done ("+g.vertices.size()+" vertices)");
    	TC.lectureEntreeStandard();
    	
    	return n;
    }
    
    /**
     * Read the edges of the graph<br>
     * Remarks: <br>
     * 	-) vertex indices range from 0..n-1
     * 
     * @return the number of nodes in the graph
     */		   
    private int readEdges(String filename, DirectedGraph g) { 
    	System.out.print("\tReading edges...");
		int e; // number of edges
		String l, text[];    	
		
		TC.lectureDansFichier(filename);
		
		l=TC.lireLigne();
		while(l.contains("\"edges\"")==false) {
			//System.out.println("\t "+l);
			l=TC.lireLigne();
		}
		
		l=TC.lireLigne();
		if(l.contains("{")==false)
			throw new Error("Error: the line "+l+" does not contain symbol '{'");
    	e=0;
    	while(l.contains("]")==false) { // set nodes
    		l=TC.lireLigne(); // line "id": 0,
    		//System.out.println("\t line x: "+l);
    		text=TC.motsDeChaine(l);
    		if(l.contains("source")==false)
    			throw new Error("Error: the line "+l+" does not contain x");
    		int source=Integer.parseInt(text[1].replace(",", ""));
    		
    		l=TC.lireLigne();
    		//System.out.println("\t line y: "+l);
    		text=TC.motsDeChaine(l);
    		if(l.contains("target")==false)
    			throw new Error("Error: the line "+l+" does not contain y");
    		int target=Integer.parseInt(text[1].replace(",", ""));
    		
			Node v1=g.getNode(String.valueOf(source));
			Node v2=g.getNode(String.valueOf(target));
			
			if(v1==null || v2==null) {
				throw new Error("Error: wrong vertex indices "+source+" "+target);
			}
			if(v1!=v2 && g.adjacent(v1, v2)==false && g.adjacent(v2, v1)==false) { // loops and multiple edges are not allowed
				g.addDirectedEdge(v1, v2); 
				e++;
			}
    		
    		l=TC.lireLigne(); // reading "},"
    		//System.out.println("\t line }: "+l);
    		l=TC.lireLigne(); // reading either "{" or "],"
    	}
    	    	
    	System.out.println("done ("+e+" edges)");
    	TC.lectureEntreeStandard();
    	
    	return e;
    }
    /**
     * Read the width and height of the drawing area<br>
     * 
     * @return the width and height of the drawing area
     */		   
    public static int[] readDrawingAreaBounds(String filename) { 
    	System.out.print("Reading width and height of the drawing area...");
		int height=-1, width=-1; 
		String l, text[];    	
		
		TC.lectureDansFichier(filename);		
		l=TC.lireLigne();
    	while(l.contains("\"width\"")==false) { // searching for the string "width"
    		l=TC.lireLigne(); // reading either "{" or "],"
    	}
    	text=TC.motsDeChaine(l);
    	width=Integer.parseInt(text[1].replace(",", ""));
    	TC.lectureEntreeStandard();

		TC.lectureDansFichier(filename);		
		l=TC.lireLigne();
    	while(l.contains("\"height\"")==false) { // searching for the string "height"
    		l=TC.lireLigne(); // reading either "{" or "],"
    	}
    	text=TC.motsDeChaine(l);
    	height=Integer.parseInt(text[1].replace(",", ""));
    	TC.lectureEntreeStandard();

    	if(width<0 && height<0) // width and height are not defined
    		throw new Error("Error: width and height not defined in Json file");
    	
    	if(height<0)
    		height=width;
    	if(width<0)
    		width=height;
    	
    	System.out.println("done ("+width+" x "+height+")");
    	
    	return new int[] {width, height};
    }

    /**
     * Read the geometric coordinates of nodes of a given graph g
     * 
     * @param g the input graph
     * @param inputData the file contained the vertex coordinates
     */		   
    public void readGeometry(DirectedGraph g, String inputData) {
    	throw new Error("Not supported: to be completed");
    }
    
    /**
     * Read the ages of nodes of a given graph g
     * 
     * @param g the input graph
     * @param inputData the file contained the vertex ages
     */		   
    public void readVertexAges(DirectedGraph g, String inputData) {
    	throw new Error("Not supported");
    }

}
