package example.regex;

import java.util.regex.*;
  
public class IntersectionExample
{
  public static void main(String[] args)
  {
    final Pattern p = Pattern.compile("[1-7&&[3-6]&&[5-8]]");

    for (int i=0; i<10; i++)
    {
      String s = String.valueOf(i);
      Matcher m = p.matcher(s);
      System.out.printf("[%s] matches: %s%n", s, m.matches());
    }
  }
}