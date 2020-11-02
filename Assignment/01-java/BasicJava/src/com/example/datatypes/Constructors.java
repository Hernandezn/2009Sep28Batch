package com.example.datatypes;

public class Constructors extends Object{
	//objects are a set of states and behaviors,we determine the states in the class and initialize them 
    //	inside the constructors
	/*
	 * by default java creates a constructor for you
	 * it takes no arguments
	 * 
	 * custom no argument constructors
	 * and an argument constructor. 
	 * 
	 * every class extends Object
	 */
	
	int age;
	String name;
	
	Constructors (int x, String na){
		age = x;
		name = na;	
	}
	
	Constructors (){
		System.out.println("This is the constructors class");
	}

	public static void main(String[] args) {
		Constructors c = new Constructors(7,"go");  //default constructor
		System.out.println(c.age);
		
		C2 c1 = new C2();
		System.out.println(c1.age);
	} 
}