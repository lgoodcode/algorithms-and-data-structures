package LinkedLists.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import LinkedLists.CircularDoublyLinkedList;
import LinkedLists.DoublyNode;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CircularDoublyLinkedListTest {
  CircularDoublyLinkedList<Integer, String> list;
  CircularDoublyLinkedList<String, String> list2;
  DoublyNode<Integer, String> node;
  DoublyNode<Integer, String> node2;

  @Test
  void is_instantiated() {
    list = new CircularDoublyLinkedList<>();
  }

  @Test
  void node_is_instantiated() {
    node = new DoublyNode<>(1, "one");
  }

  @Nested
  class When_New {

    @BeforeEach
    void create_list() {
      list = new CircularDoublyLinkedList<>();
    }

    @Test
    void insertion() {
      list.insert(1, "one");

      assertSame(list.getHead(), list.getTail());

      assertNotNull(list.search(1));
    }

    @Test 
    void search_null_on_nonexistent_key() {
      assertNull(list.search(1));
    }

    @Test
    void rSearch_null_on_nonexistent_key() {
      assertNull(list.rSearch(1));
    }

    @Test
    void get_returns_null() {
      assertNull(list.get(1));
    }

    @Test
    void rGet_returns_null() {
      assertNull(list.rGet(1));
    }

    @Test
    void empty_list_string() {
      assertEquals("{}", list.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_keys(String key) {
      list2 = new CircularDoublyLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.insert(key, "test"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void search_throws_on_bad_keys(String key) {
      list2 = new CircularDoublyLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.search(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void remove_throws_on_bad_keys(String key) {
      list2 = new CircularDoublyLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.remove(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void rRemove_throws_on_bad_keys(String key) {
      list2 = new CircularDoublyLinkedList<>();

      assertThrows(IllegalArgumentException.class, () -> list2.rRemove(key));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> list.insert(1, value));
    }
  } 

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      list = new CircularDoublyLinkedList<>();

      list.insert(1, "one");
      list.insert(2, "two");
      list.insert(3, "three");
      list.insert(4, "four");
      list.insert(5, "five");
    }

    @Test
    void all_inserts_succeed() {
      assertAll(
        () -> assertEquals("one", list.get(1)),
        () -> assertEquals("two", list.get(2)),
        () -> assertEquals("three", list.get(3)),
        () -> assertEquals("four", list.get(4)),
        () -> assertEquals("five", list.get(5))
      );
    }

    @Test
    void search() {
      node = new DoublyNode<>(4, "four");
      node2 = list.search(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void rSearch() {
      node = new DoublyNode<>(4, "four");
      node2 = list.rSearch(4);

      assertEquals(node.getKey(), node2.getKey());
      assertEquals(node.getValue(), node2.getValue());
    }

    @Test
    void search_returns_null_on_nonexistent_key() {
      assertNull(list.search(9));
    }

    @Test
    void rSearch_returns_null_on_nonexistent_key() {
      assertNull(list.rSearch(9));
    }

    @Test 
    void get() {
      assertEquals("four", list.get(4));
    }

    @Test 
    void rGet() {
      assertEquals("four", list.rGet(4));
    }

    @Test
    void remove() {
      list.remove(3);

      assertAll(
        () -> assertEquals("one", list.get(1)),
        () -> assertEquals("two", list.get(2)),
        () -> assertEquals("four", list.get(4)),
        () -> assertEquals("five", list.get(5))
      );
    }

    @Test
    void rRemove() {
      list.rRemove(3);

      assertAll(
        () -> assertEquals("one", list.get(1)),
        () -> assertEquals("two", list.get(2)),
        () -> assertEquals("four", list.get(4)),
        () -> assertEquals("five", list.get(5))
      );
    }

    @Test
    void to_string() {
      assertEquals("{"
          + "\n\"Key: 5, value: five\""
          + "\n\"Key: 4, value: four\""  
          + "\n\"Key: 3, value: three\""  
          + "\n\"Key: 2, value: two\""  
          + "\n\"Key: 1, value: one\""  
          + "\n}", 
        list.toString()
      );
    }
  }
}
