package bookmarker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import util.Util;

public class Record
{
   private static final Logger LOG = LoggerFactory.getLogger(Record.class);
   private final short MIN_STR_LEN_FOR_TOKENIZATION = 3;

   // UUID of this record
   private final UUID id;
   // name of the book
   @JsonProperty("book")
   private String bookName;
   // page number
   @JsonProperty("page")
   private Integer pageNumber;
   // date when this record was created
   private String date;
   // quote to remember
   private String quote;
   // suggested by person name
   private String suggestedBy;
   // Author
   private String author;

   private Map<TokenType, HashSet<String>> tokens;

   public Record(String bookName, String author, Integer pageNumber, String date, String quote,
         String suggestedBy)
   {
      this.id = UUID.randomUUID();
      this.bookName = bookName;
      this.author = author;
      this.pageNumber = pageNumber;//==null?-1:pageNumber;
      this.date = date;
      this.quote = quote;
      this.suggestedBy = suggestedBy;
      this.tokens = new HashMap<>();
      LOG.debug("book: {},author: {}, page: {}, date: {}, quote:{}", bookName,author,pageNumber,date,quote );
      tokenize();
   }

   private List<String> extractTokenByType(TokenType type)
   {
      String str = "";
      switch (type)
      {

         case BOOKNAME:
            str = bookName;
            break;
         case AUTHOR:
            str = author;
            break;
         case SUGGESTOR:
            str = suggestedBy;
            break;
         case QUOTE:
            str = quote;
            break;
         default:
            // no tokens will be added
            break;
      }
      if (str == null || str.isBlank() || str.isEmpty() || str.length() < MIN_STR_LEN_FOR_TOKENIZATION)
      {
         LOG.debug("null/untokenizable string skipped\n");
         return null;
      }

      LOG.debug("Incoming String is {}", str);
      List<String> returnList = Arrays.asList(str.split(Util.REGEX, 0));
      LOG.debug("POST regex split tokens are {}", returnList);
      List<String> temp = Util.toLower(returnList);
      LOG.debug("lower casing it {}", temp);
      temp.removeAll(Arrays.asList("", null));
      LOG.debug("POST cleanup split tokens are {}\n Record done", temp);
      return temp;
   }

   private void tokenize()
   {
      for (TokenType t : TokenType.values())
      {
         if (!tokens.containsKey(t))
            tokens.put(t, new HashSet<String>());
         List<String> tokenStrs = extractTokenByType(t);
         if (tokenStrs != null && !tokenStrs.isEmpty())
         {
            tokens.get(t).addAll(tokenStrs);
         }
      }
   }

   public HashSet<String> getTokenByType(TokenType t)
   {
      return tokens.get(t);
   }

   public String getBookName()
   {
      return bookName;
   }

   public void setBookName(String bookName)
   {
      this.bookName = bookName;
   }

   public int getPageNumber()
   {
      return pageNumber;
   }

   public void setPageNumber(int pageNumber)
   {
      this.pageNumber = pageNumber;
   }

   public String getDate()
   {
      return date;
   }

   public void setDate(String date)
   {
      this.date = date;
   }

   public String getQuote()
   {
      return quote;
   }

   public void setQuote(String quote)
   {
      this.quote = quote;
   }

   public String getSuggestedBy()
   {
      return suggestedBy;
   }

   public void setSuggestedBy(String suggestedBy)
   {
      this.suggestedBy = suggestedBy;
   }

   public UUID getId()
   {
      return id;
   }

   public String getAuthor()
   {
      return author;
   }

   public void setAuthor(String author)
   {
      this.author = author;
   }

   @Override
   public String toString()
   {
      try
      {
         return (Util.dumpJson(this, false));
      }
      catch (JsonProcessingException e)
      {
         // TODO implement catch JsonProcessingException
         LOG.error("Unexpected Exception", e);
         throw new UnsupportedOperationException("Unexpected Exception", e);
      }
   }
}
