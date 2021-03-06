package data_structures.graphs.singleSourceShortestPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.singleSourceShortestPaths.SSSP;
import data_structures.graphs.singleSourceShortestPaths.ShorterPathFaster;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ShorterPathFaster_Test {
  Graph G = new Graph(9, true, true);
  SSSP.Node[] nodes;

  @BeforeEach
  void setup() {
    G.addEdge(0, 1, 10);
    G.addEdge(0, 2, 5);
    G.addEdge(1, 3, 1);
    G.addEdge(1, 2, 2);
    G.addEdge(3, 4, 4);
    G.addEdge(2, 1, 3);
    G.addEdge(2, 3, 9);
    G.addEdge(2, 4, 2);
    G.addEdge(4, 3, 6);
    G.addEdge(4, 0, 7);
    G.addEdge(7, 8, 4);
  }

  @Test
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new ShorterPathFaster());
  }

  @Test
  void shorterPathFaster() {
    assertNotNull(ShorterPathFaster.run(G, 0));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> ShorterPathFaster.run(new Graph(1, false, false), 0));
  }

  @Test
  void throws_on_invalid_vertex() {
    assertThrows(IllegalArgumentException.class, () -> ShorterPathFaster.run(G, -1));
    assertThrows(IllegalArgumentException.class, () -> ShorterPathFaster.run(G, 10));
  }

  @Test
  void prints_path() {
    assertEquals("0 -> 2 -> 1 -> 3", ShorterPathFaster.printPath(G, 0, 3));
  }

  @Test
  void nodes_prints_path() {
    nodes = ShorterPathFaster.run(G, 0);
    assertEquals("0 -> 2 -> 1 -> 3", ShorterPathFaster.printPath(nodes, 0, 3));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", ShorterPathFaster.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 0, 2, 1, 3 };
    assertArrayEquals(path, ShorterPathFaster.arrayPath(G, 0, 3));
  }

  @Test 
  void nodes_array_path() {
    int[] path = { 0, 2, 1, 3 };
    nodes = ShorterPathFaster.run(G, 0);
    assertArrayEquals(path, ShorterPathFaster.arrayPath(nodes, 0, 3));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, ShorterPathFaster.arrayPath(G, 0, 5));
  }

}
