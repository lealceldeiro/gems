package example.regex; 
 

import java.util.*; 
 

class StringSplit 
{ 
	public static void main (String[] args) throws java.lang.Exception 
	{ 

		// our input string 
		String input = "green-red-blue-yellow"; 
 

		// call split and print each element from generated array 
		// using stream API 
		Arrays.stream(input.split("-")).forEach(System.out::println); 

	} 
} 
