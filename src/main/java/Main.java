//Importing Scanner 
import java.util.Scanner;


public class Main {
    public static void main (String[]args){
    Scanner input = new Scanner(System.in);
      //Calling my methods/objects 
    //DECLARATIONS 
    Login LLogin = new Login();
    

    //Loops if user is not yet registerd 
    while (!LLogin.getRegistered()) {
   
    //TAKING IN USER INPUT 
    //Username Input 
    System.out.println("Enter a username of your own desire");
    String usernameInput = input.nextLine();
    LLogin.setEnteredUsername(usernameInput);
    
    //Taking in Password
       System.out.println("Enter a Strong Password");
       String PasswordInput = input.nextLine();
       LLogin.setEnteredPassword(PasswordInput);
       
    //Taking in Cell Phone Number 
       System.out.println("Enter your Cell Phone Number");
       String CellPhoneNumberInput = input.nextLine();
       LLogin.setEnteredCellPhoneNumber(CellPhoneNumberInput);
    
    //Taking in Users Role
       System.out.println("Enter your Role: Analyst/Reviewer/Admin");
       String RoleInput = input.nextLine();
       LLogin.setEnteredRole(RoleInput);
    
    //Registration Logic
    String result = LLogin.registerUser();
    System.out.println(result);
    
 } 
    
   //Login into the App Logic
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
        
}
