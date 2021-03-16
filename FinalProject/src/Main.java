import java.io.*;
import java.util.Scanner;

public class Main {
	
	private static void login(Scanner input, HashTable<User> users, HashTable<Login> login, User currentUser) {
		String username, password;
		String firstname, lastname, city;
		System.out.print("\nPlease enter your username: ");
		username = input.nextLine();
		System.out.print("\nPlease enter your password: ");
		password = input.nextLine();
		
		if(login.contains(new Login(username, password))) {
			
			System.out.println("username: " + username);
			System.out.println("password: " + password);
		} else {
			System.out.println("\nSeem's like you are not in our system!");
			System.out.println("Let's create a new account for you\n");
			
			System.out.print("Enter your first name: ");
			firstname = input.nextLine();
			System.out.print("Enter your last name: ");
			lastname = input.nextLine();
			System.out.print("Enter which city you're from: ");
			city = input.nextLine();
			
			currentUser.setUserName(username);
			currentUser.setPassword(password);
			currentUser.setFirstName(firstname);
			currentUser.setLastName(lastname);
			currentUser.setCity(city);
			
			login.put(new Login(username, password));
			users.put(currentUser);
			
			addUserToFile(currentUser);
		}
		
	}
	
	
	public static void addUserToFile(User currentUser) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File("users.txt"), true));
			writer.println();
			writer.println(currentUser.getUserName());
			writer.println(currentUser.getPassword());
			writer.println(currentUser.getFirstName());
			writer.println(currentUser.getLastName());
			writer.println(currentUser.getCity());
			
			writer.close();
		} catch (IOException e) {
			System.out.println("\nError opening file: " + e.getMessage());
		}
	}
	
	public static void loadUsers(HashTable<User> users, HashTable<Login> login) {
		String username, password, firstname, lastname, city;
		Scanner input;
		try {
			input = new Scanner(new File("users.txt"));
			while(input.hasNextLine()) {
				username = input.nextLine();
				password = input.nextLine();
				firstname = input.nextLine();
				lastname = input.nextLine();
				city = input.nextLine();
				users.put(new User(username, password, firstname, lastname, city));
				login.put(new Login(username, password));
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("\nError reading file: " + e.getMessage());
		}
	}
	
	
	public static void main(String[] args) {
		HashTable<Login> login = new HashTable<>(100);
		HashTable<User> users = new HashTable<>(100);
		User currentUser = new User();
		Scanner input;
		String option;
		
		loadUsers(users, login);
		
		System.out.println("Welcome to FRIENDS!\n");
		
		input = new Scanner(System.in);

		
		while(true) {
			System.out.println("(A) Login ");
			System.out.println("(B) View My Friends ");
			System.out.println("(C) Search for a New Friend ");
			System.out.println("(D) Get Friend Recommendations");
			System.out.println("(Q) Quit ");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			
			if(option.compareTo("A") == 0) {
				login(input, users, login, currentUser);
			} else if(option.compareTo("B") == 0) {
				
			} else if(option.compareTo("C") == 0) {
				
			} else if(option.compareTo("D") == 0) {
				
			} else if(option.compareTo("Q") == 0) {
				System.out.println("\nSaving...");
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}		
		}
	}
}
