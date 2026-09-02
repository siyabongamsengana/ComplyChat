
public class Login {
    // What I will be capturing from the user
    // Declarations
    
    //Storing user infromation 
    private String UserName;
    private String Password;
    private String CellPhoneNumber;
    private String Role;
    
    //Entered Information by user 
    private String EnteredUserName;
    private String EnteredPassword;
    private String EnteredCellPhoneNumber;
    private String EnteredRole;
    
    /*Loged in Status to ensure they dont bypass
    the login requirements*/
    private boolean Registered = false;
    
    //Rate limiting to prevent Brute Force attacks
    private int Limit =0;
    private boolean LimitReached = false;
    
    public boolean loginUser(){
   //They cant login if the limit is reached
    if(LimitReached== true){
        return false;
    }
    
    boolean LoginApp = false;
    
    if (UserName.equals(EnteredUserName) && Password.equals(EnteredPassword)) {
        LoginApp = true;
    //Limit is Re-set as the user logged in
        Limit = 0;
     //Increment every time the user is entering wrong credentials   
    } else {
        Limit = Limit + 1;
        

    // CHECKING IF LIMIT IS REACHED 
    if (Limit >= 4){
    LimitReached = true;
    }
    }
    
    return LoginApp;
    }

    


//Methods that check the information 

//Checking Username 
public boolean checkUserName(String UserName){
//Declarations
boolean Correct = false;
    
/*Checking if username is less than 5 characters 
    and has an underscore*/

if ( UserName.contains("_") && UserName.length()<=5){
Correct = true;}

return Correct;
}


//Checking the complexity of the password 
public boolean checkPasswordComplexity(String Password){
   //Declarations
boolean Correct = false;

if ( Password.matches(".*[A-Z].*")&& Password.matches(".*[0-9].*") && Password.matches(".*[^A-Za-z0-9].*") && Password.length()>=8){
Correct = true;}

return Correct; 
}

//Checking the Cell phone number matches 

public boolean checkCellPhoneNumber(String CellPhoneNumber){
    
      //Declarations
boolean Correct = false;

if (CellPhoneNumber.matches("^\\+\\d{5,11}$")){
Correct = true;}

return Correct; 
}

//Account Registation logic and method
public String registerUser(){
//Message for not entering proper information format
//Username
    if (!checkUserName(EnteredUserName)) {
    return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
}


//Password 
if (!checkPasswordComplexity(EnteredPassword)) {
    return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
}

//Cell phone number 
if(!checkCellPhoneNumber(EnteredCellPhoneNumber)){
    return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
}

//Registration success conditions 
//Declarations
Registered = true;

UserName = EnteredUserName;
Password = EnteredPassword;
CellPhoneNumber = EnteredCellPhoneNumber;

return "Welcome to ComplyChat";
}


//Logged in Message to the user
public String returnLoginStatus(){
    if(loginUser()){
        return "Welcome back " + UserName;
    } else { 
        return "Username or password incorrect, please try again.";
    }
}



//Method so that Main can access Login while being safe
//Username method to be called 
public void setEnteredUsername(String username) {
EnteredUserName = username;
}

//Password method to be called 
public void setEnteredPassword(String Password) {
   EnteredPassword  = Password;
}
//CellPhone number method to be called 
public void setEnteredCellPhoneNumber(String CellPhoneNumber ) {
    EnteredCellPhoneNumber = CellPhoneNumber;
}

//Role  method to be called 
public void setEnteredRole(String Role ) {
   EnteredRole = Role;
}
  
//Geting if the User is regsterd or not 

public boolean getRegistered() {
    return Registered; 
    }









}