import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Shop{
    private int account;
    private ArrayList<Unit> Catalogue;
    private void initialiseCatalogue(){
        Catalogue = new ArrayList<>();
        for (int i = 1; i < 4; i++){
            Infantry u1 = new Infantry(i, 0, 0);
            Archer u2 = new Archer(i, 0,0);
            Mounted u3 = new Mounted(i, 0, 0);
            Catalogue.add(u1);
            Catalogue.add(u2);
            Catalogue.add(u3);
        }
    }
    Shop(int _account){
        account = _account;
        initialiseCatalogue();
    }
    private void printCatalogue(){
        for(int i = 0; i < Catalogue.size(); i++){
            System.out.println((i+1) + ".");
            System.out.println(Catalogue.get(i));
        }
    }
    public ArrayList<Unit> commenceShopping(boolean streamlined){
        ArrayList<Unit> Deck = new ArrayList<>();
        if (streamlined){
            for(int i = 0; i < 2; i++){
                Deck.add(new Infantry(1,i,0));
            }
            Deck.add(new Mounted(1,0,0));
            return Deck;
        }
        System.out.println("Welcome to the shop!\nUnits available:");
        printCatalogue();
        System.out.println("To quit type 'quit'");
        String input = "";
        Scanner stream = new Scanner(System.in);
        while(true){
            System.out.println("Gold left: " + account);
            System.out.println("Enter a number of a unit that you want to buy: ");
            input = stream.next();
            int choice = 0;
            if (input.equals("quit")){
                if (Deck.isEmpty()){
                    System.out.println("Buy at least one unit!");
                    continue;
                }
                break;
            }
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e){
                System.out.println("Wrong input!");
                continue;
            }
            Unit chosen = Catalogue.get(choice-1);
            Unit addable = switch (chosen.getIdentifier()) {
                case (1) -> new Infantry(chosen.getClass_identifier(), 0, 0);
                case (2) -> new Archer(chosen.getClass_identifier(), 0, 0);
                case (3) -> new Mounted(chosen.getClass_identifier(), 0, 0);
                default -> null;
            };
            if (account - chosen.getStatByName("Cost") >= 0){
                account = account - chosen.getStatByName("Cost");
                Deck.add(addable);
            } else {
                System.out.println("You're way too poor for that!");
            }
            if (account < 10){
                break;
            }

        }
        return Deck;
    }
    public ArrayList<Unit> invaderShopping(boolean streamlined){
        ArrayList<Unit> Deck = new ArrayList<>();
        if(streamlined){
            Deck.add(new Archer(3,0,0));
            Deck.add(new Infantry(2,0,0));
        } else {
            Random generator = new Random();
            for (int i = 0; i < 3; i++){
                int num = generator.nextInt(9);
                Unit chosen = Catalogue.get(num);
                Unit addable = switch (chosen.getIdentifier()) {
                    case (1) -> new Infantry(chosen.getClass_identifier(), 0, 0);
                    case (2) -> new Archer(chosen.getClass_identifier(), 0, 0);
                    case (3) -> new Mounted(chosen.getClass_identifier(), 0, 0);
                    default -> null;
                };
                Deck.add(addable);
            }
        }
        for (Unit u: Deck){
            u.swapSymbol();
        }
        return Deck;
    }
}
