package hw1;

import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest {
    static Door doorTrue;
    static Door doorFalse;
    static Player playerRisky;
    static Player playerNOTRisky;

    @BeforeAll
    static void initDoorTrue(){
        doorTrue = new Door(true);
    }

    @BeforeAll
    static void initDoorFalse(){
        doorFalse = new Door(false);
    }

    @BeforeAll
    static void initRiskyPlayer(){
        playerRisky = new Player("Player", true);
    }

    @BeforeAll
    static void initNOTRiskyPlayer(){
        playerNOTRisky = new Player("Player", false);
    }

}
