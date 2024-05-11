import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MapReader {
    private String path;
    MapReader(String path){
        this.path = path;
    }
    public MapData read() throws IOException {
        FileInputStream fStream = new FileInputStream(path);
        ObjectInputStream oStream = new ObjectInputStream(fStream);
        MapData read = null;
        try{
        read = (MapData) oStream.readObject();
        } catch(ClassNotFoundException e){
            System.out.println("Class not found!");
            System.exit(-113);
        }
        oStream.close();
        fStream.close();

        return read;
    }

    /*
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
    MapData read() throws IOException, IncorrectFileStructure {
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
        return new MapData(width,height,StringToMatrix(height,width,mapLayoutString), null);
    }
     */
}
