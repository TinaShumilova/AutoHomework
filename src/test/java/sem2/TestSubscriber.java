package sem2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSubscriber {
    @Test
    void testReceiver(){
        Subscriber receiver = new Receiver();
        Subscriber rec2 =new ReceiverTwo();
        Sender sender = new Sender();

        sender.subscribe(receiver);
        sender.subscribe(rec2);

        sender.sendToAll("Hi! Subscriber");
    }

    @Test
    void testConvertor(){
        String message = "Hello! Subs";
        Subscriber receiver = new Receiver();
        String result = receiver.convertor(message);
        assertEquals(message.toUpperCase(), result);
    }
}
