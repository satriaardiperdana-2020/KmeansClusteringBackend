package id.clustering.kmeans.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LoopListDemo {
	  public static void main(String[] args) {
		    	 
		        ArrayList < String > celebrities = new ArrayList < String > ();
		 
		        celebrities.add("Tom Cruise");
		        celebrities.add("Will Smith");
		        celebrities.add("Jackie Chan");
		        celebrities.add("Akshay Kumar");
		 
		        ListIterator < String > listIterator = celebrities.listIterator();
		 
		        System.out.println("Previous Index:- " + listIterator.previousIndex());
		 
		        System.out.println("Move to next() ");
		        listIterator.next();
		        System.out.println("Previous Index:- " + listIterator.next());
		 
		        System.out.println("Move to next()");
		        listIterator.nextIndex();
		        System.out.println("Next index:- " + listIterator.nextIndex());
		    
	  }
}