
import java.util.ArrayList;

public class InterestHashTable {

	private int numElements;
	private ArrayList<List<Interest>> Table;

	/**
	 * Constructor for the hash table. Initializes the Table to be sized according
	 * to value passed in as a parameter Inserts size empty Lists into the table.
	 * Sets numElements to 0
	 * 
	 * @param size the table size
	 */
	public InterestHashTable(int size) {
		Table = new ArrayList<List<Interest>>();
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
	private int hash(String interest) {
		String key = interest;
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

	/**
	 * Accesses a specified key in the Table
	 * 
	 * @param t the key to search for
	 * @return the value to which the specified key is mapped, or null if this table
	 *         contains no mapping for the key.
	 * @precondition t != null
	 * @throws NullPointerException if the specified key is null
	 */
	public Interest get(String interest) throws NullPointerException {
		if (interest == null) {
			throw new NullPointerException("get(): " + "Cannot get a null object!");
		} else {
			int bucket = hash(interest);
			Table.get(bucket).placeIterator();
			for (int i = 0; i < Table.get(bucket).getLength(); i++) {
				if (Table.get(bucket).getIterator().equals(new Interest(interest))) {
					return Table.get(bucket).getIterator();
				}
				Table.get(bucket).advanceIterator();
			}
		}
		return null;
	}

	/**
	 * Determines whether a specified key is in the Table
	 * 
	 * @param t the key to search for
	 * @return whether the key is in the Table
	 * @precondition t != null
	 * @throws NullPointerException if the specified key is null
	 */
	public boolean contains(String interest) throws NullPointerException {
		if (interest == null) {
			throw new NullPointerException("contains(): " + "Cannot check for a null object!");
		} else {
			int bucket = hash(interest);
			Table.get(bucket).placeIterator();
			for (int i = 0; i < Table.get(bucket).getLength(); i++) {
				if (Table.get(bucket).getIterator().equals(new Interest(interest))) {
					return true;
				}
				Table.get(bucket).advanceIterator();
			}
		}
		return false;
	}

	/** Mutators */

	/**
	 * Inserts a new element in the Table at the end of the chain in the bucket to
	 * which the key is mapped
	 * 
	 * @param t the key to insert
	 * @precondition t != null
	 * @throws NullPointerException for a null key
	 */
	public Interest put(String interest) throws NullPointerException {
		if (interest == null) {
			throw new NullPointerException("put(): " + "Cannot add a null object!");
		} else {
			int bucket = hash(interest);
			Interest interestObj;
			if(Table.get(bucket).isEmpty()) {
				interestObj = new Interest(interest, numElements);
				Table.get(bucket).addLast(interestObj);
				numElements++;
				return interestObj;
			} else {
				Table.get(bucket).placeIterator();
				for (int i = 0; i < Table.get(bucket).getLength(); i++) {
					if (Table.get(bucket).getIterator().equals(new Interest(interest))) {
						return Table.get(bucket).getIterator();
					}
					Table.get(bucket).advanceIterator();
				}
				return null;
			}
		}
	}

	/**
	 * removes the key t from the Table calls the hash method on the key to
	 * determine correct placement has no effect if t is not in the Table or for a
	 * null argument
	 * 
	 * @param t the key to remove
	 * @throws NullPointerException if the key is null
	 */
	public void remove(Interest interest) throws NullPointerException {
		if (interest == null) {
			throw new NullPointerException("remove(): " + "Cannot remove a null object!");
		} else {
			int bucket = hash(interest.getInterest());
			Table.get(bucket).placeIterator();
			while (!Table.get(bucket).offEnd()) {
				if (Table.get(bucket).getIterator().equals(interest)) {
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
		Table = new ArrayList<List<Interest>>();
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