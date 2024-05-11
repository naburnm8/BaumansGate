import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class MapData implements Serializable{
    @Serial
    private static final long serialVersionUID = 1491;
    public int width;
    public int height;
    public char[][] mapLayout;
    public ArrayList<Obstacle> obstacles;
    private void fillBlanks(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                mapLayout[i][j] = Field.gnd_symbols[0];
            }
        }
    }
    MapData(int width, int height, char[][] mapLayout, ArrayList<Obstacle> obstacles){
        this.width = width;
        this.height = height;
        this.mapLayout = mapLayout;
        this.obstacles = obstacles;

    }
    MapData(int width, int height){
        this.width = width;
        this.height = height;
        mapLayout = new char[height][width];
        obstacles = Obstacle.generateDefaultObstacles();
        fillBlanks();
    }
    private String toStringLegalTypes(){
        String out = "";
        for(Obstacle obs: obstacles){
            out = out + obs.toString() + "\n";
        }
        return out;
    }
    @Override
    public String toString() {
        String output = new String();
        for (char[] arr: mapLayout){
            for(char symb: arr){
                output = output + symb;
            }
            output = output + "\n";
        }
        output = output + toStringLegalTypes();
        return output;
    }
    public String toStringMenu() {
        return this + toStringLegalTypes();
    }

}
