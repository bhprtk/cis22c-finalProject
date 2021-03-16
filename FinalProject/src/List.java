
/**
 * Defines a doubly-linked list class
 * @author Naqib Khan
 * @author Pratik Bhandari
 */

import java.util.NoSuchElementException;

public class List<T> {
	private class Node {
		private T data;
		private Node next;
		private Node prev;

		public Node(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}

	private int length;
	private Node first;
	private Node last;
	private Node iterator;

	/**** CONSTRUCTOR ****/

	/**
	 * Instantiates a new List with default values
	 * 
	 * @postcondition A new list with default values is created
	 */
	public List() {
		first = null;
		last = null;
		length = 0;
		iterator = null;
	}

	/**** COPY CONSTRUCTOR ****/

	/**
	 * Creates a new list which is the copy of the original List
	 * 
	 * @param original the List to be copied
	 * @postcondition A new list which is the copy of the original List is created
	 */
	public List(List<T> original) {
		if (original == null) {
			return;
		}
		if (original.length == 0) {
			length = 0;
			first = last = iterator = null;
		} else {
			Node temp = original.first;
			while (temp != null) {
				addLast(temp.data);
				temp = temp.next;
			}
			iterator = null;
		}
	}

	/**** ACCESSORS ****/

	/**
	 * Returns the value stored in the first node
	 * 
	 * @precondition length != 0
	 * @return the value stored at node first
	 * @throws NoSuchElementException when precondition is violated
	 */
	public T getFirst() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("getFirst: " + "List is Empty. No data to access!");
		}
		return first.data;
	}

	/**
	 * Returns the value stored in the last node
	 * 
	 * @precondition length 1= 0
	 * @return the value stored in the node last
	 * @throws NoSuchElementException when precondition is violated
	 */
	public T getLast() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("getLast: " + "List is Empty. No data to access!");
		}
		return last.data;
	}

	/**
	 * Returns the current length of the list
	 * 
	 * @return the length of the list from 0 to n
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns whether the list is currently empty
	 * 
	 * @return whether the list is empty
	 */
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * Returns the data stored in the node iterator is currently pointing to
	 * 
	 * @precondition iterator != null
	 * @return whether the list is empty
	 * @throws NullPointerException if the precondition is violated
	 */
	public T getIterator() throws NullPointerException {
		if (iterator == null) {
			throw new NullPointerException("getIterator: " + "Iterator cannot be null.");
		} else {
			return iterator.data;
		}
	}

	/*
	 * Returns whether the iterator is off the end of the list, i.e. is NULL
	 */
	public boolean offEnd() {
		return iterator == null;
	}

	/**
	 * Determines whether two Lists have the same data in the same order
	 * 
	 * @param L the List to compare to this List
	 * @return whether the two Lists are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof List)) {
			return false;
		} else {
			List<T> L = (List<T>) o;
			if (this.length != L.length) {
				return false;
			} else {
				Node temp1 = this.first;
				Node temp2 = L.first;
				while (temp1 != null) { // Lists are same length
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
	 * Searches the List for the specified value using the linear search algorithm
	 * 
	 * @param value the value to search for
	 * @return the location of value in the List or -1 to indicate not found Note
	 *         that if the List is empty we will consider the element to be not
	 *         found post: position of the iterator remains unchanged
	 */
	public int linearSearch(T value) {
		int pos = 1;
		if (isEmpty()) {
			return -1;
		} else {
			Node temp = first;
			while (temp != null) {
				if (temp.data == value) {
					return pos;
				}
				pos++;
				temp = temp.next;
			}
		}
		return -1;
	}

	/**** MUTATORS ****/

	/**
	 * Creates a new first element
	 * 
	 * @param data the data to insert at the front of the list
	 * @postcondition adds a new item to the list
	 */
	public void addFirst(T data) {
		if (first == null) {
			first = last = new Node(data);
		} else {
			Node N = new Node(data);
			N.next = first;
			first.prev = N;
			first = N;
		}
		length++;
	}

	/**
	 * Creates a new last element
	 * 
	 * @param data the data to insert at the end of the list
	 * @postcondition adds a new to to the end of the list
	 */
	public void addLast(T data) {
		if (first == null) {
			first = last = new Node(data);
		} else {
			Node N = new Node(data);
			last.next = N;
			N.prev = last;
			last = N;
		}
		length++;
	}

	/**
	 * Removes the element at the front of the list
	 * 
	 * @precondition length != 0
	 * @postcondition The first element form the list is removed
	 * @throws NoSuchElementException when precondition is violated
	 */
	public void removeFirst() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("removeFirst(): " + "Cannot remove from an empty List!");
		} else if (length == 1) {
			// Edge case length == 1
			first = last = iterator = null;

		} else {
			if (iterator == first) { // edge case
				iterator = null;
			}
			// General Case
			first = first.next;
			first.prev = null;
		}
		length--;
	}

	/**
	 * Removes the element at the end of the list
	 * 
	 * @precondition length != 0
	 * @postcondition The last element of the list is removed
	 * @throws NoSuchElementException when precondition is violated
	 */
	public void removeLast() throws NoSuchElementException {
		if (length == 0) {
			throw new NoSuchElementException("removeLast: " + "list is empty. Nothing to remove.");
		} else if (length == 1) {
			// Edge case length == 1
			first = last = iterator = null;
		} else {
			if (iterator == last) { // edge case
				iterator = null;
			}
			// General case
			last = last.prev;
			last.next = null;
		}
		length--;
	}

	/**
	 * Places the iterator at the beginning of the list
	 * 
	 * @postcondition The iterator will be placed at the beginning of the list
	 */
	public void placeIterator() {
		iterator = first;
	}

	/**
	 * removes the element currently referenced by the iterator
	 * 
	 * @precondition iterator != null
	 * @throws NullPointerException when iterator is off end
	 * @postcondition iterator will be null
	 */
	public void removeIterator() throws NullPointerException {
		if (iterator == null) { // precondition
			throw new NullPointerException("removeIterator: " + "iterator is off end.");
		} else if (iterator == first) { // edge case
			removeFirst();
		} else if (iterator == last) { // edge case
			removeLast();
		} else { // general case
			iterator.next.prev = iterator.prev;
			iterator.prev.next = iterator.next;
			iterator = null;
			length--;
		}

	}

	/**
	 * Inserts new data in the List after the iterator
	 * 
	 * @param data the new data to insert
	 * @precondition iterator != null
	 * @throws NullPointerException when the precondition is violated
	 * @postcondition A new node will be added after the iterator
	 */
	public void addIterator(T data) throws NullPointerException {
		if (iterator == null) {
			// Precondition
			throw new NullPointerException("addIterator:" + "Iterator is off end. Cannot add.");
		} else if (iterator == last) {
			// Edge case
			this.addLast(data);
		} else {
			// General case
			Node n = new Node(data);
			n.next = iterator.next;
			n.prev = iterator;
			iterator.next.prev = n;
			iterator.next = n;
			length++;
		}
	}

	/*
	 * Moves the iterator up by one node
	 * 
	 * @precondition iterator != null
	 * 
	 * @throws NullPointerException when the precondition is violated
	 * 
	 * @postcondition Iterator is moved up by one node
	 */
	public void advanceIterator() throws NullPointerException {
		if (iterator == null) {
			// Precondition
			throw new NullPointerException("advanceIterator: " + "Iterator is off end. Cannot advance.");
		} else {
			// General case
			iterator = iterator.next;
		}
	}

	/*
	 * Moves the iterator down by one node
	 * 
	 * @precondition iterator != null
	 * 
	 * @throws NullPointerException when the precondition is violated
	 * 
	 * @postcondition Iterator is moved down by one node
	 */
	public void reverseIterator() throws NullPointerException {
		if (iterator == null) {
			// Precondition
			throw new NullPointerException("reverseIterator: " + "Iterator is off end. Cannot reverse.");
		} else {
			// General case
			iterator = iterator.prev;
		}
	}

	/**
	 * Points the iterator at first and then advances it to the specified index
	 * 
	 * @param index the index where the iterator should be placed
	 * @precondition 0 < index <= length
	 * @throws IndexOutOfBoundsException when precondition is violated
	 */
	public void iteratorToIndex(int index) throws IndexOutOfBoundsException {
		if (index <= 0 || index > length) {
			throw new IndexOutOfBoundsException("iteratorToIndex: " + "Index is out of bounds!");
		}
		placeIterator();
		for (int i = 1; i < index; i++) {
			advanceIterator();
		}
	}

	/**** ADDITIONAL OPERATIONS ****/

	/**
	 * List with each value separated by a blank space At the end of the List a new
	 * line
	 * 
	 * @return the List as a String for display
	 */
	@Override
	public String toString() {
		String result = "";
		if (!isEmpty()) {
			Node temp = first;
			while (temp != null) {
				result += temp.data + "\n";
				temp = temp.next;
			}
			result += "\n";
		}
		return result;
	}

	/*
	 * Prints the contents of the linked list to the screen in the format #:
	 * <element> followed by a newline
	 * 
	 */
	public void printNumberedList() {
		int count = 1;
		Node temp = first;
		while (temp != null) {
			System.out.println(count + ". " + temp.data + "\n");
			count++;
			temp = temp.next;
		}

	}

}