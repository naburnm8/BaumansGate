import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Obstacle implements Serializable{
    @Serial
    private static final long serialVersionUID = 1490;
    private double[] fines;
    private char symbol;
    Obstacle(double[] fines, char symbol){
        this.fines = fines;
        this.symbol = symbol;
    }
    public char getSymbol(){
        return symbol;
    }
    public double fine(int pos){
        return fines[pos];
    }
    public static ArrayList<Obstacle> generateDefaultObstacles(){
        char[] gnd_symbols = Field.gnd_symbols;
        double[][] fines = {{1,1.5,2,1.2},{1,1.8,2.2,1},{1,2.2,1.2,1.5}};
        ArrayList<Obstacle> out = new ArrayList<>();
        for (int i = 0; i < gnd_symbols.length; i++){
            char symb_out = gnd_symbols[i];
            double[] fines_out = {fines[0][i], fines[1][i], fines[2][i]};
            Obstacle out1 = new Obstacle(fines_out,symb_out);
            out.add(out1);
        }
        return out;
    }
    @Override
    public String toString(){
        return "Obstacle: " + this.symbol + ", fines: " + fines[0] + ", " + fines[1] + ", " + fines[2];
    }


}
