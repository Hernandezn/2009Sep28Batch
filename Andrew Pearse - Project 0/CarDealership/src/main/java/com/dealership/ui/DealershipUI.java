package com.dealership.ui;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.dealership.controller.CarController;
import com.dealership.controller.OfferController;
import com.dealership.controller.UserController;
import com.dealership.models.Car;
import com.dealership.models.Offer;
import com.dealership.models.User;



public class DealershipUI {
	final static Logger logger = Logger.getLogger(DealershipUI.class);
	private UserController userController;
	private CarController carController;
	private OfferController offerController;
	
	/**
	 * creates a new controller object
	 */
	public DealershipUI() {
		this(new UserController(), new CarController(), new OfferController());
	}
	
	/**
	 * creates a new or existing controller object
	 * @param controller references the controller
	 */
	public DealershipUI(UserController userController, CarController carController, OfferController offerController) {
		super();
		this.userController = userController;
		this.carController = carController;
		this.offerController = offerController;
	}
	
	/*
	 * 
	 * STARTING PROGRAM UI
	 * 
	 */
	
	/**
	 * creates the main menu
	 * @param scanner allows for user input
	 */
	public void startUp(Scanner scanner) {
		System.out.println("\nHello and welcome to Rockford Automotives!\n");
		System.out.println("0: Exit\n"
						 + "1: Log In\n"
						 + "2: Create Customer Account\n"
						 + "3: Forgot Password\n");
		
		try {
			int input = scanner.nextInt();
			User user = null;
			
			System.out.println("----------------------------------------------------------------\n");
			
			switch(input) {
			case 0:
				logger.info("User terminated the program.");
				System.out.println("\nGoodbye!");
				System.exit(0);
				
			case 1:
				user = userController.validateUser( logIn(scanner) );
				
				if ( user != null ) {
					if (user.getType().equals("customer")) {
						logger.info("User was a Customer");
						System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + ", what would you like to do?\n"
										 + "-------------------------------------------------\n");
						customerUIChoices(scanner, user);
						
					}
					else if (user.getType().equals("employee")) {
						logger.info("User was an Employee");
						System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + ", what would you like to do?\n"
										 + "-------------------------------------------------\n");
						employeeUIChoices(scanner, user);
						
					} //TODO: maybe add a manager type too
					
					
					startUp(scanner);
				}
				else {
					logger.warn("Account did not exist");
					System.out.println("\nThis account does not exist or your password was incorrect, try again.");
					System.out.println("------------------------------------------------------------------------\n");
					startUp(scanner);
				}

			case 2:
				if (userController.createCustomerAccount( createCustomerAccount(scanner) ) ) {
					logger.info("Customer account created successfully.");
					System.out.println("\nAccount Successfully Created! Log in to continue.");
					System.out.println("---------------------------------------------------\n");
					startUp(scanner);
				}
				else {
					logger.warn("Username already exists in the database.");
					System.out.println("\nAccount already exists with that username, either log in or choose a new username.");
					System.out.println("------------------------------------------------------------------------------------\n");
					startUp(scanner);
				}
				
			case 3:
				if (userController.changePassword(changePassword(scanner))) {
					logger.info("Password was successfully changed.");
					System.out.println("\nPassword successfully changed!");
					System.out.println("--------------------------------\n");
					startUp(scanner);
				}
				else {
					logger.warn("Username does not exist in the database.");
					System.out.println("\nAccount username is invalid.");
					System.out.println("------------------------------\n");
					startUp(scanner);
				}
			default:
				throw new InputMismatchException();
			}
		} catch (InputMismatchException e) {
			logger.error("Invalid input.", e);
			System.out.println("\nPlease input a valid choice.");
			System.out.println("------------------------------\n");
			scanner.nextLine();
			startUp(scanner);
		}
	}
	
	/**
	 * gathers the information needed to check if user is in the database
	 * @param scanner reads in the username and password
	 * @return returns a new user object to be checked against the database
	 */
	private User logIn(Scanner scanner){
		System.out.print("\nUsername: ");
		String userName = scanner.next();
		System.out.print("Password: ");
		String password = scanner.next();
		System.out.println();
		
		return new User(userName, password, null, null, "");
	}
	
	/**
	 * gathers user's input to create a customer account, will fail if user already exists
	 * @param scanner reads in user input
	 */
	public User createCustomerAccount(Scanner scanner) {
		
		System.out.print("\nEnter First Name: ");
		String firstName = scanner.next();
		System.out.print("Enter Last Name: ");
		String lastName = scanner.next();
		System.out.print("Enter Account Username: ");
		String userName = scanner.next();
		System.out.print("Enter Account Password: ");
		String password = scanner.next();
		
		return new User(userName, password, firstName, lastName, "customer");
	}
	
	/**
	 * changes the password of the specified user account
	 * @param scanner reads in user input
	 * @return returns a new User object that only has the username and new password
	 */
	public User changePassword(Scanner scanner) {
		System.out.println("Enter your username: ");
		String username = scanner.next();
		System.out.println("Enter your new password: ");
		String password = scanner.next();
		
		return new User(username, password, null, null, null);
	}
	
	
	/*
	 * 
	 * CUSTOMER ACCOUNT UI
	 * 
	 */

	public void customerUIChoices(Scanner scanner, User user) {
		System.out.println("0: Go Back\n"
						 + "1: View Cars on the Lot\n"
						 + "2: Make Offer on a Car\n"
						 + "3: View Owned Cars\n"
						 + "4: View Payment Statement(s)\n");
		
		try {
			int input = scanner.nextInt();
			
			System.out.println("----------------------------------------------------------------\n");
			
			switch(input) {
			case 0:
				logger.info("User returned to main menu.");
				return;
			case 1:
				logger.info("Printed cars on the lot.");
				System.out.println();
				List<Car> carsOnLot = carController.findAllCarsOnLot();
				
				if(carsOnLot.size() != 0) {
					System.out.println("Cars on lot:");
					for (Car c : carsOnLot)
						System.out.println(c);
				}
				
				customerUIChoices(scanner, user);
				break;
			case 2:
				logger.info("User made an offer on a car.");
				System.out.println();
				if (offerController.makeOffer( makeOffer(scanner, user) ) != null) {
					System.out.println("Offer successfully made!");
					System.out.println("------------------------\n");
				}
				else {
					System.out.println("Offer did not go through.");
					System.out.println("-------------------------\n");
				}
				
				customerUIChoices(scanner, user);
				break;
			case 3:
				logger.info("User viewed cars they own");
				System.out.println();
				List<Car> carsOwned = carController.findAllOwned(user.getUserName());
					
				if (carsOwned.size() != 0) {
					for (Car c : carsOwned)
						System.out.println(c);
				}
				else {
					System.out.println("You do not own any cars.");
					System.out.println("------------------------\n");
				}
				
				customerUIChoices(scanner, user);
				break;
			case 4:
				logger.info("User viewed all payments on cars.");
				System.out.println();
				List<Offer> offers = offerController.viewAllPaymentsOnCars(user.getUserName());
				
				if (offers.size() != 0) {
					System.out.println("Your payments: ");
					for (Offer o : offers)
						System.out.println(o);
				}
				else {
					System.out.println("You do not have any payments left.");
					System.out.println("----------------------------------");
				}
				
				customerUIChoices(scanner, user);
				break;
			default:
				throw new InputMismatchException();
			}
			
		} catch (InputMismatchException e) {
			logger.error("Invalid input.", e);
			System.out.println("\nPlease enter a valid choice.\n");
			scanner.nextLine();
			customerUIChoices(scanner, user);
		}
	}

	
	public Offer makeOffer(Scanner scanner, User user) throws InputMismatchException {

		System.out.println("Cars on lot:\n");
		for (Car c : carController.findAllCarsOnLot())
			System.out.println(c);
		
		System.out.println("Enter the ID of the car: ");
		int cid = scanner.nextInt();
		System.out.print("Enter your down payment: ");
		int downPayment = scanner.nextInt();
		System.out.println("Enter the number of months it will take for you to pay back the total: ");
		int monthsLeft = scanner.nextInt();
		System.out.println();
		
		return new Offer(downPayment, monthsLeft, user.getUserName(), new Car(cid, null, null, 0, null, false));
	}


	/*
	 * 
	 * EMPLOYEE ACCOUNT UI
	 * 
	 */
	
	/**
	 * UI that an employee sees if they are the one to log in
	 * @param scanner gathers user input
	 * @param user is the information of the current user
	 */
	public void employeeUIChoices(Scanner scanner, User user) {
		System.out.println("0: Go Back\n"
						 + "1: Add Car to the Lot\n"
						 + "2: Accept/Reject Customer Offers\n"
						 + "3: View All Payments\n"
						 + "4: Remove Car from the Lot\n");
		
		try {
			int input = scanner.nextInt();
			
			System.out.println("----------------------------------------------------------------\n");
			
			switch (input) {
			case 0:
				logger.info("User went back to the main menu.");
				return;
			case 1:
				logger.info("User added a car to the lot.");
				if(carController.addCarToTheLot(enterCarInfo(scanner))) {
					System.out.println("\nCar successfully added!\n");
				}
				else {
					System.out.println("\nCould not add the car to the lot!\n");
				}
				
				employeeUIChoices(scanner, user);
				break;
			case 2:
				logger.info("User accepted/rejected customer offers.");
				System.out.println();
				List<Offer> offers = offerController.viewAllOffersOnACar( chooseCarToViewOffers(scanner) );
				
				if (!(offers.isEmpty())) {
					System.out.println("\nCar Information:\n");
					System.out.println(offers.get(0).getOfferedFor().getMake() + " " + offers.get(0).getOfferedFor().getModel() + "\nPrice: $" + offers.get(0).getOfferedFor().getPrice());
					System.out.println("\nOffers\n---------------");
				}
				else {
					System.out.println("\nThere are no offers for this car!\n");
					employeeUIChoices(scanner, user);
					break;
				}
				
				for(Offer o : offers) {
					System.out.println("\nOfferID: " + o.getOfferID());
					System.out.println("Customer: " + o.getOfferer());
					System.out.println("Down Payment: " + o.getDownPayment());
					System.out.println("Months to Repay: " + o.getMonthsLeft());
				}
				
				Offer offer = acceptOrRejectOffer(scanner);
				
				offer.setOfferer(offerController.findByID(offer.getOfferID()).getOfferer());
				
				if (offerController.updateOffer(offer) != null) {
					System.out.println("\nOffer(s) successfully updated!");
					System.out.println("--------------------------------");
				}
				else {
					System.out.println("\nCould not update offer(s).");
					System.out.println("----------------------------");
				}
				
				employeeUIChoices(scanner, user);
				break;
			case 3:
				logger.info("User viewed all payments by customers.");
				System.out.println("\nAll Payments\n----------------\n");
				for(Offer o : offerController.getAllPayments()) {
					System.out.println(o.getOfferedFor().getMake() + " " + o.getOfferedFor().getModel());
					User owner = userController.findByID(o.getOfferer());
					System.out.println("Owner: " + owner.getFirstName() + " " + owner.getLastName());
					System.out.println("Paid: $" + o.getDownPayment());
					int amountLeft = o.getOfferedFor().getPrice() - o.getDownPayment();
					System.out.println("Total Left: $" + amountLeft + "\n");
				}
				
				employeeUIChoices(scanner, user);
				break;
			case 4:
				logger.info("User removed a car from the lot.");
				System.out.println();
				for (Car c : carController.findAllCarsOnLot())
					System.out.println(c);
				
				if (carController.removeCarFromLot( chooseCarToRemove(scanner)) == 1 )
					System.out.println("\nCar successfully removed from lot.\n");
				else
					System.out.println("\nCar could not be removed from lot.\n");
				
				employeeUIChoices(scanner, user);
				break;
			default:
				throw new InputMismatchException();
			}
			
		} catch (InputMismatchException e) {
			logger.error("Invalid input.", e);
			System.out.println("\nPlease enter a valid choice.\n");
			scanner.nextLine();
			employeeUIChoices(scanner, user);
		}
	}

	/**
	 * UI for entering the information for a new car on the lot
	 * @param scanner gathers user input
	 * @return returns a new Car object with information gathered from scanner
	 * @throws IllegalArgumentException if user enters a non number
	 */
	public Car enterCarInfo(Scanner scanner) throws InputMismatchException {
		
		System.out.println("Enter make: ");
		String make = scanner.next();
		System.out.println("Enter model: ");
		String model = scanner.next();
		System.out.println("Enter price: ");
		int price = scanner.nextInt();
		
		return new Car(make, model, price);
	}
	
	/**
	 * UI that prints the cars on lot and asks which car the user wishes to view the offers for
	 * @param scanner gathers user input
	 * @return returns the CID
	 * @throws IllegalArgumentException if user enters a non number
	 */
	public int chooseCarToViewOffers(Scanner scanner) throws InputMismatchException {
		for (Car c : carController.findAllCarsOnLot()) {
			System.out.println(c);
		}
		
		System.out.println("Choose a car ID to view their offers: ");
		int cid = scanner.nextInt();
		return cid;
	}
	
	/**
	 * UI that gathers what offer ID to either accept/reject
	 * @param scanner gathers user input
	 * @return returns a new Offer object that either accepts or rejects the specific offer ID
	 * @throws IllegalArgumentException if user enters something other than a number for offerID or if they do something other than accept/reject
	 */
	public Offer acceptOrRejectOffer(Scanner scanner) throws InputMismatchException{
		System.out.println("\nWhat offer would you like to accept or reject? (Enter Offer ID)");
		int offerID = scanner.nextInt();
		System.out.println("What would you like to do with the offer? Type: accept or reject ");
		String choice = scanner.next();
		
		switch(choice) {
		case "accept":
			return new Offer(offerID, 0, 0, true, false, null);
		case "reject":
			return new Offer(offerID, 0, 0, false, true, null);
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * UI that gathers what car should be removed from the lot
	 * @param scanner gathers user input
	 * @return returns the cid of the car
	 * @throws IllegalArgumentException if user does not enter a number
	 */
	public int chooseCarToRemove(Scanner scanner) throws InputMismatchException{
		System.out.println("Please enter the Car ID you would like to remove: ");
		return scanner.nextInt();
	}
}