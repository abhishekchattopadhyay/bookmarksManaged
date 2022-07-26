package bookmarker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB
{
   private static final Logger LOG = LoggerFactory.getLogger(DB.class);
   private volatile List<Record> records;

   public DB()
   {
      super();
      this.records = new LinkedList<>();
      LOG.debug("starting with {} records", records.size());
   }

   /**
    * Finds the Record r The logic first matches the Author, then the book name, then the suggested by and
    * then the tokens in the quotation available in the to be searched record. Fields are ignored whenever a
    * particular detail is null or missing
    * 
    * @param r --> the Record
    * @return
    */
   public synchronized List<Record> find(Record r)
   {
      if (r == null)
      {
         LOG.debug("Record is empty: nothing to search");
         return null;
      }

      LOG.debug("starting Search for {}", r.toString());

      // Start with full list of records
      List<Record> searchResult = new LinkedList<>();
      List<Record> searchList = null;
      // for each type of Token
      for (TokenType type : TokenType.values())
      {
         // get the token list by type from "to be searched tokens" from record provided by user
         HashSet<String> tokenList = r.getTokenByType(type);
         LOG.debug("Matching tokens of type: {}. tokens are {}", type, tokenList);

         // if this type of token list is empty then continue
         if (tokenList.isEmpty())
         {
            LOG.debug("token list is empty: continue {}", tokenList.size());
            continue;
         }
         /*
          * if the searchResult is empty then we have not found any record yet search in the master record
          * list. however if certain searchResult exists, then it would mean that we have found some records
          * and we need to further refine it.
          */

         if (!searchResult.isEmpty())
         {
            LOG.debug("search list is not empty: make new copy");
            searchList = new LinkedList<>(searchResult);
            searchResult.clear();
         }
         else
         {
            LOG.debug("searchList is empty: use full records");
            searchList = new LinkedList<>(records);
         }

         Iterator<Record> iter = searchList.listIterator();
         while (iter.hasNext())
         {
            Record rec = iter.next();
            HashSet<String> rTokens = rec.getTokenByType(type);
            // if the record in DB doesn't have any token in this type then continue
            if (rTokens.isEmpty())
            {
               LOG.debug("Record has no token of type {}", type);
               continue;
            }

            HashSet<String> tempTokenSet = new HashSet<>(rTokens);
            tempTokenSet.retainAll(tokenList);

            /*
             * if for the type of token the entries in the search criteria and the record has no match then
             * remove this entry from further searches Import thing to note here is that the order of finding
             * intersection is fixed.
             */
            if (tempTokenSet.isEmpty() || tempTokenSet.size() != tokenList.size())
               iter.remove();
            else
               searchResult.add(rec);
         }
         LOG.debug("total records are {} SearchResults are {} ", records.size(), searchResult.size());
         for (var sr : searchResult)
            LOG.debug(sr.toString());
      }
      if (searchResult.size() > 0)
         for (var sr : searchResult)
            LOG.info(sr.toString());
      return searchResult;
   }

   /**
    * Provides a copy of the list of records
    * 
    * @return
    */
   public synchronized List<Record> listAll()
   {
      return new LinkedList<>(records);
   }

   /**
    * drops the complete DB
    */
   public synchronized void drop()
   {
      records.clear();
   }

   /**
    * 
    * @param r The record that will be matched in the DB and removed
    */
   public synchronized void drop(Record r)
   {
      drop(r, 0);
   }

   /**
    * 
    * @param r The record that will be matched in the DB and removed
    * @param span 0 means only the first found result is deleted non zero means all the matched records would
    *           be removed
    */
   public synchronized void drop(Record r, int span)
   {
      var res = find(r);
      if (res == null)
         return;
      if (span == 0)
      {
         records.remove(res.get(span));
      }
      else
      {
         res.removeAll(res);
      }
   }

   @Deprecated
   public void setRecords(List<Record> records)
   {
      this.records = records;
      LOG.debug("total records now are {}", records.size());
   }

   public synchronized void insert(Record r)
   {
      records.add(r);
   }

}
