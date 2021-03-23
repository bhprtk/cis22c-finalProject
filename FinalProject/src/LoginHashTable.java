
/**
 * LoginHashTable.java
 * @author Pratik Bhandari
 * @author Naqib Khan
 * @author Young Jin Kim
 * @author Jafer Zaidi
 * @author Hanxiao Wang
 * @author Ogbe Airiodion
 * CIS 22C, Final Project
 */
import java.util.ArrayList;

public class LoginHashTable {

	private int numElements;
	private ArrayList<List<String>> Table;

	/**
	 * Constructor for the hash table. Initializes the Table to be sized according
	 * to value passed in as a parameter Inserts size empty Lists into the table.
	 * Sets numElements to 0
	 * 
	 * @param size the table size
	 */
	public LoginHashTable(int size) {
		Table = new ArrayList<List<String>>();
		for (int i = 0; i < size; i++) {
			Table.add(new List<>());
		}
		numElements = 0;

	}

	/** Accessors */

	/**
	 * returns the hash value in the Table for a given Object
	 * 
	 * @param t the Object
	 * @return the index in the Table
	 */
	private int hash(String key) {
		int code = 0;
		for (int i = 0; i < key.length(); i++) {
			code += (int) key.charAt(i);
		}
		return (code % Table.size());
	}

	/**
	 * counts the number of keys at this index
	 * 
	 * @param index the index in the Table
	 * @precondition 0 <= index < Table.length
	 * @return the count of keys at this index
	 * @throws IndexOutOfBoundsException
	 */
	public int countBucket(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= Table.size()) {
			throw new IndexOutOfBoundsException("countBucket(): " + "Index out of bounds!");
		}
		return Table.get(index).getLength();
	}

	/**
	 * returns total number of keys in the Table
	 * 
	 * @return total number of keys
	 */
	public int getNumElements() {
		return numElements;
	}

	
	public String get(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException("get(): " + "Cannot get a null object!");
		} else {
			int bucket = hash(key);
			Table.get(bucket).placeIterator();
			for (int i = 0; i < Table.get(bucket).getLength(); i++) {
				if (!Table.get(bucket).getIterator().isEmpty()) {
					return Table.get(bucket).getIterator();
				}
				Table.get(bucket).advanceIterator();
			}
		}
		return null;
	}


	public boolean contains(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException("contains(): " + "Cannot check for a null object!");
		} else {
			int bucket = hash(key);
			Table.get(bucket).placeIterator();
			for (int i = 0; i < Table.get(bucket).getLength(); i++) {
				if (!Table.get(bucket).getIterator().isEmpty()) {
					return true;
				}
				Table.get(bucket).advanceIterator();
			}
		}
		return false;
	}


	public void put(String key, String fullName) throws NullPointerException {
		if (key == null || fullName == null) {
			throw new NullPointerException("put(): " + "Cannot add a null object!");
		} else {
			int bucket = hash(key);
			Table.get(bucket).addLast(fullName);
			numElements++;
		}
	}

	
	public void remove(String key, String fullName) throws NullPointerException {
		if (key == null || fullName == null) {
			throw new NullPointerException("remove(): " + "Cannot remove a null object!");
		} else {
			int bucket = hash(key);
			Table.get(bucket).placeIterator();
			while (!Table.get(bucket).offEnd()) {
				if (Table.get(bucket).getIterator().compareTo(fullName) == 0) {
					Table.get(bucket).removeIterator();
					numElements--;
					return;
				}
				Table.get(bucket).advanceIterator();
			}
		}
	}

	/**
	 * Clears this hash table so that it contains no keys.
	 */
	public void clear() {
		int size = Table.size();
		Table.clear();
		Table = new ArrayList<List<String>>();
		for (int i = 0; i < size; i++) {
			Table.add(new List<>());
		}
	}

	/** Additional Methods */

	/**
	 * Prints all the keys at a specified bucket in the Table. Each key displayed on
	 * its own line, with a blank line separating each key Above the keys, prints
	 * the message "Printing bucket #<bucket>:" Note that there is no <> in the
	 * output
	 * 
	 * @param bucket the index in the Table
	 */
	public void printBucket(int bucket) {
		System.out.println("Printing bucket #" + bucket);
		System.out.println(Table.get(bucket));
	}

	/**
	 * Prints the first key at each bucket along with a count of the total keys with
	 * the message "+ <count> -1 more at this bucket." Each bucket separated with
	 * two blank lines. When the bucket is empty, prints the message "This bucket is
	 * empty." followed by two blank lines
	 */
	public void printTable() {
		for (int i = 0; i < Table.size(); i++) {
			if (Table.get(i).isEmpty()) {
				System.out.println("This bucket is empty.\n\n");
			} else {
				System.out.println(
						Table.get(i).getFirst() + " + " + (Table.get(i).getLength() - 1) + " more at this bucket.\n\n");
			}
		}
	}

	/**
	 * Starting at the first bucket, and continuing in order until the last bucket,
	 * concatenates all elements at all buckets into one String
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < Table.size(); i++) {
			result += Table.get(i);
		}
		return result;
	}

}