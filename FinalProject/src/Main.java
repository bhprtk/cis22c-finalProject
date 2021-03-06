
/**
 * Main.java
 * @author Pratik Bhandari
 * @author Naqib Khan
 * @author Young Jin Kim
 * @author Jafer Zaidi
 * @author Hanxiao Wang
 * @author Ogbe Airiodion
 * CIS 22C, Final Project
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

	/**
	 * Logs in existing user or creates a new User
	 * 
	 * @param input          Scanner Object to input from user
	 * @param users          Hash Table for users
	 * @param login          Hash Table for user name and password
	 * @param interestHT     Hash Table for interests
	 * @param interestUserAL ArrayList mapping interest id to BST of users
	 * @param userList       ArrayList mapping user id to users
	 * @param network        Graph of users
	 * @return
	 */
	private static User login(Scanner input, UserHashTable users, LoginHashTable login, InterestHashTable interestHT,
			ArrayList<BST<User>> interestUserAL, ArrayList<User> userList, Graph network) {
		String username, password;
		String fullName, city;
		String topic1;
		User currentUser = new User();
		System.out.print("\nPlease enter your username: ");
		username = input.nextLine();
		System.out.print("\nPlease enter your password: ");
		password = input.nextLine();
		if (login.contains(username + password)) {
			fullName = login.get(username + password);
			currentUser = users.get(fullName);
			System.out.println("\nWelcome back, " + currentUser.getFullName() + "!\n");
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
			for (int i = 0; i < 2; i++) {
				System.out.print("Enter a topic you're interested in: ");
				topic1 = input.nextLine();
				Interest currentInterest;
				if (!interestHT.contains(topic1)) {
					currentInterest = interestHT.put(topic1);

				} else {
					currentInterest = interestHT.get(topic1);
				}
				int interestIndex = currentInterest.getInterestId();
				if (interestIndex >= interestUserAL.size()) {
					interestUserAL.add(currentInterest.getInterestId(), new BST<User>());
				}
				interestUserAL.get(currentInterest.getInterestId()).insert(currentUser, new NameComparator());
				interests.addLast(currentInterest);

			}

			currentUser.setInterestList(interests);

			ArrayList<User> tempReco = new ArrayList<>();
			interests.placeIterator();
			while (!interests.offEnd()) {
				if (interestHT.contains(interests.getIterator().getInterest())) {
					ArrayList<User> tempFriendList = interestUserAL.get(interests.getIterator().getInterestId())
							.dataInOrder();
					for (int i = 0; i < tempFriendList.size(); i++) {
						if (!tempReco.contains(tempFriendList.get(i)) && !tempFriendList.get(i).equals(currentUser)) {
							tempReco.add(tempFriendList.get(i));
						}
					}
				}
				interests.advanceIterator();
			}

			System.out.println("\nHere is a list of people who share the same interests with you:");
			for (int i = 0; i < tempReco.size(); i++) {
				System.out.println(tempReco.get(i));
			}
			System.out.println("\nAdd a friend to complete your profile:");

			while (!currentUser.hasFriends()) {
				addNewFriend(input, currentUser, users, network);
			}

			login.put(username + password, fullName);
			users.put(currentUser);
			userList.add(currentUser);

			System.out.println("\nWelcome to FRIENDS, " + fullName + "\n");
		}
		return currentUser;

	}

	/**
	 * Loads user data from the file into the given data structures
	 * 
	 * @param users          Hash Table for users
	 * @param login          Hash Table for user name and password
	 * @param interestHT     Hash Table for interests
	 * @param interestUserAL ArrayList mapping interest id to BST of users
	 * @param userList       ArrayList mapping user id to users
	 */
	private static void loadUsers(UserHashTable users, LoginHashTable login, InterestHashTable interestHT,
			ArrayList<BST<User>> interestUserAL, ArrayList<User> userList) {
		String username, password, fullName, city;
		ArrayList<List<Integer>> friendsList = new ArrayList<List<Integer>>();
		int uid;
		Scanner input;
		try {
			input = new Scanner(new File("users.txt"));
			while (input.hasNextLine()) {
				String userId = input.nextLine();
				uid = Integer.parseInt(userId);
				username = input.nextLine();
				password = input.nextLine();
				fullName = input.nextLine();
				city = input.nextLine();
				String nFriends = input.nextLine();
				List<Integer> l = new List<>();
				for (int i = 0; i < Integer.parseInt(nFriends); i++) {
					String nextLine = input.nextLine();
					l.addLast(Integer.parseInt(nextLine));
				}
				String nInterests = input.nextLine();
				List<Interest> interestList = new List<>();
				for (int i = 0; i < Integer.parseInt(nInterests); i++) {
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

		for (int i = 0; i < userList.size(); i++) {
			List<Interest> tempInterestList = userList.get(i).getInterestList();
			tempInterestList.placeIterator();
			while (!tempInterestList.offEnd()) {
				Interest currentInterest = interestHT.get(tempInterestList.getIterator().getInterest());
				int interestIndex = currentInterest.getInterestId();
				if (interestIndex >= interestUserAL.size()) {
					interestUserAL.add(currentInterest.getInterestId(), new BST<User>());
				}
				interestUserAL.get(currentInterest.getInterestId()).insert(userList.get(i), new NameComparator());
				tempInterestList.advanceIterator();
			}
			List<Integer> tempList = friendsList.get(i);
			tempList.placeIterator();
			while (!tempList.offEnd()) {
				User friend = userList.get(tempList.getIterator());
				userList.get(i).addFriend(friend);
				tempList.advanceIterator();
			}
		}

	}

	/**
	 * Lets current user view or remove friends
	 * 
	 * @param input       Scanner Object to input from user
	 * @param currentUser The user that is currently logged in
	 * @param usersHash   Table for users
	 * @param network     Graph of users
	 */
	private static void viewMyFriends(Scanner input, User currentUser, UserHashTable users, Graph network) {
		System.out.println("\nHere is a list of your friends, " + currentUser.getFullName() + "\n");
		currentUser.printFriends();
		String option;
		String fullName;
		while (true) {
			System.out.println("\n(V) View a friend's profile");
			System.out.println("(R) Remove a friend");
			System.out.println("(B) Go back to the main menu");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			if (option.compareTo("V") == 0) {
				System.out.print("\nEnter the full name of your friend: ");
				fullName = input.nextLine();
				if (!currentUser.hasFriend(new User(fullName))) {
					System.out.println("\nYou don't have any friends with that name!");
				} else {
					User person;
					if (users.hasMultiple(fullName)) {
						System.out.println("\nThere are multiple people with this name:\n");
						List<User> multiple = users.getMultiple(fullName);
						multiple.printNumberedList();
						while (true) {
							System.out.print("\nEnter the number of the correct " + fullName + ": ");
							String correctName = input.nextLine();
							int correctNumber = Integer.parseInt(correctName);
							if (correctNumber > multiple.getLength()) {
								System.out.println("\nPlease input a valid option\n");
							} else {
								multiple.placeIterator();
								for (int i = 1; i < correctNumber; i++) {
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
				}
			} else if (option.compareTo("R") == 0) {
				if (currentUser.getFriends().size() == 0) {
					System.out.println("\nYou don't have any friends to remove!");
				} else {
					System.out.print("\nEnter the full name of a friend: ");
					fullName = input.nextLine();
					User person;
					if (!currentUser.hasFriend(new User(fullName))) {
						System.out.println("\nYou don't have any friends with that name!");
					} else {
						if (users.hasMultiple(fullName)) {
							System.out.println("\nThere are multiple people with this name:\n");
							List<User> multiple = users.getMultiple(fullName);
							multiple.printNumberedList();
							while (true) {
								System.out.print("\nEnter the number of the correct " + fullName + ": ");
								String correctName = input.nextLine();
								int correctNumber = Integer.parseInt(correctName);
								if (correctNumber > multiple.getLength()) {
									System.out.println("\nPlease input a valid option\n");
								} else {
									multiple.placeIterator();
									for (int i = 1; i < correctNumber; i++) {
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
						currentUser.removeFriend(person);
						person.removeFriend(currentUser);
						network.removeDirectedEdge(currentUser.getUserId(), person.getUserId());
						network.removeDirectedEdge(person.getUserId(), currentUser.getUserId());
						System.out.println("\nYou are no longer friends with " + person.getFullName() + "!");
					}
				}
			} else if (option.compareTo("B") == 0) {
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}
		}
	}

	/**
	 * Lets current user add a new friend
	 * 
	 * @param inputScanner Object to input from user
	 * @param currentUser  The user that is currently logged in
	 * @param usersHash    Table for users
	 * @param network      Graph of users
	 */
	private static void addNewFriend(Scanner input, User currentUser, UserHashTable users, Graph network) {
		String fullName;
		System.out.print("\nEnter the full name of a person: ");
		fullName = input.nextLine();
		if (currentUser.getFullName().compareTo(fullName) == 0) {
			System.out.println("\nYou cannot add yourself as a friend!");
		} else {
			User person;
			if (users.contains(fullName)) {
				String choice;
				if (users.hasMultiple(fullName)) {
					System.out.println("\nThere are multiple people with this name:\n");
					List<User> multiple = users.getMultiple(fullName);
					multiple.printNumberedList();
					while (true) {
						System.out.print("\nEnter the number of the correct " + fullName + ": ");
						String correctName = input.nextLine();
						int correctNumber = Integer.parseInt(correctName);
						if (correctNumber > multiple.getLength()) {
							System.out.println("\nPlease input a valid option\n");
						} else {
							multiple.placeIterator();
							for (int i = 1; i < correctNumber; i++) {
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
				if (currentUser.isFriendsWith(person)) {
					System.out.println("\nYou are already friends with " + fullName + "!");
				} else {
					while (true) {
						System.out.print("\nWould you like to add " + fullName + " as a friend? (Y/N): ");
						choice = input.nextLine();
						if (choice.compareTo("Y") == 0) {
							currentUser.addFriend(person);
							person.addFriend(currentUser);
							network.addDirectedEdge(currentUser.getUserId(), person.getUserId());
							network.addDirectedEdge(person.getUserId(), currentUser.getUserId());
							System.out.println("\nYou are now friends with " + fullName);
							break;
						} else if (choice.compareTo("N") == 0) {
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

	/**
	 * Lets the current user search for friends
	 * 
	 * @param input          Scanner Object to input from user
	 * @param currentUser    The user that is currently logged in
	 * @param users          Hash Table for users
	 * @param interestHT     Hash Table for interests
	 * @param interestUserAL ArrayList mapping interest id to BST of users
	 * @param network        Graph of users
	 */
	private static void searchFriends(Scanner input, User currentUser, UserHashTable users,
			InterestHashTable interestHT, ArrayList<BST<User>> interestUserAL, Graph network) {
		String option;
		while (true) {
			System.out.println("\n(N) Search a friend by name");
			System.out.println("(I) Search a friend by interest");
			System.out.println("(B) Go back to the main menu");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();
			if (option.compareTo("N") == 0) {
				addNewFriend(input, currentUser, users, network);
			} else if (option.compareTo("I") == 0) {
				String interest;
				System.out.print("\nEnter an interest: ");
				interest = input.nextLine();
				Interest interestObj = interestHT.get(interest);
				if (interestObj == null) {
					System.out.println("\n" + interest + " is not listed as an interest in our system!");
				} else {
					System.out.println("\nThe following people are also interested in " + interest);
					interestUserAL.get(interestObj.getInterestId()).inOrderPrint();
					addNewFriend(input, currentUser, users, network);
				}
			} else if (option.compareTo("B") == 0) {
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}
		}
	}

	/**
	 * Saves all data updated during runtime in the system file
	 * 
	 * @param userList ArrayList of users mapped by user id
	 */
	private static void saveData(ArrayList<User> userList) {
		try {
			PrintWriter writer = new PrintWriter(new File("users.txt"));
			for (int i = 0; i < userList.size(); i++) {
				User currentUser = userList.get(i);
				writer.println(currentUser.getUserId());
				writer.println(currentUser.getUserName());
				writer.println(currentUser.getPassword());
				writer.println(currentUser.getFullName());
				writer.println(currentUser.getCity());

				ArrayList<User> friends = currentUser.getFriends();
				writer.println(friends.size());
				for (int j = 0; j < friends.size(); j++) {
					writer.println(friends.get(j).getUserId());
				}

				List<Interest> tempList = currentUser.getInterestList();
				writer.println(tempList.getLength());
				tempList.placeIterator();
				while (!tempList.offEnd()) {
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

	/**
	 * Method to fill out the adjacent lists in the network graph
	 * 
	 * @param network  Graph of users
	 * @param userList ArrayList of users mapped by user id
	 * @param users    Hash Table for users
	 */
	private static void fillAdjList(Graph network, ArrayList<User> userList, UserHashTable users) {
		for (int i = 0; i < userList.size(); i++) {
			ArrayList<User> friends = userList.get(i).getFriends();
			for (int j = 0; j < friends.size(); j++) {
				network.addDirectedEdge(i, friends.get(j).getUserId());
			}
		}
	}

	/**
	 * The friend recommendation engine
	 * 
	 * @param network        Hash Table for users
	 * @param currentUser    The user that is currently logged in
	 * @param userList       ArrayList of users mapped by user id
	 * @param interestUserAL ArrayList mapping interest id to BST of users
	 * @param finalReco      ArrayList of the final recommendation of the users
	 * @param users          Hash Table for users
	 */
	private static void getFriendRecommendation(Graph network, User currentUser, ArrayList<User> userList,
			ArrayList<BST<User>> interestUserAL, ArrayList<User> finalReco, UserHashTable users) {
		fillAdjList(network, userList, users);
		network.BFS(currentUser.getUserId());
		List<Integer> recommendations = network.getRecommendations();
		ArrayList<User> recoList = new ArrayList<>();
		recommendations.placeIterator();
		while (!recommendations.offEnd()) {
			recoList.add(userList.get(recommendations.getIterator()));
			recommendations.advanceIterator();
		}
		ArrayList<Double> rank = new ArrayList<>();
		for (int i = 0; i < recoList.size(); i++) {
			Double score = 0.0;
			List<Interest> rl = recoList.get(i).getInterestList();
			rl.placeIterator();
			while (!rl.offEnd()) {
				if (interestUserAL.get(rl.getIterator().getInterestId()).search(currentUser,
						new NameComparator()) != null) {
					score += 0.1;
				}
				rl.advanceIterator();
			}
			rank.add(recoList.size() - i + score);
		}
		Double max = Collections.max(rank);
		while (Double.compare(max, 0.0) != 0) {
			for (int i = 0; i < rank.size(); i++) {
				if (Double.compare(rank.get(i), max) == 0) {
					finalReco.add(recoList.get(i));
					rank.set(i, 0.0);
				}
			}
			max = Collections.max(rank);
		}
	}

	/**
	 * Method to let the current user add friends from the recommendation list
	 * 
	 * @param input       Scanner Object to input from user
	 * @param currentUser The user that is currently logged in
	 * @param users       Hash Table for users
	 * @param finalReco   ArrayList of the final recommendation of the users
	 * @param network     Hash Table for users
	 */
	private static void addRecommendedFriends(Scanner input, User currentUser, UserHashTable users,
			ArrayList<User> finalReco, Graph network) {
		System.out.println("\nHere are some friend recommendatations based on your mutual friends and interests:");
		for (int i = 0; i < finalReco.size(); i++) {
			System.out.println(finalReco.get(i));
		}
		addNewFriend(input, currentUser, users, network);
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
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

		System.out.println("Welcome to FRIENDS!\n");

		input = new Scanner(System.in);

		while (true) {
			System.out.println("(L) Login ");
			System.out.println("(Q) Quit ");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();

			if (option.compareTo("L") == 0) {
				currentUser = login(input, users, login, interestHT, interestUserAL, userList, network);
				break;
			} else if (option.compareTo("Q") == 0) {
				System.out.println("\nSaving...");
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("\nPlease input a valid option\n");
			}
		}

		while (true) {
			System.out.println("\n(V) View My Friends ");
			System.out.println("(S) Search for a New Friend ");
			System.out.println("(G) Get Friend Recommendations");
			System.out.println("(Q) Save and Quit ");
			System.out.print("\nPlease choose an option: ");
			option = input.nextLine();

			if (option.compareTo("V") == 0) {
				if (currentUser.hasFriends()) {
					viewMyFriends(input, currentUser, users, network);
				} else {
					System.out.println("\nYou do not have any friends at the moment!");
				}
			} else if (option.compareTo("S") == 0) {
				searchFriends(input, currentUser, users, interestHT, interestUserAL, network);
			} else if (option.compareTo("G") == 0) {
				getFriendRecommendation(network, currentUser, userList, interestUserAL, finalReco, users);
				addRecommendedFriends(input, currentUser, users, finalReco, network);
				finalReco.clear();
			} else if (option.compareTo("Q") == 0) {
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
