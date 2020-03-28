import jdg.graph.DirectedGraph;
import jdg.graph.GridPoint_2;
import jdg.graph.GridSegment_2;
import jdg.io.GraphReader_Json;
import jdg.io.GraphWriter_Json;

public class Test {
	public static void test_isValid(String filename) {
		GraphReader_Json testGraphReader_Json = new GraphReader_Json();
		DirectedGraph graph = testGraphReader_Json.read(filename);
		int[] a = GraphReader_Json.readDrawingAreaBounds(filename);
		UpwardDrawing drawing = new UpwardDrawing(graph, a[0], a[1]);
		System.out.println(drawing.isValid());
		System.out.println(drawing.getCrossings());
	}
	public static void test_generating_graph(String filename) {
		GraphReader_Json testGraphReader_Json = new GraphReader_Json();
		DirectedGraph graph = testGraphReader_Json.read(filename);
		int[] a = GraphReader_Json.readDrawingAreaBounds(filename);
		UpwardDrawing drawing = new UpwardDrawing(graph, a[0], a[1]);
		
		System.out.println(drawing.isValid());
		drawing.computeValidInitialLayout();
		System.out.println(drawing.isValid());
		
		
		System.out.println(drawing.getCrossings());
		GraphWriter_Json.write(drawing.g, a[0], a[1], "test1-"+filename);
		
		graph = testGraphReader_Json.read("test1-"+filename);
		a = GraphReader_Json.readDrawingAreaBounds("test1-"+filename);
		drawing = new UpwardDrawing(graph, a[0], a[1]);
		System.out.println(drawing.isValid());
		System.out.println(drawing.getCrossings());
	}
	public static void test_hasOn(int i1, int i2, int j1, int j2, int k1, int k2) {
		GridPoint_2 p1 = new GridPoint_2(i1, i2);
		GridPoint_2 p2 = new GridPoint_2(j1, j2);
		GridPoint_2 p3 = new GridPoint_2(k1, k2);
		GridSegment_2 segment_2 = new GridSegment_2(p1, p2);
		System.out.println(segment_2.hasOn(p3));
	}
	public static void test_forcedDirected(String filename) {
		GraphReader_Json testGraphReader_Json = new GraphReader_Json();
		DirectedGraph graph = testGraphReader_Json.read(filename);
		int[] a = GraphReader_Json.readDrawingAreaBounds(filename);
		UpwardDrawing drawing = new UpwardDrawing(graph, a[0], a[1]);
		System.out.println(drawing.isValid());
		drawing.computeValidInitialLayout();
		System.out.println(drawing.isValid());
		System.out.println(drawing.getCrossings());
		drawing.forceDirectedHeuristic();
		System.out.println(drawing.isValid());
		System.out.println(drawing.getCrossings());
		GraphWriter_Json.write(drawing.g, a[0], a[1], "test2-"+filename);
		
	}
	public static void test_localSearch(String filename) {
		GraphReader_Json testGraphReader_Json = new GraphReader_Json();
		DirectedGraph graph = testGraphReader_Json.read(filename);
		int[] a = GraphReader_Json.readDrawingAreaBounds(filename);
		UpwardDrawing drawing = new UpwardDrawing(graph, a[0], a[1]);
		System.out.println(drawing.isValid());
		
		System.out.println(drawing.getCrossings());
		drawing.localSearchHeuristic();
		System.out.println(drawing.isValid());
		System.out.println(drawing.getCrossings());
		
		GraphWriter_Json.write(drawing.g, a[0], a[1], "test3-"+filename);
		
	}
	
	public static void sortVertices(String filename) {
		GraphReader_Json testGraphReader_Json = new GraphReader_Json();
		DirectedGraph graph = testGraphReader_Json.read(filename);
		int[] a = GraphReader_Json.readDrawingAreaBounds(filename);
		UpwardDrawing drawing = new UpwardDrawing(graph, a[0], a[1]);
		//System.out.println(drawing.isValid());
		drawing.g.sortVertices();
		System.out.println(drawing.isValid());
		GraphWriter_Json.write(drawing.g, a[0], a[1], "test4-"+filename);
	}
	public static void main(String[] args) {
		//test_generating_graph("19-auto-10.txt");
		
		sortVertices("test1-19-auto-10.txt");
		//test_isValid("test4-test3-.txt");
		//test_hasOn(0, 0, 4, 2, 2, 1);
		
		
		//test_forcedDirected("19-auto-1.txt");
		//test_localSearch("19-auto-8.txt");
		//test_localSearch("graph_05.json");
	}
}
