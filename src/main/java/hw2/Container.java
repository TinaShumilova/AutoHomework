package hw2;

import java.util.ArrayList;
import java.util.List;

public class Container implements ElementAndContainer{

    private List<ElementAndContainer> elementsList;

    public Container(){
        elementsList = new ArrayList<>();
    }

    @Override
    public double getAmount() {
        double result = 0;
        for (ElementAndContainer e: elementsList) {
            result += e.getAmount();
        }
        return result;
    }

    public void add(ElementAndContainer e){
        elementsList.add(e);
    }

    public void remove(ElementAndContainer e){elementsList.remove(e);}
}
