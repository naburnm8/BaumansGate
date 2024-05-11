import java.io.*;
import java.util.Scanner;
class NotEnoughFieldSpace extends Exception{}

public class BaumansGate {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Bauman's Gate!");
        System.out.println("Current gamemode: " + args[0]);
        int width = 0;
        int height = 0;
        boolean streamlined = false;
        Scanner stream = new Scanner(System.in);
        if (args[0].equals("streamlined")){
            width = 10;
            height = 10;
            streamlined = true;
        }
        else if (args[0].equals("default")){
            System.out.println("Enter game's parameters: \n Syntax: width height");
            String input = stream.nextLine();
            String[] serialized = input.split(" ");
            try {
                width = Integer.parseInt(serialized[0]);
                height = Integer.parseInt(serialized[1]);
            } catch (NumberFormatException e){
                System.out.println("Wrong syntax!");
                System.exit(-13);
            }
        }
        else {
            System.out.println("Unknown gamemode. Program halted");
            System.exit(-1);
        }
        System.out.println("Enter your nickname: ");
        Scanner stream1 = new Scanner(System.in);
        String playerName = stream1.next();
        File f = new File(playerName + ".sav");
        SaveGame saveGame = null;
        GameHandler instance1 = null;
        MapData loaded = null;
        if(f.exists() && !f.isDirectory()){
            FileInputStream outputStream = new FileInputStream(playerName + ".sav");
            ObjectInputStream objectInputStream = new ObjectInputStream(outputStream);
            try{
            saveGame = (SaveGame) objectInputStream.readObject();}
            catch (ClassNotFoundException e){
                System.out.println("Class not found exception!");
                System.exit(-112);
            }
            outputStream.close();
            objectInputStream.close();
        }
        System.out.println("Would you like to load a map? [path/n]");
        String path = stream1.next();
        if (!path.equals("n")){
            MapReader reader = new MapReader(path);
            loaded = reader.read();
            System.out.println(loaded);
        }

        instance1 = new GameHandler(streamlined,width,height, saveGame, loaded, playerName);
        while(true){
            System.out.println(instance1);
            try {
                instance1.playerTurn();
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Out of bounds! Turn lost. (ur stupid)");
            }
            instance1.invaderTurn();
            if (instance1.endCondition()){
                System.exit(0);
            }
        }
    }
}