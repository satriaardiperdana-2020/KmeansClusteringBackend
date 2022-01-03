package id.clustering.kmeans.test;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] peopleToGrades = { 
				{ "Tremaine", "A" }, 
				{ "Jeremy", "A-" }, 
				{ "Yuta", "B" }, 
				{ "Steph", "A+" },
				{ "Klay", "B+" }, 
				{ "Damian", "A" }, 
				{ "Klay", "A" }, 
				{ "Jeremy", "B-" }, };

		HashMap<String, ArrayList<String>> peopleToGradesMap = new HashMap<>();

		for (int i = 0; i < peopleToGrades.length; i++) {
			if (!peopleToGradesMap.containsKey(peopleToGrades[i][0])) { // [i][0] is the name of the person
				peopleToGradesMap.put(peopleToGrades[i][0], new ArrayList<String>()); // ArrayList is empty at this
																						// point
			}
			peopleToGradesMap.get(peopleToGrades[i][0]).add(peopleToGrades[i][1]);
		}
		System.out.println("output=="+peopleToGradesMap);

	}

}
