//Importing Scanner 
import java.util.Scanner;


public class Main {
    public static void main (String[]args){
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
