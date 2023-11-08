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

        System.out.println("Welcome to Tile Flipper (name pending)!\n");
        System.out.println("The goal of the game is to flip all of the tiles of the grid to (O)!");
        System.out.println("Every time you select a tile to flip, all of the other tiles surrounding it will flip too!\n");
        System.out.println("To flip a tile, type the:\nLetter of the column +\nThe number of the row\nof the tile you want to flip.");
        System.out.println("Examples of valid moves: D4, 4b, 7X, f0, etc.\n");
        System.out.println("Here's an extra challenge:\nYou may also play in random mode, which at the start of the game,\nwill randomly pick which surrounding tiles will flip around your selection.\n");


        System.out.print("Enter the grid width (Try 5 if playing for the first time): ");
        gridWidth = Integer.parseInt(s.nextLine());
        System.out.print("Enter the grid length (Try 5 if playing for the first time): ");
        gridLength = Integer.parseInt(s.nextLine());
        System.out.print("Do you want to enable random mode? (Try N if playing for the first time) (Y/N): ");
        input = s.nextLine();
        if (input.equalsIgnoreCase("y")) {
            randomMode = true;
        }

        System.out.println();

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
                System.out.println("You ran out of turns!");
                gameRunning = false;
            }
        }
    }
}