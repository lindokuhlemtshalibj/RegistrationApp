package registrationapp;

import java.util.Scanner;

/**
 * Main application class for QuickChat.
 * Part 3 additions: 4th main menu option "Stored Messages" with sub-menu (a-f).
 *
 * @author Lindokuhle Mtshali
 */
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

            if (myLogin.loginUser(loginUsername, loginPassword)) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");
                System.out.println();

                // Sender display name for stored messages report
                String senderDisplay = firstName + " " + lastName;

                System.out.println("How many messages would you like to send?");
                int numMessages = Integer.parseInt(input.nextLine().trim());

                int menuChoice = 0;

                do {
                    System.out.println();
                    System.out.println("--- Main Menu ---");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Stored Messages");    // Part 3 - new 4th option
                    System.out.println("4) Quit");
                    System.out.print("Enter your choice: ");
                    menuChoice = Integer.parseInt(input.nextLine().trim());

                    // --------------------------------------------------------
                    // Option 1: Send Messages
                    // --------------------------------------------------------
                    if (menuChoice == 1) {
                        for (int i = 1; i <= numMessages; i++) {
                            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

                            System.out.println("Enter recipient cell number (e.g. +27718693002): ");
                            String recipient = input.nextLine();

                            System.out.println("Enter your message (max 250 characters): ");
                            String messageText = input.nextLine();

                            Message msg = new Message(i, recipient, messageText);

                            System.out.println(msg.checkRecipientCell());
                            String lengthCheck = msg.checkMessageLength();
                            System.out.println(lengthCheck);

                            if (!msg.checkRecipientCell().equals("Cell phone number successfully captured.")
                                    || !lengthCheck.equals("Message ready to send.")) {
                                System.out.println("Message not sent due to validation errors. Please try again.");
                                i--;
                                continue;
                            }

                            System.out.println("Message ID generated: " + msg.getMessageID());
                            System.out.println("Message Hash: "         + msg.getMessageHash());

                            System.out.println("\nWhat would you like to do with this message?");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Enter your choice: ");
                            int sendChoice = Integer.parseInt(input.nextLine().trim());

                            String sendResult = msg.sentMessage(sendChoice);
                            System.out.println(sendResult);

                            if (sendChoice == 3) {
                                msg.storeMessage();
                            }

                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID: "   + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: "    + msg.getRecipientCell());
                            System.out.println("Message: "      + msg.getMessageText());
                        }
                        System.out.println("\nTotal number of messages sent: " + Message.getTotalMessagesSent());

                    // --------------------------------------------------------
                    // Option 2: Show recently sent messages
                    // --------------------------------------------------------
                    } else if (menuChoice == 2) {
                        // Create a temporary Message to call printMessages()
                        if (Message.getAllMessages().isEmpty()) {
                            System.out.println("No messages to display yet.");
                        } else {
                            Message temp = new Message(0, "+27000000000", "temp");
                            System.out.println(temp.printMessages());
                        }

                    // --------------------------------------------------------
                    // Option 3: Stored Messages (Part 3 - sub-menu a-f)
                    // --------------------------------------------------------
                    } else if (menuChoice == 3) {
                        int storedMenuChoice = 0;
                        do {
                            System.out.println();
                            System.out.println("--- Stored Messages Menu ---");
                            System.out.println("a) Display sender and recipient of all stored messages");
                            System.out.println("b) Display the longest stored message");
                            System.out.println("c) Search by message ID");
                            System.out.println("d) Search by recipient");
                            System.out.println("e) Delete a message using message hash");
                            System.out.println("f) Display sent messages report");
                            System.out.println("0) Back to main menu");
                            System.out.print("Enter your choice: ");
                            String subChoice = input.nextLine().trim().toLowerCase();

                            switch (subChoice) {
                                case "a":
                                    System.out.println(Message.displayStoredSendersAndRecipients(senderDisplay));
                                    break;

                                case "b":
                                    System.out.println(Message.getLongestStoredMessage());
                                    break;

                                case "c":
                                    System.out.print("Enter message ID to search: ");
                                    String searchID = input.nextLine().trim();
                                    System.out.println(Message.searchByMessageID(searchID));
                                    break;

                                case "d":
                                    System.out.print("Enter recipient cell number to search: ");
                                    String searchRecipient = input.nextLine().trim();
                                    System.out.println(Message.searchByRecipient(searchRecipient));
                                    break;

                                case "e":
                                    System.out.print("Enter message hash to delete: ");
                                    String deleteHash = input.nextLine().trim();
                                    System.out.println(Message.deleteByHash(deleteHash));
                                    break;

                                case "f":
                                    System.out.println(Message.displaySentReport());
                                    break;

                                case "0":
                                    storedMenuChoice = -1; // trigger exit
                                    break;

                                default:
                                    System.out.println("Invalid option. Please enter a, b, c, d, e, f, or 0.");
                            }
                        } while (storedMenuChoice != -1);

                    // --------------------------------------------------------
                    // Option 4: Quit
                    // --------------------------------------------------------
                    } else if (menuChoice == 4) {
                        System.out.println("Goodbye!");

                    } else {
                        System.out.println("Invalid option, please enter 1, 2, 3, or 4.");
                    }

                } while (menuChoice != 4);
            }
        }

        input.close();
    }
}
