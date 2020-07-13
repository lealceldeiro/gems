package example.regex;

import com.jcabi.matchers.RegexMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

public class RegexTest
{
	@Test
	public void matchesDecimalNumberPattern()
	{
		MatcherAssert.assertThat(
			"[+-]?\\d*\\.?\\d+",
			RegexMatchers.matchesPattern("-145.78")
		);
	}
}
