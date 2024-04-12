import java.util.HashMap;

public abstract class Unit {
    protected int[] stats = new int[6];
    protected int curr_health;
    final protected String[] stat_names = {"Health","Attack","Range","Defence","Movement","Cost"};
    protected int[] coordinates = new int[2];
    protected String name;
    protected char symbol;
    protected char alt_symbol;
    protected int identifier;
    protected int class_identifier;
    protected HashMap<String, Integer> effects;
    protected int StatNameToIndex(String stat){
        for(int i = 0; i < stat_names.length; i++){
            if (stat_names[i].equals(stat)){
                return i;
            }
        }
        return -1;
    }
    public int getEffect(String key){
        if (effects.get(key) == null){
            return 0;
        }
        return effects.get(key);
    }
    public void setHealth(int health){
        curr_health = health;
    }
    public void addEffect(String key, int value){
        effects.put(key, value);
    }
    public int getStatByName(String stat){
        int stat_index = StatNameToIndex(stat);
        if (stat_index == -1){
            return -1;
        }
        return stats[stat_index];
    }
    public int getCurrentHealth(){
        return curr_health;
    }
    public boolean recieveDamage_Death(int damage){
        curr_health = curr_health - damage;
        if (curr_health <= 0){
            return true;
        }
        return false;
    }
    public int getX(){
        return coordinates[0];
    }
    public int getY(){
        return coordinates[1];
    }
    public void modifyCoordinates(int x, int y){
        coordinates[0] = x;
        coordinates[1] = y;
    }
    @Override
    public String toString(){
        String output = "Unit: " + name;
        for (int i = 0; i < stats.length; i++){
            output = output + "\n" + stat_names[i] + ": " + stats[i];
        }
        output = output + "\n" + "Current health: " + curr_health;
        return output;
    }
    public String shortToString(){
        return "Unit: " + name + ", current health: " + curr_health + ", available movement: " + getStatByName("Movement") + ", symbol: " + symbol;
    }
    public char getSymbol(){
        return symbol;
    }
    public void swapSymbol(){
        symbol = alt_symbol;
    }
    public int getIdentifier(){
        return identifier;
    }
    public int getCurr_health(){
        return curr_health;
    }
    public String getName(){
        return name;
    }
    public int getClass_identifier(){
        return class_identifier;
    }
}
