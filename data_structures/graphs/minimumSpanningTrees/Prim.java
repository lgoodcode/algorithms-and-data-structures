package data_structures.graphs.minimumSpanningTrees;

import java.util.function.BiFunction;

import data_structures.graphs.Graph;
import data_structures.heaps.FibonacciHeap;
import data_structures.queues.MinPriorityQueue;
import data_structures.queues.exceptions.QueueEmptyException;
import data_structures.queues.exceptions.QueueFullException;

/**
 * MST-Prim(G, w, r)
 * 1   for each u of G.V
 * 2       u.key = Infinity
 * 3       u.p = NIL
 * 4   r.key = 0
 * 5   Q = G.V (Queue contains all vertices)
 * 6   while Q != 0 (empty)
 * 7       u = Extract-Min(Q)
 * 8       for each v of G.Adj[u]
 * 9           if v of Q and w(u, v) < v.key
 * 10              v.p = u
 * 11              v.key = w(u, v)
 */

/**
 * Prim's algorithm forms the tree from an arbitrary root vertex {@code r} and
 * grows until the tree spans all the vertices in {@code V}. Each step adds to
 * the tree {@code E} a light edge that connects {@code E} to an isolated vertex
 * - one on which no edge of {@code E} is incident.
 * 
 * <p>
 * This implementation uses the {@link FibonacciHeap} to get improved
 * performance over the {@code Binary Min Heap}. It also includes a
 * <i>visited</i> attribute to prevent reusing vertices.
 * </p>
 *
 * <p>
 * During execution, all vertices that are not in the tree reside in the
 * min-priority queue {@code Q} based on the {@code key} attribute. For each
 * vertex {@code v}, the {@code v.key} is the minimum weight of any edge
 * connecting {@code v} to a vertex in the tree; {@code v.key = Infinity} if
 * there is no such edge. The {@code v.p} attribute names the parent of the
 * {@code v} in the tree.
 * </p>
 *
 * <p>
 * The algorithm implicitly maintains the set {@code A} from Generic-MST as:
 * </p>
 * <p>
 * <i><b>A = { (v, v.p ) : v of V - {r} - Q }</b></i>
 * </p>
 * 
 * <p>
 * When the min-priority queue Q is empty; the MST A for G is thus:
 * </p>
 * <p>
 * <i><b>A = { (v, v.p ) : v of V - {r} }</b></i>
 * </p>
 *
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(E lg V)}</h3>
 *
 * <p>
 * The running time of Prim's algorithm depends on how the min-priority queue
 * {@code Q} is implemented. If using a binary min-heap, lines 1-5 will take
 * {@code O(V)} time. Since it runs {@code |V|} calls to Extract-Min operation
 * which takes {@code O(lg V)} time, the total time is for the extractMin
 * operations {@code O(V lg V)}.
 * </p>
 *
 */
public final class Prim {
  private static BiFunction<FibonacciHeap.Node<Integer, Node>, 
FibonacciHeap.Node<Integer, Node>, Boolean> compare = (x, y) -> x.getValue().key < y.getValue().key;

  public static void run(Graph graph, int startVertex) {
    if (!graph.weighted)
      throw new IllegalArgumentException("Graph must be weighted.");
    if (startVertex < 0)
      throw new IllegalArgumentException("Start vertex cannot be negative.");
    if (startVertex >= graph.rows)
      throw new IllegalArgumentException("Start vertex cannot be greater than graph length.");
    _run(graph, startVertex);
  }

  public static void _run(Graph G, int r) {
    int[] V = G.getVertices();
    int i, u, v, w, numVertices = V.length;
    Node[] VTS = new Node[numVertices];
    // FibonacciHeap<Integer, Node> Q = new FibonacciHeap<>(compare);
    MinPriorityQueue<Node> Q = new MinPriorityQueue<>(numVertices, (Node x, Node y) -> x.key < y.key);

    for (i = 0; i < numVertices; i++) {
      u = V[i];
      VTS[i] = new Node(u);
      // Q.insert(VTS[i].key, VTS[i]);
      try {
        Q.insert(VTS[i]);
      } catch (QueueFullException e) {}

      if (u == r)
        VTS[i].key = 0;
    }
  
    while (!Q.isEmpty()) {
      try {
        u = Q.extractMin().vertex;
      } catch (QueueEmptyException e) { u = -1; }
      // u = Q.extractMin().vertex;

      VTS[u].visited = true;

      for (i = 0; i < numVertices; i++) {
        v = V[i];

        if (G.hasEdge(u, v)) {
          w = G.getEdgeWeight(u, v);
          
          if (w < VTS[v].key) {
            VTS[v].parent = u;
            VTS[v].key = w;
          }
        }
      }
    }

    u = 0;

    // return VTS;
  }

  public static final class Node {
    protected int vertex;
    protected int key;
    protected int parent;
    protected boolean visited;

    protected Node(int vertex) {
      this.vertex = vertex;
      key = Integer.MAX_VALUE;
      parent = -1;
      visited = false;
    }
  }
}
