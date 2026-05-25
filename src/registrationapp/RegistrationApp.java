package registrationapp;

// I am importing the Scanner class so I can read input from the user
import java.util.Scanner;

// I am creating the main RegistrationApp class that will run the program
public class RegistrationApp {

    public static void main(String[] args) {

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

        Login myLogin = new Login(firstName, lastName, username, password, cellPhoneNumber);

        String registrationResult = myLogin.registerUser();
        System.out.println(registrationResult);

        System.out.println();

        if (myLogin.checkUserName() && myLogin.checkPasswordComplexity() && myLogin.checkCellPhoneNumber()) {

            System.out.println("--- Login ---");

            System.out.println("Enter your username to log in: ");
            String loginUsername = input.nextLine();

            System.out.println("Enter your password to log in: ");
            String loginPassword = input.nextLine();

            System.out.println();

            String loginResult = myLogin.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginResult);

            // Only enter QuickChat if login was successful
            if (myLogin.loginUser(loginUsername, loginPassword)) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");
                System.out.println();

                // Ask how many messages the user wants to send
                System.out.println("How many messages would you like to send?");
                int numMessages = Integer.parseInt(input.nextLine().trim());

                int menuChoice = 0;

                // Keep running until user selects Quit (3)
                do {
                    System.out.println();
                    System.out.println("--- Menu ---");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.print("Enter your choice: ");
                    menuChoice = Integer.parseInt(input.nextLine().trim());

                    if (menuChoice == 1) {
                        // Loop for the number of messages the user specified
                        for (int i = 1; i <= numMessages; i++) {
                            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

                            System.out.println("Enter recipient cell number (e.g. +27718693002): ");
                            String recipient = input.nextLine();

                            System.out.println("Enter your message (max 250 characters): ");
                            String messageText = input.nextLine();

                            // Create the Message object
                            Message msg = new Message(i, recipient, messageText);

                            // Validate recipient
                            System.out.println(msg.checkRecipientCell());

                            // Validate message length
                            String lengthCheck = msg.checkMessageLength();
                            System.out.println(lengthCheck);

                            // Only proceed if both checks pass
                            if (!msg.checkRecipientCell().equals("Cell phone number successfully captured.") ||
                                !lengthCheck.equals("Message ready to send.")) {
                                System.out.println("Message not sent due to validation errors. Please try again.");
                                i--; // Don't count this as a successful message entry
                                continue;
                            }

                            // Show the generated details
                            System.out.println("Message ID generated: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());

                            // Ask what to do with the message
                            System.out.println("\nWhat would you like to do with this message?");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Enter your choice: ");
                            int sendChoice = Integer.parseInt(input.nextLine().trim());

                            String sendResult = msg.sentMessage(sendChoice);
                            System.out.println(sendResult);

                            // If stored, also write to JSON
                            if (sendChoice == 3) {
                                msg.storeMessage();
                            }

                            // Display full message details
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipientCell());
                            System.out.println("Message: " + msg.getMessageText());
                        }

                        // After all messages, show total sent
                        System.out.println("\nTotal number of messages sent: " + Message.getTotalMessagesSent());

                    } else if (menuChoice == 2) {
                        System.out.println("Coming Soon.");

                    } else if (menuChoice == 3) {
                        System.out.println("Goodbye!");

                    } else {
                        System.out.println("Invalid option, please enter 1, 2, or 3.");
                    }

                } while (menuChoice != 3);
            }
        }

        input.close();
    }
}