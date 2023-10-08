package hw2;

public class Elements implements ElementAndContainer{

    private double elementPrice;

    public Elements(double price){
        this.elementPrice=price;
    }

    @Override
    public double getAmount() {
        return elementPrice;
    }
}
