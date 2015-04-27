/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gmpte.passenger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author mbgm2rm2
 * @param <T>
 */
public class PathSolution<T extends Comparable<T>> {
  private ArrayList<Vertex<T>> path;
  private ArrayList<T> data;
  

	public PathSolution() {
		path = new ArrayList<>();
        data = new ArrayList<>();
	}

	public void addVertex(Vertex<T> v) {
		path.add(v);
        data.add(v.getData());
	}

	public void addHeadVertex(Vertex<T> v) {
		path.add(0, v);
        data.add(0, v.getData());
	}

	public ArrayList<Vertex<T>> getPath() {
		return path;
	}

	public boolean contains(Vertex v) {
		return path.contains(v);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		PathSolution<T> newPath = new PathSolution<>();

		for(Vertex v : path)
			newPath.addVertex(v);

		//newPath.path = (LinkedList<Vertex>)path.clone();
		return newPath;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		Iterator<Vertex<T>> it = path.iterator();

		while(it.hasNext()) {
			Vertex next = it.next();
			builder.append(next);
			if(it.hasNext())
				builder.append(" --> ");
		}

		return builder.append("\n").toString();
	}
    
    public ArrayList<T> getData() {
      return data;
    }
}
