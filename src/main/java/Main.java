//Importing Scanner
import java.util.Scanner;
import java.util.List;

public class Main {
    //Checking the number the user enters so the app does not crash
    public static int getNumber(Scanner input) {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //Calling my methods/objects
        //DECLARATIONS
        Login LLogin = new Login();

        //Username
        String usernameInput;
        do {
            System.out.println("Enter a username of your own desire");
            usernameInput = input.nextLine();
            if (!LLogin.checkUserName(usernameInput)) {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        } while (!LLogin.checkUserName(usernameInput));
        LLogin.setEnteredUsername(usernameInput);

        //Password
        String PasswordInput;
        do {
            System.out.println("Enter a Strong Password");
            PasswordInput = input.nextLine();
            if (!LLogin.checkPasswordComplexity(PasswordInput)) {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        } while (!LLogin.checkPasswordComplexity(PasswordInput));
        LLogin.setEnteredPassword(PasswordInput);

        //Cell Phone Number
        String CellPhoneNumberInput;
        do {
            System.out.println("Enter your Cell Phone Number");
            CellPhoneNumberInput = input.nextLine();
            if (!LLogin.checkCellPhoneNumber(CellPhoneNumberInput)) {
                System.out.println("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
            }
        } while (!LLogin.checkCellPhoneNumber(CellPhoneNumberInput));
        LLogin.setEnteredCellPhoneNumber(CellPhoneNumberInput);

        //GRC Role
        String RoleInput;
        boolean validRole;
        do {
            System.out.println("Enter your GRC role: Analyst / Reviewer / Admin");
            RoleInput = input.nextLine();
            validRole = RoleInput.equalsIgnoreCase("Analyst")
                    || RoleInput.equalsIgnoreCase("Reviewer")
                    || RoleInput.equalsIgnoreCase("Admin");
            if (!validRole) {
                System.out.println("Role not recognised; please enter Analyst, Reviewer or Admin.");
            }
        } while (!validRole);
        LLogin.setEnteredRole(RoleInput);

        //Registration
        String result = LLogin.registerUser();
        System.out.println(result);

        //Login into the App Logic
        //Keeps asking until they log in or the account gets locked
        while (!LLogin.getLoggedIn() && !LLogin.getLimitReached()) {
            System.out.println("Welcome back; Enter your User name");
            String usernameLogin = input.nextLine();
            LLogin.setEnteredUsername(usernameLogin);
            System.out.println("Enter Your Password");
            String PasswordLogin = input.nextLine();
            LLogin.setEnteredPassword(PasswordLogin);
            String loginResult = LLogin.returnLoginStatus();
            System.out.println(loginResult);
        }

        //Only logged in users can get to the messaging part
        if (LLogin.getLoggedIn()) {
            System.out.println("Logged in as: " + LLogin.getRole());
            Message msg = new Message();
            msg.setSender(usernameInput);
            //Welcome to ComplyChat. App ya batho ba GRC
            System.out.println("Welcome to ComplyChat.");
            System.out.println("How many messages do you want to enter?");

            //Declarations
            int Messages = getNumber(input);
            while (Messages < 0) {
                System.out.println("Please enter 0 or more messages.");
                Messages = getNumber(input);
            }
            int choice = 0;
            int messagesEntered = 0;

            //When they enter the app, they choose what they want to do within it
            while (choice != 3) {
                System.out.println("\n1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                choice = getNumber(input);

                //Choice 1: Sending, disregarding or storing a message
                if (choice == 1) {
                    if (messagesEntered >= Messages) {
                        System.out.println("You have already entered all " + Messages + " messages.");
                    } else {
                        String id = msg.generateMessageID();
                        String recipient;
                        String cellResult;
                        String messageText;
                        String lengthResult;

                        //Checking the recipient's number
                        do {
                            System.out.println("Enter the recipient's cell number");
                            recipient = input.nextLine();
                            cellResult = msg.checkRecipientCell(recipient);
                            System.out.println(cellResult);
                        } while (!cellResult.equals("Cell phone number successfully captured."));

                        //Checking the length of the message
                        do {
                            System.out.println("Enter your message (250 characters maximum)");
                            messageText = input.nextLine();
                            lengthResult = msg.checkMessageLength(messageText);
                            System.out.println(lengthResult);
                        } while (!lengthResult.equals("Message ready to send."));

                        //Making a hash and showing the generated ID
                        String hash = msg.createMessageHash(id, messagesEntered + 1, messageText);
                        System.out.println("Message ID generated: " + id);
                        System.out.println("Message Hash: " + hash);

                        int sendChoice;
                        do {
                            System.out.println("1) Send message");
                            System.out.println("2) Disregard message");
                            System.out.println("3) Store message to send later");
                            sendChoice = getNumber(input);
                            if (sendChoice < 1 || sendChoice > 3) {
                                System.out.println("Please choose 1, 2 or 3.");
                            }
                        } while (sendChoice < 1 || sendChoice > 3);
                        System.out.println(msg.SentMessage(sendChoice));

                        //Doing what the user chose
                        if (sendChoice == 1) {
                            msg.addSentMessage(id, hash, recipient, messageText);
                            System.out.println("Message ID: " + id);
                            System.out.println("Message Hash: " + hash);
                            System.out.println("Message Recipient: " + recipient);
                            System.out.println("Message: " + messageText);
                        } else if (sendChoice == 2) {
                            int deleteKey = getNumber(input);
                            if (deleteKey == 0) {
                                msg.addDisregardedMessage(id, hash, recipient, messageText);
                                System.out.println("Message disregarded.");
                            } else {
                                System.out.println("Message was not disregarded. Storing it instead.");
                                msg.storeMessage(id, hash, recipient, messageText);
                            }
                        } else {
                            msg.storeMessage(id, hash, recipient, messageText);
                        }
                        messagesEntered = messagesEntered + 1;
                    }

                //Choice 2: Showing recently sent messages
                } else if (choice == 2) {
                    System.out.println(msg.printMessages());

                //Choice 3: Closing the app
                } else if (choice == 3) {
                    System.out.println("Thank you for using ComplyChat");

                //Choice 4: Part 3 Stored Messages menu
                } else if (choice == 4) {
                    int storedChoice = 0;
                    while (storedChoice != 7) {
                        System.out.println("\nSTORED MESSAGES");
                        System.out.println("1) Display sender and recipient of sent messages");
                        System.out.println("2) Display the longest sent message");
                        System.out.println("3) Search for a message using its ID");
                        System.out.println("4) Search for messages using a recipient");
                        System.out.println("5) Delete a message using its hash");
                        System.out.println("6) Display full message report");
                        System.out.println("7) Back to main menu");
                        storedChoice = getNumber(input);

                        if (storedChoice == 1) {
                            //Showing who sent the message and who received it
                            List<Message.Record> sent = msg.getSentMessages();
                            if (sent.isEmpty()) {
                                System.out.println("No sent messages found.");
                            } else {
                                for (Message.Record record : sent) {
                                    System.out.println("Sender: " + record.sender);
                                    System.out.println("Recipient: " + record.recipient);
                                    System.out.println();
                                }
                            }
                        } else if (storedChoice == 2) {
                            //Finding the message with the most characters
                            Message.Record longest = msg.longestMessage();
                            if (longest == null) {
                                System.out.println("No messages found.");
                            } else {
                                System.out.println("Longest message: " + longest.text);
                                System.out.println("Message length: " + longest.text.length());
                            }
                        } else if (storedChoice == 3) {
                            //Searching for a message using the message ID
                            System.out.println("Enter the Message ID");
                            String searchID = input.nextLine();
                            Message.Record found = msg.findByID(searchID);
                            System.out.println(found == null ? "Message not found." : found.details());
                        } else if (storedChoice == 4) {
                            //Searching for all messages sent to a recipient
                            System.out.println("Enter the recipient's cell number");
                            String searchRecipient = input.nextLine();
                            System.out.println(msg.report(msg.findByRecipient(searchRecipient)));
                        } else if (storedChoice == 5) {
                            //Deleting a message using its hash
                            System.out.println("Enter the Message Hash");
                            String searchHash = input.nextLine();
                            Message.Record deleted = msg.deleteByHash(searchHash);
                            System.out.println(deleted == null ? "Message not found." : "Message successfully deleted.");
                        } else if (storedChoice == 6) {
                            //Showing the full report of sent and stored messages
                            System.out.println("FULL MESSAGE REPORT");
                            System.out.println(msg.report(msg.getAllActiveMessages()));
                        } else if (storedChoice != 7) {
                            System.out.println("Please choose a number from 1 to 7.");
                        }
                    }
                } else {
                    System.out.println("Please choose 1, 2, 3 or 4.");
                }
            }
            System.out.println("Total messages sent: " + msg.returnTotalMessages());
        } else {
            System.out.println("Too many failed attempts. Your account is locked.");
        }
    }
}
