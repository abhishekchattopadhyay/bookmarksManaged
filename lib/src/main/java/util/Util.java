package util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Util
{
   public static final String REGEX = "\\s+|,\\s*|\\.\\s*";

   public static List<String> toLower(List<String> strList)
   {
      return strList.stream().map(String::toLowerCase).collect(Collectors.toList());
   }

   /**
    * 
    * @param o --> the object to be dumped to json
    * @param pretty --> true/false is pretty print is required
    * @return -> String
    * @throws JsonProcessingException
    */
   public static String dumpJson(Object o, boolean pretty)
      throws JsonProcessingException
   {
      ObjectMapper mapper = new ObjectMapper();
      if (pretty)
      {
         ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
         return writer.writeValueAsString(o);
      }
      return mapper.writeValueAsString(o);
   }

   /**
    * 
    * @param o --> the object to be dumped to json
    * @param pretty --> true/false is pretty print is required
    * @param f --> file where the json needs to be dumped
    * @throws IOException
    * @throws DatabindException
    * @throws StreamWriteException
    */
   public void dumpJson(Object o, boolean pretty, String path)
      throws StreamWriteException,
         DatabindException,
         IOException
   {
      ObjectMapper mapper = new ObjectMapper();
      ObjectWriter writer = null;
      if (pretty)
         writer = mapper.writer(new DefaultPrettyPrinter());
      else
         writer = mapper.writer();

      writer.writeValue(Paths.get(path).toFile(), o);
   }
   
   public static void  readJsonFile(String path, Object o, List<Object> lo)
   {
      try {
         // create object mapper instance
         ObjectMapper mapper = new ObjectMapper();
         // convert JSON string to object
         lo.add(mapper.readValue(Paths.get(path).toFile(), o.getClass()));
         Arrays.asList(mapper.readValue(Paths.get(path).toFile(), o.getClass()));

     } catch (Exception ex) {
         ex.printStackTrace();
     }
   }
   // https://attacomsian.com/blog/jackson-read-write-json

}
