package sem2;

public class ReceiverTwo implements Subscriber{
    @Override
    public void getMessage(String message) {
        System.out.println("Message two "+convertor(message));
    }

    @Override
    public String convertor(String message) {
        return message.toLowerCase();
    }
}
