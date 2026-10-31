//Importing die JAva tools ill use 

//Handels file erros
import java.io.IOException;
//Utf - 8 text when reading and saving file
import java.nio.charset.StandardCharsets;
//Reads and writes the JSON file
import java.nio.file.Files;
//Tell Java where the JSON file is
import java.nio.file.Path;
import java.nio.file.Paths;
//Control how files are saved
import java.nio.file.StandardOpenOption;
//Stores multiple messages and grows when needed
import java.util.ArrayList;
//Allows us to work with collections of messages
import java.util.List;
//random message IDs
import java.util.Random;
//Help find information inside JSON text
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Message {
    //Keeping all the details of one message together to look good
    public static class Record {
      
        //Declarations
        public final String id; 
        public final String hash;
        public final String sender;
        public final String recipient;
        public final String text;
        public final String status;
        
                
           //Getting the message information and saving it
        public Record(String id, String hash, String sender, String recipient, String text, String status) {
            this.id = id; 
            this.hash = hash; 
            this.sender = sender;
            this.recipient = recipient;
            this.text = text;
            this.status = status;
        }
        
        //Using this to assign the values to the message
        public String details() {
            return "Message ID: " + id + "\nMessage Hash: " + hash
                + "\nSender: " + sender + "\nRecipient: " + recipient
                + "\nMessage: " + text + "\nStatus: " + status;
        }
    }
     //My ArrayLists for keeping track of messages
    //Array list declarations
    private final List<Record> sentMessages = new ArrayList<>();
    private final List<Record> disregardedMessages = new ArrayList<>();
    private final List<Record> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();
    private final Path storagePath;
    private String sender = "ComplyChat User";
 
    //My constructor to (When I create a Message object, use messages.json as my storage file).
    public Message() { this(Paths.get("messages.json")); }
    public Message(Path storagePath) {
        this.storagePath = storagePath;
        loadStoredMessages();
    }
    
     //Setting the sender of the message
 public void setSender(String sender) { this.sender = sender; }


    //Checking that the Message ID is within the character limit
    public boolean checkMessageID(String id) { return id != null && id.length() <= 10; }

    //Checking if the recipient cellphone number is correctly formatted
    public String checkRecipientCell(String cell) {
        return cell != null && cell.matches("^\\+\\d{5,11}$")
            ? "Cell phone number successfully captured."
            : "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    //Checking that the message is not empty or more than 250 characters
    public String checkMessageLength(String text) {
        if (text == null || text.isEmpty()) return "Please enter a message.";
        return text.length() <= 250 ? "Message ready to send."
            : "Message exceeds 250 characters by " + (text.length() - 250) + "; please reduce the size.";
    }

    //Generating a random 10 digit Message ID
    public String generateMessageID() {
        //Declarations
        Random random = new Random();
        String id;
        do {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 10; i++) result.append(random.nextInt(10));
            id = result.toString();
        } while (messageIDs.contains(id));
        return id;
    }

    //Creating a hash using the ID, message number and message words
    public String createMessageHash(String id, int number, String text) {
        if (id == null || id.length() < 2 || text == null || text.trim().isEmpty())
            throw new IllegalArgumentException("Message ID and text are required.");
        String[] words = text.trim().split("\\s+");
        return (id.substring(0, 2) + ":" + number + ":"
                + words[0].replaceAll("[^A-Za-z]", "")
                + words[words.length - 1].replaceAll("[^A-Za-z]", "")).toUpperCase();
    }

    //Telling the user if the message was sent, disregarded or stored
    public String SentMessage(int choice) {
        if (choice == 1) return "Message successfully sent.";
        if (choice == 2) return "Press 0 to delete the message.";
        if (choice == 3) return "Message successfully stored.";
        return "Invalid choice";
    }


    //Saving the message ID and hash in their lists
    private void track(Record record) {
        messageIDs.add(record.id);
        messageHashes.add(record.hash);
    }

    //Adding a sent message to the sent messages list
    public void addSentMessage(String id, String hash, String recipient, String text) {
        Record record = new Record(id, hash, sender, recipient, text, "Sent");
        sentMessages.add(record); track(record);
    }

    //Adding a disregarded message to its own list
    public void addDisregardedMessage(String id, String hash, String recipient, String text) {
        Record record = new Record(id, hash, sender, recipient, text, "Disregarded");
        disregardedMessages.add(record); track(record);
    }

    //Storing a message and saving it in the JSON file
    public void storeMessage(String id, String hash, String recipient, String text) {
        Record record = new Record(id, hash, sender, recipient, text, "Stored");
        storedMessages.add(record); track(record);
        saveStoredMessages();
    }

    //Returning the number of sent messages
    public int returnTotalMessages() { return sentMessages.size(); }

    //Displaying the report of sent messages
    public String printMessages() { return report(sentMessages); }

    //Getting the text of all sent messages
    public ArrayList<String> getSentList() {
        ArrayList<String> list = new ArrayList<>();
        for (Record r : sentMessages) list.add(r.text);
        return list;
    }

    //Getting the text of all disregarded messages
    public ArrayList<String> getDisregardedList() {
        ArrayList<String> list = new ArrayList<>();
        for (Record r : disregardedMessages) list.add(r.text);
        return list;
    }

    //Getting the text of all stored messages
    public ArrayList<String> getStoredList() {
        ArrayList<String> list = new ArrayList<>();
        for (Record r : storedMessages) list.add(r.text);
        return list;
    }

    //Getting the list of message hashes
    public ArrayList<String> getHashList() { return new ArrayList<>(messageHashes); }

    //Getting the list of message IDs
    public ArrayList<String> getIdList() { return new ArrayList<>(messageIDs); }


    //Recording the message based on the choice made by the user
    public void recordMessage(String sender, String recipient, String text,
                              String id, String hash, String flag) {
        setSender(sender);
        if (flag.equalsIgnoreCase("Sent")) {
            addSentMessage(id, hash, recipient, text);
        } else if (flag.equalsIgnoreCase("Disregard") || flag.equalsIgnoreCase("Disregarded")) {
            addDisregardedMessage(id, hash, recipient, text);
        } else if (flag.equalsIgnoreCase("Stored")) {
            storeMessage(id, hash, recipient, text);
        } else {
            throw new IllegalArgumentException("Unknown message flag: " + flag);
        }
    }

    //Getting copies of the sent message records
    public List<Record> getSentMessages() { return new ArrayList<>(sentMessages); }

    //Getting copies of the disregarded message records
    public List<Record> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }

    //Getting copies of the stored message records
    public List<Record> getStoredMessages() { return new ArrayList<>(storedMessages); }

    //Getting a copy of the message hashes
    public List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }

    //Getting a copy of the message IDs
    public List<String> getMessageIDs() { return new ArrayList<>(messageIDs); }

    //Putting sent and stored messages together for searching
    public List<Record> getAllActiveMessages() {
        List<Record> records = new ArrayList<>(sentMessages);
        records.addAll(storedMessages);
        return records;
    }

    //Searching for a message using its ID
    public Record findByID(String id) {
        for (Record r : getAllActiveMessages()) if (r.id.equals(id)) return r;
        return null;
    }

    //Finding all sent or stored messages for one recipient
    public List<Record> findByRecipient(String recipient) {
        //Declarations
        List<Record> results = new ArrayList<>();
        for (Record r : getAllActiveMessages()) if (r.recipient.equals(recipient)) results.add(r);
        return results;
    }

    //Finding the stored message with the most characters
    public Record longestStoredMessage() {
        //Declarations
        Record longest = null;
        for (Record r : storedMessages)
            if (longest == null || r.text.length() > longest.text.length()) longest = r;
        return longest;
    }

    //Finding the longest sent or stored message
    public Record longestMessage() {
        //Declarations
        Record longest = null;
        for (Record r : getAllActiveMessages())
            if (longest == null || r.text.length() > longest.text.length()) longest = r;
        return longest;
    }

    //Deleting a message using its hash
    public Record deleteByHash(String hash) {
        for (List<Record> group : List.of(sentMessages, storedMessages, disregardedMessages)) {
            for (int i = 0; i < group.size(); i++) {
                Record r = group.get(i);
                if (r.hash.equals(hash)) {
                    group.remove(i);
                    messageIDs.remove(r.id);
                    messageHashes.remove(r.hash);
                    if (group == storedMessages) saveStoredMessages();
                    return r;
                }
            }
        }
        return null;
    }

    //Creating a report with the details of the messages
    public String report(List<Record> records) {
        if (records.isEmpty()) return "No messages found.";
        //Declarations
        StringBuilder out = new StringBuilder();
        for (Record r : records) out.append(r.details()).append("\n\n");
        return out.toString();
    }

    //Displaying the report of stored messages
    public String storedReport() { return report(storedMessages); }


    //Changing special characters so the JSON file can save them
    private static String jsonEscape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                .replace("\r", "\\r").replace("\t", "\\t");
    }

    //Changing saved JSON characters back to normal text
    private static String jsonUnescape(String s) {
        //Declarations
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                switch (next) {
                    case 'n': out.append('\n'); break;
                    case 'r': out.append('\r'); break;
                    case 't': out.append('\t'); break;
                    case '"': out.append('"'); break;
                    case '\\': out.append('\\'); break;
                    default: out.append(next);
                }
            } else out.append(ch);
        }
        return out.toString();
    }

    //Finding one message detail in the JSON text
    private static String field(String json, String name) {
        Pattern p = Pattern.compile("\\\"" + Pattern.quote(name) + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"\\\\])*)\\\"");
        Matcher m = p.matcher(json);
        return m.find() ? jsonUnescape(m.group(1)) : "";
    }

    //Saving all stored messages inside one JSON file
    private void saveStoredMessages() {
        //Declarations
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Record r = storedMessages.get(i);
            json.append("  {\"messageId\":\"").append(jsonEscape(r.id))
                .append("\",\"hash\":\"").append(jsonEscape(r.hash))
                .append("\",\"sender\":\"").append(jsonEscape(r.sender))
                .append("\",\"recipient\":\"").append(jsonEscape(r.recipient))
                .append("\",\"message\":\"").append(jsonEscape(r.text)).append("\"}");
            if (i < storedMessages.size() - 1) json.append(',');
            json.append('\n');
        }
        json.append("]\n");
        try {
            Files.writeString(storagePath, json, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save messages: " + e.getMessage(), e);
        }
    }

    //Reading messages previously saved in the JSON file
    public void loadStoredMessages() {
        if (!Files.exists(storagePath)) return;
        try {
            String json = Files.readString(storagePath, StandardCharsets.UTF_8);
            storedMessages.clear();
            messageHashes.clear(); messageIDs.clear();
            for (Record r : sentMessages) track(r);
            for (Record r : disregardedMessages) track(r);
            //Finding each JSON message without confusing brackets inside text
            boolean quoted = false, escaped = false;
            int start = -1;
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                if (escaped) { escaped = false; continue; }
                if (quoted && c == '\\') { escaped = true; continue; }
                if (c == '"') { quoted = !quoted; continue; }
                if (!quoted && c == '{') start = i;
                if (!quoted && c == '}' && start >= 0) {
                    String obj = json.substring(start, i + 1);
                    String id = field(obj, "messageId");
                    if (!id.isEmpty()) {
                        Record r = new Record(id, field(obj, "hash"),
                                field(obj, "sender").isEmpty() ? "Unknown" : field(obj, "sender"),
                                field(obj, "recipient"), field(obj, "message"), "Stored");
                        storedMessages.add(r); track(r);
                    }
                    start = -1;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read messages: " + e.getMessage(), e);
        }
    }
}
   