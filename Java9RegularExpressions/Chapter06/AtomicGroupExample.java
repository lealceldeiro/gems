package example.regex;

class AtomicGroupExample
{
	public static void main (String[] args) throws java.lang.Exception
	{
		final String input = "foodie";

		// regex with non-atomic group
		final String re1 = "foo(?:d|die|lish)";


		// regex with an atomic group
		final String re2 = "foo(?>d|die|lish)";

		// regex with an alternate atomic group with correct order
		final String re3 = "foo(?>lish|die|d)";

		// now execute all 3 regex against same input 
		System.out.printf("%s: %s%n", re1, input.matches(re1));

		System.out.printf("%s: %s%n", re2, input.matches(re2));

		System.out.printf("%s: %s%n", re3, input.matches(re3));
	}
}
