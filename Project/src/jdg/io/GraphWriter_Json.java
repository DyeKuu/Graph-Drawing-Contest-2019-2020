package jdg.io;

import jdg.graph.DirectedGraph;
import jdg.graph.Node;
import tc.TC;

/**
 * @author Luca Castelli Aleardi
 * 
 * Class for saving a graph into a file in JSON format
 *
 */
public class GraphWriter_Json {

    /**
     * Output the graph in a Json file
     * 
     */		   
    public static void write(DirectedGraph g, int width, int height, String filename) {
    	System.out.print("Saving upward drawing to Json file: "+filename+" ...");
    	
    	TC.ecritureDansNouveauFichier(filename);
    	
    	int n=g.vertices.size();
    	int m=0; // number of arcs
    	
    	TC.println("{"); // first line
    	
    	// write nodes
    	TC.println("    \"nodes\": [");
    	int i=0;
    	for(Node u: g.vertices) {
    		TC.println("\t{"); // start encoding a new node
    		TC.println("\t    \"id\": "+u.index+",");
    		TC.println("\t    \"x\": "+u.getPoint().getX()+",");
    		TC.println("\t    \"y\": "+u.getPoint().getY());
    		
    		if(i!=n-1)
    			TC.println("\t},"); // the node is not the last one
    		else
    			TC.println("\t}"); // last node
    		
    		i++;
    	}
    	TC.println("    ],");

    	// write edges
    	i=0;
    	int nE=g.sizeEdges();
    	TC.println("    \"edges\": [");
    	for(Node u: g.vertices) {
    		for(Node v: u.successors) {
    			TC.println("\t{"); // start encoding a new edges
        		TC.println("\t    \"source\": "+u.index+",");
        		TC.println("\t    \"target\": "+v.index);
    			
        		if(i!=nE-1)
        			TC.println("\t},"); // the edge is not the last one
        		else
        			TC.println("\t}"); // last edge
        		
        		i++;
    		}
    	}
    	TC.println("    ],");
    	
    	// output the width and height
    	TC.println("    \"width\": "+width+",");
    	TC.println("    \"height\": "+height);
    	TC.println("}");
    	
    	TC.ecritureSortieStandard();
    	System.out.println("done ("+n+" vertices, "+i+" edges)");
    }
    
}
