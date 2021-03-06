package algorithms.dynamic;

/**
 * 0-1 Knapsack problem - modified to allow items to be reused
 * 
 * <p>
 * Runs in {@code O(n^2 W)} time, where {@code n} is the number of items and
 * {@code W} is the capacity
 * </p>
 * 
 * <p>
 * This maximizes the value of items in a fixed capacity with whole items only.
 * If fractional parts of an item are acceptable, the greedy variation is
 * preferred.
 * </p>
 * 
 * <p>
 * The initialized matrix is {@code numItems + 1} by {@code capacity + 1}. The
 * additional row and column is to initialize the {@code 0th} column and row to
 * zeroes to be able to calculate the previous and current item.
 * </p>
 */
public final class KnapsackMultiple {
  private static int TOTAL = 0;
  // private static int ITEMS = 1;

  @SuppressWarnings("unchecked")
  private static <T> T run(int type, int capacity, int[] weights, int[] values) {
    if (weights.length != values.length)
      throw new IllegalArgumentException("Weights and Values array lengths don't match.");

    int n = weights.length;
    int[][] K = new int[n+1][capacity+1]; // Initialize the matrix (n+1 X capacity+1)
    int a, b, i, j, k;

    for (k = 1; k <= n; k++) {
      if (weights[k-1] > capacity)
        break;
        
      for (j = 1; j <= capacity; j++) {
        for (i = 1; i <= k; i++) {
          // If current capacity is less than item weight
          if (j < weights[i-1])
            K[i][j] = K[i-1][j];  // Set table position with corresponding item/weight
          else {  
            a = K[k-1][j];                              // Previous item value
            b = K[k-1][j - weights[i-1]] + values[i-1]; // Current item value plus the previous item value
            K[i][j] = a > b ? a : b;                    // Set the position with the highest value
          }
        }
      }
    }

    // TODO: need to figure out how to get the items indices
    return (T) Integer.valueOf(K[k-1][capacity]);
  }

  public static int total(int capacity, int[] weights, int[] values) {
    return run(TOTAL, capacity, weights, values);
  }

  // public static int[] items(int capacity, int[] weights, int[] values) {
  //   return run(capacity, weights, values);
  // }

}