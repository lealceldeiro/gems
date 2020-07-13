package example.regex;

import java.util.regex.*;

class LookAroundExample2
{
	public static void main (String[] args)
	{
		String input = "var1,var2,var3 (var1,var2,var3) var4,var5,var6 (var4,var5,var6)";

		final Pattern p = Pattern.compile(",(?![^()]*\\))");

		Matcher m = p.matcher(input);

		System.out.printf("%s%n", m.replaceAll(";"));
	}
}

