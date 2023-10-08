package hw2;

import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTestElements {

    static Elements element;

    @BeforeAll
    static void initElement() {
        element = new Elements(100);
    }
}
