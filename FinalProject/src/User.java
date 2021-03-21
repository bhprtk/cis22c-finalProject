import java.util.ArrayList;
import java.util.Comparator;

public class User {
	private String fullName;
    private String userName;
    private String password;
    private String city;
    private int userId;
    private BST<User> friends;
    private List<Interest> interests; //recommended to create an Interest class
    
    public User() {
    	this.fullName = "";
    	this.userName = "";
    	this.password = "";
    	this.city = "";
    	this.friends = new BST<>();
    }
    
    public User(int userId, String userName, String password, String fullName, String city, List<Interest> interestList) {
    	this.fullName = fullName;
    	this.userName = userName;
    	this.password = password;
    	this.city = city;
    	this.userId = userId;
    	this.friends = new BST<>();
    	this.interests = new List<Interest>(interestList);
    }
    
    public User(String userName, String password) {
    	this.userName = userName;
    	this.password = password;
    }
    
    public User(String fullName) {
    	this.fullName = fullName;
//    	this.friends = new BST<>();
    }
    
    public User(String userName, String password, String fullName) {
    	this.userName = userName;
    	this.password = password;
    	this.fullName = fullName;
    }
    
    public void setUserId(int userId) {
    	this.userId = userId;
    }
 
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    public void setPassword(String password) {
    	this.password = userName;
    }
    
    public void setFullName(String fullName) {
    	this.fullName = fullName;
    }
    
    
    public void setCity(String city) {
    	this.city = city;
    }
    
    public String getUserName() {
    	return userName;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public String getFullName() {
    	return fullName;
    }
    
    public int getUserId() {
    	return userId;
    }
    
    
    public ArrayList<User> getFriends() {
    	return friends.dataInOrder();
    }
    
    public List<Interest> getInterestList() {
    	return interests;
    }
    
    public void setInterestList(List<Interest> interests) {
    	this.interests = new List<Interest>(interests);
    }
    
    public boolean isFriendsWith(User user) {
    	if(friends.search(user, new NameComparator()) != null) {
    		return true;
    	}
    	return false;
    }
    
    public String getCity() {
    	return city;
    }
    
    public void printFriends() {
    	friends.inOrderPrint();
    }
    
    public User getFriend(User friend) {
    	return friends.search(friend, new NameComparator());
    }
    
    public void addFriend(User friend) {
    	friends.insert(friend, new NameComparator());
    }
    
    public void removeFriend(User friend) {
    	friends.remove(friend, new NameComparator());
    }
    
    public void printProfile() {
    	System.out.println("\n--------------------------------------");
    	System.out.println("\nName: " + fullName);
    	System.out.println("City: " + city);
    	System.out.println("\nFriends: ");
    	friends.inOrderPrint();
    	System.out.println("\nInterests: ");
    	System.out.println(interests);
    	System.out.println("\n--------------------------------------");
    }
    
    @Override
	public int hashCode() {
		String key = fullName;
		int sum = 0;
		for (int i = 0; i < key.length(); i++) {
			sum += (int) key.charAt(i);
		}
		return sum;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof User)) {
			return false;
		} else {
			User c = (User) o;
			if (this.fullName.equals(c.fullName)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String result = "\n";
		result += fullName + "\n";
		result += city + "\n";
		return result;
	}
	
}


class NameComparator implements Comparator<User> {
	/**
	 * Compares the two mutual fund accounts by name of the fund uses the String
	 * compareTo method to make the comparison
	 * 
	 * @param account1 the first MutualFundAccount
	 * @param account2 the second MutualFundAccount
	 */
	@Override
	public int compare(User account1, User account2) {
		return account1.getFullName().compareTo(account2.getFullName());
	}
} // end class NameComparator

