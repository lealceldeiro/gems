package example.regex;

import java.util.regex.*;

class LookAroundExample1
{
	public static void main (String[] args)
	{
		final String[] inputs = {"abcd98732", "pqrn", "qwerty12345678xyz", "123"};

		final Pattern p = Pattern.compile("(?!^)(?=(.{3})+$)");
		

		for (String s: inputs) {
			Matcher m = p.matcher(s);
			System.out.printf("%s => %s%n", s, m.replaceAll(":"));
		}
	}
}

