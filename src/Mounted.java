import java.util.HashMap;

public class Mounted extends Unit{
    Mounted(int type, int x, int y){
        modifyCoordinates(x,y);
        name = "Mounted type ";
        if(type == 1){
            stats = new int[]{30, 5, 1, 3, 6, 20};
            name = name + "Knight";
            symbol = 'ŕ';
            alt_symbol = 'Ŕ';
            class_identifier = 1;
        }
        else if (type == 2) {
            stats = new int[]{50,2,1,7,5,23};
            name = name + "Armoured";
            symbol = 'ŗ';
            alt_symbol = 'Ŗ';
            class_identifier = 2;
        }
        else if (type == 3) {
            stats = new int[]{25,3,3,2,5,25};
            name = name + "Archer";
            symbol = 'ř';
            alt_symbol = 'Ř';
            class_identifier = 3;
        }
        else {
            stats = new int[]{30, 5, 1, 3, 6, 20};
            name = name + "Knight";
            symbol = 'ŕ';
            alt_symbol = 'Ŕ';
            class_identifier = 1;
        }
        effects = new HashMap<>();
        identifier = 3;
        curr_health = stats[0];
    }
}