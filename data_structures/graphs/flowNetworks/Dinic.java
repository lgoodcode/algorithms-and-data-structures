package data_structures.graphs.flowNetworks;

import data_structures.linkedLists.LinkedList;
import data_structures.queues.Queue;

/**
 * Dinics(G, s, t)
 * 1   Initialize residual graph G as given graph
 * 2   Do BFS of G to construct a level graph and determine
 *       if there is positive flow to send
 * 3   while there is positive flow and a path from s to t
 * 4       send multiple flows in G using level graph until
 *             blocking flow is reached
 */ 

/**
 * <h3>Dinic's {@code O(E V^2)}</h3>
 *
 * <p>
 * Main function of Dinics that runs in a DFS-like fashion, getting the residual
 * capacity of edges on the path from {@code s} to {@code t} until we either
 * reach the sink {@code t}, or don't and return {@code 0} for no flow left to
 * push. If we do reach {@code t}, we are sending the maximum flow; the smallest
 * residual capacity of the edges along the path. We then augment the edges to
 * preserve the flow by reducing the flow of forward edges from {@code s} to
 * {@code t} and adding to the residual edges {@code (v, u)}, then returning the
 * flow which is the smallest residual capacity, aka the maximum flow for the
 * path, and then adding it to the total max flow.
 * </p>
 *
 * <p>
 * The algorithm using a <i>Level</i> graph which is a residual graph with all
 * vertices labeled with their levels; the {@code i}th vertex along a simple
 * path from {@code s} to {@code t} where {@code s} is {@code 0} and {@code t}
 * is the number of edges from {@code s} to {@code t}.
 * </p>
 *
 * <p>
 * <b>Blocking Flow:</b> If no more flow can be sent using the level graph.
 * i.e., no more paths exist from {@code s} to {@code t} such that paths have
 * current levels, {@code 0, 1, 2, ...} in order.
 * </p>
 *
 * <h3>Aggregate Analysis {@code O(EV^2)}</h3>
 *
 * <p>
 * Doing a BFS to construct level graph takes {@code O(E)} time. Sending
 * multiple more flows until a blocking flow is reached takes {@code O(VE)}
 * time. The outer loop runs at-most {@code O(V)} time. In each iteration, we
 * construct new level graph and find blocking flow. It can be proved that the
 * number of levels increase at least by one in every iteration. So the outer
 * loop runs at most {@code O(V)} times. Therefore overall time complexity is
 * {@code O(EV^2)}.
 * </p>
 */
public final class Dinic extends MaxFlowAlgorithm {
  public Dinic() {
    super();
  }

  private static final class Node extends MaxFlowAlgorithm.Node {
    int level;

    Node(int vertex) {
      super(vertex);
      level = -1;
    }
  }

  /**
   * Runs the Dinic algorithm to find the maximum flow in the specified flow
   * network from the specified source to the sink.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the maximum flow from the source to the sink or {@code 0} if the
   *         source is the sink
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static int maxFlow(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return 0;
    return computeMaxFlow(new FlowNetwork(network), source, sink);
  }

  /**
   * Runs the Dinic algorithm to find the paths for the maximum flow in the
   * specified flow network from the specified source to the sink. Returns an
   * array of strings for each path and their respective.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the strings of paths for the maximum flow from source to sink or an
   *         array of {@code 0} length if no paths exist for a maximum flow
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static String[] maxFlowPaths(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new String[0];
    return computeMaxFlowPaths(new FlowNetwork(network), source, sink);
  }

  /**
   * Runs the Dinic algorithm to find the paths for the maximum flow in the
   * specified flow network from the specified source to the sink. Returns an
   * array of the paths and their flow as the first index followed by the vertices
   * of the path.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the arrays of paths for the maximum flow from source to sink or an
   *         array of {@code 0} length if no paths exist for a maximum flow
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static Integer[][] maxFlowArray(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new Integer[0][];
    return computeMaxFlowArray(new FlowNetwork(network), source, sink);
  }

  private static int computeMaxFlow(FlowNetwork G, int s, int t) {
    Node[] levels = new Node[G.getRows()];
    int maxFlow = 0;

    // Initialize nodes for vertices that exist
    for (int v : G.getVertices())
      levels[v] = new Node(v);

    while (D_BFS(G, levels, s, t))
      maxFlow += sendFlow(G, levels, Integer.MAX_VALUE, s, t);
    return maxFlow;
  }

  /**
   * Similar to {@link #residualCapacity()} except that this has to account for
   * the levels. It finds the residual capacity only for edges along a path where
   * the vertex {@code v} of the edge {@code (u, v)} is one level higher, similar
   * to how a slope where the flow runs down and doesn't skip.
   *
   * <p>
   * Modified to include the ability to build the string path, using a
   * {@code StringBuilder} while calculating the maximum flow.
   * </p>
   *
   * @param G    the flow network
   * @param L    the nodes containg the level for the vertex
   * @param flow the current maximum flow
   * @param u    the vertex to find an edge for the path
   * @param t    the sink to determine once an augmenting path is found or not
   * @return the residual capacity that can be pushed or {@code 0} if there is no
   *         augmenting path available
   */
  private static int sendFlow(FlowNetwork G, Node[] L, int flow, int u, int t) {
    // Base case: once the sink is reached, return the calculated residual capacity
    // cf(p) = min {cf(u, v) : (u, v) is in p} where cf(u, v) = c(u, v) - f(u, v)
    if (u == t)
      return flow;

    int c, f, v, currentFlow, cfP;
    for (FlowNetwork.Edge edge : G.getEdges(u)) {
      v = edge.getVertices()[1];
      c = edge.getCapacity();
      f = edge.getFlow();

      if (L[v].level == L[u].level + 1 && f < c) {
        currentFlow = Math.min(flow, c - f);
        cfP = sendFlow(G, L, currentFlow, v, t);

        if (cfP > 0) {
          edge.addFlow(cfP);
          // Need to lookup the reverse edge to subtract the flow
          G.getEdge(v, u).subtractFlow(cfP);

          return cfP;
        }
      }
    }

    return 0;
  }

  /**
   * Modified BFS that uses <i>levels</i> rather than a boolean visited property.
   * By default it has a value of {@code -1} to indicate no possible edge to push
   * flow to. If greater than {@code -1} then the vertex is visited and has a
   * possible edge to push flow to.
   *
   * <p>
   * The level idea is similar to the concept of flow running down a hill, which
   * goes down a level and doesn't skip around.
   * </p>
   *
   * @param G the flow network
   * @param L the nodes with the level property
   * @param s the source vertex
   * @param t the sink vertex
   * @return if the level of vertex {@code t} is greater than {@code -1},
   *         indicating there is a augmenting path available from the source to
   *         the sink.
   */
  private static boolean D_BFS(FlowNetwork G, Node[] L, int s, int t) {
    Queue<Integer> Q = new Queue<>(G.getRows());
    int u, v;

    // Reset levels
    for (Node node : L)
      if (node != null)
        node.level = -1;

    L[s].level = 0;
    Q.enqueue(s);

    while (!Q.isEmpty()) {
      u = Q.dequeue();

      for (FlowNetwork.Edge edge : G.getEdges(u)) {
        v = edge.getVertices()[1];
        // Find edges with no level and has a positive residual capacity
        if (L[v].level < 0 && edge.getFlow() < edge.getCapacity()) {
          L[v].level = L[u].level + 1;
          Q.enqueue(v);
        }
      }
    }

    return L[t].level > -1;
  }

  private static String[] computeMaxFlowPaths(FlowNetwork G, int s, int t) {
    Node[] levels = new Node[G.getRows()];
    FlowPaths P = new FlowPaths(true);
    StringBuilder sb = new StringBuilder();
    int flow = 0;

    for (int u : G.getVertices())
      levels[u] = new Node(u);

    while (D_BFS(G, levels, s, t)) {
      flow = sendFlowStringPath(G, levels, sb, Integer.MAX_VALUE, s, t);
      // Prepends the path with the residual capacity for that path
      P.addPath(flow + ": " + sb.reverse().toString() + t);
      // Reset the stringbuilder for the next path;
      sb = new StringBuilder();
    }

    return P.getStringPaths();
  }

  private static int sendFlowStringPath(FlowNetwork G, Node[] L, StringBuilder sb, int flow, int u, int t) {
    if (u == t)
      return flow;

    int c, f, v, currentFlow, cfP;
    for (FlowNetwork.Edge edge : G.getEdges(u)) {
      v = edge.getVertices()[1];
      c = edge.getCapacity();
      f = edge.getFlow();

      if (L[v].level == L[u].level + 1 && f < c) {
        currentFlow = Math.min(flow, c - f);
        cfP = sendFlowStringPath(G, L, sb, currentFlow, v, t);

        if (cfP > 0) {
          edge.addFlow(cfP);
          G.getEdge(v, u).subtractFlow(cfP);
          // Add vertex to the path (has to be reversed so we add it in reverse)
          sb.append(" >- " + u);

          return cfP;
        }
      }
    }

    return 0;
  }

  private static Integer[][] computeMaxFlowArray(FlowNetwork G, int s, int t) {
    Node[] levels = new Node[G.getRows()];
    FlowPaths P = new FlowPaths(false);
    LinkedList<Integer> L = new LinkedList<>();
    int flow = 0;

    for (int u : G.getVertices())
      levels[u] = new Node(u);

    while (D_BFS(G, levels, s, t)) {
      flow = sendFlowArrayPath(G, levels, L, Integer.MAX_VALUE, s, t);
      // Add the sink to the end of the path of vertices
      L.insertLast(t);
      // Add the flow for this path
      P.addPath(L, flow);
      // Reset the linkedlist for the next path
      L = new LinkedList<>();
    }

    return P.getArrayPaths();
  }

  private static int sendFlowArrayPath(FlowNetwork G, Node[] L, LinkedList<Integer> LL, int flow, int u, int t) {
    if (u == t)
      return flow;

    int c, f, v, currentFlow, cfP;
    for (FlowNetwork.Edge edge : G.getEdges(u)) {
      v = edge.getVertices()[1];
      c = edge.getCapacity();
      f = edge.getFlow();

      if (L[v].level == L[u].level + 1 && f < c) {
        currentFlow = Math.min(flow, c - f);
        cfP = sendFlowArrayPath(G, L, LL, currentFlow, v, t);

        if (cfP > 0) {
          edge.addFlow(cfP);
          // Need to lookup the reverse edge to subtract the flow
          G.getEdge(v, u).subtractFlow(cfP);
          // Add vertex to the path
          LL.insert(u);

          return cfP;
        }
      }
    }

    return 0;
  }

}