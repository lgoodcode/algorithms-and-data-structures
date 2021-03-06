package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.allPairsShortesPaths.Minimax;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Minimax_Test {
  Graph G = new Graph(9, true, true);
  int[][] table;

  @BeforeEach
  void setup() {
    G.addEdge(1, 2, 3);
    G.addEdge(1, 3, 8);
    G.addEdge(1, 5, 14);
    G.addEdge(2, 4, 1);
    G.addEdge(2, 5, 7);
    G.addEdge(3, 2, 4);
    G.addEdge(4, 1, 2);
    G.addEdge(4, 3, 15);
    G.addEdge(5, 4, 6);
  }

  @Test
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new Minimax());
  }

  @Test
  void minimax() {
    assertNotNull(Minimax.run(G));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> Minimax.run(new Graph(1, false, false)));
  }

  @Test
  void no_negative_weight_cycle() {
    assertFalse(Minimax.hasNegativeWeightCycle(G));
  }

  @Test
  void prints_path() {
    assertEquals("1 -> 2 -> 5", Minimax.printPath(G, 1, 5));
  }

  @Test
  void table_prints_path() {
    table = Minimax.table(G);
    assertEquals("1 -> 2 -> 5", Minimax.printPath(table, 1, 5));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", Minimax.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 1, 2, 5 };
    assertArrayEquals(path, Minimax.arrayPath(G, 1, 5));
  }

  @Test 
  void table_array_path() {
    int[] path = { 1, 2, 5 };
    table = Minimax.table(G);
    assertArrayEquals(path, Minimax.arrayPath(table, 1, 5));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, Minimax.arrayPath(G, 0, 5));
  }

  @Test
  void negative_weight_cycle_detection() {
    Graph W = new Graph(9, true, true);

    W.addEdge(1, 2, 3);
    W.addEdge(1, 3, 8);
    W.addEdge(1, 5, -4);
    W.addEdge(2, 4, 1);
    W.addEdge(2, 5, 7);
    W.addEdge(3, 2, 4);
    W.addEdge(4, 1, -2);
    W.addEdge(4, 3, -5);
    W.addEdge(5, 4, -3);

    assertTrue(Minimax.hasNegativeWeightCycle(W));

  }

}