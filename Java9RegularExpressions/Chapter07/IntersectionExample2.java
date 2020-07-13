package example.regex;

import java.util.regex.*;
  
public class IntersectionExample2
{
  public static void main(String[] args)
  {
    final Pattern p = Pattern.compile("[\\p{InGreek}&&[\\p{Lu}]]");
    final String[] arr = new String[] {
      "Γ", "Δ", "Θ", "Ξ", "Π", "Σ", "Φ", "α", "β", "γ", "δ", "ε"
    };

    for (String s: arr) 
    {
      Matcher m = p.matcher(s);
      System.out.printf("[%s] matches: %s%n", s, m.matches());
    }
  }
}