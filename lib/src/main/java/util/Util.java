package util;

import java.util.List;
import java.util.stream.Collectors;

public class Util
{
   public static final String REGEX = "\\s+|,\\s*|\\.\\s*";
   public static List<String> toLower(List<String> strList)
   {
      return strList.stream().map(String::toLowerCase).collect(Collectors.toList());
   }
}
