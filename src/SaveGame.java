import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class SaveGame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1488;
    private ArrayList<Unit> researchedUnits;
    private int[] buildings;
    private int[] resources;
    SaveGame(int[] buildings, int[] resources, ArrayList<Unit> researchedUnits){
        this.buildings = buildings;
        this.researchedUnits = researchedUnits;
        this.resources = resources;
    }
    public int[] buildings(){
        return buildings;
    }
    public ArrayList<Unit> researchedUnits(){
        return researchedUnits;
    }
    public int[] resources(){
        return resources;
    }
}
