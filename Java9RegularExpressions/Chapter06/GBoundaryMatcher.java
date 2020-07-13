package example.regex;

class GBoundaryMatcher
{
	public static void main (String[] args) throws java.lang.Exception
	{
		String input = "{%var1%, %var2%, %var3%} {%var4%, %var5%, %var6%}";
		final String re = "(^[^{]*\\{|\\G(?!^),\\h*)%([^%]+)%";

		
		// now use above regex in replaceAll method
		String repl = input.replaceAll(re, "$1#$2#");

		
		System.out.println(repl);
	}
}
