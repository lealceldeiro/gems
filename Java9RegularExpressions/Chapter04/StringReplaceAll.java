package example.regex;

public class StringReplaceAll
{
	public static void main(String[] args)
	{

		// our input string
		String input = "Let''''''s learn::: how to    write cool regex...";


		// call replaceAll and assign replaced string to same variable
		input = input.replaceAll("(\\W)\\1+", "$1");


		// print the result
		System.out.printf("Replaced result: %s%n", input);

	}
}
