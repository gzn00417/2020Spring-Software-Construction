/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    /*
     * use a universe L to represent vertices; use edge class to represent edges
     */

    // Representation invariant:
    /*
     * the vertex must be not null; the weight must be positive
     */

    // Safety from rep exposure:
    // make the rep be private and final ,don't provide public function to modify
    // the value of these fields
    // never return mutable rep, only return the clone data

    ConcreteEdgesGraph() {

    }

    // checkRep
    private void checkRep() {
        for (L vertex : vertices)
            assert (vertex != null);
        for (Edge<L> edge : edges)
            assert (edge != null);
    }

    @Override
    public boolean add(L vertex) {
        if (vertices.contains(vertex))
            return false;
        vertices.add(vertex);
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        if (weight < 0)
            throw new RuntimeException("Negative weight");
        if (!vertices.contains(source) || !vertices.contains(target)) {
            if (!vertices.contains(source))
                this.add(source);
            if (!vertices.contains(target))
                this.add(target);
        }
        if (source.equals(target)) // source is the same with target, REFUSE to set the Edge.
            return 0;
        // Find the same edge
        Iterator<Edge<L>> it = edges.iterator();
        while (it.hasNext()) {
            Edge<L> edge = it.next();
            if (edge.sameEdge(source, target)) {
                int lastEdgeWeight = edge.weight();
                it.remove();
                if (weight > 0) {
                    Edge<L> newEdge = new Edge<L>(source, target, weight);
                    edges.add(newEdge);
                }
                checkRep();
                return lastEdgeWeight;
            }
        }
        // weight=0 means delete an edge, so it can't be before FINDING
        if (weight == 0)
            return 0;
        // new positive edge
        Edge<L> newEdge = new Edge<L>(source, target, weight);
        edges.add(newEdge);
        checkRep();
        return 0;
    }

    @Override
    public boolean remove(L vertex) {
        if (!vertices.contains(vertex))
            return false;
        edges.removeIf(edge -> edge.source().equals(vertex) || edge.target().equals(vertex));
        vertices.remove(vertex);
        checkRep();
        return true;
    }

    @Override
    public Set<L> vertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (target.equals(edge.target())) {
                sources.put((L) edge.source(), edge.weight());
            }
        }
        checkRep();
        return sources;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (source.equals(edge.source())) {
                targets.put((L) edge.target(), edge.weight());
            }
        }
        checkRep();
        return targets;
    }

    // TODO toString()
    @Override
    public String toString() {
        return "the number of vertices is  " + vertices.size() + " ,the number of edges is " + edges.size();
    }

}

/**
 * The edge class must be immutable
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is up to you.
 */
class Edge<L> {

    // fields
    private final L source, target;
    private final int weight;

    // Abstraction function:
    /*
     * source: the start vertex of the edge
     * target: the end vertex of the edge
     * weight: the value of the edge
     */

    // Representation invariant:
    /*
     * the weight must be positive the source and target must be different
     */

    // Safety from rep exposure:
    /*
     * make source, target and weight unchangeable
     */

    Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    // checkRep
    private void checkRep() {
        assert (weight > 0 && !source.equals(target));
    }

    // methods
    public L source() {
        return this.source;
    }

    public L target() {
        return this.target;
    }

    public int weight() {
        return this.weight;
    }

    public boolean sameEdge(L source, L target) {
        return source.equals(this.source) && target.equals(this.target);
    }

    // toString()
    @Override
    public String toString() {
        return "Source: " + this.source + "; Target: " + this.target + "; Weight: " + this.weight;
    }
}
