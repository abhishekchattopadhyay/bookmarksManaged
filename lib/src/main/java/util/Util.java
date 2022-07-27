package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
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

}
