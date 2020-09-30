package com.example.oop;


// * brings in all classes inside a package, but not sub packages/nested packages
// you can create naming collisions by importing two classes with the same name
//In that case, you need to specify it using the fully qualified name
import com.example.oop.encapsulation.HubCap;
import com.example.oop.inheritance.Child;
import com.example.oop.inheritance.Parent;
import com.example.oop.accesscheck.*;

public class OOPDriver {

	public static void main(String[] args) {
		//encapsulation
		// TODO Auto-generated method stub
		//HubCap h = new HubCap(17.5,"orange", "badCapsShop");
		
		//fully qualified name
		//com.example.oop.accesscheck.FirstHubCap first = new com.example.oop.accesscheck.FirstHubCap();
		
		
		//inheritance
//		Parent p = new Parent(35, "Samantha", "Smith", 6.0);
//		System.out.println(p.getHeight());
//		Child c = new Child(12, "brat", "danger", 3.0);
//		System.out.println(c.getLastName());
		
		//casting
//		Parent c = new Child();
//		System.out.println(c.age); // parent's value of age because we are using a parent reference(up casting)
//		System.out.println(((Child)c).age); // child's value of age, because we are using the child reference, and this works c is actually a child (down casting)
//		Child p = (Child) new Parent(); // this is down casting, can't do this because its a parent class. 
		
		//You can get the parent's field's values by upcasting. This is called shadowing. 
		//You can't get the parent's method implementation by upcasting(overridden methods cannot be shadowed)
		
		Child c = new Child();
		toString(c);

	}
	
	//polymorphic parameters: the ability to up cast to a parent gives us the ability to make a method that
	//takes in a parent or interface and we can use whatever instance we want with it, as long as it 
	//inherits from the parent or interface.
	
public static String toString(Parent p) {
	return p.toString();
}

}