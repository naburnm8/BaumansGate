import java.util.HashMap;
import java.util.Random;

public class MarketShop {
    private int level;
    private final String[] marketResources = {"Cloth", "Leather", "Silk"};
    private final int basePrice = 10;
    private HashMap<String, Integer> priceMap;
    private HashMap<String, Integer> inventory;
    private boolean ongoingCelebration;
    MarketShop(int level, boolean ongoingCelebration){
        this.level = level;
        priceMap = new HashMap<>();
        inventory = new HashMap<>();
        int padding = 0;
        this.ongoingCelebration = ongoingCelebration;
        for(String name: marketResources){
            priceMap.put(name, basePrice + padding);
            inventory.put(name,0);
            padding = padding + 5;
        }
    }
    MarketShop(){
        this(1, false);
    }
    MarketShop(boolean ongoingCelebration){
        this(1, ongoingCelebration);
    }
    @Override
    public String toString(){
        String out = "";
        for(String name: marketResources){
            out = out + name + ", current price: " + priceMap.get(name) + ", current stock: " + inventory.get(name) + "\n";
        }
        return out;
    }
    private void adjustPrices(){
        int newPriceCloth = basePrice - (int)(1.75*inventory.get("Cloth"));
        int newPriceLeather = basePrice - (int)(0.75*inventory.get("Leather"));
        int newPriceSilk = basePrice - (int)(0.25*inventory.get("Silk"));
        if (newPriceCloth <= 0){
            newPriceCloth = 1;
        }
        if (newPriceLeather <= 0){
            newPriceLeather = 1;
        }
        if (newPriceSilk <= 0){
            newPriceSilk = 1;
        }
        priceMap.put("Cloth", newPriceCloth);
        priceMap.put("Leather", newPriceLeather);
        priceMap.put("Silk", newPriceSilk);
    }
    private String probabilityCheck(int randint){
        if (randint >= 1 && randint <= 5){
            return "Cloth";
        }
        if(randint > 5 && randint <= 8){
            return "Leather";
        }
        if(randint > 8 && randint <= 10){
            return "Silk";
        }
        return "Cloth";
    }
    public void productionDEB(boolean debug){
        if (debug){
            inventory.put("Cloth", 3);
            inventory.put("Leather", 2);
            inventory.put("Silk", 4);
            adjustPrices();
            return;
        }
        for (int i = 0; i < level; i++){
            Random rand = new Random();
            String produced = probabilityCheck(rand.nextInt(1,10));
            int randQ = rand.nextInt(1,3);
            if(ongoingCelebration){
                randQ = randQ*2;
            }
            inventory.put(produced, inventory.get(produced) + randQ);
        }
        adjustPrices();
    }
    public void production(){
        productionDEB(false);
    }
    public int sell(String name){
        int income = inventory.get(name) * priceMap.get(name);
        System.out.println("Sold " + inventory.get(name) + " pcs. of " + name + " for " + income + " gold");
        inventory.put(name, 0);
        adjustPrices();
        return income;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setOngoingCelebration(boolean ongoingCelebration){
        this.ongoingCelebration = ongoingCelebration;
    }
    public boolean isOngoingCelebration(){
        return ongoingCelebration;
    }

}
