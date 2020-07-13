package example.regex;

import java.util.regex.*;
  
public class SubtractionExample
{
  public static void main(String[] args)
  {
    final Pattern p = Pattern.compile("[\\p{Punct}&&[^+*/-]]");
    final String[] arr = new String[] {
      "!", "@", "#", "$", "%", "+", "-", "*", "/"
    };

    for (String s: arr) 
    {
      Matcher m = p.matcher(s);
      System.out.printf("[%s] matches: %s%n", s, m.matches());
    }
  }
}