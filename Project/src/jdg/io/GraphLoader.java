package jdg.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jdg.graph.DirectedGraph;

/**
 * A class for loading graphs from input files in JSON format
 * 
 * @author Luca Castelli Aleardi (2019)
 *
 */
public class GraphLoader {
	
    /**
     * Construct the graph from an input file.
     */		   
    public static DirectedGraph loadGraph(String inputFile) {
    	DirectedGraph graph; // resulting graph
    	GraphReader graphReader;
   	
    	checkFileExist(inputFile);
    	
    	graphReader=new GraphReader_Json();
    	graph = graphReader.read(inputFile);
    	
    	return graph;
    }
	
	/**
	 * Check whether the input file exists
	 */
	public static void checkFileExist(String input) {
		Path path = Paths.get(input);

		if (Files.notExists(path)) {
			System.out.println("Error: input file not found: "+input);
			System.exit(0);
		}	
	}

}
