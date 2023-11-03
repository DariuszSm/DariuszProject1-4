import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String input;

        FlipGrid f = new FlipGrid(3, 3, true);

        Scanner s = new Scanner(System.in);

        System.out.println(f);

        while (true) {
            input = s.nextLine();
            f.makeMove(input);
            System.out.println(f);
        }
    }
}