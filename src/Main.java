import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String input;
        int gridLength;
        int gridWidth;
        boolean randomMode = false;

        boolean gameRunning = true;

        Scanner s = new Scanner(System.in);

        System.out.println("Enter the grid length: ");
        gridLength = Integer.parseInt(s.nextLine());
        System.out.println("Enter the grid width: ");
        gridWidth = Integer.parseInt(s.nextLine());
        System.out.println("Do you want random a selection area? (Y/N): ");
        input = s.nextLine();
        if (input.equalsIgnoreCase("y")) {
            randomMode = true;
        }

        FlipGrid f = new FlipGrid(gridLength, gridWidth, randomMode);
        System.out.println(f);

        while (gameRunning) {
            input = s.nextLine();
            f.makePlayerMove(input);
            System.out.println(f);
            if (f.isGameWon()) {
                System.out.println("You won!");
                gameRunning = false;
            } else if (f.getTurnsLeft() < 1) {
                System.out.println("You lost!");
                gameRunning = false;
            }
        }
    }
}