/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registrationapp;

/**
 *
 * @author Student
 */
// I am creating a Login class to store user details and validate them
// This class will handle registration and login functionality
public class Login {
    // I am declaring instance variables to store the user's information
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String cellPhoneNumber;
 
    // I am creating a constructor to set up the Login object with the user's details
    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }
 
    // I am creating this method to check that the username contains an underscore
    // and is no more than five characters long
    public boolean checkUserName() {
        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }
 
    // I am creating this method to check that the password meets all complexity rules:
    // at least 8 characters, one capital letter, one number, and one special character
    public boolean checkPasswordComplexity() {
        // I am first checking the length requirement
        if (password.length() < 8) {
            return false;
        }
 
        // I am setting up flags to track which requirements are met
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
 
        // I am looping through each character in the password to check each rule
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
 
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialCharacter = true;
            }
        }
 
        // I am returning true only if all three requirements are met
        if (hasCapital && hasNumber && hasSpecialCharacter) {
            return true;
        } else {
            return false;
        }
    }
 
    // I am creating this method to check that the cell phone number has an
    // international country code and is the correct length
    public boolean checkCellPhoneNumber() {
        // I am using a regular expression to validate the phone number format
        // The number must start with + followed by a country code and then the local number
        // Regular expression pattern researched and adapted from the Java SE documentation on Pattern class
        // and validated against South African number formatting standards (E.164 international format)
        // Source: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
        
        if (cellPhoneNumber.matches("^\\+27[0-9]{9}$")) {
            return true;
        } else {
            return false;
        }
    }
 
    // I am creating this method to attempt to register the user
    // It checks each condition in order and returns the appropriate message
    public String registerUser() {
        // I am checking the username first
        if (checkUserName() == false) {
            return "Username is not correctly formatted; please ensure that your username " +
                   "contains an underscore and is no more than five characters in length.";
        }
 
        // I am checking the password next
        if (checkPasswordComplexity() == false) {
            return "Password is not correctly formatted; please ensure that the password " +
                   "contains at least eight characters, a capital letter, a number, and a special character.";
        }
 
        // I am checking the cell phone number last
        if (checkCellPhoneNumber() == false) {
            return "Cell number is incorrectly formatted or does not contain an international code; " +
                   "please correct the number and try again.";
        }
 
        // I am returning success messages if all three checks passed
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }
 
    // I am creating this method to check if the entered login details match what was registered
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (username.equals(enteredUsername) && password.equals(enteredPassword)) {
            return true;
        } else {
            return false;
        }
    }
 
    // I am creating this method to return the correct login status message
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword) == true) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
}
