package com.dealership.models;

import java.util.Set;

public class Car {

	private String make;
	private String model;
	private boolean onLot;
	private int carID;
	private int price;
	private Set<Offer> offers;
	private String owner;

	/**
	 * constructor that should only be called when retrieving information from the database
	 * @param carID
	 * @param make
	 * @param model
	 * @param price
	 * @param owner
	 * @param onLot
	 */
	public Car(int carID, String make, String model, int price, String owner, boolean onLot) {
		super();
		this.carID = carID;
		this.make = make;
		this.model = model;
		this.price = price;
		this.owner = owner;
		this.onLot = onLot;
	}
	
	/**
	 * constructor that should only be used when inserting a car into the database
	 * @param make is the make of the car
	 * @param model is the model of the car
	 * @param price is the total price of the car
	 * @param owner is a User that owns the car
	 */
	public Car(String make, String model, int price) {
		super();
		this.make = make;
		this.model = model;
		this.price = price;
		this.setOwner(null);
//		this.year = year;
		onLot = true;
	}
	
	public void setCarID(int carID) {
		this.carID = carID;
	}
	
	public int getCarID() {
		return carID;
	}
	
	public boolean isOnLot() {
		return onLot;
	}

	public void setOnLot(boolean onLot) {
		this.onLot = onLot;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "ID: " + carID + "\n" + make + " " +  model + "\nPrice: $" + price + "\n";
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<Offer> getOffers() {
		return offers;
	}

	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}
	
}