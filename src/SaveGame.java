import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SaveGame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1488;
    private ArrayList<Unit> researchedUnits;
    private HashMap<String,Integer> buildings;
    private int[] resources;
    private boolean celebration;
    SaveGame(HashMap<String,Integer> buildings, int[] resources, ArrayList<Unit> researchedUnits, boolean celebration){
        this.buildings = buildings;
        this.researchedUnits = researchedUnits;
        this.resources = resources;
        this.celebration = celebration;
    }
    @Override
    public String toString(){
        return "Buildings: " + buildings + "\n" + "Researched units: " + researchedUnits + "\n" + "Resources: " + resources[0] + " " + resources[1] + "\n" + "Is celebrating: " + celebration;
    }
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        SaveGame saveObj = (SaveGame) obj;
        return this.toString().equals(saveObj.toString());
    }
    public HashMap<String, Integer> buildings(){
        return buildings;
    }
    public ArrayList<Unit> researchedUnits(){
        return researchedUnits;
    }
    public int[] resources(){
        return resources;
    }
    public boolean ongoingCelebration(){
        return celebration;
    }
}
