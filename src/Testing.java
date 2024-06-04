import org.apache.log4j.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.*;

public class Testing{
    static Logger logger = Logger.getLogger(Testing.class.getName());
    @BeforeClass
    public static void setUpLogger(){
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFile("resources/logging/log.txt");
        fileAppender.setLayout(new PatternLayout("%d [%t] %-5p %c - %m%n"));
        fileAppender.setAppend(false);
        fileAppender.activateOptions();
        /*
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(new PatternLayout("%d [%t] %-5p %c - %m%n"));
        consoleAppender.activateOptions();
         */
        logger.addAppender(fileAppender);
        //logger.addAppender(consoleAppender);
        logger.setLevel(Level.toLevel(getLoggingLevel()));
    }
    @Test
    public void testPlayerWin() throws IOException {

        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        testing.attackAndKill(Player_Deck.get(0), Invader_Deck.get(0));
        testing.attackAndKill(Player_Deck.get(1), Invader_Deck.get(0));
        assertEquals(0, Invader_Deck.size());
        logger.info("Player win test passed");
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
        logger.info("Bot win test passed");
    }
    @Test
    public void finesTest() throws IOException {
        MapReader read = new MapReader("junitTestMap.map");
        MapData map = read.read();
        GameHandler testing = new GameHandler(true,7,7, null, map, "TESTER");
        int costExpected = (int)(1.5 + 2.0 + 1.2);
        int costActual = testing.evaluateMovement(testing.getUnits(false).get(0), new Point(0,0), new Point(0,3));
        assertEquals(costExpected, costActual);
        logger.info("Fines test passed");
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
        logger.info("Attack range test passed");
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
        logger.info("Attack test passed");
    }
    @Test
    public void movementTest() throws IOException {
        MapReader read = new MapReader("junitTestMap.map");
        MapData map = read.read();
        GameHandler testing = new GameHandler(true,7,7, null, map, "TESTER");
        ArrayList<Unit> Player_Deck = testing.getUnits(false);
        assertFalse(testing.canMove(Player_Deck.get(0), new Point(0,0), new Point(0,3))); // pdeck[0] movement = 3, 0 3 has no other units
        assertTrue(testing.canMove(Player_Deck.get(2), new Point(0,0), new Point(0,3))); // pdeck[2] movement = 6, 0 3 has no other units
        testing.putPlayfield(new Point(0,3), 'W');
        assertFalse(testing.canMove(Player_Deck.get(2), new Point(0,0), new Point(0,3))); // pdeck[2] movement = 6, 0 3 has a unit
        logger.info("Movement test passed");
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
        logger.info("Death test passed");
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
        logger.info("Defence test passed");
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
        logger.info("Shop test passed");
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
        logger.info("Bot test passed");
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
        logger.info("Field print test passed");
    }
    @Test
    public void retreatBotTest(){
        GameHandler testing = new GameHandler(true, 7,7,"TESTER");
        ArrayList<Unit> Invader_Deck = testing.getUnits(true);
        //System.out.println(Invader_Deck);
        Invader_Deck.get(1).setHealth(5); // has 35 initial health
        testing.invaderTurn();
        assertEquals(1, Invader_Deck.get(1).getEffect("Retreats"));
        logger.info("Bot retreat test passed");
    }
    @Test
    public void buildingsLevelingTest(){
        GameHandler testing = new GameHandler(true, 7, 7, "TESTER");
        testing.buildBuilding("Market");
        HashMap<String, Integer> actual_map = testing.getBuildings();
        int actual = actual_map.get("Market");
        assertEquals(1, actual);
        logger.info("Buildings leveling test passed");
    }
    private boolean marketTest(GameHandler instance) throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "-getResources\n10\n-move 8 8";
        InputStream old_stream = System.in;
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        System.setIn(stream);
        logger.warn("Turn skip inbound");
        try{
            instance.playerTurn();
        }   catch(ArrayIndexOutOfBoundsException e){
            logger.error("Turn skipped by " + e);
            }
        Field field = GameHandler.class.getDeclaredField("city");
        field.setAccessible(true);
        City instance_city = (City) field.get(instance);
        int[] resources = instance_city.getResources();
        System.setIn(old_stream);
        field.setAccessible(false);
        return resources[0] == 30 && resources[1] == 30 && instance.getAccount() == 62;
    }
    private boolean workshopTest(GameHandler instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = GameHandler.class.getDeclaredMethod("assignWorkshopEffects");
        method.setAccessible(true);
        method.invoke(instance);
        int account = instance.getAccount();
        method.setAccessible(false);
        return account == 71;
    }
    private boolean academyTest(GameHandler instance) throws NoSuchFieldException, IllegalAccessException {
        Field field = GameHandler.class.getDeclaredField("city");
        field.setAccessible(true);
        City instance_city = (City) field.get(instance);
        String searchIn = instance_city.toString();
        boolean researchAvailable = searchIn.contains("Research available");
        String input = "1\n1,1,1,1,1\n";
        InputStream old_stream = System.in;
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        System.setIn(stream);
        instance_city.researchUnit();
        System.setIn(old_stream);
        CustomUnit expected = new CustomUnit(1, 0, 0, (char)(97 + 1));
        expected.replaceStats(new int[]{1, 1, 1, 1, 1, 0});
        boolean correctlyAdded = expected.equals(instance_city.getResearchedUnitsSafe().get(0));
        return researchAvailable && correctlyAdded;
    }
    @Test
    public void buildingsHandlingTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
        GameHandler testing = new GameHandler(true, 7, 7, "TESTER");
        testing.setBuildingLevel("Academy", 1);
        testing.setBuildingLevel("Market", 1);
        testing.setBuildingLevel("Workshop", 1);
        assertTrue(workshopTest(testing));
        assertTrue(marketTest(testing));
        assertTrue(academyTest(testing));
        logger.info("Buildings handling test passed");
    }
    private static SaveGame getSaveGame() {
        HashMap<String, Integer> expected_map = new HashMap<>();
        expected_map.put("Tavern", 1);
        expected_map.put("Blacksmith", 1);
        expected_map.put("Academy", 1);
        expected_map.put("MarketShop", 1);
        int[] expected_resources = {31,46};
        boolean expected_celebration = true;
        ArrayList<Unit> researchedUnitsExpected = new ArrayList<>();
        return new SaveGame(expected_map, expected_resources, researchedUnitsExpected, expected_celebration);
    }
    @Test
    public void saveTest() throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream("junitSaveTest" + ".sav");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        SaveGame actual = (SaveGame) objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();
        SaveGame expected = getSaveGame();
        assertEquals(expected,actual);
        logger.info("Save test passed");
    }
    private static String getLoggingLevel() {
        String pathToProperties = "resources/logging/logging.properties";
        Properties properties = new Properties();
        try (FileInputStream fstream = new FileInputStream(pathToProperties)){
            properties.load(fstream);
        } catch (IOException e) {
            System.err.println("Exception loading properties file");
            return "ALL";
        }
        return properties.getProperty("logging_level");
    }

}
