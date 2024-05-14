import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Testing {
    @Test
    public void testPlayerWin() throws IOException {
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        testing.attackAndKill(Player_Deck.get(0), Invader_Deck.get(0));
        testing.attackAndKill(Player_Deck.get(1), Invader_Deck.get(0));
        assertEquals(0, Invader_Deck.size());
    }
    @Test
    public void testBotWin() throws IOException {
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        testing.attackAndKill(Invader_Deck.get(1), Player_Deck.get(0));
        testing.attackAndKill(Invader_Deck.get(1), Player_Deck.get(0));
        testing.attackAndKill(Invader_Deck.get(1), Player_Deck.get(0));
        assertEquals(0, Player_Deck.size());
    }
    @Test
    public void finesTest() throws IOException {
        MapReader read = new MapReader("junitTestMap.map");
        MapData map = read.read();
        GameHandler testing = new GameHandler(true,7,7, null, map, "TESTER");
        int costExpected = (int)(1.5 + 2.0 + 1.2);
        int costActual = testing.evaluateMovement(testing.getUnits(false).get(0), new Point(0,0), new Point(0,3));
        assertEquals(costExpected, costActual);
    }
    @Test
    public void attackRangeTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        Point a = new Point(0,0);
        Point b = new Point(7,8);
        int x_dif = b.x - a.x;
        int y_dif = b.y - a.y;
        int expected = (int)Math.sqrt(Math.pow(x_dif,2) + Math.pow(y_dif,2));
        System.out.println(expected);
        int actual = testing.evaluateDistanceEUC(a,b);
        assertEquals(expected,actual);
    }
    @Test
    public void attackTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        Player_Deck.add(new Archer(1,6,0));
        Point a = new Point(0,0);
        Point b = new Point(3,3);
        int x_dif = b.x - a.x;
        int y_dif = b.y - a.y;
        int range = (int)Math.sqrt(Math.pow(x_dif,2) + Math.pow(y_dif,2)); // range = 4, pdeck[3] = 5, pdeck[2] = 3:
        assertTrue(testing.canAttack(Player_Deck.get(3), range));
        assertFalse(testing.canAttack(Player_Deck.get(2), range));
    }
    @Test
    public void movementTest() throws IOException {
        MapReader read = new MapReader("junitTestMap.map");
        MapData map = read.read();
        GameHandler testing = new GameHandler(true,7,7, null, map, "TESTER");
        int cost = (int)(1.5 + 2.0 + 1.2); // = 4
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        assertFalse(testing.canMove(Player_Deck.get(0), new Point(0,0), new Point(0,3))); // pdeck[0] movement = 3, 0 3 has no other units
        assertTrue(testing.canMove(Player_Deck.get(2), new Point(0,0), new Point(0,3))); // pdeck[2] movement = 6, 0 3 has no other units
        testing.putPlayfield(new Point(0,3), 'W');
        assertFalse(testing.canMove(Player_Deck.get(2), new Point(0,0), new Point(0,3))); // pdeck[2] movement = 6, 0 3 has a unit
    }
    @Test
    public void deathTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        boolean result1 = testing.attack(Player_Deck.get(0), Invader_Deck.get(0)); // plr attack 100, invdr hp <100
        boolean result2 = testing.attack(Player_Deck.get(2), Invader_Deck.get(1)); // plr attack 5, invdr hp > 5
        assertTrue(result1);
        assertFalse(result2);
    }
    @Test
    public void defenceTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        Unit attacker = Player_Deck.get(2);
        Unit defender = Invader_Deck.get(1);
        int damage = attacker.getStatByName("Attack");
        double reduction_coef = (double)defender.getStatByName("Defence") / 8;
        damage = (int) ((double)damage - (double)damage*0.33*reduction_coef);
        int health = defender.getStatByName("Health");
        int expected = health - damage;
        testing.attack(attacker,defender);
        int actual = defender.getCurrentHealth();
        assertEquals(expected, actual);
    }
    @Test
    public void shopTest(){
        Shop testing = new Shop(70);
        ArrayList<Unit> expected = new ArrayList<>();
        expected.add(new Infantry(1,0,0));
        expected.add(new Infantry(2,0,0));
        expected.add(new Infantry(3,0,0));
        ArrayList<Unit> actual = testing.shopByIndex(new int[]{0,3,6});
        String expectedSTR = expected.toString();
        String actualSTR = actual.toString();
        assertEquals(expectedSTR, actualSTR);
    }
    @Test
    public void botTest(){
        GameHandler testing = new GameHandler(true, 10,10,"TESTER");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream oldOut = System.out;
        System.setOut(printStream);
        testing.invaderTurn();
        System.setOut(oldOut);
        String actual = outputStream.toString();
        String expected = "Enemy's Archer type Crossbow moves forward!\n" +
                "Enemy's Infantry type Spearman attacks your Infantry type Swordman\n" +
                "Infantry type Swordman dies!";
        actual = actual.replaceAll("\n", "");
        actual = actual.replaceAll("\r", "");
        expected = expected.replaceAll("\n", "");
        assertEquals(expected,actual);
    }
    @Test
    public void fieldPrintTest() throws IOException {
        MapReader read = new MapReader("junitTestMap.map");
        MapData map = read.read();
        GameHandler testing = new GameHandler(true,7,7, null, map, "TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        for (Unit u: Player_Deck){
            assertTrue(testing.isOccupied(u.getCoordinates()));
        }
        for (Unit u: Invader_Deck){
            assertTrue(testing.isOccupied(u.getCoordinates()));
        }
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                assertEquals(map.mapLayout[i][j], testing.fieldAt(new Point(j,i), false));
            }
        }
    }
    @Test
    public void retreatBotTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        //System.out.println(Invader_Deck);
        Invader_Deck.get(1).setHealth(5); // has 35 initial health
        testing.invaderTurn();
        assertEquals(1, Invader_Deck.get(1).getEffect("Retreats"));
    }
    @Test
    public void buildingsLevelingTest(){
        GameHandler testing = new GameHandler(true, 7, 7, "TESTER");
        testing.buildBuilding("Market");
        HashMap<String, Integer> actual_map = testing.getBuildings();
        int actual = actual_map.get("Market");
        assertEquals(1, actual);
    }
    private boolean marketTest(GameHandler instance) throws IOException {
        String input = "-getResources\n10\n";
        InputStream old_stream = System.in;
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        System.setIn(stream);
        instance.playerTurn();

        return true;
    }
    private boolean workshopTest(GameHandler instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = GameHandler.class.getDeclaredMethod("assignWorkshopEffects");
        method.setAccessible(true);
        method.invoke(instance);
        int account = instance.getAccount();
        method.setAccessible(false);
        return account == 71;
    }
    private boolean academyTest(GameHandler instance){


        return true;
    }
    @Test
    public void buildingsHandlingTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        GameHandler testing = new GameHandler(true, 7, 7, "TESTER");
        testing.setBuildingLevel("Academy", 1);
        testing.setBuildingLevel("Market", 1);
        testing.setBuildingLevel("Workshop", 1);
        assertTrue(marketTest(testing));
        assertTrue(workshopTest(testing));
        assertTrue(academyTest(testing));


    }

}
