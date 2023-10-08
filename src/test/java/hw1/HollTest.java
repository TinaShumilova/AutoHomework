package hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HollTest extends AbstractTest {
    static List<Door> doorList;

    @BeforeEach
    void createDoorList(){
        doorList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            doorList.add(doorFalse);
        }
        doorList.add(doorTrue);
    }

    @Test
    void testDoor(){
        assertTrue(doorTrue.isPrize());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Player1", "Player2", "Not Player"})
    void testPlayerName(String name){
        Player player = new Player(name,true);
        assertEquals(name, player.getName());
    }
    @Test
    void testPlayerRisk(){
        assertFalse(playerNOTRisky.getRisk());
    }

    @Test
    void testRoundPlayerChooseCorrectDoorAndDidntChangeChoice(){
        Game game = new Game(playerNOTRisky,doorList);
        assertTrue(game.round(2).isPrize());
    }
    @Test
    void testRoundPlayerChooseWrongDoorAndDidntChangeChoice(){
        Game game = new Game(playerNOTRisky,doorList);
        assertFalse(game.round(0).isPrize());
    }
    @Test
    void testRoundPlayerChooseWrongDoorAndChangeChoice(){
        Game game = new Game(playerRisky,doorList);
        assertTrue(game.round(0).isPrize());
    }
    @Test
    void testRoundPlayerChooseCorrectDoorAndChangeChoice() {
        Game game = new Game(playerRisky, doorList);
        assertFalse(game.round(2).isPrize());
    }
}
