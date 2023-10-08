package sem2;

import java.util.ArrayList;
import java.util.List;

public class Sender implements Publisher{

    private List<Subscriber> subscribers;

    public Sender(){
        this.subscribers = new ArrayList<>();
    }
    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void sendToAll(String message){
        subscribers.forEach(i -> i.getMessage(message));
    }
}
