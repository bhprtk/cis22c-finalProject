
public class Login {
	private String username;
	private String password;
	
	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	public int hashCode() {
		String key = username + password;
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
		} else if (!(o instanceof Login)) {
			return false;
		} else {
			Login c = (Login) o;
			if (this.username.equals(c.username) && this.password.equals(c.password)) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		String result = "";
		result += "username: " + username + "\n";
		result += "password: " + password + "\n";
		return result;
	}
}
