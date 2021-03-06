package data_structures.trees;

import java.util.function.BiFunction;

public final class BinarySearchTree<K, V> extends AbstractTree<K, V> {
  /**
   * The root of the tree
   */
  private Node<K, V> root;

  /**
   * Creates an empty, BinaryTree, using the specified compare function to
   * determine whether a given {@code TreeNode} is smaller than another.
   *
   * @param compare an anonymous function that compares two {@code TreeNode}
   *                  objects
   */
  public BinarySearchTree(BiFunction<K, K, Boolean> compare) {
    super(compare);
  }

  /**
   * The default constructor that uses the default compare function and calls the
   * main constructor.
   */
  public BinarySearchTree() {
    super();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  protected <TreeNode extends Node<K, V>> void checkType(TreeNode node) {
    if (node != null && !(node instanceof Node))
      throw new IllegalArgumentException("TreeNode must be an instance of AbstractTree.Node");
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public final Node<K, V> getRoot() {
    return root;
  }

  /**
   * {@inheritDoc}
   */
  public final void clear() {
    root = null;
    size = 0;
    modCount++;
  }

  /**
   * Insert O(h)
   *
   * Begins at the root and traces a simple path downward looking for a
   * NIL to replace with z. The procedure maintains a "Trailing Pointer"
   * y as the parent of x.
   *
   * Tree-Insert(T, z)
   * 1   y = NIL
   * 2   x = T.root
   * 3   while x != NIL
   * 4       y = x
   * 5       if z.key < x.key
   * 6           x = x.left
   * 7       else x = x.right
   * 8   z.p = y
   * 9   if y == NIL
   * 10      T.root = z
   * 11  elseif z.key < y.key
   * 12      y.left = z
   * 13  else y.right = z
   */

  /**
   * {@inheritDoc}
   *
   * <p>
   * This is the recursive implementation. The base case is if the root is
   * {@code null}, which we simply set the new node as the root. Otherwise, we set
   * the parent and current node as root and insert the new node.
   * </p>
   *
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized void insert(K key, V value) {
    checkKey(key);
    checkValue(value);
    checkDuplicate(key);

    if (root == null)
      root = new Node<K, V>(key, value);
    else
      insertRecursive(root, root, new Node<K, V>(key, value));

    size++;
    modCount++;
  }

  /**
   * Recursively inserts the new node by keeping a reference of the parent node of
   * the current node. This is so once we reach a leaf, where the left or right of
   * the parent node is {@code null}, we need a reference to it to insert it as
   * the left or right.
   *
   * @param p the parent node of {@code y}
   * @param x the current node to determine where new node {@code z} is placed
   * @param y the new node to insert
   */
  private void insertRecursive(Node<K, V> p, Node<K, V> x, Node<K, V> y) {
    if (x != null) {
      if (isLessThan(y, x))
        insertRecursive(x, x.left, y);
      else
        insertRecursive(x, x.right, y);
    }
    else {
      y.parent = p;

      if (isLessThan(y, p))
        p.left = y;
      else
        p.right = y;
    }


  }

  /**
   * Search O(h)
   *
   * Traces a simple path downward, comparing keys until we reach NIL or the node
   * is found
   *
   * Tree-Search(x, k)
   * 1   if x == NIL or k == x.key
   * 2       return x
   * 3   if k < x.key
   * 4       return Tree-Search(x.left, k)
   * 5   else return Tree-Search(x.right, k)
   */

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> TreeNode _search(TreeNode node, K key) {
    if (node == null || key == node.getKey())
      return node;
    if (isLessThan(key, node.getKey()))
      return (TreeNode) _search(node.left, key);
    return (TreeNode) _search(node.right, key);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> TreeNode _minimum(TreeNode node) {
    if (node == null)
      return null;
    if (node.left != null)
      return (TreeNode) _minimum(node.left);
    return node;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  protected <TreeNode extends Node<K, V>> TreeNode _maximum(TreeNode node) {
    if (node == null)
      return null;
    if (node.right != null)
      return (TreeNode) _maximum(node.right);
    return node;
  }

  /**
   * Transplant
   *
   * Subroutine to move subtrees around the tree. Replaces one subtree as a child
   * of its parent with another subtree. x's parent becomes y's parent and x's
   * parent ends up having y as its child.
   *
   * This does not update y.left or y.right, this is the responsibility of the
   * Transplant caller.
   *
   * Transplant(T, u, v)
   * 1   if u.p == NIL
   * 2       T.root = v
   * 3   elseif u == u.p.left
   * 4       u.p.left = v
   * 5   else u.p.right = v
   * 6   if v != NIL
   * 7       v.p = u.p
   */

  /**
   * {@inheritDoc}
   */
  protected <TreeNode extends Node<K, V>> void transplant(TreeNode x, TreeNode y) {
    // If x is the root of the tree, make y the root
    if (x.parent == null)
      root = y;
    // Updates x.parent.left if x is a left child
    else if (x == x.parent.left)
      x.parent.left = y;
    // Otherwise, updates x.parent.right because x must be a right child
    else
      x.parent.right = y;

    // Updates y.parent if y is not null.
    if (y != null)
      y.parent = x.parent;
  }

  /**
   * Delete O(h)
   *
   * Tree-Delete(T, z)
   * 1   if z.left == NIL
   * 2       Transplant(T, z, z.right)
   * 3   elseif z.right == NIL
   * 4       Transplant(T, z, z.left)
   * 5   else y = Tree-Minimum(z.right)
   * 6       if y.p != z
   * 7           Transplant(T, y, y.right)
   * 8           y.right = z.right
   * 9           y.right.p = y
   * 10      Transplant(T, z, y)
   * 11      y.left = z.left
   * 12      y.left.p y
   */

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  public synchronized <TreeNode extends Node<K, V>> void deleteNode(TreeNode node) {
    checkNode(node);
    checkType(node);

    /**
     * Case 1: z has no left child
     *
     * We replace z by its right child, which may or may not be NIL.
     */
    if (node.left == null)
      transplant(node, node.right);
    /**
     * Case 2: z has no right child
     *
     * We replace z by its left child
     */
    else if (node.right == null)
      transplant(node, node.left);
    else {
      /**
       * Case 3: z has two children
       *
       * Need to find z's successor y - which must in turn be in z's right subtree -
       * and have y take z's position in the tree. The rest of z's original right
       * subtree becomes y's new right subtree, and z's left subtree becomes y's new
       * left subtree. This case is tricky because it matters whether y is z's right
       * child.
       */

      /**
       * Find node y; successor of z. Because z has a nonempty right subtree, its
       * successor must be the node in that subtree with the smallest key; hence the
       * call to minimum(z.right).
       */
      Node<K, V> y = _minimum(node.right); // Use main method without checking node

      if (y.parent != node) {
        /**
         * Case 3a: y is in z's right subtree but is not z's right child. We need to
         * replace y by its own right child, and then replace z by y.
         *
         * In this case, z's successor y, in z's right subtree, will have no left child.
         */
        transplant(y, y.right);
        y.right = node.right;
        y.right.parent = y;
      }

      /**
       * y's right subtree stays the same because y was either z's right child or the
       * found successor that was transplanted for case 3a. We now make z's parent and
       * left child pointers be the same for y, to take z's place.
       */
      transplant(node, y);
      y.left = node.left;
      y.left.parent = y;
    }

    size--;
    modCount++;
  }

  /**
   * Successor O(h)
   *
   * The structure of the binary search tree allows us to determine the successor
   * without ever comparing keys. The procedure returns x if it exists and NIL if
   * x is the largest key in the tree.
   *
   * Tree-Successor(x)
   * 1   if x.right != NIL
   * 2       return Tree-Minimum(x.right)
   * 3   y = x.p
   * 4   while y != NIL and x == y.right
   * 5       x = y
   * 6       y = y.p
   * 7   return y
   */

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode successor(TreeNode node) {
    checkNode(node);
    checkType(node);

    if (node.right != null)
      return (TreeNode) _minimum(node.right);

    TreeNode y = (TreeNode) node.parent;

    while (y != null && node.equals(y.right)) {
      node = y;
      y = (TreeNode) y.parent;
    }

    return y;
  }

  /**
   * Predecessor O(h)
   *
   * The structure of the binary search tree allows us to determine the predecessor
   * without ever comparing keys. The procedure returns x if it exists and NIL if
   * x is the smallest key in the tree.
   *
   * Tree-Predecessor(x)
   * 1   if x.left != NIL
   * 2       return Tree-Maximum(x.right)
   * 3   y = x.p
   * 4   while y != NIL and x == y.right
   * 5       x = y
   * 6       y = y.p
   * 7   return y
   */

  /**
   * {@inheritDoc}
   *
   * @throws NullPointerException     {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <TreeNode extends Node<K, V>> TreeNode predecessor(TreeNode node) {
    checkNode(node);
    checkType(node);

    if (node.left != null)
      return (TreeNode) _maximum(node.left);

    TreeNode y = (TreeNode) node.parent;

    while (y != null && node.equals(y.left)) {
      node = y;
      y = (TreeNode) y.parent;
    }

    return y;
  }

}