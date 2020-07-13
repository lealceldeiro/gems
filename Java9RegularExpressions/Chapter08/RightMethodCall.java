package example.regex;

import java.util.regex.*;
  
public class RightMethodCall
{
  public static void main(String[] args)
  {
    final Pattern p = Pattern.compile("(\\d*)\\.?(\\d+)");
    final String input = "123.75";
 
    Matcher m = p.matcher(input);
    
    if (m.find()) // or m.lookingAt() or m.matches()
    {
      System.out.printf("Number Value [%s], Decimal Value [%s]%n", m.group(1), m.group(2));
    }
  }
}