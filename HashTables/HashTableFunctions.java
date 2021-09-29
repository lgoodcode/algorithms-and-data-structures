package Data_Structures.HashTables;

class InvalidPrimeNumberException extends Exception {
  int num;

  <T extends Number>InvalidPrimeNumberException(T p) { num = p.intValue(); }

  public String toString() {
    return "\nNumber: " + num + " is not a valid prime number";
  }
}

public final class HashTableFunctions {
  /**
   * Given a valid {@code Number} type, determines if it is a prime number.
   * 
   * @param <T> {@code Number}
   * @param p The number to check if prime or not
   * @return Is number prime
   */
  public static <T extends Number> boolean isPrime(T p) {
    int x = p.intValue();

    if (x * 0 != 0)
      return false;
  
    if (x <= 3) 
      return x > 1;
  
    if (x % 2 == 0 || x % 3 == 0) 
      return false;
  
    for (int i=5; i * i <= x; i += 6) {
      if (x % i == 0 || x % (i + 2) == 0) 
        return false;    
    }
    return true;
  }

  /**
   * Creates values that will be injective for the given set, table size, and prime number
   * 
   * @param <T> {@code Number}
   * @param S The set or list of elements to create the hash function for
   * @param m The maximal size of the subtable
   * @param p The prime number of the hash table T 
   * @throws InvalidPrimeNumberException when given an invalid prime number
   * @return [a, b] int constants for table hash function
   */
  public static <T extends Number> int[] injectiveIntegers(T[] S, T m, T p) 
    throws InvalidPrimeNumberException {
    if (HashTableFunctions.isPrime(p) == false) {
      throw new InvalidPrimeNumberException(p);
    }

    int[] used = new int[100];
    int a = (int) (Math.random() * p.intValue() - 2) + 1;
    int b = S.length;
    int used_idx = 0;
    int hash;

    // Iterate through every value in the set
    for (T k : S) {
      // Get the hash value
      hash = ((a * k.intValue() + b) % p.intValue() % m.intValue());

      if (used_idx == 0) {
        used[used_idx++] = hash;
      }
      else {
        // Determine whether the hash has already been used
        for (int i=0; i<used_idx; ++i) {
          if (hash == used[i]) {
            // If used, the constants doesn't give us a good hash function try again
            return HashTableFunctions.injectiveIntegers(S, m, p);
          }
          else {
            // Otherwise, add used hash function to used array and continue to next set item
            used[used_idx++] = hash;
            break;
          }
        }
      }
    }

    int[] valid = {a, b};

    return valid;
  }
}

class HashTableFunctionsDemo {
  public static void main(String[] args) {
    boolean prime = HashTableFunctions.isPrime(1277);
    boolean prime2 = HashTableFunctions.isPrime(334);

    System.out.println("Is 1277 prime: " + prime);
    System.out.println("Is 334 prime: " + prime2);

    Integer[] set = { 10, 22, 37, 40, 52, 60, 70, 72, 75 };

    try {
      int[] constants = HashTableFunctions.injectiveIntegers(set, 100, 1277);
      System.out.println("Injective constants for set: ");

      for (int x : constants) System.out.println(x);
      
    } catch (InvalidPrimeNumberException err) {
      System.out.println(err);
    }
  }
}