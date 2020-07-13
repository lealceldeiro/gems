package example.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LookaheadOverlapping
{
	public static void main (String[] args) throws java.lang.Exception
	{
		final String kw = "dad";
		final String regex = "(?=" + Pattern.quote(kw) + ")";
		final String string = "dadadsaddadadads";
		

		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);

		int count = 0;
		while (matcher.find())
		{
		    System.out.printf("Start: %d\tEnd:%d%n", matcher.start(), matcher.start() + kw.length() -1);
		    count++;
		}

		System.out.printf("Match count: %d%n", count);
	}
}
