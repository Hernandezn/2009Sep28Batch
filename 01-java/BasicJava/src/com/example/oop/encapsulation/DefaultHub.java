package com.example.oop.encapsulation;

public class DefaultHub{

	
	public String getManufacturer() {
		HubCap h = new HubCap(16.0, "red","hubsnstuff");
		return h.manufacturer;
	}
}
