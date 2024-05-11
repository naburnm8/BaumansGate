import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MapWriter {
    private String path;
    MapWriter(String path){
        this.path = path;
    }
    public void write(MapData map) throws IOException {
        FileOutputStream fStream = new FileOutputStream(path);
        ObjectOutputStream oStream = new ObjectOutputStream(fStream);
        oStream.writeObject(map);
        oStream.close();
        fStream.close();
    }
}
