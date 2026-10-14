//Importing Scanner 
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //Calling my methods/objects 
        //DECLARATIONS 
        Login LLogin = new Login();

        // Username
        String usernameInput;

        do {
            System.out.println("Enter a username of your own desire");
            usernameInput = input.nextLine();

            if (!LLogin.checkUserName(usernameInput)) {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }

        } while (!LLogin.checkUserName(usernameInput));

        LLogin.setEnteredUsername(usernameInput);


        // Password
        String PasswordInput;

        do {
            System.out.println("Enter a Strong Password");
            PasswordInput = input.nextLine();

            if (!LLogin.checkPasswordComplexity(PasswordInput)) {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }

        } while (!LLogin.checkPasswordComplexity(PasswordInput));

        LLogin.setEnteredPassword(PasswordInput);


        // Cell Phone Number
        String CellPhoneNumberInput;

        do {
            System.out.println("Enter your Cell Phone Number");
            CellPhoneNumberInput = input.nextLine();

            if (!LLogin.checkCellPhoneNumber(CellPhoneNumberInput)) {
                System.out.println("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
            }

        } while (!LLogin.checkCellPhoneNumber(CellPhoneNumberInput));

        LLogin.setEnteredCellPhoneNumber(CellPhoneNumberInput);


        // GRC Role
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


        // Registration
        String result = LLogin.registerUser();
        System.out.println(result);


        //Login into the App Logic
        //Keeps asking until they log in or the account gets locked
        while (!LLogin.getLoggedIn() && !LLogin.getLimitReached()) {

            //Taking their username 
            System.out.println("Welcome back; Enter your User name");
            String usernameLogin = input.nextLine();
            LLogin.setEnteredUsername(usernameLogin);

            //Taking their Password
            System.out.println("Enter Your Password");
            String PasswordLogin = input.nextLine();
            LLogin.setEnteredPassword(PasswordLogin);

            //Showing them If they Loggedin 
            String loginResult = LLogin.returnLoginStatus();
            System.out.println(loginResult);
        }

        //Only logged in users can get to the messaging part
        if (LLogin.getLoggedIn()) {
            System.out.println("Logged in as: " + LLogin.getRole());
            //Message object
            Message msg = new Message();
            //Welcome to Complychat. app ya batho ba GRC
            System.out.println("Welcome to ComplyChat.");
            //Asking user how many messages they desire to send 
            System.out.println("How many messages do you want to enter?");
            
            //Declarations
             //parsing int into a string
            int Messages = Integer.parseInt(input.nextLine());
            int choice = 0;
            int messagesEntered = 0;
            
//When they enter the app, they choose what they want to do within it 
while (choice != 3) {

    System.out.println("1) Send Messages");
    System.out.println("2) Show recently sent messages");
    System.out.println("3) Quit");
   
//parsing int into a string
    choice = Integer.parseInt(input.nextLine());
   
    
     //choice 1
    if (choice == 1) {
     if (messagesEntered >= Messages) {
    System.out.println("You have already entered all " + Messages + " messages.");
} else {
    String id = msg.generateMessageID();
  
  
  //Declarations
  String recipient;
  String cellResult;
  String text;
  String lengthResult;
  
  //Loop to get the reciver of the texts number
do {
    System.out.println("Enter the recipient's cell number");
    recipient = input.nextLine();
    cellResult = msg.checkRecipientCell(recipient);
    System.out.println(cellResult);
} while (!cellResult.equals("Cell phone number successfully captured."));

    
 //Loop to guide the user towards the correc text length
do {
    System.out.println("Enter your message (250 characters maximum)");
    text = input.nextLine();
    lengthResult = msg.checkMessageLength(text);
    System.out.println(lengthResult);
} while (!lengthResult.equals("Message has the correct length."));
  
//Putting/geting a hash a Hash in the text and the unique user ID 
String hash = msg.createMessageHash(id, messagesEntered, text);
System.out.println("Message ID generated: " + id);
System.out.println("Message Hash: " + hash);    
 
//Giving the user options to either stop the message, send it or save the message for later 
System.out.println("1) Send message");
System.out.println("2) Disregard message");
System.out.println("3) Store message to send later");
int sendChoice = Integer.parseInt(input.nextLine());
System.out.println(msg.SentMessage(sendChoice));  

//User doing what they choose to do
if (sendChoice == 1) {
    msg.addSentMessage(id, hash, recipient, text);
    //Message details( information of the message)
System.out.println("Message ID: " + id);
System.out.println("Message Hash: " + hash);
System.out.println("Message Recipient: " + recipient);
System.out.println("Message: " + text);
} else if (sendChoice == 2) {
    int deleteKey = Integer.parseInt(input.nextLine());
    if (deleteKey == 0) {
        System.out.println("Message deleted.");
    }
} else {
    msg.storeMessage(id, hash, recipient, text);
}
//Counting the amount of messages entered 
messagesEntered = messagesEntered + 1;
 
     
     
  }
     
     
    //choice 2
    } else if (choice == 2) {
        System.out.println("Coming Soon...");
     

    //Choice3
    } else if (choice == 3) {
        System.out.println("Thank you for using ComplyChat");
    } else {
        System.out.println("Please choose 1, 2 or 3.");
    }
    
}
System.out.println("Total messages sent: " + msg.returnTotalMessages());
        } else {
            System.out.println("Too many failed attempts. Your account is locked.");
        }
    }




}