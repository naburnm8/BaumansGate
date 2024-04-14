import java.io.FileWriter;
import java.io.IOException;

public class MapWriter {
    private String path;
    MapWriter(String path){
        this.path = path;
    }
    public void write(MapData map) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write(Integer.toString(map.width)+"\n");
        fw.write(Integer.toString(map.height)+"\n");
        fw.write(map.toString());
        fw.close();
    }
}
