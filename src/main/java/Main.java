//Importing Scanner 
import java.util.Scanner;


public class Main {
    public static void main (String[]args){
    Scanner input = new Scanner(System.in);
      //Calling my methods/objects 
    //DECLARATIONS 
    Login LLogin = new Login();
    

// Taking Username
String usernameInput;

do {
    System.out.println("Welcome to ComplyChat");
    System.out.println("Enter a username of your own desire");
    usernameInput = input.nextLine();
} while (!LLogin.checkUserName(usernameInput));

LLogin.setEnteredUsername(usernameInput);


// Taking Password
String PasswordInput;

do {
    System.out.println("Enter a Strong Password");
    PasswordInput = input.nextLine();
} while (!LLogin.checkPasswordComplexity(PasswordInput));

LLogin.setEnteredPassword(PasswordInput);


// Taking Cell Phone Number
String CellPhoneNumberInput;

do {
    System.out.println("Enter your Cell Phone Number");
    CellPhoneNumberInput = input.nextLine();
} while (!LLogin.checkCellPhoneNumber(CellPhoneNumberInput));

LLogin.setEnteredCellPhoneNumber(CellPhoneNumberInput);


// Registration
String result = LLogin.registerUser();
System.out.println(result);

    
 
    
   //Login into the App Logic
   //Taking their username 
   while (!LLogin.getLimitReached()) {
   
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
        
    }
}
