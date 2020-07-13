package example.regex;

import java.util.regex.*;
  
public class MissingMethodCall
{
  public static void main(String[] args)
  {
    final Pattern p = Pattern.compile("(\\d*)\\.?(\\d+)");
    final String input = "123.75";
 
    Matcher m = p.matcher(input);
    
    System.out.printf("Number Value [%s], Decimal Value [%s]%n", m.group(1), m.group(2));
  }
}