package a4q2;

import java.util.LinkedList;
import java.util.Set;

public class ShapeGraph extends Graph<Shape> {

	public ShapeGraph() {
	}

	public void resetVisited() {
		Set<String>  vertexKeySet =  vertexMap.keySet();
		for( String key : vertexKeySet ){
			vertexMap.get(key).visited = false;
		}
	}


	/**
	 * Returns a list of lists, each inner list is a path to a node that can be reached from a given node
	 * if the total area along the path to that node is greater than the threshold.
	 * Your solution must be a recursive, depth first implementation for a graph traversal.
	 * The Strings in the returned list of lists should be the vertex labels (keys).
	 */

	public LinkedList<LinkedList<String>> traverseFrom(String key, float threshold)
	{
		LinkedList<LinkedList<String>> masterList = new LinkedList<>();

		//   ADD YOUR CODE HERE.  (IF YOU WISH TO ADD A HELPER METHOD, THEN ADD IT AFTER THIS METHOD.)

		Vertex<Shape> start = vertexMap.get(key);

		LinkedList<Vertex<Shape>> tempMasterList = new LinkedList<Vertex<Shape>>();
		tempMasterList.addFirst(start);
		//construct a list to hold the key of each vertex adding up above the threshold
		masterList = DepthFirst(start, threshold, tempMasterList, masterList);

		this.resetVisited();

		return masterList;

	}


	//helper method
	public LinkedList<LinkedList<String>> DepthFirst(Vertex<Shape> start,
								float threshold, LinkedList<Vertex<Shape>> tempMasterList,
								LinkedList<LinkedList<String>> masterList){
		start.setVisited(true);

		//we get a empty LinkedList at first, set the current node to true
		int flag = 0;
		// if all of the connected vertices are visited, we get to the endVertex of this path
		// So we operate on each element included in this path
		for(Edge e : start.adjList){
			//for every edge of this vertex
			if(e == null){
				break;
			}
			// if no there are no edges for the current vertex, clear temp.
			Vertex<Shape> cur = e.endVertex;
			//get the adjacent vertex
			if(cur.getVisited() == false){
				//if not visited
				flag = 1;
				// there are edges connecting to other vertices

				while(!tempMasterList.getFirst().equals(start)){
					tempMasterList.remove(tempMasterList.getFirst());
				}
				//pop the elemment
				tempMasterList.addFirst(cur);
				//add the current vertex to tne 
				masterList = DepthFirst(cur, threshold, tempMasterList, masterList);

			}

		}

		if(flag == 0){
			//until getting to the end, calculate the area
			
			LinkedList<String> tempTempMasterList = new LinkedList<String>();
			//make a new temp linked list to hold the subsets of the current list. Renew it every time

			float sumArea = 0;
			for(int i = tempMasterList.size() - 1; i >= 0; i--){
				Vertex<Shape> v = tempMasterList.get(i);

				sumArea += v.element.getArea();
				tempTempMasterList.addLast(v.key);
				//add the current key to temp
				if(sumArea > threshold){
				// if the sum area is greater than threshhold,
					if(!masterList.contains(tempTempMasterList)){
					//make sure the current list is not contained in the list
						LinkedList<String> deepcopy = new LinkedList<String>();
						
						for(int index = 0; index < tempTempMasterList.size(); index++){
							deepcopy.addLast(tempTempMasterList.get(index));
						}
						// make a new object to hold the current list, add it to the list to avoid referencing problem
						masterList.addLast(deepcopy);
					}

				}
				
			}
			tempMasterList.remove(start);
			// remove start vertex
		}
		return masterList;
		// if all of the connected vertices are visited, clear temp
	}
}
