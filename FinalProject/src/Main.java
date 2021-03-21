import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	private static User login(Scanner input, UserHashTable users, LoginHashTable login, InterestHashTable interestHT, ArrayList<BST<User>> interestUserAL) {
		String username, password;
		String fullName, city;
		String topic1, topic2;
		User currentUser = new User();
		System.out.print("\nPlease enter your username: ");
		username = input.nextLine();
		System.out.print("\nPlease enter your password: ");
		password = input.nextLine();
		if(login.contains(username + password)) {
			fullName = login.get(username + password);
			currentUser = users.get(fullName);
			System.out.println ("\nWelcome back, " + currentUser.getFullName() + "!\n");
		} else {
			System.out.println("\nSeem's like you are not in our system!");
			System.out.println("Let's create a new account for you\n");
			
			System.out.print("Enter your full name: ");
			fullName = input.nextLine();
			System.out.print("Enter which city you're from: ");
			city = input.nextLine();
			
			currentUser.setUserId(users.getNumElements());
			currentUser.setUserName(username);
			currentUser.setPassword(password);
			currentUser.setFullName(fullName);
			currentUser.setCity(city);
			
			List<Interest> interests = new List<>();
			for(int i = 0; i < 2; i++) {
				System.out.print("Enter a topic you're interested in: ");
				topic1 = input.nextLine();
				Interest currentInterest;
				if(!interestHT.contains(topic1)) {
					currentInterest = interestHT.put(topic1);
					
				} else {
					currentInterest = interestHT.get(topic1);
				}
				int interestIndex = currentInterest.getInterestId();
				if(interestIndex >= interestUserAL.size()) {
					interestUserAL.add(currentInterest.getInterestId(), new BST<User>());					
				}
				interestUserAL.get(currentInterest.getInterestId()).insert(currentUser, new NameComparator());
				interests.addLast(currentInterest);	
				
			}
			
			
			currentUser.setInterestList(interests);
			
			login.put(username + password, fullName);
			users.put(currentUser);
			
			addUserToFile(currentUser);
			
			System.out.println("\nWelcome to FRIENDS, " + fullName + "\n");
		}
		return currentUser;
		
	}
	
	
	public static void loadUsers(UserHashTable users, LoginHashTable login, InterestHashTable interestHT, ArrayList<BST<User>> interestUserAL, ArrayList<User> userList) {
		String username, password, fullName, city;
		ArrayList<List<String>> friendsList = new ArrayList<List<String>>();
		int uid;
		Scanner input;
		try {
			input = new Scanner(new File("users.txt"));
			while(input.hasNextLine()) {
				String userId = input.nextLine();
				uid = Integer.parseInt(userId);
				username = input.nextLine();
				password = input.nextLine();
				fullName = input.nextLine();
				city = input.nextLine();
				String nFriends = input.nextLine();
				List<String> l = new List<>();
				for(int i = 0; i < Integer.parseInt(nFriends); i++) {
					l.addLast(input.nextLine());
				}
				String nInterests = input.nextLine();
				List<Interest> interestList = new List<>();
				for(int i = 0; i < Integer.parseInt(nInterests); i++) {
					String interest = input.nextLine();
					Interest interestObj = interestHT.put(interest);
					interestList.addLast(interestObj);
				}
				input.nextLine();
				friendsList.add(l);
				User newUser = new User(uid, username, password, fullName, city, interestList); 
				users.put(newUser);
				userList.add(newUser);
				login.put(username + password, fullName);
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("\nError reading file: " + e.getMessage());
		}
		
		
		// Load Friends into BST
		for(int i = 0; i < userList.size(); i++) {
			List<Interest> tempInterestList = userList.get(i).getInterestList();
			tempInterestList.placeIterator();
			while(!tempInterestList.offEnd()) {
				Interest currentInterest = interestHT.get(tempInterestList.getIterator().getInterest());
				int interestIndex = currentInterest.getInterestId();
				if(interestIndex >= interestUserAL.size()) {
					interestUserAL.add(currentInterest.getInterestId(), new BST<User>());					
				}
				interestUserAL.get(currentInterest.getInterestId()).insert(userList.get(i), new NameComparator());
				tempInterestList.advanceIterator();					
			}
			List<String> tempList = friendsList.get(i);
			tempList.placeIterator();
			while(!tempList.offEnd()) {
				User friend = users.get(tempList.getIterator());
				userList.get(i).addFriend(friend);
				tempList.advanceIterator();
			}
		}
		
	}
	
	
	private static void viewMyFriends(Scanner input, User currentUser, UserHashTable users, Graph network) {
		System.out.println("\nHere is a list of your friends, " + currentUser.getFullName() + "\n");
		currentUser.printFriends();
		String option;
		String fullName;
		while(true) {
			System.out.println("\n(V) View a friend's profile");
			System.out.println("(R) Remove a friend");
			System.out.println("(B) Go back to the main menu");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			if(option.compareTo("V") == 0) {
				System.out.print("\nEnter the full name of your friend: ");
				fullName = input.nextLine();
				User friendProfile = currentUser.getFriend(new User(fullName));
				if(friendProfile == null) {
					System.out.println("\nYou don't have any friends with that name!");
				} else {
					friendProfile.printProfile();					
				}
			} else if(option.compareTo("R") == 0) {
				if(currentUser.getFriends().size() == 0) {
					System.out.println("\nYou don't have any friends to remove!");
				} else {
					System.out.print("\nEnter the full name of a friend: ");
					fullName = input.nextLine();
					User friendProfile = users.get(fullName);
					currentUser.removeFriend(friendProfile);
					friendProfile.removeFriend(currentUser);
					network.removeDirectedEdge(currentUser.getUserId(), friendProfile.getUserId());
					network.removeDirectedEdge(friendProfile.getUserId(), currentUser.getUserId());
					System.out.println("\nYou are no longer friends with " + friendProfile.getFullName() + "!");					
				}
			} else if(option.compareTo("B") == 0) {
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}			
		}
	}
	
	
	private static void addNewFriend(Scanner input, User currentUser, UserHashTable users, Graph network) {
		String fullName;
		System.out.print("\nEnter the full name of a person: ");
		fullName = input.nextLine();
		if(currentUser.getFullName().compareTo(fullName) == 0) {
			System.out.println("\nYou cannot add yourself as a friend!");
		} else {
			User person;
			if(users.contains(fullName)) {
				String choice;
				if(users.hasMultiple(fullName)) {
					System.out.println("\nThere are multiple people with this name:\n");
					List<User> multiple = users.getMultiple(fullName);
					multiple.printNumberedList();
					while(true) {
						System.out.print("\nEnter the number of the correct " + fullName + ": ");
						String correctName = input.nextLine();
						int correctNumber = Integer.parseInt(correctName);
						if(correctNumber > multiple.getLength()) {
							System.out.println("\nPlease input a valid option\n");
						} else {
							multiple.placeIterator();
							for(int i = 1; i < correctNumber; i++) {
								multiple.advanceIterator();
							}
							person = multiple.getIterator();
							break;
						}
					}	
				} else {
					person = users.get(fullName);
				}
				person.printProfile();							
				if(currentUser.isFriendsWith(person)) {
					System.out.println("\nYou are already friends with " + fullName + "!");
				} else {
					while(true) {
						System.out.print("\nWould you like to add " + fullName + " as a friend? (Y/N): ");
						choice = input.nextLine();
						if(choice.compareTo("Y") == 0) {
							currentUser.addFriend(person);
							person.addFriend(currentUser);
							network.addDirectedEdge(currentUser.getUserId(), person.getUserId());
							network.addDirectedEdge(person.getUserId(), currentUser.getUserId());
							System.out.println("\nYou are now friends with " + fullName);
							break;
						} else if(choice.compareTo("N") == 0) {
							break;
						} else {
							System.out.println("\nPlease input a valid option\n");								
						}
					}
				}
			} else {
				System.out.println("\nThe person is not in our system!");
			}
			
		}
	}
	
	private static void searchFriends(Scanner input, User currentUser, UserHashTable users, InterestHashTable interestHT, ArrayList<BST<User>> interestUserAL, Graph network) {
		String option;
		while(true) {
			System.out.println("\n(N) Search a friend by name");
			System.out.println("(I) Search a friend by interest");
			System.out.println("(B) Go back to the main menu");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			if(option.compareTo("N") == 0) {
				addNewFriend(input, currentUser, users, network);
			} else if (option.compareTo("I") == 0) {
				String interest;
				System.out.print("\nEnter an interest: ");
				interest = input.nextLine();
				Interest interestObj = interestHT.get(interest);
				if(interestObj == null) {
					System.out.println("\n" + interest + " is not listed as an interest in our system!");
				} else {
					System.out.println("\nThe following people are also interested in " + interest);
					interestUserAL.get(interestObj.getInterestId()).inOrderPrint();
					addNewFriend(input, currentUser, users, network);
				}
			} else if(option.compareTo("B") == 0) {
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}
		}
	}
	
	public static void addUserToFile(User currentUser) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File("users.txt"), true));
			writer.println();
			writer.println(currentUser.getUserId());
			writer.println(currentUser.getUserName());
			writer.println(currentUser.getPassword());
			writer.println(currentUser.getFullName());
			writer.println(currentUser.getCity());
			writer.println(0);
			writer.println(2);
			List<Interest> tempList = currentUser.getInterestList();
			tempList.placeIterator();
			while(!tempList.offEnd()) {
				writer.println(tempList.getIterator().getInterest());
				tempList.advanceIterator();
			}
			writer.print("--");
			writer.close();
		} catch (IOException e) {
			System.out.println("\nError opening file: " + e.getMessage());
		}
	}
	
	
	private static void saveData(ArrayList<User> userList) {
		try {
			PrintWriter writer = new PrintWriter(new File("users.txt"));
			for(int i = 0; i < userList.size(); i++) {
				User currentUser = userList.get(i);
				writer.println(currentUser.getUserId());
				writer.println(currentUser.getUserName());
				writer.println(currentUser.getPassword());
				writer.println(currentUser.getFullName());
				writer.println(currentUser.getCity());

				ArrayList<User> friends = currentUser.getFriends();
				writer.println(friends.size());
				for(int j = 0; j < friends.size(); j++) {
					writer.println(friends.get(j).getFullName());
				}

				List<Interest> tempList = currentUser.getInterestList();
				writer.println(tempList.getLength());
				tempList.placeIterator();
				while(!tempList.offEnd()) {
					writer.println(tempList.getIterator().getInterest());
					tempList.advanceIterator();
				}
				writer.println("--");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("\nError opening file: " + e.getMessage());
		}
	}
	
	
	private static void fillAdjList(Graph network, ArrayList<User> userList, UserHashTable users) {
		for(int i = 0; i < userList.size(); i++) {
			ArrayList<User> friends = userList.get(i).getFriends();
			for(int j = 0; j < friends.size(); j++) {
				network.addDirectedEdge(i, friends.get(j).getUserId());
			}
		}
//		System.out.println(network); // for debugging
	}
	
	private static void getFriendRecommendation(Graph network, User currentUser, ArrayList<User> userList, ArrayList<BST<User>> interestUserAL, ArrayList<User> finalReco) {
		network.BFS(currentUser.getUserId());
//		network.printBFS(); // for debugging
		List<Integer> recommendations = network.getRecommendations();
		ArrayList<User> recoList = new ArrayList<>();
		recommendations.placeIterator();
		while(!recommendations.offEnd()) {
			recoList.add(userList.get(recommendations.getIterator()));
			recommendations.advanceIterator();
		}
		ArrayList<Double> rank = new ArrayList<>();
		for(int i = 0; i < recoList.size(); i++) {
			Double score = 0.0;
			List<Interest> rl = recoList.get(i).getInterestList();
			rl.placeIterator();
			while(!rl.offEnd()) {
				if(interestUserAL.get(rl.getIterator().getInterestId()).search(currentUser, new NameComparator()) != null) {
					score += 0.1;
				}
				rl.advanceIterator();
			}
			rank.add(recoList.size() -  i + score);
		}
		Double max = Collections.max(rank);
		while(Double.compare(max, 0.0) != 0) {
			for(int i = 0; i < rank.size(); i++) {
				if(Double.compare(rank.get(i), max) == 0) {
					finalReco.add(recoList.get(i));
					rank.set(i, 0.0);
				}
			}
			max = Collections.max(rank);
		}
	}
	
	private static void addRecommendedFriends(Scanner input, User currentUser, UserHashTable users, ArrayList<User> finalReco, Graph network) {
		System.out.println("\nHere are some friend recommendatations based on your mutual friends and interests:");
		for(int i = 0; i < finalReco.size(); i++) {
			System.out.println(finalReco.get(i));
		}
		addNewFriend(input, currentUser, users, network);
	}
	
	public static void main(String[] args) {
		LoginHashTable login = new LoginHashTable(500);
		UserHashTable users = new UserHashTable(500);
		InterestHashTable interestHT = new InterestHashTable(500);
		ArrayList<BST<User>> interestUserAL = new ArrayList<>();
		ArrayList<User> userList = new ArrayList<>();
		ArrayList<User> finalReco = new ArrayList<>();

		User currentUser = new User();
		Scanner input;
		String option;
		
		
		loadUsers(users, login, interestHT, interestUserAL, userList);
		Graph network = new Graph(userList.size() + 1);
		
		fillAdjList(network, userList, users);
		
		System.out.println("Welcome to FRIENDS!\n");
		
		input = new Scanner(System.in);

		while(true) {
			System.out.println("(L) Login ");
			System.out.println("(Q) Quit ");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();

			if(option.compareTo("L") == 0) {
				currentUser = login(input, users, login, interestHT, interestUserAL);
				break;
			} else if(option.compareTo("Q") == 0) {
				System.out.println("\nSaving...");
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}		
		}
		
		
		while(true) {
			System.out.println("\n(V) View My Friends ");
			System.out.println("(S) Search for a New Friend ");
			System.out.println("(G) Get Friend Recommendations");
			System.out.println("(Q) Save and Quit ");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			
			if(option.compareTo("V") == 0) {
				viewMyFriends(input, currentUser, users, network);
			} else if(option.compareTo("S") == 0) {
				searchFriends(input, currentUser, users, interestHT, interestUserAL, network);
			} else if(option.compareTo("G") == 0) {
				getFriendRecommendation(network, currentUser, userList, interestUserAL, finalReco);
				addRecommendedFriends(input, currentUser, users, finalReco, network);
				finalReco.clear();
			} else if(option.compareTo("Q") == 0) {
				System.out.println("\nSaving...");
				saveData(userList);
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}		
		}
	}
}
