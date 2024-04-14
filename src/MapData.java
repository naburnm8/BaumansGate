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
    MapData(int width, int height, char[][] mapLayout){
        this.width = width;
        this.height = height;
        this.mapLayout = mapLayout;
    }
    MapData(int width, int height){
        this.width = width;
        this.height = height;
        mapLayout = new char[height][width];
        fillBlanks();
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
        return output;
    }

}
