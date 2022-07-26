package bookmarker;

import java.util.HashSet;
import java.util.List;

public class Tokens
{
   private final TokenType t;
   private HashSet<String> tokens;
   
   public Tokens(TokenType t) {
      this.t = t;
      this.tokens = new HashSet<>();
   }

   public HashSet<String> getTokens()
   {
      return tokens;
   }

   public void setTokens(List<String> tokens)
   {
      this.tokens.addAll(tokens);
   }
   
   public TokenType getTokenType()
   {
      return t;
   }
}
