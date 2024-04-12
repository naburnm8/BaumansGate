import java.util.Arrays;
import java.util.Random;

public class Field{
    private int width;
    private int height;
    private char[][] field;
    private char[][] field_printable;
    static public char[] gnd_symbols = {'▒','░','▓','◆'};
    final private double TREES_P = 0.05;
    final private double HILL_P = 0.1;
    final private double SWAMP_P = 0.04;

    private boolean matrixContains(int[][] matrix, int[] query){
        for (int[] pair: matrix){
            if(Arrays.equals(pair,query)){
                return true;
            }
        }
        return false;
    }
    private void copyField(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                field_printable[i][j] = field[i][j];
            }
        }
    }
    private void addObstacles(){
        int trees_q = (int) (width*height*TREES_P);
        int hill_q = (int) (width*height*HILL_P);
        int swamp_q = (int) (width*height*SWAMP_P);
        Random generator = new Random();
        int[][] trees = new int[trees_q][2];
        int[][] hills = new int[hill_q][2];
        int[][] swamps = new int[swamp_q][2];
        for (int i = 0; i < trees_q; i++){
            int x = generator.nextInt(width);
            int y = generator.nextInt(height);
            trees[i] = new int[]{x,y};
        }
        for (int i = 0; i < hill_q; i++){
            int x = generator.nextInt(width);
            int y = generator.nextInt(height);
            hills[i] = new int[]{x,y};
        }
        for (int i = 0; i < swamp_q; i++){
            int x = generator.nextInt(width);
            int y = generator.nextInt(height);
            swamps[i] = new int[]{x,y};
        }
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int[] coords = {j,i};
                if(matrixContains(trees, coords)){
                    field[i][j] = gnd_symbols[3];
                }
                if(matrixContains(hills, coords)){
                    field[i][j] = gnd_symbols[2];
                }
                if(matrixContains(swamps, coords)){
                    field[i][j] = gnd_symbols[1];
                }
            }
        }
    }
    private void fillField(){
        for (int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                field[i][j] = gnd_symbols[0];
            }
        }
        addObstacles();
    }
    Field(int _width, int _height){
        width = _width;
        height = _height;
        field = new char[height][width];
        field_printable = new char[height][width];
        fillField();
        copyField();
    }
    @Override
    public String toString(){
        String output = new String();
        for (char[] arr: field_printable){
            for(char symb: arr){
                output = output + symb;
            }
            output = output + "\n";
        }
        return output;
    }
    public String toStringField(){
        String output = new String();
        for (char[] arr: field){
            for(char symb: arr){
                output = output + symb;
            }
            output = output + "\n";
        }
        return output;
    }
    public boolean move(Point a, Point b){
        if (field_printable[a.y][a.x] == field[a.y][a.x]){
            return false;
        }
        field_printable[b.y][b.x] = field_printable[a.y][a.x];
        field_printable[a.y][a.x] = field[a.y][a.x];
        return true;
    }
    public void put(Point a, char symbol){
        field_printable[a.y][a.x] = symbol;
    }
    public boolean isOccupied(Point a){
        return field_printable[a.y][a.x] != field[a.y][a.x];
    }
    public char atPoint(Point a, boolean printable){
        if (printable){
            return field_printable[a.y][a.x];
        }
        return field[a.y][a.x];
    }
    public char at(int x, int y, boolean printable){
        if (printable){
            return field_printable[y][x];
        }
        return field[y][x];
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void remove(Point a){
        field_printable[a.y][a.x] = field[a.y][a.x];
    }
}
