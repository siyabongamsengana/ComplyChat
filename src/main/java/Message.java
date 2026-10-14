//Class for texting etc
//JSon file tools 
import java.io.FileWriter;
import java.io.IOException;
//Random number Tool 
import java.util.Random;
public class Message {
  
    //Declarations
  private  String MessageId;
 private   int MessageNumber;
  private  String RecipientCellNumber;
  private  String MessageText;
  private  String MessageHash;
  private int TotalSent = 0;
private String SentMessages = "";
 
//Checking if the Message ID characters are not more than 10
public boolean checkMessageID(String MessageId) {
    //Declarations
    boolean Correct = false;

    if (MessageId.length()<=10) {
        Correct = true;
    }

    return Correct;
}  

//Checking Recipient CellPhone tenz to see if they are correctly formeted.
public String checkRecipientCell(String RecipientCellNumber) {

    if (RecipientCellNumber.matches("^\\+\\d{5,11}$")) {
        return "Cell phone number successfully captured.";
    } else {
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }
}

//Creating a HAsh for the message to ensure security.
public String createMessageHash(String MessageId, int messageNumber, String messageText) {
//Declarations 
//Piece 1: The first two characters of the ID 
String FirstTwo = MessageId.substring(0,2);
//Piece 2: First and last word of the message
String[] words = messageText.split(" ");
//First word of the message
String FirstWord = words[0];
FirstWord = FirstWord.replaceAll("[^A-Za-z]", "");
//Last word of the message
String LastWord = words[words.length - 1];
//Removig the question mark
LastWord = LastWord.replaceAll("[^A-Za-z]", "");
//Piece 4: building the hash and returning it
String hash = (FirstTwo + ":" + messageNumber + ":" + FirstWord + LastWord).toUpperCase();
return hash;
}
//Piece 5: MAking the Rndom Number 
public String generateMessageID() {
    //Random number method
    Random rand = new Random();
    String id = "";
    //Loop 
    for (int i = 0; i < 10; i++) {
        id = id + rand.nextInt(10);
    }
 return id;
}
//Telling the user if they sent a message/stored it or how to delete it
public String SentMessage(int choice) {

    if (choice == 1) {
        return "Message sent";
    } else if (choice == 2) {
        return "Press 0 to delete the message";
    } else {
        return "Message stored";
    }
}

//Total messages sent 
public int returnTotalMessages() {
    return TotalSent;
}
//Printing the texts 
public String printMessages() {
    return SentMessages;
}

//How the messages will be recived/ sent from a person to another.
public void addSentMessage(String messageId, String hash, String recipient, String message) {

    TotalSent = TotalSent + 1;
   //They will each be on  new line for neat purposes 
    SentMessages = SentMessages + "Message ID: " + messageId + "\n"
                                + "Message Hash: " + hash + "\n"
                                + "Message Recipient: " + recipient + "\n"
                                + "Message: " + message + "\n\n";
}
//Message length limits
public String checkMessageLength(String text) {

    if (text.length() <= 250) {
        return "Message has the correct length.";
    } else {
        return "Message exceeds 250 characters by " + (text.length() - 250) + "; please reduce the size.";
    }
}

//Storing the mesage in a JSON file
public void storeMessage(String messageId,String hash, String recipient, String message){
String json = "{\n" + "  \"messageId\": \"" + messageId + "\",\n";
json = json + "  \"hash\": \"" + hash + "\",\n";
json = json + " \"recipient\": \"" + recipient + "\",\n";
json = json + "  \"message\": \"" + message + "\"\n";
//Closing the JSOn file
json = json + "}\n";

//Source: Oracle (2026). Java SE API documentation, java.io.FileWriter.
//https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/FileWriter.html
//Accessed: 14 October 2026
 try {
        FileWriter writer = new FileWriter("messages.json", true);
        writer.write(json);
        writer.close();
    } catch (IOException e) {
        System.out.println("Could not store the message.");
    }
}
}
