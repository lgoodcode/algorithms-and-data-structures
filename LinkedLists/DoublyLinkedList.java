package data_structures.linkedLists;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class DoublyLinkedList<K, V> extends LinkedList<K, V> {
  protected DoublyNode<K, V> head = null;
  protected DoublyNode<K, V> tail = null;

  /**
   * Empty contructor because there is no initialization besides the call to
   * super() because it extends {@link LinkedList}.
   */
  public DoublyLinkedList() { super() ;} 

  /**
   * Returns the node at the head of list.
   * 
   * @return the node at the head or {@code null} if none
   */
  @Override
  public DoublyNode<K, V> getHead() {
    return head;
  }

  /**
   * Returns the node at the tail of list.
   * 
   * @return the {@code DoublyNode} at the tail or {@code null} if none
   */
  public DoublyNode<K, V> getTail() {
    return tail;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}.
   */
  @Override
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);

    DoublyNode<K, V> node = new DoublyNode<>(key, value);

    if (head == null)
      head = tail = node;
    else {
      node.next = head;
      head.prev = node;
      head = node;
    }

    size++;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public synchronized void insertAt(int index, K key, V value) {
    checkIndex(index);
    checkKey(key);
    checkValue(value);

    DoublyNode<K, V> node = new DoublyNode<>(key, value);
    DoublyNode<K, V> prev;

    if (index == 0) {
      node.next = head.next;
      head = node;

      if (tail == null)
        tail = node;
    }
    else {
      prev = searchIndex(index).prev;
      prev.next.prev = node;
      node.next = prev.next;
      node.prev = prev;
      prev.next = node;
    }

    size++;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  @Override
  public DoublyNode<K, V> search(K key) {
    checkKey(key);

    DoublyNode<K, V> node = head;
    
    while (node != null && !node.getKey().equals(key))
      node = node.next;
    return node;
  }

  /**
   * Iterates through the list from the tail and goes in reverse until the desired
   * node with the corresponding specified key is found or the end is reached,
   * returning the {@code DoublyNode} or {@code null} if not found.
   * 
   * @param key the key of the desired node to find
   * @return the {@code DoublyNode} or {@code null} if not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public DoublyNode<K, V> rSearch(K key) {
    checkKey(key);

    DoublyNode<K, V> node = tail;

    while (node != null && !node.getKey().equals(key))
      node = node.prev;
    return node;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Since we have a pointer to the end of the list, we can increase performance
   * by determining whether the node at the specified index as towards the head or
   * tail and start iterating from either side making it perform at worst
   * {@code O(n/2)}.
   * </p>
   * 
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public DoublyNode<K, V> searchIndex(int index) {
    checkIndex(index);

    if (head == null)
      return null;

    DoublyNode<K, V> node;

    // Shifting to the right a bit reduces value by a power of 2
    // which is the equivalent of a division by 2.
    if (index < (size >> 1)) {
      node = head;

      for (int i=0; i<index; i++)
        node = node.next;
      return node;
    }
    else {
      node = tail;

      for (int i=size-1; i>index; i--)
        node = node.prev;
      return node; 
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public V get(K key) {
    DoublyNode<K, V> node = search(key);
    return node != null ? node.getValue() : null;
  }

  /**
   * Retrieves the value of the node with the specified key or {@code null}
   * if not found but, starts iterating from the tail and goes in reverse.
   * 
   * @param key the key of the desired nodes' value to retrieve
   * @return the value or {@code null} if not node not found
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public V rGet(K key) {
    DoublyNode<K, V> node = rSearch(key);
    return node != null ? node.getValue() : null;  
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public V getIndex(int index) {
    DoublyNode<K, V> node = searchIndex(index);
    return node != null ? node.getValue() : null;
  }

  /**
   * Removes a {@code DoublyNode} containing the specified key. When the desired
   * node with the corresponding key is found, we want to simply dereference the
   * node by setting the current node {@code next} pointer to the node that
   * follows the desired node to remove.
   * 
   * <p>
   * This is an internal method that is used for the different removal variations
   * that differ by how the node is found.
   * </p>
   * 
   * @param node the node to remove
   */
  public synchronized void remove(DoublyNode<K, V> node) {
    if (node == null)
      return;
    if (node == head && node == tail) {
      head = tail = null;
      return;
    }
    else if (node == head) {
      head = node.next;
      head.prev = null;
    }
    else if (node == tail) {
      tail = node.prev;
      tail.next = null;
    }
    else {
      node.prev.next = node.next;
      node.next.prev = node.prev;
    }

    size--;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @Override
  public synchronized void remove(K key) {
    remove(search(key));
  }

  /**
   * Removes the node containing the specified key. It starts at the
   * tail and then goes in reverse order of the list looking at each previous
   * node. This is so when the next node is the desired node with the
   * corresponding key, we want to simply dereference the node by setting the
   * current node {@code next} pointer to the node that follows the desired node
   * to remove.
   * 
   * @param key the key of the desired node to remove
   * 
   * @throws IllegalArgumentException if the key is {@code null} or blank
   */
  public synchronized void rRemove(K key) {
    remove(rSearch(key));
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public synchronized void removeIndex(int index) {
    remove(searchIndex(index));
  }

  /**
   * Displays the contents of the list in order in a JSON format. Overrides due to
   * the node implemented being different.
   * 
   * @return the string format of the object
   */
  @Override
  public String toString() {
    if (head == null)
      return "{}";
    
    StringBuilder sb = new StringBuilder();
    Iterable<DoublyNode<K, V>> entries = entries();
    
    sb.append("{\n");

    entries.forEach((node) -> sb.append(node.toString() + "\n"));
    
    return sb.toString() + "}";
  }

  protected <T> Iterable<T> getIterable(int type) {
    if (isEmpty())
      return new EmptyIterable<>();
    return new Enumerator<>(type, true);
  }

  protected <T> Iterator<T> getIterator(int type) {
    if (isEmpty())
      return Collections.emptyIterator();
    return new Enumerator<>(type, true);
  }

  protected <T> Enumeration<T> getEnumeration(int type) {
    if (isEmpty())
      return Collections.emptyEnumeration();
    return new Enumerator<>(type, false);
  }

  protected class Enumerator<T> extends AbstractEnumerator<T> {
    Enumerator(int type, boolean iterator) {
      list = new DoublyNode<?, ?>[size];
      this.type = type;
      this.iterator = iterator;
      index = 0;

      DoublyNode<K, V> node = DoublyLinkedList.this.getHead();

      do {
        list[index++] = node;
        node = node.next;
      } while (node != null && node != head);
    }
  }
}