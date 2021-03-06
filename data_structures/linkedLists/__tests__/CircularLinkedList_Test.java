package data_structures.linkedLists.__tests__;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import data_structures.linkedLists.CircularLinkedList;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CircularLinkedList_Test {
  CircularLinkedList<String> list;
  CircularLinkedList<String> list2;

  @Nested
  class When_New {

    @BeforeEach
    void create_list() {
      list = new CircularLinkedList<>();
    }

    @Test
    void insertion() {
      list.insert("one");
    }

    @Test
    void getHead_returns_null() {
      assertNull(list.getHead());
    }

    @Test
    void getTail_returns_null() {
      assertNull(list.getTail());
    }

    @Test
    void peek_returns_null() {
      assertNull(list.peek());
    }

    @Test
    void peekLast_returns_null() {
      assertNull(list.peekLast());
    }

    @Test
    void poll_returns_null() {
      assertNull(list.poll());
    }

    @Test
    void pollLast_returns_null() {
      assertNull(list.pollLast());
    }

    @Test
    void search_throws_on_invalid_index() {
      assertThrows(IndexOutOfBoundsException.class, () -> list.search(1));
    }

    @Test
    void get_throws_on_invalid_index() {
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void empty_array() {
      assertArrayEquals(new Object[0], list.toArray());
    }

    @Test
    void empty_list_string() {
      assertEquals("[]", list.toString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\t", "\n" })
    void insert_throws_on_bad_values(String value) {
      assertThrows(IllegalArgumentException.class, () -> list.insert(value));
    }

    @Test
    void iterator_is_empty() {
      Iterator<String> values = list.iterator();
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertThrows(IllegalStateException.class, () -> values.remove());
    }
  
  }

  @Nested
  class Multiple_Insertions {

    @BeforeEach
    void create_and_insert() {
      list = new CircularLinkedList<>();

      list.insert("one");
      list.insert("two");
      list.insert("three");
      list.insert("four");
      list.insert("five");
    }

    @Test
    void getHead() {
      assertEquals(list.search(0), list.getHead());
    }

    @Test
    void getTail() {
      assertEquals(list.search(4), list.getTail());
    }

    @Test
    void peek() {
      assertEquals("five", list.peek());
    }

    @Test
    void peekLast() {
      assertEquals("one", list.peekLast());
    }

    @Test
    void poll() {
      assertEquals("five", list.poll());
      assertEquals("four", list.peek());
    }

    @Test
    void pollLast() {
      assertEquals("one", list.pollLast());
      assertEquals("two", list.peekLast());
    }

    @Test
    void clear() {
      list.clear();
      assertEquals(0, list.size());
      assertTrue(list.isEmpty());
    }

    @Test
    void insert() {
      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("three", list.get(2));
      assertEquals("two", list.get(3));
      assertEquals("one", list.get(4));
    }

    @Test
    void insertBefore() {
      list.insertBefore("six", list.search(2));

      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("six", list.get(2));
      assertEquals("three", list.get(3));
      assertEquals("two", list.get(4));
      assertEquals("one", list.get(5));
    }

    @Test
    void insertBefore_first() {
      list.insertBefore("six", list.search(0));

      assertEquals("six", list.get(0));
      assertEquals("five", list.get(1));
      assertEquals("four", list.get(2));
      assertEquals("three", list.get(3));
      assertEquals("two", list.get(4));
      assertEquals("one", list.get(5));
    }

    @Test
    void insertAfter() {
      list.insertAfter("six", list.search(1));

      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("six", list.get(2));
      assertEquals("three", list.get(3));
      assertEquals("two", list.get(4));
      assertEquals("one", list.get(5));
    }

    @Test
    void insertAfter_last() {
      list.insertAfter("six", list.search(4));

      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("three", list.get(2));
      assertEquals("two", list.get(3));
      assertEquals("one", list.get(4));
      assertEquals("six", list.get(5));
    }

    @Test
    void insertLast() {
      list.insertLast("six");

      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("three", list.get(2));
      assertEquals("two", list.get(3));
      assertEquals("one", list.get(4));
      assertEquals("six", list.get(5));
    }

    @Test
    void insertAt() {
      list.insertAt(3, "six");

      assertEquals("five", list.get(0));
      assertEquals("four", list.get(1));
      assertEquals("three", list.get(2));
      assertEquals("six", list.get(3));
      assertEquals("two", list.get(4));
      assertEquals("one", list.get(5));
    }

    @Test
    void all_insertAt() {
      list = new CircularLinkedList<>();

      list.insertAt(0, "one");
      list.insertAt(1, "two");
      list.insertAt(1, "three");
      list.insertAt(3, "four");
      list.insertAt(2, "five");

      assertEquals("one", list.get(0));
      assertEquals("three", list.get(1));
      assertEquals("five", list.get(2));
      assertEquals("two", list.get(3));
      assertEquals("four", list.get(4));
    }

    @Test
    void indexOf() {
      assertEquals(4, list.indexOf("one"));
      assertEquals(3, list.indexOf("two"));
      assertEquals(2, list.indexOf("three"));
      assertEquals(1, list.indexOf("four"));
      assertEquals(0, list.indexOf("five"));
    }

    @Test
    void indexOf_returns_negative_on_not_found() {
      assertEquals(-1, list.indexOf("test"));
    }

    @Test
    void lastIndexOf() {
      assertEquals(4, list.lastIndexOf("one"));
      assertEquals(3, list.lastIndexOf("two"));
      assertEquals(2, list.lastIndexOf("three"));
      assertEquals(1, list.lastIndexOf("four"));
      assertEquals(0, list.lastIndexOf("five"));
    }

    @Test
    void lastIndexOf_returns_negative_on_not_found() {
      assertEquals(-1, list.lastIndexOf("test"));
    }

    @Test
    void contains() {
      assertTrue(list.contains("two"));
      assertFalse(list.contains("test"));
    }

    @Test
    void get() {
      assertAll(
        () -> assertEquals("one", list.get(4)),
        () -> assertEquals("two", list.get(3)),
        () -> assertEquals("three", list.get(2)),
        () -> assertEquals("four", list.get(1)),
        () -> assertEquals("five", list.get(0))
      );
    }

    @Test
    void remove() {
      list.remove(3);

      assertAll(
        () -> assertEquals("one", list.get(3)),
        () -> assertEquals("three", list.get(2)),
        () -> assertEquals("four", list.get(1)),
        () -> assertEquals("five", list.get(0))
      );
    }

    @Test
    void removeRange() {
      list.removeRange(1, 4);

      assertAll(
        () -> assertEquals("five", list.get(0)),
        () -> assertEquals("one", list.get(1))
      );
    }
    
    @Test
    void iterator() {
      Iterator<String> values = list.iterator();
      assertTrue(values.hasNext());
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals("five", values.next());
      assertEquals("four", values.next());
      assertEquals("three", values.next());
      assertEquals("two", values.next());
      assertEquals("one", values.next());
      assertFalse(values.hasNext());
      assertThrows(NoSuchElementException.class, () -> values.next());
    }

    @Test
    void iterator_remove() {
      Iterator<String> values = list.iterator();
      assertTrue(values.hasNext());
      assertEquals("five", values.next());
      assertEquals("four", values.next());
      assertDoesNotThrow(() -> values.remove());
      assertFalse(list.contains("four"));
      assertThrows(IllegalStateException.class, () -> values.remove());
      assertEquals("three", values.next());
      assertEquals("two", values.next());
      assertEquals("one", values.next());
      assertThrows(NoSuchElementException.class, () -> values.next());
      assertEquals(4, list.size());
    }

    @Test
    void toArray() {
      Object[] arr = { "five", "four", "three", "two", "one" };
      assertArrayEquals(arr, list.toArray());
    }

    @Test
    void to_string() {
      assertEquals("[five, four, three, two, one]", list.toString());
    }
  }
}
