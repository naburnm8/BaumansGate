import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class GameHandler{
    private ArrayList<Unit> Player_Deck;
    private ArrayList<Unit> Invader_Deck;
    private ArrayList<Unit> Dead_Deck;
    private String playerName;
    private Shop shop;
    private int account;
    private Field playfield;
    private char[] gnd_symbols;
    private City city;
    final private double[][] fines = {{1,1.5,2,1.2},{1,1.8,2.2,1},{1,2.2,1.2,1.5}};
    private int gndSymbolToIndex(char symbol){
        for (int i = 0; i < gnd_symbols.length; i++){
            if(gnd_symbols[i] == symbol){
                return i;
            }
        }
        return -1;
    }
    private void assignCoords() throws NotEnoughFieldSpace {
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < playfield.getWidth(); i++){
            if(i%2 == 0){
                positions.add(i);
            }
        }
        if (positions.size() < Player_Deck.size() || positions.size() < Invader_Deck.size()) {
            throw new NotEnoughFieldSpace();
        }
        for (int i = 0; i < Player_Deck.size(); i++){
            Player_Deck.get(i).modifyCoordinates(positions.get(i),0);
        }
        for (int i = 0; i < Invader_Deck.size(); i++){
            Invader_Deck.get(i).modifyCoordinates(positions.get(positions.size() - i - 1),playfield.getHeight()-1);
        }
    }
    private void putCharacters(){
        for (Unit u: Player_Deck){
            Point a = new Point(u.getX(),u.getY());
            char symbol = u.getSymbol();
            playfield.put(a, symbol);
        }
        for (Unit u: Invader_Deck){
            Point a = new Point(u.getX(),u.getY());
            char symbol = u.getSymbol();
            playfield.put(a, symbol);
        }
    }
    GameHandler(boolean streamlined, int _width, int _height, String playerName){
        this(streamlined, _width, _height, null, null,playerName);
    }
    GameHandler(boolean streamlined, int _width, int _height, SaveGame loaded, MapData map, String playerName){
        Dead_Deck = new ArrayList<>();
        this.playerName = playerName;
        gnd_symbols = Field.gnd_symbols;
        Player_Deck = new ArrayList<>();
        Invader_Deck = new ArrayList<>();
        account = 70;
        shop = new Shop(account);
        Player_Deck = shop.commenceShopping(streamlined);
        account = shop.getAccount();
        Invader_Deck = shop.invaderShopping(streamlined);
        if (streamlined){
            playfield = new Field(10,10);
        }
        playfield = new Field(_width,_height);
        try {
            assignCoords();
        } catch (NotEnoughFieldSpace e){
            System.out.println("Field is not big enough!");
            System.exit(-113);
        }
        putCharacters();
        city = new City(20,20);
        if (loaded != null){
            city = new City(loaded.resources()[0], loaded.resources()[1], loaded.buildings(), loaded.researchedUnits());
        }
        if (map != null){
            playfield = new Field(map);
        }
    }
    @Override
    public String toString() {
        String output = playfield.toString();
        for (Unit u: Player_Deck){
            output = output + "\n" + u.shortToString() + ", standing on: " + playfield.at(u.getX(),u.getY(),false);
        }
        return output;
    }
    private int evaluateDistanceEUC(Point a, Point b){
        Point difference = new Point(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
        return (int)Math.sqrt(Math.pow(difference.x,2) + Math.pow(difference.y,2));
    }
    private int evaluateMovement(Unit u, Point a, Point b){
        ArrayList<Character> passedTiles = new ArrayList<>();
        int differenceX = b.x - a.x;
        //System.out.println(differenceX);
        if (differenceX > 0){
            for(int i = a.x + 1; i < b.x + 1; i++){
                passedTiles.add(playfield.at(i,a.y,false));
            }
        } else {
            for (int i = a.x - 1; i > b.x - 1; i--){
                passedTiles.add(playfield.at(i,a.y,false));
            }
        }
        int differenceY = b.y - a.y;
        if (differenceY > 0){
            for(int i = a.y + 1; i < b.y + 1; i++){
                passedTiles.add(playfield.at(b.x,i,false));
            }
        } else {
            for (int i = a.y - 1; i > b.y - 1; i--){
                passedTiles.add(playfield.at(b.x,i,false));
            }
        }
        int identifier = u.getIdentifier();
        int totalMovementCost = 0;
        for (char tile: passedTiles){
            System.out.print(tile);
            totalMovementCost = totalMovementCost + (int)fines[identifier-1][gndSymbolToIndex(tile)];
        }
        System.out.println(totalMovementCost);
        return totalMovementCost;
    }
    private boolean attack (Unit attacker, Unit defender){
        int damage = attacker.getStatByName("Attack");
        double reduction_coef = (double)defender.getStatByName("Defence") / 8;
        damage = (int) ((double)damage - (double)damage*0.33*reduction_coef);
        if(defender.recieveDamage_Death(damage)){
            return true;
        }
        return false;
    }
    private int findUnitByCoordinates(ArrayList<Unit> units, Point a){
        for(int i = 0; i < units.size(); i++){
            if(units.get(i).getX() == a.x && units.get(i).getY() == a.y){
                return i;
            }
        }
        return -1;
    }
    private Point scanForPlayer(Unit u){
        for(int i = 0; i < playfield.getHeight(); i++){
            for(int j = 0; j < playfield.getWidth(); j++){
                boolean distanceCheck = evaluateDistanceEUC(new Point(u.getX(),u.getY()), new Point(j,i)) <= u.getStatByName("Range");
                boolean playerCheck = findUnitByCoordinates(Player_Deck, new Point(j,i)) != -1;
                if (distanceCheck && playerCheck){
                    return new Point(j,i);
                }
            }
        }

        return new Point(-1,-1);
    }
    private Point scanForOpportunity(Unit u, ArrayList<Unit> unitsToCheck){
        for(int i = 0; i < playfield.getHeight(); i++){
            for(int j = 0; j < playfield.getWidth(); j++){
                int indexOfInvader = findUnitByCoordinates(unitsToCheck, new Point(j,i));
                int unitRange;
                if (indexOfInvader != -1){
                    unitRange = 1;
                }
                else {
                    continue;
                }
                boolean distanceCheck = evaluateDistanceEUC(new Point(u.getX(),u.getY()), new Point(j,i)) <= unitRange;
                boolean invaderCheck = findUnitByCoordinates(unitsToCheck, new Point(j,i)) != -1;
                if (distanceCheck && invaderCheck){
                    return new Point(j,i);
                }
            }
        }
        return new Point(-1,-1);
    }
    private void assignThreatenedStatus(){
        for(Unit u: Player_Deck){
            Point closestEnemy = scanForOpportunity(u, Invader_Deck);
            if (closestEnemy.x == -1){
                u.addEffect("Threatened", 0);
                u.addEffect("OpportunityDamage", 0);
                //System.out.println("FRIEND " + u.getName() + " " + "Effect: Threatened " + u.getEffect("Threatened") + ", " + "Effect: OpportunityDamage " + u.getEffect("OpportunityDamage"));
                continue;
            }
            u.addEffect("Threatened", 1);
            int indexOfInvader = findUnitByCoordinates(Invader_Deck, closestEnemy);
            int opportunityDamage = Invader_Deck.get(indexOfInvader).getStatByName("Attack");
            u.addEffect("OpportunityDamage", opportunityDamage);
            //System.out.println("FRIEND " + u.getName() + " " + "Effect: Threatened " + u.getEffect("Threatened") + ", " + "Effect: OpportunityDamage " + u.getEffect("OpportunityDamage"));
        }
        for(Unit u: Invader_Deck){
            Point closestEnemy = scanForOpportunity(u, Player_Deck);
            if (closestEnemy.x == -1){
                u.addEffect("Threatened", 0);
                u.addEffect("OpportunityDamage", 0);
                //System.out.println("ENEMY " + u.getName() + " " + "Effect: Threatened " + u.getEffect("Threatened") + ", " + "Effect: OpportunityDamage " + u.getEffect("OpportunityDamage"));
                continue;
            }
            u.addEffect("Threatened", 1);
            int indexOfInvader = findUnitByCoordinates(Player_Deck, closestEnemy);
            int opportunityDamage = Player_Deck.get(indexOfInvader).getStatByName("Attack");
            u.addEffect("OpportunityDamage", opportunityDamage);
            //System.out.println("ENEMY " + u.getName() + " " + "Effect: Threatened " + u.getEffect("Threatened") + ", " + "Effect: OpportunityDamage " + u.getEffect("OpportunityDamage"));
        }
    }
    private void drawDeadUnits(){
        for(Unit u: Dead_Deck){
            if(!playfield.isOccupied(new Point(u.getX(),u.getY()))){
                playfield.put(new Point(u.getX(),u.getY()), 'ඞ');
            }
        }
    }
    private void bleedOut(){
        for(Unit u: Dead_Deck){
            u.addEffect("Dead", u.getEffect("Dead") + 1);
        }
        Iterator<Unit> unitIterator = Dead_Deck.iterator();
        while(unitIterator.hasNext()){
            Unit nextUnit = unitIterator.next();
            if(nextUnit.getEffect("Dead") == 5){
                System.out.println(nextUnit.getName() + " cannot be revived anymore.");
                Point location = new Point(nextUnit.getX(), nextUnit.getY());
                playfield.remove(location);
                unitIterator.remove();
            }
        }
    }
    private void assignCityEffects(){
        for(Unit u: Player_Deck){
            for(int i = 0; i < city.modStats.length; i++){
                u.modifyStatByName(city.modStats[i], u.getBaseStatByName(city.modStats[i]) + city.getBuildingByName(city.names[i]));
            }
        }
    }
    public void playerTurn() throws IOException {
        bleedOut();
        Scanner stream = new Scanner(System.in);
        System.out.println("Your turn!\nSyntax: -attack x y; -move x y; -skip; -retreat; -help; -build; -addResearched; -getResources");
        String input = "";
        ArrayList<Unit> revived = new ArrayList<>();
        for (Unit u: Player_Deck){
            System.out.println("Now in control of: " + u.getName());
            int actions = 2;
            int threat = 0;
            int opDamage = 0;
            while (actions > 0){
                Unit addable = null;
                assignCityEffects();
                drawDeadUnits();
                assignThreatenedStatus();
                if (threat == 1 && u.getEffect("Threatened") == 0){
                    if (u.recieveDamage_Death(opDamage)){
                        System.out.println(u.getName() + " gets killed by an opportunity attack!");
                        int index = findUnitByCoordinates(Player_Deck, new Point(u.getX(), u.getY()));
                        u.addEffect("Dead", 0);
                        Dead_Deck.add(u);
                        playfield.remove(new Point(u.getX(), u.getY()));
                        Player_Deck.remove(index);
                    }
                    else{
                        System.out.println(u.getName() + " gets " + opDamage + " damage by an opportunity attack!");
                    }
                }
                opDamage = u.getEffect("OpportunityDamage");
                threat = u.getEffect("Threatened");
                input = stream.nextLine();
                if(input.equals("-getResources")){
                    if(city.getBuildingByName("Market") == 0){
                        System.out.println("No market built!");
                        continue;
                    }
                    System.out.println("Account: " + account + "\n" + "How much gold would you like to spend? ");
                    Scanner stream2 = new Scanner(System.in);
                    int spent = stream2.nextInt();
                    if(account - spent < 0){
                        System.out.println("Not enough gold!");
                        continue;
                    }
                    account = account - spent;
                    city.getResources(spent);
                    System.out.println("Resources acquired!");
                    continue;
                }
                if(input.equals("-addResearched")){
                    ArrayList<Unit> toAdd = city.getResearchedUnitsSafe();
                    boolean skipped = false;
                    for (Unit unit: toAdd){
                        Scanner stream3 = new Scanner(System.in);
                        System.out.println("Enter coordinates for unit " + unit.getName() + ": \nSyntax: x,y");
                        String scanned = stream3.nextLine();
                        String[] split = scanned.split(",");
                        int x = Integer.parseInt(split[0]);
                        int y = Integer.parseInt(split[1]);
                        if (playfield.isOccupied(new Point(x,y))){
                            System.out.println("Spot is occupied!");
                            skipped = true;
                            break;
                        }
                        unit.modifyCoordinates(x,y);
                    }
                    if (skipped){
                        continue;
                    }
                    revived.addAll(city.getResearchedUnits());
                    continue;
                }
                if(input.equals("-build")){
                    while(true){
                        System.out.println(city);
                        System.out.println(city.toStringShop());
                        System.out.println("Syntax: -new BuildingName; -research; -quit");
                        Scanner stream1 = new Scanner(System.in);
                        String scanned = stream1.nextLine();
                        String[] split = scanned.split(" ");
                        if (scanned.equals("-quit")){
                            break;
                        }
                        if (scanned.equals("-research")){
                            if(!city.researchUnit()){
                                System.out.println("No research for you!");
                                continue;
                            }
                        }
                        if (split[0].equals("-new")){
                            if(!city.BuildBuilding(split[1])){
                                System.out.println("No building was built");
                                continue;
                            }
                        }
                    }
                    continue;
                }
                if (input.equals("-skip")){
                    actions = actions - 1;
                    System.out.println(this);
                    if (endCondition()){
                        System.exit(0);
                    }
                    continue;
                }
                if (input.equals("-retreat")){
                    actions = actions - 1;
                    System.out.println(this);
                    threat = 0;
                    System.out.println(u.getName() + " successfully retreated!");
                    if (endCondition()){
                        System.exit(0);
                    }
                    continue;
                }
                if (input.equals("-help")){
                    actions = actions - 1;
                    Point injured = new Point(u.getX(), u.getY() + 1);
                    if (injured.y > playfield.getHeight()){
                        System.out.println(u.getName() + " says: " + "No one needs help there, Sir!");
                        System.out.println(this);
                        if (endCondition()){
                            System.exit(0);
                        }
                        continue;
                    }
                    if (findUnitByCoordinates(Dead_Deck, injured) == -1){
                        System.out.println(u.getName() + " says: " + "No one needs help there, Sir!");
                        System.out.println(this);
                        if (endCondition()){
                            System.exit(0);
                        }
                        continue;
                    }
                    int indexOfInjured = findUnitByCoordinates(Dead_Deck, injured);
                    //Player_Deck.add(Dead_Deck.get(indexOfInjured));
                    playfield.remove(injured);
                    addable = Dead_Deck.get(indexOfInjured);
                    Dead_Deck.remove(indexOfInjured);
                    playfield.put(injured, addable.getSymbol());
                    addable.setHealth(10);
                    revived.add(addable);
                    //Player_Deck.get(Player_Deck.size()-1).setHealth(10);
                    System.out.println(u.getName() + " says: " + "Heroes never die!");
                    System.out.println(this);
                    if (endCondition()){
                        System.exit(0);
                    }
                    continue;
                }
                String[] serialized = input.split(" ");
                if (!(serialized[0].equals("-attack") || serialized[0].equals("-move"))){
                    System.out.println(u.getName() + " says: " + "Commander, what are you mumbling? I'd rather wait for you to come to your senses.");
                    actions = actions - 1;
                    System.out.println(this);
                    if (endCondition()){
                        System.exit(0);
                    }
                    continue;
                }
                Point b;
                try{
                    b = new Point(Integer.parseInt(serialized[1]), Integer.parseInt(serialized[2]));
                } catch (NumberFormatException e) {
                    System.out.println(u.getName() + " says: " + "Commander, what are you mumbling? I'd rather wait for you to come to your senses.");
                    actions = actions - 1;
                    System.out.println(this);
                    if (endCondition()){
                        System.exit(0);
                    }
                    continue;
                }
                if (serialized[0].equals("-attack")){
                    int distance = evaluateDistanceEUC(new Point(u.getX(),u.getY()), b);
                    if (distance > u.getStatByName("Range")){
                        System.out.println(u.getName() + " says: " + " I'll never be able to hit that...");
                        actions = actions - 1;
                        System.out.println(this);
                        if (endCondition()){
                            System.exit(0);
                        }
                        continue;
                    }
                    else {
                        int indexOfDefender = findUnitByCoordinates(Invader_Deck, b);
                        if (indexOfDefender == -1){
                            System.out.println(u.getName() + " says: " + "Commander, what are you mumbling? I'd rather wait for you to come to your senses.");
                            actions = actions - 1;
                            System.out.println(this);
                            if (endCondition()){
                                System.exit(0);
                            }
                            continue;
                        }
                        if (attack(u, Invader_Deck.get(indexOfDefender))){
                            System.out.println(u.getName() + " says: " + "Good hit! Target eliminated!");
                            playfield.remove(b);
                            Invader_Deck.remove(indexOfDefender);
                            actions = actions - 1;
                            System.out.println(this);
                            System.out.println("Resources acquired: 5 wood, 5 rock");
                            city.addResources(5,5);
                            if (endCondition()){
                                System.exit(0);
                            }
                            continue;
                        } else {
                            System.out.println(u.getName() + " says: " + "Good hit!");
                            actions = actions - 1;
                            System.out.println(this);
                            if (endCondition()){
                                System.exit(0);
                            }
                            continue;
                        }
                    }
                }
                if (serialized[0].equals("-move")){
                    int reqMovement = evaluateMovement(u, new Point(u.getX(),u.getY()), b);
                    if (reqMovement > u.getStatByName("Movement")){
                        System.out.println(u.getName() + " says: " + "Commander, I'm way too slow for that...");
                        actions = actions - 1;
                        System.out.println(this);
                        if (endCondition()){
                            System.exit(0);
                        }
                    }
                    else {
                        if (findUnitByCoordinates(Player_Deck, b) != -1 || findUnitByCoordinates(Invader_Deck, b) != -1){
                            System.out.println(u.getName() + " says: " + "Commander, there is someone already there!");
                            actions = actions - 1;
                            System.out.println(this);
                            if (endCondition()){
                                System.exit(0);
                            }
                            continue;
                        }
                        playfield.move(new Point(u.getX(),u.getY()), b);
                        u.modifyCoordinates(b.x,b.y);
                        System.out.println(u.getName() + " says: " + "Got it! Moving.");
                        actions = actions - 1;
                        System.out.println(this);
                        if (endCondition()){
                            System.exit(0);
                        }
                    }
                }

            }
            if (threat == 1 && u.getEffect("Threatened") == 0){
                if (u.recieveDamage_Death(u.getEffect("OpportunityDamage"))){
                    System.out.println(u.getName() + " gets killed by an opportunity attack!");
                    int index = findUnitByCoordinates(Player_Deck, new Point(u.getX(), u.getY()));
                    Player_Deck.remove(index);
                }
                else{
                    System.out.println(u.getName() + " gets damaged by an opportunity attack!");
                }
            }
        }
        Player_Deck.addAll(revived);
    }

    public void invaderTurn(){
        String log = "";
        for(Unit u: Invader_Deck){
            assignThreatenedStatus();
            Point playerUnitLocation = scanForPlayer(u);
            if (playerUnitLocation.x != -1){
                int indexOfDefender = findUnitByCoordinates(Player_Deck, playerUnitLocation);
                log = log + "\n" + "Enemy's " + u.getName() + " attacks your " + Player_Deck.get(indexOfDefender).getName();
                if(attack(u, Player_Deck.get(indexOfDefender))){
                    log = log + "\n" + Player_Deck.get(indexOfDefender).getName() + " dies!";
                    playfield.remove(playerUnitLocation);
                    u.addEffect("Dead", 0);
                    Dead_Deck.add(Player_Deck.get(indexOfDefender));
                    Player_Deck.remove(indexOfDefender);
                    continue;
                } else {
                    log = log + "\n" + Player_Deck.get(indexOfDefender).getName() + " gets hit!";
                    continue;
                }
            }
            if (u.getY() != 0) {
                playfield.move(new Point(u.getX(),u.getY()), new Point(u.getX(),u.getY() - 1));
                u.modifyCoordinates(u.getX(), u.getY() - 1);
                log = log + "\n" + "Enemy's " + u.getName() + " moves forward!";
            } else if (u.getStatByName("Movement") < playfield.getHeight()){
                playfield.move(new Point(u.getX(),u.getY()), new Point(u.getX(), u.getY() + u.getStatByName("Movement")));
                u.modifyCoordinates(u.getX(), u.getY() + u.getStatByName("Movement"));
                log = log + "\n" + "Enemy's " + u.getName() + " moves backwards!";
            } else {
                playfield.move(new Point(u.getX(),u.getY()), new Point(u.getX(), playfield.getHeight() - 1));
                u.modifyCoordinates(u.getX(), playfield.getHeight()-1);
                log = log + "\n" + "Enemy's " + u.getName() + " moves backwards!";

            }
        }
        drawDeadUnits();
        System.out.println(log);
    }
    private void save() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(playerName + ".sav");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(city.export());
        objectOutputStream.close();
        outputStream.close();
    }
    public boolean endCondition() throws IOException {
        if (Player_Deck.isEmpty()){
            System.out.println("Invader wins!");
            save();
            return true;
        }
        if (Invader_Deck.isEmpty()){
            System.out.println("Player wins!");
            city.addResources(20,20);
            save();
            return true;
        }
        return false;
    }
}
