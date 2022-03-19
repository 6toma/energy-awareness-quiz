package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void emptyConstructorTest() {
        Player player = new Player();
        assertNotNull(player);
    }

    @Test
    public void nameConstructorTest() {
        Player player = new Player("p1");
        assertNotNull(player);
    }

    @Test
    public void nameScoreConstructorTest() {
        Player player = new Player("p1", 1200);
        assertNotNull(player);
    }

    @Test
    public void idNameScoreConstructorTest() {
        Player player = new Player(1L, "p1", 1200);
        assertNotNull(player);
    }

    @Test
    public void equalsTest1() {
        Player player = new Player("p1", 1200);
        assertEquals(player, player);
    }

    @Test
    public void equalsTest2() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p1", 1200);
        assertEquals(player1, player2);
    }

    @Test
    public void equalsTest3() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p2", 1200);
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest4() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p1", 2000);
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest5() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p2", 2000);
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest6() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player(1L,"p1", 2000);
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest7() {
        Player player1 = new Player(1L, "p1", 1200);
        Player player2 = new Player(2L, "p1", 2000);
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest8() {
        Player player1 = new Player(1L, "p1", 1200);
        Player player2 = new Player(1L,"p1", 1200);
        assertEquals(player1, player2);
    }

    @Test
    public void equalsTest9() {
        Player player = new Player(1L,"p1", 1200);
        assertEquals(player, player);
    }

    @Test
    public void equalsTest10() {
        Player player = new Player("p1");
        assertEquals(player, player);
    }

    @Test
    public void equalsTest11() {
        Player player = new Player();
        assertEquals(player, player);
    }

    @Test
    public void equalsTest12() {
        Player player1 = new Player("p1", 1200);
        Player player2 = new Player("p1");
        assertNotEquals(player1, player2);
    }

    @Test
    public void equalsTest13() {
        Player player1 = new Player("p1", 0);
        Player player2 = new Player("p1");
        assertEquals(player1, player2);
    }

    @Test
    public void getIdTest() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals(1L, player.getId());
    }

    @Test
    public void setIdTest1() {
        Player player = new Player(1L, "p1", 1200);
        player.setId(4L);
        assertEquals(4L, player.getId());
    }

    @Test
    public void setIdTest2() {
        Player player = new Player("p1", 1200);
        player.setId(4L);
        assertEquals(4L, player.getId());
    }

    @Test
    public void getNameTest() {
        Player player = new Player("p1");
        assertEquals("p1", player.getName());
    }

    @Test
    public void setNameTest1() {
        Player player = new Player("p1");
        player.setName("player1");
        assertEquals("player1", player.getName());
    }

    @Test
    public void setNameTest2() {
        Player player = new Player("p1");
        assertThrows(IllegalArgumentException.class, () -> {
            player.setName("");
        }, "Name cannot be empty");
    }

    @Test
    public void getScoreTest1() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals(1200, player.getScore());
    }

    @Test
    public void getScoreTest2() {
        Player player = new Player("p1");
        assertEquals(0, player.getScore());
    }

    @Test
    public void setScoreTest1() {
        Player player = new Player(1L, "p1", 1200);
        player.setScore(4000);
        assertEquals(4000, player.getScore());
    }

    @Test
    public void setScoreTest2() {
        Player player = new Player("p1");
        player.setScore(4000);
        assertEquals(4000, player.getScore());
    }

    @Test
    public void setScoreTest3() {
        Player player = new Player("p1");
        assertThrows(IllegalArgumentException.class, () -> {
            player.setScore(-200);
        }, "Score cannot be negative");
    }

    @Test
    public void setScoreTest4() {
        Player player = new Player("p1", 1200);
        assertThrows(IllegalArgumentException.class, () -> {
            player.setScore(-200);
        }, "Score cannot be negative");
    }

    @Test
    public void toStringTest1() {
        Player player = new Player(1L, "p1", 1200);
        assertEquals("Player(id=1, name=p1, score=1200)", player.toString());
    }

    @Test
    public void toStringTest2() {
        Player player = new Player("p1", 1200);
        assertEquals("Player(id=null, name=p1, score=1200)", player.toString());
    }

    @Test
    public void toStringTest3() {
        Player player = new Player("p1");
        assertEquals("Player(id=null, name=p1, score=0)", player.toString());
    }

    @Test
    public void toStringTest4() {
        Player player = new Player();
        assertEquals("Player(id=null, name=null, score=null)", player.toString());
    }

}