package data_structures.queues.exceptions;

public final class QueueEmptyException extends Exception {
  public QueueEmptyException() { super(); }

  public String toString() {
    return "\nQueue is empty.";
  }
}
