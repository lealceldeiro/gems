package example.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatastropicBacktracking
{
  public static void main(String[] args)
  {
    final int MAX = 30;
    
    for (int i = 1; i < MAX; i++)
    {
      StringBuilder sb1 = new StringBuilder(i);
      StringBuilder sb2 = new StringBuilder(i);
      
      for (int j = i; j > 0; j--)
      {
        sb1.append('a');
        sb2.append("a?");
      }
      
      sb2.append(sb1);

      final Pattern p = Pattern.compile("^" + sb2.toString() + "$");
      Matcher m = p.matcher(sb1.toString());

      long start = System.nanoTime();
      m.matches();
      long end = System.nanoTime();
      
      System.out.printf("%s:: ( %sms ) :: Pattern <%s>, Input <%s>%n", i, (end - start)/1_000_000, sb2, sb1);
    }
  }
}