package example.regex; 

public class StringMatches 
{ 
	public static void main(String[] args) 
	{ 
		boolean result; 
		String regex; 

		// first regex
		String input = "Sky is blue";
		regex = "\\b(red|blue|green)\\b"; 
		result = input.matches(regex); 
		System.out.printf("Match result: %s%n", result); 
		// prints false 

		// Second regex 
		regex = ".*\\b(red|blue|green)\\b.*"; 
		result = input.matches(regex); 
		System.out.printf("Match result: %s%n", result); 
		// prints true 
	} 
}
