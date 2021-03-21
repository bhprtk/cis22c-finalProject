
/**
 * Graph.java
 * @author
 * CIS 22C, Final Project
 */

import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	private int vertices;
	private int edges;
	private ArrayList<List<Integer>> adj;
	private ArrayList<Character> color;
	private ArrayList<Integer> distance;
	private ArrayList<Integer> parent;

	/** Constructors */

	/**
	 * initializes an empty graph, with n vertices and 0 edges
	 * 
	 * @param n the number of vertices in the graph
	 */
	public Graph(int n) {
		vertices = n;
		adj = new ArrayList<>();
		color = new ArrayList<>();
		distance = new ArrayList<>();
		parent = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			adj.add(new List<Integer>());
			color.add('w');
			distance.add(-1);
			parent.add(-1);
		}
		edges = 0;
	}

	/*** Accessors ***/

	/**
	 * Returns the number of edges in the graph
	 * 
	 * @return the number of edges
	 */
	public int getNumEdges() {
		return edges;
	}

	/**
	 * Returns the number of vertices in the graph
	 * 
	 * @return the number of vertices
	 */
	public int getNumVertices() {
		return vertices;
	}

	/**
	 * returns whether the graph is empty (no edges)
	 * 
	 * @return whether the graph is empty
	 */
	public boolean isEmpty() {
		return edges == 0;
	}

	/**
	 * Returns the value of the distance[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition 0 < v <= vertices
	 * @return the distance of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Integer getDistance(Integer v) throws IndexOutOfBoundsException {
		if (v <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException("getDistance(): Vertex is out of bounds!");
		} else {
			return distance.get(v);
		}
	}

	/**
	 * Returns the value of the parent[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition v <= vertices
	 * @return the parent of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Integer getParent(Integer v) throws IndexOutOfBoundsException {
		if (v > vertices) {
			throw new IndexOutOfBoundsException("getParent(): Vertex is out of bounds!");
		} else {
			return parent.get(v);
		}
	}

	/**
	 * Returns the value of the color[v]
	 * 
	 * @param v a vertex in the graph
	 * @precondition 0< v <= vertices
	 * @return the color of vertex v
	 * @throws IndexOutOfBoundsException when the precondition is violated
	 */
	public Character getColor(Integer v) throws IndexOutOfBoundsException {
		if (v <= 0 || v > vertices) {
			throw new IndexOutOfBoundsException("getColor(): Vertex is out of bounds!");
		} else {
			return color.get(v);
		}
	}

	/*** Mutators ***/

	/**
	 * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into the
	 * list at index u) @precondition, 0 < u, v <= vertices
	 * 
	 * @throws IndexOutOfBounds exception when the precondition is violated
	 */
	public void addDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
		if (u < 0 || v > vertices) {
			throw new IndexOutOfBoundsException("addDirectedEdge(): Index out of bounds!");
		} else {
			if (adj.get(u).isEmpty()) {
				adj.get(u).addLast(v);
			} else {
				if (v < adj.get(u).getFirst()) {
					adj.get(u).addFirst(v);
				} else if (v > adj.get(u).getLast()) {
					adj.get(u).addLast(v);
				} else {
					adj.get(u).placeIterator();
					if (v > adj.get(u).getIterator()) {
						adj.get(u).advanceIterator();
					} else {
						adj.get(u).reverseIterator();
						adj.get(u).addIterator(v);
					}
				}
			}
			edges++;
		}
	}

	/**
	 * Inserts vertex v into the adjacency list of vertex u (i.e. inserts v into the
	 * list at index u) and inserts u into the adjacent vertex list of
	 * v @precondition, 0 < u, v <= vertices
	 * 
	 */
	public void addUndirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
		if (u < 0 || v > vertices) {
			throw new IndexOutOfBoundsException("addUndirectedEdge(): Index out of bounds!");
		} else {
			if (adj.get(u).isEmpty()) {
				adj.get(u).addLast(v);
			} else {
				if (v < adj.get(u).getFirst()) {
					adj.get(u).addFirst(v);
				} else if (v > adj.get(u).getLast()) {
					adj.get(u).addLast(v);
				} else {
					adj.get(u).placeIterator();
					if (v > adj.get(u).getIterator()) {
						adj.get(u).advanceIterator();
					} else {
						adj.get(u).reverseIterator();
						adj.get(u).addIterator(v);
					}
				}
			}
			if (adj.get(v).isEmpty()) {
				adj.get(v).addLast(u);
			} else {
				if (u < adj.get(v).getFirst()) {
					adj.get(v).addFirst(u);
				} else if (u > adj.get(v).getLast()) {
					adj.get(v).addLast(u);
				} else {
					adj.get(v).placeIterator();
					if (u > adj.get(v).getIterator()) {
						adj.get(v).advanceIterator();
					} else {
						adj.get(v).reverseIterator();
						adj.get(v).addIterator(u);
					}
				}
			}
			edges++;
		}
	}

	
	public void removeDirectedEdge(Integer u, Integer v) throws IndexOutOfBoundsException {
		if (u < 0 || v > vertices) {
			throw new IndexOutOfBoundsException("removeUndirectedEdge(): Index out of bounds!");
		} else {
			if(adj.get(u).getLength() == 1) {
				adj.get(u).removeFirst();
			} else {
				adj.get(u).placeIterator();
				while(!adj.get(u).offEnd()) {
					if(adj.get(u).getIterator() == v) {
						adj.get(u).removeIterator();
						return;
					}
					adj.get(u).advanceIterator();
				}
			}
			edges--;
		}
	}
	
	/*** Additional Operations ***/

	/**
	 * Creates a String representation of the Graph Prints the adjacency list of
	 * each vertex in the graph, vertex: <space separated list of adjacent vertices>
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < adj.size(); i++) {
			result += i + ": ";
			adj.get(i).placeIterator();
			while (!adj.get(i).offEnd()) {
				result += adj.get(i).getIterator() + " ";
				adj.get(i).advanceIterator();
			}
			result += "\n";
		}
		return result;
	}

	public List<Integer> getRecommendations() {
		List<Integer> recommendations = new List<>();
		int max = Collections.max(distance);
		
		for(int i = 2; i <= max; i++) {
			for(int j = 0; j < distance.size(); j++) {
				if(distance.get(j) == i) {
					recommendations.addFirst(j);
				}
			}
		}
		return recommendations;
	}
	
	/**
	 * Prints the current values in the parallel ArrayLists after executing BFS.
	 * First prints the heading: v <tab> c <tab> p <tab> d Then, prints out this
	 * information for each vertex in the graph Note that this method is intended
	 * purely to help you debug your code
	 */
	public void printBFS() {
		System.out.println("v\tc\td\tp");
		
		for(int i = 0; i < adj.size(); i++) {
			System.out.println(i + "\t" + adj.get(i) + "\t" + distance.get(i) + "\t" + parent.get(i));
		}
	}

	/**
	 * Performs breath first search on this Graph give a source vertex
	 * 
	 * @param source
	 * @precondition graph must not be empty
	 * @precondition source is a vertex in the graph
	 * @throws IllegalStateException     if the graph is empty
	 * @throws IndexOutOfBoundsException when the source vertex is not a vertex in
	 *                                   the graph
	 */

	public void BFS(Integer source) throws IllegalStateException, IndexOutOfBoundsException {
		if (vertices <= 0) {
			throw new IllegalStateException("BFS: The graph is empty!");
		} else if (source < 0 || source >= vertices) {
			throw new IndexOutOfBoundsException("BFS: Index out of bounds!");
		} else {
			Queue<Integer> Q = new Queue<>();
			int x;
			for(int i = 0; i < adj.size(); i++) {
				color.set(i, 'w');
				distance.set(i, -1);
				parent.set(i, -1);	
			}
			color.set(source, 'g');
			distance.set(source, 0);
			Q.enqueue(source);
			while(!Q.isEmpty()) {
				x = Q.getFront();
				Q.dequeue();
				adj.get(x).placeIterator();
				while(!adj.get(x).offEnd()) {
					int y = adj.get(x).getIterator();
					if(color.get(y) == 'w') {
						color.set(y, 'g');
						distance.set(y, distance.get(x) + 1);
						parent.set(y, x);
						Q.enqueue(y);
					}
					adj.get(x).advanceIterator();
				}
				color.set(x, 'b');
			}
		}
	}

}