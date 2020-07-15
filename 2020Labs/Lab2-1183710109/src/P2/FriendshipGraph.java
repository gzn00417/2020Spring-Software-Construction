package P2;

import P1.graph.*;

import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

public class FriendshipGraph {
	private final Graph<Person> graph = Graph.empty();

	/**
	 * @param newPerson adding person
	 * @return true if add it successfully
	 */

	public boolean addVertex(Person newPerson) {
		return graph.add(newPerson);
	}

	/**
	 * addEdge add edges of double directions
	 * 
	 * @param a,b 2 persons being linking with an edge
	 * @param A,B 2 nodes of the 2 persons
	 */

	public int addEdge(Person a, Person b) {
		int lastEdgeWeight;
		lastEdgeWeight = graph.set(a, b, 1);
		lastEdgeWeight = graph.set(b, a, 1);
		return lastEdgeWeight;
	}

	/**
	 * get Distance of two of them
	 * 
	 * @param sta path starting person
	 * @param end path ending person
	 * @return distance between 2 persons or -1 when unlinked
	 */

	public int getDistance(Person sta, Person end) {
		if (sta.equals(end))
			return 0;
		Map<Person, Integer> dis = new HashMap<>();
		Map<Person, Boolean> vis = new HashMap<>();
		Queue<Person> qu = new LinkedList<Person>();
		Set<Person> persons = graph.vertices();
		for (Person person : persons) {
			dis.put(person, 0);
			vis.put(person, false);
		}
		vis.remove(sta);
		vis.put(sta, true);
		for (qu.offer(sta); !qu.isEmpty();) {
			Person person = qu.poll();
			for (Map.Entry<Person, Integer> edge : graph.targets(person).entrySet()) {
				Person target = edge.getKey();
				if (!vis.get(target)) {
					qu.offer(target);
					vis.remove(target);
					vis.put(target, true);
					dis.remove(target);
					dis.put(target, dis.get(person) + 1);
					if (target.equals(end))
						return dis.get(target);
				}
			}
		}
		return -1;
	}
}