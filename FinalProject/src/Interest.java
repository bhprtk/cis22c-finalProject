
/**
 * Interest.java
 * 
 * @author Pratik Bhandari
 * @author Naqib Khan
 * @author Young Jin Kim
 * @author Jafer Zaidi
 * @author Hanxiao Wang
 * @author Ogbe Airiodion CIS 22C, Final Project
 */
public class Interest {
	private String interest;
	private int interestId;

	public Interest(String interest, int interestId) {
		this.interest = interest;
		this.interestId = interestId;
	}

	public Interest(String interest) {
		this.interest = interest;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public int getInterestId() {
		return interestId;
	}

	public void setInterestId(int interestId) {
		this.interestId = interestId;
	}

	/**
	 * Compares this Interest to another Object for equality
	 * 
	 * @param o another Object
	 * @return whether o is an Interest and has the same interest as this Interest
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Interest)) {
			return false;
		} else {
			Interest t = (Interest) o;
			if (this.interest.compareTo(t.interest) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts the class into a string
	 */
	@Override
	public String toString() {
		String result = "";
		result += interest + " ";
		result += "\n";
		return result;
	}
}
