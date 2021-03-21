
import java.util.NoSuchElementException;

public class Queue<T extends Comparable<T>> {
	private class Node {
		private T data;
		private Node next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}
	}

	private int length;
	private Node front;
	private Node end;

	/**** CONSTRUCTORS ****/

	/**
	 * Default constructor for the Queue class
	 * 
	 * @postcondition a new Queue object with all fields assigned default values
	 */
	public Queue() {
		length = 0;
		front = end = null;
	}

	/**
	 * Copy constructor for the Queue class
	 * 
	 * @param original the Queue to copy
	 * @postcondition a new Queue object which is an identical, but distinct, copy
	 *                of original
	 */
	public Queue(Queue<T> original) {
		if (original == null) {
			return;
		}
		if (original.length == 0) {
			length = 0;
			front = end = null;
		} else {
			Node N = original.front;
			while (N != null) {
				enqueue(N.data);
				N = N.next;
			}
		}
	}

	/**** ACCESSORS ****/

	/**
	 * Returns the value stored at the front of the Queue
	 * 
	 * @return the value at the front of the queue
	 * @precondition !isEmpty()
	 * @throws NoSuchElementException when the precondition is violated
	 */
	public T getFront() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("getFront(): " + "The list is empty, cannot get front!");
		}
		return front.data;
	}

	/**
	 * Returns the length of the Queue
	 * 
	 * @return the length from 0 to n
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Determines whether a Queue is empty
	 * 
	 * @return whether the Queue is empty
	 */
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * Determines whether two Queues contain the same values in the same order
	 * 
	 * @param o the Object to compare to this
	 * @return whether o and this are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Queue)) {
			return false;
		} else {
			Queue<T> Q = (Queue<T>) o;
			if (this.length != Q.length) {
				return false;
			} else {
				Node temp1 = this.front;
				Node temp2 = Q.front;
				while (temp1 != null) {
					if (!(temp1.data.equals(temp2.data))) {
						return false;
					}
					temp1 = temp1.next;
					temp2 = temp2.next;
				}
				return true;
			}
		}
	}

	/**
	 * Determines whether data is sorted in ascending order by calling its recursive
	 * helper method isSorted() Note: when length == 0 data is (trivially) sorted
	 * 
	 * @return whether the data is sorted
	 */
	public boolean isSorted() {
		return isSorted(front);
	}

	/**
	 * Helper method to isSorted Recursively determines whether data is sorted
	 * 
	 * @param node the current node
	 * @return whether the data is sorted
	 */
	private boolean isSorted(Node node) {
		if (node == null || node.next == null) {
			return true;
		}
		if (node.data.compareTo(node.next.data) >= 1) {
			return false;
		}
		return isSorted(node.next);
	}

	/**
	 * Uses the iterative linear search algorithm to locate a specific element and
	 * return its position
	 * 
	 * @param element the value to search for
	 * @return the location of value from 1 to length Note that in the case
	 *         length==0 the element is considered not found
	 */
	public int linearSearch(T element) {
		int position = 1;
		if (length > 0) {
			Node iterator = front;
			while (iterator != null) {
				if (iterator.data.compareTo(element) == 0) {
					return position;
				}
				iterator = iterator.next;
				position++;
			}
		}
		return -1;
	}

	/**
	 * Returns the location from 1 to length where value is located by calling the
	 * private helper method binarySearch
	 * 
	 * @param value the value to search for
	 * @return the location where value is stored from 1 to length, or -1 to
	 *         indicate not found
	 * @precondition isSorted()
	 * @throws IllegalStateException when the precondition is violated.
	 */
	public int binarySearch(T value) throws IllegalStateException {
		if (!isSorted()) {
			throw new IllegalStateException("binarySearch(): " + "Queue is not sorted, cannot binary search!");
		}

		return binarySearch(1, length, value);
	}

	/**
	 * Searches for the specified value in by implementing the recursive
	 * binarySearch algorithm
	 * 
	 * @param low   the lowest bounds of the search
	 * @param high  the highest bounds of the search
	 * @param value the value to search for
	 * @return the location at which value is located from 1 to length or -1 to
	 *         indicate not found
	 */
	private int binarySearch(int low, int high, T value) {
		if (high < low) {
			return -1;
		}
		int mid = low + (high - low) / 2;
		Node iterator = front;
		int index = 1;

		while (index < mid) {
			iterator = iterator.next;
			index++;
		}
		if (iterator.data.compareTo(value) == 0) {
			return mid;
		} else if (iterator.data.compareTo(value) <= -1) {
			return binarySearch(mid + 1, high, value);
		} else {
			return binarySearch(low, mid - 1, value);
		}
	}

	/**** MUTATORS ****/

	/**
	 * Inserts a new value at the end of the Queue
	 * 
	 * @param data the new data to insert
	 * @postcondition a new node at the end of the Queue
	 */
	public void enqueue(T data) {
		if (length == 0) { // edge case
			front = end = new Node(data);
		} else { // general case
			end.next = new Node(data);
			end = end.next;
		}
		length++;
	}

	/**
	 * Removes the front element in the Queue
	 * 
	 * @precondition !isEmpty()
	 * @throws NoSuchElementException when the precondition is violated
	 * @postcondition the front element has been removed
	 */
	public void dequeue() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("dequeue(): " + "The list is empty, cannot dequeue!");
		}
		front = front.next;
		length--;
	}

	/**** ADDITONAL OPERATIONS ****/

	/**
	 * Prints in reverse order to the console, followed by a new line by calling the
	 * recursive helper method printReverse
	 */
	public void printReverse() {
		if(!isEmpty()) {
			printReverse(front);
			System.out.println();			
		}
	}

	/**
	 * Recursively prints to the console the data in reverse order (no loops)
	 * 
	 * @param node the current node
	 */

	private void printReverse(Node node) {
		if (node.next != null) {
			printReverse(node.next);
		}
		System.out.print(node.data + " ");
	}

	/**
	 * Returns the values stored in the Queue as a String, separated by a blank
	 * space with a new line character at the end
	 * 
	 * @return a String of Queue values
	 */
	@Override
	public String toString() {
		String result = "";
		Node N = front;
		while (N != null) {
			result += N.data + " ";
			N = N.next;
		}
		return result += "\n";
	}

}