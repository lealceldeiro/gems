package example.regex;

import java.util.regex.*;
  
public class UnionExample
{
  public static void main(String[] args)
  {
    final String re = "[#@.[^\\p{Punct}\\s]]";
    final String[] arr = new String[] {
      "A", "#", "@", "1", "5", " ", "\n", ":", ".", "a", "%", "-", "3"
    };

    for (String s: arr) 
    {
      System.out.printf("[%s] matches: %s%n", s, s.matches(re));
    }
  }
}