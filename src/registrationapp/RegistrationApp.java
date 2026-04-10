package registrationapp;

// I am importing the Scanner class so I can read input from the user
import java.util.Scanner;

// I am creating the main RegistrationApp class that will run the program
public class RegistrationApp {

    public static void main(String[] args) {

        // I am creating a Scanner object to read what the user types
        Scanner input = new Scanner(System.in);

        System.out.println("=== Welcome to the Registration and Login System ===");
        System.out.println();
        System.out.println("--- Registration ---");

        System.out.println("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.println("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.println("Enter a username (must contain _ and be no more than 5 characters): ");
        String username = input.nextLine();

        System.out.println("Enter a password (at least 8 characters, 1 capital, 1 number, 1 special character): ");
        String password = input.nextLine();

        System.out.println("Enter your cell phone number with international code (e.g. +27838968976): ");
        String cellPhoneNumber = input.nextLine();

        System.out.println();

        // I am creating a Login object using the details the user entered
        Login myLogin = new Login(firstName, lastName, username, password, cellPhoneNumber);

        // I am calling registerUser() and printing the result to the console
        String registrationResult = myLogin.registerUser();
        System.out.println(registrationResult);

        System.out.println();

        // I am only allowing the user to log in if registration was fully successful
        if (myLogin.checkUserName() && myLogin.checkPasswordComplexity() && myLogin.checkCellPhoneNumber()) {

            System.out.println("--- Login ---");

            System.out.println("Enter your username to log in: ");
            String loginUsername = input.nextLine();

            System.out.println("Enter your password to log in: ");
            String loginPassword = input.nextLine();

            System.out.println();

            // I am calling returnLoginStatus() and printing the login message
            String loginResult = myLogin.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginResult);
        }

        // I am closing the Scanner when I am done using it
        input.close();
    }
}