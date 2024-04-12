import java.util.HashMap;

public class Archer extends Unit{
    Archer(int type, int x, int y){
        modifyCoordinates(x,y);
        name = "Archer type ";
        if(type == 1){
            stats = new int[]{30, 6, 5, 8, 2, 15};
            name = name + "Longbow";
            symbol = 'ā';
            alt_symbol = 'Ā';
            class_identifier = 1;
        }
        else if (type == 2) {
            stats = new int[]{25,3,3,4,4,19};
            name = name + "Shortbow";
            symbol = 'ă';
            alt_symbol = 'Ă';
            class_identifier = 2;
        }
        else if (type == 3) {
            stats = new int[]{40,7,6,3,2,23};
            name = name + "Crossbow";
            symbol = 'ą';
            alt_symbol = 'Ą';
            class_identifier = 3;
        }
        else{
            stats = new int[]{30, 6, 5, 8, 2, 15};
            name = name + "Longbow";
            symbol = 'ā';
            alt_symbol = 'Ā';
            class_identifier = 1;
        }
        effects = new HashMap<>();
        identifier = 2;
        curr_health = stats[0];
    }
}
