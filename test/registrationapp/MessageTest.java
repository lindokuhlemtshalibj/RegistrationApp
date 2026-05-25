/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registrationapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Lindokuhle Mtshali
 */
public class MessageTest {
  
    // Test Case 1 data
    Message message1;
    // Test Case 2 data  
    Message message2;

    @Before
    public void setUp() {
        Message.resetAll(); // clean static state before each test
        message1 = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message(2, "08575975889", "Hi Keegan, did you receive the payment?");
    }

    @After
    public void tearDown() {
        Message.resetAll();
    }

    // --- Message Length Tests ---

    @Test
    public void testMessageLengthSuccess() {
        // Message 1 is under 250 chars — should pass
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        // Create a message over 250 characters
        String longMessage = "A".repeat(260);
        Message longMsg = new Message(3, "+27718693002", longMessage);
        String result = longMsg.checkMessageLength();
        assertTrue(result.contains("Message exceeds 250 characters by"));
    }

    // --- Recipient Cell Number Tests ---

    @Test
    public void testCheckRecipientCellSuccess() {
        // Message 1 has valid international number
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellFailure() {
        // Message 2 has no international code (no +)
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. " +
            "Please correct the number and try again.",
            message2.checkRecipientCell()
        );
    }

    // --- Message Hash Test ---

    @Test
    public void testMessageHashCorrect() {
        // The hash should follow format: first2ofID:messageNum:FIRSTLAST (all caps)
        String hash = message1.getMessageHash();
        // Must contain a colon and be all caps for the words part
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        // The words part: first word "HI", last word "TONIGHT"
        assertTrue(hash.toUpperCase().contains("HITONIGHT"));
    }

    @Test
    public void testMessageHashesInLoop() {
        // Test all message hashes contain the required format using a loop
        Message[] messages = {message1, message2};
        for (Message msg : messages) {
            String hash = msg.getMessageHash();
            assertNotNull(hash);
            assertTrue("Hash should contain colon separator", hash.contains(":"));
            // Hash should be in all caps (words portion)
            String[] parts = hash.split(":");
            assertEquals("Hash should have 3 parts", 3, parts.length);
        }
    }

    // --- Message ID Test ---

    @Test
    public void testMessageIDCreated() {
        String id = message1.getMessageID();
        assertNotNull(id);
        System.out.println("Message ID generated: " + id);
        assertTrue("Message ID should be 10 or fewer characters", id.length() <= 10);
    }

    // --- SentMessage Tests ---

    @Test
    public void testSentMessageSend() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // --- Return Total Messages ---

    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(1); // sent
        message2.sentMessage(2); // disregarded — should NOT count
        // Only 1 was "sent"
        assertEquals(1, Message.getTotalMessagesSent());
    }
}
