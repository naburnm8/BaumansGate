
class IllegalTileSymbol extends Exception{}
public class MapData {
    public int width;
    public int height;
    public char[][] mapLayout;
    private void fillBlanks(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                mapLayout[i][j] = Field.gnd_symbols[0];
            }
        }
    }
    private boolean isLegalCharacter(char query){
        for(int i = 0; i < Field.gnd_symbols.length; i++){
            if (query == Field.gnd_symbols[i]){
                return true;
            }
        }
        return false;
    }
    private boolean checkLegitimacy(){
        for (char[] arr: mapLayout){
            for(char symb: arr){
                if(!isLegalCharacter(symb)){
                    return false;
                }
            }
        }
        return true;
    }
    MapData(int width, int height, char[][] mapLayout) throws IllegalTileSymbol {
        this.width = width;
        this.height = height;
        this.mapLayout = mapLayout;
        if(!checkLegitimacy()){
            throw new IllegalTileSymbol();
        }

    }
    MapData(int width, int height){
        this.width = width;
        this.height = height;
        mapLayout = new char[height][width];
        fillBlanks();
    }
    private String toStringLegalTypes(){
        String out = "";
        for(int i = 0; i < Field.gnd_symbols.length; i++){
            out = out + (i+1) + ". " + Field.gnd_symbols[i] + "\n";
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
        //output = output + toStringLegalTypes();
        return output;
    }
    public String toStringMenu() {
        return this + toStringLegalTypes();
    }
}
