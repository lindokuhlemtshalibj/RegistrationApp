package registrationapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    // ==================== assertEquals Tests ====================

    // Test: Username correctly formatted - system returns welcome message
    @Test
    public void testRegisterUserUsernameCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertEquals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.", result);
    }

    // Test: Username incorrectly formatted - system returns error message
    @Test
    public void testRegisterUserUsernameIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    // Test: Password meets complexity requirements
    @Test
    public void testRegisterUserPasswordCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertEquals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.", result);
    }

    // Test: Password does not meet complexity requirements
    @Test
    public void testRegisterUserPasswordIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        String result = login.registerUser();
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    // Test: Cell phone correctly formatted
    @Test
    public void testRegisterUserCellCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        String result = login.registerUser();
        assertEquals("Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.", result);
    }

    // Test: Cell phone incorrectly formatted
    @Test
    public void testRegisterUserCellIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        String result = login.registerUser();
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.", result);
    }

    // ==================== assertTrue/assertFalse Tests ====================

    // Test: Login successful returns true
    @Test
    public void testLoginSuccessful() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // Test: Login failed returns false
    @Test
    public void testLoginFailed() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongpassword"));
    }

    // Test: Username correctly formatted returns true
    @Test
    public void testCheckUserNameCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName());
    }

    // Test: Username incorrectly formatted returns false
    @Test
    public void testCheckUserNameIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName());
    }

    // Test: Password meets complexity returns true
    @Test
    public void testCheckPasswordCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity());
    }

    // Test: Password does not meet complexity returns false
    @Test
    public void testCheckPasswordIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse(login.checkPasswordComplexity());
    }

    // Test: Cell phone correctly formatted returns true
    @Test
    public void testCheckCellCorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    // Test: Cell phone incorrectly formatted returns false
    @Test
    public void testCheckCellIncorrect() {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }
}