package hw2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class TestElementAmount extends AbstractTestElements {
    Container container1;

    @BeforeEach
    void initContainer() {
        container1 = new Container();
    }

    @Test
    void testElementAmount() {
        assertEquals(100, element.getAmount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.50, 245, 309.56})
    void testContainerAmount(double price) {
        container1.add(new Elements(price));
        container1.add(element);
        double expectedResult = price+element.getAmount();
        assertEquals(expectedResult, container1.getAmount());
    }

    @Test
    void testDoubleContainer() {
        Container container2 = new Container();
        container1.add(element);
        container1.add(new Elements(125.35));
        container2.add(element);
        container2.add(new Elements(400));
        container1.add(container2);
        assertEquals(725.35, container1.getAmount());
    }

    @Test
    void testRemoveContainer() {
        Container container2 = new Container();
        container1.add(element);
        container1.add(new Elements(125.35));
        container2.add(element);
        container1.add(container2);
        container1.remove(container2);
        assertEquals(225.35, container1.getAmount());
    }

    @Test
    void testRemoveElement() {
        Container container2 = new Container();
        Elements element = new Elements(125.35);
        container1.add(new Elements(125.65));
        container1.add(element);
        container2.add(new Elements(100));
        container2.add(new Elements(400));
        container1.add(container2);
        container1.remove(element);
        assertEquals(625.65, container1.getAmount());
    }
}
