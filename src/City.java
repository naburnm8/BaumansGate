import java.util.ArrayList;
import java.util.Scanner;

public class City {
    private int[] resources;
    private int[] buildings;
    private ArrayList<Unit> researchedUnits;
    private ArrayList<Unit> addableResearchedUnits;
    private boolean didResearch;
    public final String[] names = {"Hospital", "Tavern", "Blacksmith", "Armoury", "Academy", "Market", "Workshop"};
    public final String[] modStats = {"Health", "Movement", "Attack", "Defence"};
    private final int[][] prices = {{10,10}, {5,15},{20, 0}, {9,9}, {14,14}, {5,5}, {10,5}};
    private int BuildingNameToIndex(String building){
        for (int i = 0; i < names.length; i++){
            if (building.equals(names[i])){
                return i;
            }
        }
        return -1;
    }
    public SaveGame export(){
        return new SaveGame(buildings,resources,researchedUnits);
    }
    public int getBuildingByName(String building){
        int index = BuildingNameToIndex(building);
        if (index == -1){
            return -1;
        }
        return buildings[index];
    }
    City(int wood, int stone, int[] buildings, ArrayList<Unit> researchedUnits){
        didResearch = false;
        resources = new int[]{wood, stone};
        this.buildings = buildings;
        this.researchedUnits = researchedUnits;
        addableResearchedUnits = new ArrayList<>();
        addableResearchedUnits.addAll(researchedUnits);
    }
    City (int wood, int stone){
        this(wood, stone, new int[]{0,0,0,0,0,0,0}, new ArrayList<Unit>());
    }
    public boolean BuildBuilding(String building){
        int index = BuildingNameToIndex(building);
        if (resources[0] - prices[index][0] < 0 || resources[1] - prices[index][1] < 0){
            System.out.println("Not enough resources!");
            return false;
        }
        if (buildings[index] >= 4){
            System.out.println("Maximum lvl/quantity reached!");
            return false;
        }
        if(building.equals("Academy") && buildings[index] >= 1){
            System.out.println("Academy max lvl reached!");
            return false;
        }
        resources[0] = resources[0] - prices[index][0];
        resources[1] = resources[1] - prices[index][1];
        buildings[index] = buildings[index] + 1;
        return true;
    }
    @Override
    public String toString(){
        String output = "";
        output = output + "Remaining resources: Wood - " + resources[0] + "; Stone - " + resources[1] + "\n";
        for (String building: names){
            int index = BuildingNameToIndex(building);
            output = output + "Building: " + building + ", " + buildings[index] + "\n";
        }
        if (buildings[BuildingNameToIndex("Academy")] == 1 && !didResearch){
            output = output + "Research available" + "\n";
        }
        return output;
    }
    public String toStringShop(){
        String output = "City management prices table: \n";
        for (String building: names){
            int index = BuildingNameToIndex(building);
            output = output + building + ": " + "Wood: " + prices[index][0] + ", Stone: " + prices[index][1] + "\n";
        }
        return output;
    }
    public boolean researchUnit(){
        if (didResearch || buildings[BuildingNameToIndex("Academy")] < 1){
            return false;
        }
        System.out.println("Enter unit type: [Infantry - 1; Archer - 2; Mounted - 3]");
        Scanner stream = new Scanner(System.in);
        int scanned = stream.nextInt();
        CustomUnit Researched = new CustomUnit(scanned, 0, 0, (char)(97 + researchedUnits.size() + 1));
        System.out.println("Enter new stats: \n Syntax: health,attack,range,defence,movement");
        String scanned1 = stream.next();
        String[] split = scanned1.split(",");
        Researched.replaceStats(new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), 0});
        System.out.println("help");
        researchedUnits.add(Researched);
        addableResearchedUnits.add(Researched);
        didResearch = true;
        return true;
    }
    public void addResources(int wood, int stone){
        resources[0] = resources[0] + wood;
        resources[1] = resources[1] + stone;
    }
    public void getResearchedUnits(){
        addableResearchedUnits = new ArrayList<>();
        return;
    }
    public ArrayList<Unit> getResearchedUnitsSafe(){
        for(Unit u: addableResearchedUnits){
            System.out.println("addable");
            System.out.println(u);
        }
        return addableResearchedUnits;
    }
    public void getResources(int gold){
        resources[0] = resources[0] + gold*getBuildingByName("Market");
        resources[1] = resources[1] + gold*getBuildingByName("Market");
        //System.out.println(gold*buildings[getBuildingByName("Market")]+ " " + gold + " " + buildings[getBuildingByName("Market")]);
    }


}
