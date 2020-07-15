package P3;

import java.util.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class FriendshipGraph {

	/**
	 * @Class Node : A Node is linked with a particular Person based on graph
	 *        theory. TODO Build nodes and Linking persons
	 * @param next     the person joins in the graph after him/her
	 * @param person   class Person matched with
	 * @param vis      visited mark for BFS
	 * @param dis      lowest distance from a chose person
	 * @param lastEdge first edge in First-Search
	 * @method LoadData record which person THIS belongs to
	 * @method addNode add a new Node for a person
	 * @method addNodeEdge add a edge between 2 Nodes
	 */

	public class Node {
		private Node next = null;
		Person person;
		private boolean vis;
		private int dis = Integer.MAX_VALUE;

		private Edge lastEdge = null;

		public void LoadData(Person person) {
			this.person = person;
			person.node = this;
		}

		public void addNode(Node nextVertex) {
			nextVertex.next = this.next;
			this.next = nextVertex;
		}

		/**
		 * @Class Edge A edge between 2 Nodes
		 * @param origin   one Node
		 * @param terminal another Node
		 * @param nextEdge the next edge which has the same ORIGIN with THIS
		 */

		public class Edge {
			public Node origin = null, terminal = null;
			public Edge nextEdge = null;
		}

		public void addNodeEdge(Node toVertex) {
			Edge newEdge = new Edge();
			newEdge.origin = this;
			newEdge.terminal = toVertex;
			newEdge.nextEdge = this.lastEdge;
			this.lastEdge = newEdge;
		}
	}

	/**
	 * @method addVertex add new vertex if the person's name is unduplicated
	 * @param newPerson adding person
	 * @param head      head of Linked List of persons
	 * @param NameSet   set of persons' names, used for removing duplication
	 */

	private Node head = null;
	private HashSet<String> NameSet = new HashSet<>();

	public void addVertex(Person newPerson) {
		if (NameSet.contains(newPerson.Name)) {
			System.out.println("Person " + newPerson.Name + " already existed.");
			System.exit(0);
		}
		NameSet.add(newPerson.Name);
		Node NewVertex = new Node();
		newPerson.node = NewVertex;
		NewVertex.LoadData(newPerson);
		if (head == null)
			head = NewVertex;
		else
			head.addNode(NewVertex);
		return;
	}

	/**
	 * @method addEdge add edges of double directions
	 * @param a,b 2 persons being linking with an edge
	 * @param A,B 2 nodes of the 2 persons
	 */

	public void addEdge(Person a, Person b) {
		if (a == b) {
			System.out.println("They are the same one.");
			System.exit(0);
		}
		Node A = a.node, B = b.node;
		A.addNodeEdge(B);
		B.addNodeEdge(A);
		return;
	}

	/**
	 * @method getDistance
	 * @param sta path starting person
	 * @param end path ending person
	 * @return distance between 2 persons or -1 when unlinked
	 */

	public int getDistance(Person sta, Person end) {
		if (sta == end)
			return 0;
		Queue<Person> qu = new LinkedList<Person>();
		for (Node p = head; p != null; p = p.next) {
			p.vis = false;
			p.dis = 0;
		}
		sta.node.vis = true;
		for (qu.offer(sta); !qu.isEmpty();) {
			Person p = qu.poll();
			for (Node.Edge e = p.node.lastEdge; e != null; e = e.nextEdge) {
				if (!e.terminal.vis) {
					qu.offer(e.terminal.person);
					e.terminal.vis = true;
					e.terminal.dis = p.node.dis + 1;
					if (e.terminal.person == end)
						return end.node.dis;
				}
			}
		}
		return -1;
	}
}