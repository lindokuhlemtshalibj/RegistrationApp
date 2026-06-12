/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registrationapp;

import java.util.ArrayList;
import java.util.Random;

/**
 * Message class for QuickChat application.
 * Handles creating, sending, storing and managing messages.
 * Part 3 additions: parallel arrays, JSON reading, stored message operations.
 *
 * JSON reading approach sourced from:
 * Baeldung (2023). "Read a JSON File in Java."
 * https://www.baeldung.com/reading-file-in-java
 * Adapted to manually parse simple JSON records without external libraries.
 *
 * @author Lindokuhle Mtshali
 */
public class Message {

    // -----------------------------------------------------------------------
    // Instance variables for a single message
    // -----------------------------------------------------------------------
    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String sendStatus;  // "Sent", "Stored", "Disregarded"

    // -----------------------------------------------------------------------
    // Static parallel arrays (Part 3 - requirement 1)
    // -----------------------------------------------------------------------
    private static ArrayList<String> sentMessages       = new ArrayList<>();  // messages flagged Sent
    private static ArrayList<String> disregardedMessages = new ArrayList<>(); // messages flagged Disregarded
    private static ArrayList<String> storedMessages     = new ArrayList<>();  // messages loaded from JSON
    private static ArrayList<String> messageHashes      = new ArrayList<>();  // all message hashes
    private static ArrayList<String> messageIDs         = new ArrayList<>();  // all message IDs

    // Parallel recipient arrays so we can search/report alongside stored messages
    private static ArrayList<String> storedRecipients   = new ArrayList<>();
    private static ArrayList<String> sentRecipients     = new ArrayList<>();
    private static ArrayList<String> sentHashes         = new ArrayList<>();

    // Legacy list used by printMessages() and the existing menu option 2
    private static ArrayList<String[]> allMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    public Message(int messageNumber, String recipientCell, String messageText) {
        this.messageNumber   = messageNumber;
        this.recipientCell   = recipientCell;
        this.messageText     = messageText;
        this.messageID       = generateMessageID();
        this.messageHash     = createMessageHash();
    }

    // -----------------------------------------------------------------------
    // Core message methods 
    // -----------------------------------------------------------------------

    /** Generates a random 10-digit message ID. */
    private String generateMessageID() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    /** Checks that message ID is not more than 10 characters. */
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    /** Validates recipient cell number (must start with + and be 10–12 chars total). */
    public String checkRecipientCell() {
        if (recipientCell.matches("^\\+[0-9]{9,11}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. "
                 + "Please correct the number and try again.";
        }
    }

    /**
     * Creates the message hash.
     * Format: first2charsOfID : messageNumber : FIRSTWORDLASTWORD (caps)
     */
    public String createMessageHash() {
        String idPrefix  = messageID.substring(0, 2);
        String trimmed   = messageText.trim();
        String[] words   = trimmed.split("\\s+");
        String firstWord = words[0];
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String hash      = idPrefix + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
        this.messageHash = hash;
        return hash;
    }

    /** Checks message text is within 250 characters. */
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int over = messageText.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    /**
     * Sends, disregards or stores the message and populates the parallel arrays.
     * @param choice 1=Send, 2=Disregard, 3=Store
     */
    public String sentMessage(int choice) {
        // Always track the hash and ID
        messageHashes.add(messageHash);
        messageIDs.add(messageID);

        switch (choice) {
            case 1:
                sendStatus = "Sent";
                totalMessagesSent++;
                sentMessages.add(messageText);
                sentRecipients.add(recipientCell);
                sentHashes.add(messageHash);
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Sent"});
                return "Message successfully sent.";

            case 2:
                sendStatus = "Disregarded";
                disregardedMessages.add(messageText);
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Disregarded"});
                return "Press 0 to delete the message.";

            case 3:
                sendStatus = "Stored";
                // storedMessages / storedRecipients populated when user adds to JSON
                allMessages.add(new String[]{messageID, messageHash, recipientCell, messageText, "Stored"});
                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    // -----------------------------------------------------------------------
    // Part 3 — JSON storage (writes a properly-formatted JSON array file)
    // -----------------------------------------------------------------------

    /**
     * Stores this message to messages.json.
     * Appends a new record; the file is rewritten as a valid JSON array each time.
     *
     * JSON writing approach adapted from:
     * Baeldung (2023). "Write to a File in Java."
     * https://www.baeldung.com/java-write-to-file
     */
    public void storeMessage() {
        try {
            // Read existing records first
            java.io.File file = new java.io.File("messages.json");
            ArrayList<String> existingLines = new ArrayList<>();
            if (file.exists()) {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    existingLines.add(line);
                }
                reader.close();
            }

            // Build the new JSON record
            String record =
                "  {\n"
              + "    \"messageID\": \""     + messageID                          + "\",\n"
              + "    \"messageHash\": \""   + messageHash                        + "\",\n"
              + "    \"messageNumber\": "   + messageNumber                      + ",\n"
              + "    \"recipient\": \""     + recipientCell                      + "\",\n"
              + "    \"message\": \""       + messageText.replace("\"", "\\\"")  + "\",\n"
              + "    \"status\": \"Stored\"\n"
              + "  }";

            // Parse old records from existing file content
            ArrayList<String> records = parseJsonRecordsFromLines(existingLines);
            records.add(record);

            // Write back as a proper JSON array
            java.io.FileWriter writer = new java.io.FileWriter(file, false);
            writer.write("[\n");
            for (int i = 0; i < records.size(); i++) {
                writer.write(records.get(i));
                if (i < records.size() - 1) writer.write(",\n");
                else writer.write("\n");
            }
            writer.write("]\n");
            writer.close();

            // Also add to in-memory stored arrays
            storedMessages.add(messageText);
            storedRecipients.add(recipientCell);

        } catch (Exception e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    /**
     * Helper: extract raw JSON record blocks from a list of file lines.
     * Simple brace-matching parser; does not require an external JSON library.
     *
     * Parsing technique adapted from:
     * Stack Overflow user "erickson" (2010). "How to parse JSON in Java."
     * https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
     */
    private ArrayList<String> parseJsonRecordsFromLines(ArrayList<String> lines) {
        ArrayList<String> records = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceDepth = 0;
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                if (c == '{') braceDepth++;
                if (c == '}') braceDepth--;
            }
            current.append(line).append("\n");
            if (braceDepth == 0 && current.toString().contains("{")) {
                String rec = current.toString().trim();
                if (rec.endsWith(",")) rec = rec.substring(0, rec.length() - 1);
                records.add(rec);
                current = new StringBuilder();
            }
        }
        return records;
    }

    // -----------------------------------------------------------------------
    // Part 3 — Load stored messages from JSON into the storedMessages array
    // -----------------------------------------------------------------------

    /**
     * Reads messages.json and loads all Stored-flagged entries into
     * the storedMessages and storedRecipients parallel arrays.
     *
     * JSON reading approach sourced from:
     * Baeldung (2023). "Reading a File Line by Line in Java."
     * https://www.baeldung.com/java-read-lines-large-file
     * Adapted to manually extract key-value pairs from simple JSON records.
     */
    public static void loadStoredMessagesFromJson() {
        storedMessages.clear();
        storedRecipients.clear();
        try {
            java.io.File file = new java.io.File("messages.json");
            if (!file.exists()) return;

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;
            String currentRecipient = "";
            String currentMessage   = "";
            String currentStatus    = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"recipient\"")) {
                    currentRecipient = extractJsonValue(line);
                } else if (line.startsWith("\"message\"")) {
                    currentMessage = extractJsonValue(line);
                } else if (line.startsWith("\"status\"")) {
                    currentStatus = extractJsonValue(line);
                } else if (line.startsWith("}")) {
                    if ("Stored".equalsIgnoreCase(currentStatus)
                            && !currentMessage.isEmpty()) {
                        storedMessages.add(currentMessage);
                        storedRecipients.add(currentRecipient);
                    }
                    currentRecipient = "";
                    currentMessage   = "";
                    currentStatus    = "";
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    /**
     * Extracts the value from a simple JSON line such as:
     *   "key": "value",
     * Returns the value string without surrounding quotes.
     */
    private static String extractJsonValue(String line) {
        int first = line.indexOf('"', line.indexOf(':') + 1);
        int last  = line.lastIndexOf('"');
        if (first >= 0 && last > first) {
            return line.substring(first + 1, last);
        }
        return "";
    }

    // -----------------------------------------------------------------------
    // Part 3 — Stored Messages menu operations  (requirement 2 a-f)
    // -----------------------------------------------------------------------

    /**
     * (a) Displays the sender (always the logged-in user) and recipient
     *     of every stored message.
     * @param senderName the logged-in user's display name
     */
    public static String displayStoredSendersAndRecipients(String senderName) {
        loadStoredMessagesFromJson();
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder("--- Stored Message Senders & Recipients ---\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append("  Sender   : ").append(senderName).append("\n");
            sb.append("  Recipient: ").append(storedRecipients.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * (b) Finds and returns the longest stored message.
     */
    public static String getLongestStoredMessage() {
        loadStoredMessagesFromJson();
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        String longest = storedMessages.get(0);
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return "Longest stored message: \"" + longest + "\"";
    }

    /**
     * (c) Searches all messages (sent + stored) by message ID and returns
     *     the corresponding recipient and message text.
     * @param searchID the message ID to search for
     */
    public static String searchByMessageID(String searchID) {
        for (String[] msg : allMessages) {
            // msg[0]=ID, msg[1]=hash, msg[2]=recipient, msg[3]=text, msg[4]=status
            if (msg[0].equals(searchID)) {
                return "Recipient: " + msg[2] + "\nMessage: \"" + msg[3] + "\"";
            }
        }
        return "Message ID \"" + searchID + "\" not found.";
    }

    /**
     * (d) Searches all sent and stored messages for a particular recipient
     *     and returns every matching message.
     * @param recipient the recipient cell number to search for
     */
    public static String searchByRecipient(String recipient) {
        boolean found = false;
        StringBuilder sb = new StringBuilder("--- Messages for " + recipient + " ---\n");
        for (String[] msg : allMessages) {
            if (msg[2].equals(recipient)
                    && (msg[4].equals("Sent") || msg[4].equals("Stored"))) {
                sb.append("\"").append(msg[3]).append("\"\n");
                found = true;
            }
        }
        if (!found) {
            return "No sent or stored messages found for recipient: " + recipient;
        }
        return sb.toString().trim();
    }

    /**
     * (e) Deletes a message from the stored arrays using its message hash.
     * @param hash the hash of the message to delete
     * @return confirmation or error message
     */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < allMessages.size(); i++) {
            if (allMessages.get(i)[1].equals(hash)) {
                String text = allMessages.get(i)[3];
                allMessages.remove(i);
                // Also remove from hash/ID parallel arrays if present
                messageHashes.remove(hash);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Hash \"" + hash + "\" not found. No message deleted.";
    }

    /**
     * (f) Displays a full report of all sent messages: hash, recipient, message.
     */
    public static String displaySentReport() {
        boolean hasSent = false;
        StringBuilder sb = new StringBuilder("========== Sent Messages Report ==========\n");
        sb.append(String.format("%-30s %-18s %s%n", "Message Hash", "Recipient", "Message"));
        sb.append("-".repeat(80)).append("\n");
        for (String[] msg : allMessages) {
            if (msg[4].equals("Sent")) {
                sb.append(String.format("%-30s %-18s %s%n", msg[1], msg[2], msg[3]));
                hasSent = true;
            }
        }
        if (!hasSent) {
            return "No sent messages to report.";
        }
        return sb.toString();
    }

    // -----------------------------------------------------------------------
    // Existing utility methods 
    // -----------------------------------------------------------------------

    /** Returns all messages as a formatted string (used by menu option 2). */
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

    /** Returns the total number of sent messages. */
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // -----------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------
    public String getMessageID()      { return messageID; }
    public String getMessageHash()    { return messageHash; }
    public String getRecipientCell()  { return recipientCell; }
    public String getMessageText()    { return messageText; }
    public int    getMessageNumber()  { return messageNumber; }

    public static int getTotalMessagesSent()            { return totalMessagesSent; }
    public static ArrayList<String[]> getAllMessages()  { return allMessages; }
    public static ArrayList<String> getSentMessages()  { return sentMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String> getStoredMessages()      { return storedMessages; }
    public static ArrayList<String> getMessageHashes()       { return messageHashes; }
    public static ArrayList<String> getMessageIDs()          { return messageIDs; }

    // -----------------------------------------------------------------------
    // Reset for unit tests
    // -----------------------------------------------------------------------
    public static void resetAll() {
        allMessages.clear();
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        storedRecipients.clear();
        sentRecipients.clear();
        sentHashes.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
    }
}
