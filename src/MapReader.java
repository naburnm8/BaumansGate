
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class IncorrectFileStructure extends Exception{}

public class MapReader {
    private String path;
    MapReader(String path){
        this.path = path;
    }
    private char[][] StringToMatrix(int height, int width, String mapLayoutString){
        char[][] converted = new char[height][width];
        String[] split = mapLayoutString.split("\n");
        for (int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                converted[i][j] = split[i].charAt(j);
            }
        }
        return converted;
    }
    MapData read() throws IOException, IncorrectFileStructure, IllegalTileSymbol {
        FileReader fr = new FileReader(path);
        Scanner stream = new Scanner(fr);
        if (!stream.hasNextInt()){
            throw new IncorrectFileStructure();
        }
        int width = stream.nextInt();
        if (!stream.hasNextInt()){
            throw new IncorrectFileStructure();
        }
        int height = stream.nextInt();
        stream.nextLine();
        stream.useDelimiter("\\Z");
        String mapLayoutString = stream.next();
        fr.close();
        return new MapData(width,height,StringToMatrix(height,width,mapLayoutString));
    }
}
