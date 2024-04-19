import java.util.HashMap;

public class CustomUnit extends Unit{
    CustomUnit(int type, int x, int y, char symbol){
        modifyCoordinates(x,y);
        name = "Custom Unit type ";
        if(type == 1){
            stats = new int[]{50, 100, 100, 8, 10, 10};
            name = name + "Infantry";
            this.symbol = symbol;
            this.alt_symbol = '0';
            class_identifier = 1;
        }
        else if (type == 2) {
            stats = new int[]{30, 6, 5, 8, 2, 15};
            name = name + "Archer";
            this.symbol = symbol;
            this.alt_symbol = '0';
            class_identifier = 2;
        }
        else if (type == 3) {
            stats = new int[]{30, 5, 1, 3, 6, 20};
            name = name + "Mounted";
            this.symbol = symbol;
            this.alt_symbol = '0';
            class_identifier = 3;
        }
        else {
            stats = new int[]{50, 100, 100, 8, 10, 10};
            name = name + "Infantry";
            this.symbol = symbol;
            this.alt_symbol = '0';
            class_identifier = 1;
        }
        effects = new HashMap<>();
        identifier = 4;
        curr_health = stats[0];
    }
}
