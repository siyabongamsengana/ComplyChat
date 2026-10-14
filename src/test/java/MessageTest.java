import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//JUnit tests to prove that Message.java's methods work as expected
//Each test uses the test data from the Part 2 brief
public class MessageTest {

//Test data from the assighnment examples
//Test case 1 (Message 1)
String recipient1 = "+27718693002";
String text1 = "Hi Mike, can you join us for dinner tonight?";
//Message 2
String text2 = "Hi Keegan, did you receive the payment?";

//Message length: 250 characters or fewer should be ready to send
@Test
void testMessageLengthSuccess() {
    Message msg = new Message();
    assertEquals("Message ready to send.", msg.checkMessageLength(text1));
}

//Message length: 255 characters is 5 over the limit, so the error must say by 5
@Test
void testMessageLengthFailure() {
    Message msg = new Message();
    String longText = "a".repeat(255);
    assertEquals("Message exceeds 250 characters by 5; please reduce the size.",
                 msg.checkMessageLength(longText));
}

//Recipient number: correctly formatted (starts with +27, correct length)
@Test
void testRecipientCellSuccess() {
    Message msg = new Message();
    assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell(recipient1));
}

//Recipient number: no international code, so it must fail
@Test
void testRecipientCellFailure() {
    Message msg = new Message();
    assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                 msg.checkRecipientCell(recipient2));
}

//Message hash for Test case 1 should be 00:0:HITONIGHT
@Test
void testMessageHashTestCase1() {
    Message msg = new Message();
    assertEquals("00:0:HITONIGHT", msg.createMessageHash("0012345678", 0, text1));
}

//Testing the remainder of the message hashes in a loop
//Message number goes up by one each time, so message 2 is number 1
@Test
void testMessageHashesInALoop() {
    Message msg = new Message();
    String[] texts = { text1, text2 };
    String[] expected = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };

    for (int i = 0; i < texts.length; i++) {
        assertEquals(expected[i], msg.createMessageHash("0012345678", i, texts[i]));
    }
}

//Message ID: a generated ID must be ten digits and pass the ID check
@Test
void testGeneratedMessageIDIsValid() {
    Message msg = new Message();
    String id = msg.generateMessageID();
    assertEquals(10, id.length());
    assertTrue(msg.checkMessageID(id));
}

//Message ID: 11 characters is too long, so the check must return false
@Test
void testMessageIDTooLong() {
    Message msg = new Message();
    assertFalse(msg.checkMessageID("12345678901"));
}

//Message sent: the user selected Send
@Test
void testSentMessageSend() {
    Message msg = new Message();
    assertEquals("Message successfully sent.", msg.SentMessage(1));
}

//Message sent: the user selected Disregard
@Test
void testSentMessageDisregard() {
    Message msg = new Message();
    assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
}

//Message sent: the user selected Store
@Test
void testSentMessageStore() {
    Message msg = new Message();
    assertEquals("Message successfully stored.", msg.SentMessage(3));
}

//Total messages: after one message is sent the total must be 1
@Test
void testReturnTotalMessages() {
    Message msg = new Message();
    msg.addSentMessage("0012345678", "00:0:HITONIGHT", recipient1, text1);
    assertEquals(1, msg.returnTotalMessages());
}

//Print messages: the sent message details must be in the printed text
@Test
void testPrintMessages() {
    Message msg = new Message();
    msg.addSentMessage("0012345678", "00:0:HITONIGHT", recipient1, text1);
    assertTrue(msg.printMessages().contains("Message ID: 0012345678"));
    assertTrue(msg.printMessages().contains("Message Hash: 00:0:HITONIGHT"));
}

}
