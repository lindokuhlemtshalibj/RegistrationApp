/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registrationapp;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Lindokuhle Mtshali
 */
public class Message {
    // Instance variables for a single message
    private String messageID;
    private int messageNumber;      // which message number this is (1, 2, 3...)
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String sendStatus;      // "Sent", "Stored", "Disregarded"

    // Static list to track ALL sent messages across the whole session
    private static ArrayList<String[]> allMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Constructor
    public Message(int messageNumber, String recipientCell, String messageText) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // Generates a random 10-digit message ID
private String generateMessageID() {
        Random rand = new Random();
        long id = (long)(rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // Checks that message ID is not more than 10 characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Checks recipient cell number: starts with + and is no more than 10 chars (after +)
    // Using same regex pattern approach as Login.checkCellPhoneNumber()
    public String checkRecipientCell() {
        // Must start with + and be no more than 10 characters total after the +
        // Reusing the international format validation approach from Login class
        if (recipientCell.matches("^\\+[0-9]{9,11}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. " +
                   "Please correct the number and try again.";
        }
    }

    // Creates and returns the Message Hash
    // Format: first 2 digits of ID : message number : first word + last word (ALL CAPS)
    public String createMessageHash() {
        // Get first two characters of the message ID
        String idPrefix = messageID.substring(0, 2);

        // Get the first and last word of the message
        String trimmed = messageText.trim();
        String[] words = trimmed.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        // Remove any trailing punctuation from last word for clean hash
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        // Build hash: e.g. 00:0:HITONIGHT
        String hash = idPrefix + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
        this.messageHash = hash;
        return hash;
    }

    // Checks message length and returns appropriate message
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    // Allows the user to send, store, or disregard the message
    // Takes the user's menu choice as input
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sendStatus = "Sent";
                totalMessagesSent++;
                // Store in the sent messages list: [messageID, messageHash, recipientCell, messageText]
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Sent"});
                return "Message successfully sent.";
            case 2:
                sendStatus = "Disregarded";
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Disregarded"});
                return "Press 0 to delete the message.";
            case 3:
                sendStatus = "Stored";
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Stored"});
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Returns all messages sent/stored/disregarded during the session as a formatted string
    public String printMessages() {
        if (allMessages.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        for (String[] msg : allMessages) {
            sb.append("Message ID: ").append(msg[0]).append("\n");
            sb.append("Message Hash: ").append(msg[1]).append("\n");
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n");
            sb.append("Status: ").append(msg[4]).append("\n");
            sb.append("----------------------------\n");
        }
        return sb.toString();
    }

    // Returns the total number of messages sent (not stored/disregarded — only sent)
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Stores the message details into a JSON file (Research requirement)
    public void storeMessage() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("messages.json", true);
            writer.write("{\n");
            writer.write("  \"messageID\": \"" + messageID + "\",\n");
            writer.write("  \"messageHash\": \"" + messageHash + "\",\n");
            writer.write("  \"messageNumber\": " + messageNumber + ",\n");
            writer.write("  \"recipient\": \"" + recipientCell + "\",\n");
            writer.write("  \"message\": \"" + messageText.replace("\"", "\\\"") + "\",\n");
            writer.write("  \"status\": \"" + sendStatus + "\"\n");
            writer.write("},\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    // Getters needed for testing and display
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
    public static int getTotalMessagesSent() { return totalMessagesSent; }
    public static ArrayList<String[]> getAllMessages() { return allMessages; }
    
    // Reset static state (needed for clean unit tests)
    public static void resetAll() {
        allMessages.clear();
        totalMessagesSent = 0;
    }
}
