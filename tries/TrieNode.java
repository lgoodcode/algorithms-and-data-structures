package data_structures.tries;

public class TrieNode<V> {
  /**
   * The array of the child nodes.
   */
  private TrieNode<V>[] children;

  /**
   * The parent node of the node used for tracing up to the root.
   */
  protected TrieNode<V> parent;

  /**
   * Whether the current node is a word or contains a child that is or contains word(s).
   */
  protected boolean hasWord;

  /**
   * The key of the node.
   */
  private char key;

  /**
   * The value of the node.
   */
  private V value;

  /**
   * Initializes an empty node with the specified key. The key is used to determine
   * what position the node is in and what words it could contain.
   * 
   * @param key the to set the node with
   */
  @SuppressWarnings("unchecked")
  public TrieNode(char key, TrieNode<V> parent) {
    children = (TrieNode<V>[]) new TrieNode<?>[26];
    this.key = key;
    this.parent = parent;
    hasWord = false;
    value = null;
  }

  /**
   * Initializes the root node with no key or value.
   */
  @SuppressWarnings("unchecked")
  protected TrieNode() {
    children = (TrieNode<V>[]) new TrieNode<?>[26];
    key = '\s';
    hasWord = false;
  }

  /**
   * Internal method to get the numeric value of the character to map to the
   * proper index location based on the letter in the range of {@code [A, Z]}
   * case-insensitive.
   * 
   * @param c the character to get the index of
   * @return the index of the character
   */
  private int getIndex(char c) {
    int index = Character.getNumericValue(c);
    
    if (index < 10 || index > 35)
      throw new IllegalArgumentException("Character must be a letter A-Z, case-insensitive.");
    return index - 10;
  }

  /**
   * Returns the child {@code TrieNode} for the given character.
   * 
   * @param c the character of the child to get
   * @return the child node
   */
  public TrieNode<V> getChild(char c) {
    return children[getIndex(c)];
  }

  /**
   * Sets a new child {@code TrieNode} for the specified character index.
   * 
   * @param c the character index slot to insert the new child
   * @param child the new {@TrieNode} child
   */
  public void setChild(char c, TrieNode<V> child) {
    children[getIndex(c)] = child;
  }

  /**
   * Gets the array of {@TrieNode} children. Used for iterating for the
   * {@code Trie.toString()}.
   * 
   * @return the children
   */
  protected TrieNode<V>[] getChildren() {
    return children;
  }

  /**
   * Removes a child {@code TrieNode} if it doesn't contain any {@code TrieNode}
   * with a word or is a word.
   * 
   * @param c the character key of the node to remove
   */
  protected void removeChild(char c) {
    children[getIndex(c)] = null;
  }

  /**
   * Gets the key of the node
   * 
   * @return the key of the node
   */
  public char getKey() {
    return key;
  }

  /**
   * Sets the value for the node
   * 
   * @param value the value to set
   */
  public void setValue(V value) {
    this.value = value;
  }

  /**
   * Gets the value for the node
   * 
   * @return the value of the node or {@code null} if none
   */
  public V getValue() {
    return value;
  }

  /**
   * Determines whether the current word is a word with a value.
   * 
   * @return if the node has a word value
   */
  public boolean isWord() {
    return value != null;
  }

  /**
   * Returns the node string
   * 
   * @return the key and value string of the node
   */
  public String toString() {
    return key + " -> " + value;
  }

}