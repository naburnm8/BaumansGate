import java.util.HashMap;

public class Infantry extends Unit{
    Infantry(int type, int x, int y){
        modifyCoordinates(x,y);
        name = "Infantry type ";
        if(type == 1){
            stats = new int[]{50, 100, 100, 8, 10, 10};
            name = name + "Swordman";
            symbol = 'ĩ';
            alt_symbol = 'Ĩ';
            class_identifier = 1;
        }
        else if (type == 2) {
            stats = new int[]{35,100,100,4,6,15};
            name = name + "Spearman";
            symbol = 'ī';
            alt_symbol = 'Ī';
            class_identifier = 2;
        }
        else if (type == 3) {
            stats = new int[]{45,9,1,3,4,20};
            name = name + "Hatchetman";
            symbol = 'ĭ';
            alt_symbol = 'Ĭ';
            class_identifier = 3;
        }
        else {
            stats = new int[]{50, 5, 1, 8, 3, 10};
            name = name + "Swordman";
            symbol = 'ĩ';
            alt_symbol = 'Ĩ';
            class_identifier = 1;
        }
        effects = new HashMap<>();
        identifier = 1;
        curr_health = stats[0];
    }
}
