package example.regex; 

import java.util.*; 

class ScannerApi 
{ 
	public static void main (String[] args) throws java.lang.Exception 
	{ 
		// Initialize Scanner object with a string that is delimited by 1 or more 
		// punctuation characters 
		String str = "London:Rome#Paris::Munich///Moscow"; 

		Scanner scanner = new Scanner(str); 


		// set the delimiter to be used as 1 or more punctuation character 
		scanner.useDelimiter("\\p{Punct}+"); 
 

		// valid city name regex 
 		final String cityPattern = "\\p{L}+"; 

 
		while(scanner.hasNext()) { 

			// make sure generated token consists of declared city pattern 

			if(scanner.hasNext(cityPattern)) { 
				System.out.println(scanner.next()); 
			} 

			else { 
				// skip printing and move to next token 
				scanner.next(); 
			} 

		} 

 
		// close the scanner 
		scanner.close(); 

	} 

}
