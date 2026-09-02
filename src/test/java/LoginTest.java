import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
 
//JUnit tests to prove that Login.java's methods actually work as expected
//Each test uses the exact test data given in the brief
public class LoginTest {
 
//Checking that a correctly formatted username (has "_" and <=5 characters) returns true
@Test
void testUsernameCorrectlyFormatted() {
    Login login = new Login();
    assertTrue(login.checkUserName("kyl_1"));
}
 
//Checking that an incorrectly formatted username (no "_", too long) returns false
@Test
void testUsernameIncorrectlyFormatted() {
    Login login = new Login();
    assertFalse(login.checkUserName("kyle!!!!!!"));
}
 
//Checking that a password with 8+ chars, a capital, a number, and a special
//character returns true (meets all complexity rules)
@Test
void testPasswordMeetsComplexity() {
    Login login = new Login();
    assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
}
 
//Checking that a weak password (no capital, no number, no special character)
//correctly returns false
@Test
void testPasswordDoesNotMeetComplexity() {
    Login login = new Login();
    assertFalse(login.checkPasswordComplexity("password"));
}
 
//Checking that a correctly formatted SA cell number (+27 followed by digits)
//returns true
@Test
void testCellPhoneCorrectlyFormatted() {
    Login login = new Login();
    assertTrue(login.checkCellPhoneNumber("+27838968976"));
}
 
//Checking that a cell number with no "+" / international code returns false
@Test
void testCellPhoneIncorrectlyFormatted() {
    Login login = new Login();
    assertFalse(login.checkCellPhoneNumber("08966553"));
}
 
//Testing a full login flow that SHOULD succeed:
//Step 1 - register a valid account
//Step 2 - "type in" the SAME username and password to log in
//Step 3 - loginUser() should return true, since the details match
@Test
void testLoginSuccessful() {
    Login login = new Login();
    
    //Registering the account first (needed before we can log in)
    login.setEnteredUsername("kyl_1");
    login.setEnteredPassword("Ch&&sec@ke99!");
    login.setEnteredCellPhoneNumber("+27838968976");
    login.registerUser();
    
    //Simulating the user typing the correct details to log in
    login.setEnteredUsername("kyl_1");
    login.setEnteredPassword("Ch&&sec@ke99!");
    
    assertTrue(login.loginUser());
}
 
//Testing a full login flow that SHOULD fail:
//Step 1 - register a valid account (same as above)
//Step 2 - "type in" the correct username but a WRONG password
//Step 3 - loginUser() should return false, since the password doesn't match
@Test
void testLoginFailed() {
    Login login = new Login();
    
    //Registering the account first (needed before we can log in)
    login.setEnteredUsername("kyl_1");
    login.setEnteredPassword("Ch&&sec@ke99!");
    login.setEnteredCellPhoneNumber("+27838968976");
    login.registerUser();
    
    //Simulating the user typing the WRONG password to log in
    login.setEnteredUsername("kyl_1");
    login.setEnteredPassword("WrongPassword1!");
    
    assertFalse(login.loginUser());
}
 
}
