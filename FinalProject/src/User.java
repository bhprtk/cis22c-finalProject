
public class User {
	private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String city;
    private int userid;
    private BST<User> friends;
    private List<Interest> interests; //recommended to create an Interest class
    
    public User() {
    	this.firstName = "";
    	this.lastName = "";
    	this.userName = "";
    	this.password = "";
    	this.city = "";
    }
    
    public User(String userName, String password) {
    	this.userName = userName;
    	this.password = password;
    }
    
    public User(String userName, String password, String firstName, String lastName, String city) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.userName = userName;
    	this.password = password;
    	this.city = city;
    }
    
    public void setUserName(String userName) {
    	this.userName = userName;
    }
    
    public void setPassword(String password) {
    	this.password = userName;
    }
    
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
    	this.lastName = lastName;
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
    
    public String getFirstName() {
    	return firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }
    
    public String getCity() {
    	return city;
    }
    
    
    @Override
	public int hashCode() {
		String key = firstName + lastName;
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
			if (this.firstName.equals(c.firstName) && this.lastName.equals(c.lastName)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "Name: " + firstName + " " + lastName + "\n";
		result += "username: " + userName + "\n";
		result += "City: " + city + "\n";

		return result;
	}
	
}
