package PREP;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bookmarker.DB;
import bookmarker.Record;
import bookmarker.TokenType;
import util.Util;

public class UT
{
   private static final Logger LOG = LoggerFactory.getLogger(UT.class);
   private static String author1, author2;
   private static String bookName1, bookName2;
   private static int page1, page2;
   private static String suggestor1, suggestor2;
   private static String quote1, quote2;
   private static String date1, date2;

   @BeforeAll
   public static void before()
   {
      author1 = "author 1";
      author2 = "author 2";
      bookName1 = "book 1";
      bookName2 = "book 2";
      page1 = 100;
      page2 = 200;
      suggestor1 = "suggestor 1";
      suggestor2 = "suggestor 2";
      quote1 = "this";
      quote2 = "this is quoTe 2";
      date1 = "01-01-2001";
      date2 = "02-02-2002";
   }

   @Test
   public void tokenTypeTest()
   {
      TokenType[] x = TokenType.values();
      assertTrue(x[0].equals(TokenType.AUTHOR));
      assertTrue(x[1].equals(TokenType.BOOKNAME));
      assertTrue(x[2].equals(TokenType.SUGGESTOR));
      assertTrue(x[3].equals(TokenType.QUOTE));
   }

   @Test
   public void recordCreationTest()
   {
      Record rec1 = new Record(bookName1, author1, page1, date1, quote1, suggestor1);
      Record rec2 = new Record(bookName2, author2, page2, date2, quote2, suggestor2);

      assertTrue(rec1.getId() != rec2.getId());
   }

   @Test
   public void recordTokenizeTest()
   {
      Record rec1 = new Record(bookName1, author1, page1, date1, quote1, suggestor1);
      LOG.debug("record is {}", rec1.toString());
      assertTrue(!rec1.getTokenByType(TokenType.AUTHOR).retainAll(Arrays.asList(author1.split(" "))));
      assertTrue(!rec1.getTokenByType(TokenType.BOOKNAME).retainAll(Arrays.asList(bookName1.split(" "))));
      assertTrue(!rec1.getTokenByType(TokenType.SUGGESTOR).retainAll(Arrays.asList(suggestor1.split(" "))));
      assertTrue(
            !rec1.getTokenByType(TokenType.QUOTE).retainAll(Util.toLower(Arrays.asList(quote1.split(" ")))));
   }

   @Test
   public void dbTest()
   {
      DB db = new DB();
      assertTrue(db.listAll().isEmpty());

      recordCreator().forEach(rec->db.insert(rec));
      String quote = null;
      assertTrue(db.listAll().size() == 100);
      Record r = new Record("bookname_9", "authorname_4", null, null,
            quote, null);
      LOG.debug("Starting UT: dbTest");
      List<Record> res = db.find(r);
      assertTrue(res.size() > 0);
      // for (int i = 0; i < res.size(); ++i)
      // assertTrue(res.get(i).getQuote().contains("order"));

   }

   private List<Record> recordCreator()
   {
      List<Record> output = new LinkedList<>();

      String quote[] = { "In order for a Diameter node to perform failover procedures, it is",
            "   necessary for the node to maintain a pending message queue for a",
            "   given peer.  When an answer message is received, the corresponding",
            "   request is removed from the queue.  The Hop-by-Hop Identifier field",
            "   is used to match the answer with the queued request.",
            "   When a transport failure is detected, if possible, all messages in",
            "   the queue are sent to an alternate agent with the T flag set.  On",
            "   booting a Diameter client or agent, the T flag is also set on any",
            "   remaining records in non-volatile storage that are still waiting to",
            "   be transmitted.  An example of a case where it is not possible to",
            "   forward the message to an alternate server is when the message has a",
            "   fixed destination, and the unavailable peer is the message's final",
            "   destination (see Destination-Host AVP).  Such an error requires that",
            "   the agent return an answer message with the 'E' bit set and the",
            "   Result-Code AVP set to DIAMETER_UNABLE_TO_DELIVER.",
            "   It is important to note that multiple identical requests or answers",
            "   MAY be received as a result of a failover.  The End-to-End Identifier",
            "   field in the Diameter header along with the Origin-Host AVP MUST be",
            "   used to identify duplicate messages.",
            "   As described in Section 2.1, a connection request should be",
            "   periodically attempted with the failed peer in order to re-establish",
            "   the transport connection.  Once a connection has been successfully",
            "   established, messages can once again be forwarded to the peer.  This",
            "   is commonly referred to as \"failback\".",
            "The stable states that a state machine may be in are Closed, I-Open,",
            "   and R-Open; all other states are intermediate.  Note that I-Open and",
            "   R-Open are equivalent except for whether the initiator or responder",
            "   transport connection is used for communication.",
            "   A CER message is always sent on the initiating connection immediately",
            "   after the connection request is successfully completed.  In the case",
            "   of an election, one of the two connections will shut down.  The",
            "   responder connection will survive if the Origin-Host of the local",
            "   Diameter entity is higher than that of the peer; the initiator",
            "   connection will survive if the peer's Origin-Host is higher.  All",
            "   subsequent messages are sent on the surviving connection.  Note that",
            "   the results of an election on one peer are guaranteed to be the",
            "   inverse of the results on the other.",
            "   For TLS/TCP and DTLS/SCTP usage, a TLS/TCP and DTLS/SCTP handshake",
            "   SHOULD begin when both ends are in the closed state prior to any",
            "   Diameter message exchanges.  The TLS/TCP and DTLS/SCTP connection",
            "   SHOULD be established before sending any CER or CEA message to secure",
            "   and protect the capabilities information of both peers.  The TLS/TCP",
            "   and DTLS/SCTP connection SHOULD be disconnected when the state",
            "   machine moves to the closed state.  When connecting to responders",
            "   that do not conform to this document (i.e., older Diameter",
            "   implementations that are not prepared to received TLS/TCP and DTLS/",
            "   SCTP connections in the closed state), the initial TLS/TCP and DTLS/",
            "   SCTP connection attempt will fail.  The initiator MAY then attempt to",
            "   connect via TCP or SCTP and initiate the TLS/TCP and DTLS/SCTP",
            "   handshake when both ends are in the open state.  If the handshake is",
            "   successful, all further messages will be sent via TLS/TCP and DTLS/",
            "   SCTP.  If the handshake fails, both ends move to the closed state.",
            "   The state machine constrains only the behavior of a Diameter",
            "   implementation as seen by Diameter peers through events on the wire.",
            "   Any implementation that produces equivalent results is considered", "   compliant.",
            " When a connection request is received from a Diameter peer, it is",
            "   not, in the general case, possible to know the identity of that peer",
            "   until a CER is received from it.  This is because host and port",
            "   determine the identity of a Diameter peer; the source port of an",
            "   incoming connection is arbitrary.  Upon receipt of a CER, the",
            "   identity of the connecting peer can be uniquely determined from the", "   Origin-Host.",
            "   For this reason, a Diameter peer must employ logic separate from the",
            "   state machine to receive connection requests, accept them, and await",
            "   the CER.  Once the CER arrives on a new connection, the Origin-Host",
            "   that identifies the peer is used to locate the state machine",
            "   associated with that peer, and the new connection and CER are passed",
            "   to the state machine as an R-Conn-CER event.",
            "   The logic that handles incoming connections SHOULD close and discard",
            "   the connection if any message other than a CER arrives or if an",
            "   implementation-defined timeout occurs prior to receipt of CER.",
            "   Because handling of incoming connections up to and including receipt",
            "   of a CER requires logic, separate from that of any individual state",
            "   machine associated with a particular peer, it is described separately",
            "   in this section rather than in the state machine above.",
            "In general, Diameter can provide two different types of services to",
            "   applications.  The first involves authentication and authorization,",
            "   and it can optionally make use of accounting.  The second only makes",
            "   use of accounting.", "   When a service makes use of the authentication and/or authorization",
            "   portion of an application, and a user requests access to the network,",
            "   the Diameter client issues an auth request to its local server.  The",
            "   auth request is defined in a service-specific Diameter application",
            "   (e.g., NASREQ).  The request contains a Session-Id AVP, which is used",
            "   in subsequent messages (e.g., subsequent authorization, accounting,",
            "   etc.) relating to the user's session.  The Session-Id AVP is a means",
            "   for the client and servers to correlate a Diameter message with a", "   user session.",
            "   When a Diameter server authorizes a user to implement network",
            "   resources for a finite amount of time, and it is willing to extend",
            "   the authorization via a future request, it MUST add the",
            "   Authorization- Lifetime AVP to the answer message.  The",
            "   Authorization-Lifetime AVP defines the maximum number of seconds a",
            "   user MAY make use of the resources before another authorization",
            "   request is expected by the server.  The Auth-Grace-Period AVP",
            "   contains the number of seconds following the expiration of the",
            "   Authorization-Lifetime, after which the server will release all state",
            "   information related to the user's session.  Note that if payment for",
            "   services is expected by the serving realm from the user's home realm,",
            "   the Authorization-Lifetime AVP, combined with the Auth-Grace-Period",
            "   AVP, implies the maximum length of the session for which the home",
            "   realm is willing to be fiscally responsible.  Services provided past",
            "   the expiration of the Authorization-Lifetime and Auth-Grace-Period",
            "   AVPs are the responsibility of the access device.  Of course, the",
            "   actual cost of services rendered is clearly outside the scope of the", 
            "   protocol.",
            "   An access device that does not expect to send a re-authorization or a",
            "   session termination request to the server MAY include the Auth-",
            "   Session-State AVP with the value set to NO_STATE_MAINTAINED as a hint",
            "   to the server.  If the server accepts the hint, it agrees that since",
            "   no session termination message will be received once service to the",
            "   user is terminated, it cannot maintain state for the session.  If the",
            "   answer message from the server contains a different value in the",
            "   Auth-Session-State AVP (or the default value if the AVP is absent),",
            "   the access device MUST follow the server's directives.  Note that the",
            "   value NO_STATE_MAINTAINED MUST NOT be set in subsequent re-",
            "   authorization requests and answers.",
            "The base protocol does not include any authorization request",
            "   messages, since these are largely application-specific and are",
            "   defined in a Diameter application document.  However, the base",
            "   protocol does define a set of messages that are used to terminate",
            "   user sessions.  These are used to allow servers that maintain state",
            "   information to free resources.",
            "   When a service only makes use of the accounting portion of the",
            "   Diameter protocol, even in combination with an application, the",
            "   Session-Id is still used to identify user sessions.  However, the",
            "   session termination messages are not used, since a session is",
            "   signaled as being terminated by issuing an accounting stop message.",
            "   Diameter may also be used for services that cannot be easily",
            "   categorized as authentication, authorization, or accounting (e.g.,",
            "   certain Third Generation Partnership Project Internet Multimedia",
            "   System (3GPP IMS) interfaces).  In such cases, the finite state",
            "   machine defined in subsequent sections may not be applicable.",
            "   Therefore, the application itself MAY need to define its own finite",
            "   state machine.  However, such application-specific state machines",
            "   SHOULD follow the general state machine framework outlined in this",
            "   document such as the use of Session-Id AVPs and the use of STR/STA,",
            "   ASR/ASA messages for stateful sessions." };
      String bookName = "bookName_";
      String authorName = "authorName_";
      String SuggestedBy = "suggestorName_";
      String date = "01-01-2000";
      for (int i = 0; i < 110; ++i)
      {
         output.add(
               new Record(bookName + i % 10, authorName + i % 5, i, date, quote[i], SuggestedBy + i % 20));
      }

      return output;
   }
}
