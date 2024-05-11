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
    SaveGame(HashMap<String,Integer> buildings, int[] resources, ArrayList<Unit> researchedUnits){
        this.buildings = buildings;
        this.researchedUnits = researchedUnits;
        this.resources = resources;
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
}
