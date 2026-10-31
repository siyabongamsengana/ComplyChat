//Importing JUnit tools for testing my Message class
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

//Importing Path so each test uses its own JSON file
import java.nio.file.Path;

//Testing the methods from Message.java for Part 3
public class MessageTest {

    //Declaration for a temporary folder used by my tests
    //This stops the tests from changing my real messages.json file
    @TempDir
    Path TestFolder;

    //Creating sample messages to test the Part 3 features
    private Message createTestMessages() {

        //Declarations
        Message msg = new Message(TestFolder.resolve("messages.json"));
        msg.setSender("Siya");

        //Adding two sent messages
        msg.addSentMessage("1111111111", "11:1:DIDCAKE", "+27831234567", "Did you get the cake?");
        msg.addSentMessage("2222222222", "22:2:ITTIME", "+27839876543", "It is dinner time!");

        //Adding a message that was disregarded
        msg.addDisregardedMessage("3333333333", "33:3:HELLOTHERE", "+27831234567", "Hello there");

        //Adding two stored messages
        msg.storeMessage("4444444444", "44:4:WHERETIME", "+27839876543",
                "Where are you? You are late! I have asked you to be on time.");
        msg.storeMessage("5555555555", "55:5:OKYOU", "+27839876543",
                "Ok, I am leaving without you.");

        return msg;
    }

    //Testing if a valid Message ID is accepted
    @Test
    public void testMessageID() {
        Message msg = new Message(TestFolder.resolve("id.json"));

        assertTrue(msg.checkMessageID("1234567890"));
        assertFalse(msg.checkMessageID("12345678901"));
        assertFalse(msg.checkMessageID(null));
    }

    //Testing that the generated Message ID has 10 digits
    @Test
    public void testGenerateMessageID() {
        Message msg = new Message(TestFolder.resolve("id.json"));

        String id = msg.generateMessageID();

        assertEquals(10, id.length());
        assertTrue(id.matches("\\d{10}"));
    }

    //Testing that the message hash follows the correct format
    @Test
    public void testMessageHash() {
        Message msg = new Message(TestFolder.resolve("hash.json"));

        String hash = msg.createMessageHash("1234567890", 1, "Hello there");

        assertEquals("12:1:HELLOTHERE", hash);
    }

    //Testing the recipient's cell number
    @Test
    public void testRecipientCell() {
        Message msg = new Message(TestFolder.resolve("cell.json"));

        assertEquals("Cell phone number successfully captured.",
                msg.checkRecipientCell("+27831234567"));
        assertTrue(msg.checkRecipientCell("0831234567").contains("incorrectly formatted"));
    }

    //Testing that messages must not exceed 250 characters
    @Test
    public void testMessageLength() {
        Message msg = new Message(TestFolder.resolve("length.json"));

        assertEquals("Message ready to send.", msg.checkMessageLength("Hello"));
        assertEquals("Please enter a message.", msg.checkMessageLength(""));
        assertTrue(msg.checkMessageLength("A".repeat(251)).contains("by 1"));
    }

    //Testing if messages are added to their correct ArrayLists
    @Test
    public void testMessageArrays() {
        Message msg = createTestMessages();

        assertEquals(2, msg.getSentList().size());
        assertEquals(1, msg.getDisregardedList().size());
        assertEquals(2, msg.getStoredList().size());
        assertEquals(5, msg.getIdList().size());
        assertEquals(5, msg.getHashList().size());
    }

    //Testing the total number of sent messages
    @Test
    public void testTotalSentMessages() {
        Message msg = createTestMessages();

        assertEquals(2, msg.returnTotalMessages());
    }

    //Testing if the sender and recipient appear in the sent messages report
    @Test
    public void testSentMessageReport() {
        Message msg = createTestMessages();

        String report = msg.printMessages();

        assertTrue(report.contains("Sender: Siya"));
        assertTrue(report.contains("Recipient: +27831234567"));
        assertTrue(report.contains("Did you get the cake?"));
    }

    //Testing if the program can find the longest message
    @Test
    public void testLongestMessage() {
        Message msg = createTestMessages();

        Message.Record longest = msg.longestMessage();

        assertNotNull(longest);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.text);
    }

    //Testing if the program can search for a message using its ID
    @Test
    public void testFindMessageByID() {
        Message msg = createTestMessages();

        Message.Record found = msg.findByID("2222222222");

        assertNotNull(found);
        assertEquals("It is dinner time!", found.text);
        assertNull(msg.findByID("9999999999"));
    }

    //Testing if the program can find messages using the recipient number
    @Test
    public void testFindMessagesByRecipient() {
        Message msg = createTestMessages();

        //There are three active messages for this number: one sent and two stored
        assertEquals(3, msg.findByRecipient("+27839876543").size());
        assertTrue(msg.findByRecipient("+27000000000").isEmpty());
    }

    //Testing if a message can be deleted using its hash
    @Test
    public void testDeleteMessageByHash() {
        Message msg = createTestMessages();

        Message.Record deleted = msg.deleteByHash("22:2:ITTIME");

        assertNotNull(deleted);
        assertEquals("It is dinner time!", deleted.text);
        assertNull(msg.findByID("2222222222"));
        assertFalse(msg.getHashList().contains("22:2:ITTIME"));
        assertNull(msg.deleteByHash("NOHASH"));
    }

    //Testing if the full stored message report has the required details
    @Test
    public void testStoredMessageReport() {
        Message msg = createTestMessages();

        String report = msg.storedReport();

        assertTrue(report.contains("Message ID: 4444444444"));
        assertTrue(report.contains("Message Hash: 44:4:WHERETIME"));
        assertTrue(report.contains("Sender: Siya"));
        assertTrue(report.contains("Recipient: +27839876543"));
        assertTrue(report.contains("Status: Stored"));
    }

    //Testing if stored messages can be read from the JSON file
    @Test
    public void testReadStoredMessagesFromJSON() {
        createTestMessages();

        //Opening a new Message object using the same JSON file
        Message reopened = new Message(TestFolder.resolve("messages.json"));

        assertEquals(2, reopened.getStoredList().size());
        assertEquals("Ok, I am leaving without you.", reopened.findByID("5555555555").text);
    }

    //Testing if deleting a stored message updates the JSON file
    @Test
    public void testDeleteStoredMessageFromJSON() {
        Message msg = createTestMessages();

        msg.deleteByHash("55:5:OKYOU");

        Message reopened = new Message(TestFolder.resolve("messages.json"));

        assertEquals(1, reopened.getStoredList().size());
        assertNull(reopened.findByID("5555555555"));
    }

    //Testing if special characters are saved and loaded properly
    @Test
    public void testJSONSpecialCharacters() {
        Message msg = new Message(TestFolder.resolve("special.json"));

        msg.storeMessage("6666666666", "66:6:TEST", "+27831234567", "He said \"hello\" \\ again");

        Message reopened = new Message(TestFolder.resolve("special.json"));

        assertEquals("He said \"hello\" \\ again", reopened.getStoredMessages().get(0).text);
    }
}
