import java.util.Scanner;

public class Main {

    /**
     * Prints out the initial instructions for the game.
     */
    public static void printHelp() {
        System.out.println("Welcome to Tile Flipper!\n");
        System.out.println("The goal of the game is to flip all of the tiles of the grid to (O)!");
        System.out.println("Every time you select a tile to flip, all of the other tiles surrounding it will flip too!\n");
        System.out.println("To flip a tile, type the:\nLetter of the column +\nThe number of the row\nof the tile you want to flip.");
        System.out.println("Examples of valid moves: D4, 4b, 7X, f0, etc.\n");
        System.out.println("Here's an extra challenge:\nYou may also play in random mode, which at the start of the game,\nwill randomly pick which surrounding tiles will flip around your selection.\n");
    }

    /**
     * Starts the game-specific instructions, starting with gathering grid customizations and then the main game loop.
     */
    public static void gameLoop() {
        Scanner s = new Scanner(System.in);

        String input;
        int gridLength;
        int gridWidth;
        boolean randomMode = false;
        boolean gameRunning = true;

        // gather grid customizations
        System.out.print("Enter the grid width (Try 3 if playing for the first time): ");
        input = s.nextLine();

        // collect user input for length and width, if the input given is not a number for either, resort to a default value
        try {
            gridWidth = Integer.parseInt(input);
        } catch (Exception InvalidWidth) {
            gridWidth = 3;
            System.out.println("Invalid width, defaulting to a width of 3!");
        }
        System.out.print("Enter the grid length (Try 3 if playing for the first time): ");
        try {
            gridLength = Integer.parseInt(s.nextLine());
        } catch (Exception InvalidLength) {
            gridLength = 3;
            System.out.println("Invalid length, defaulting to a length of 3!");
        }

        System.out.print("Do you want to enable random mode? (Try N if playing for the first time) (Y/N): ");
        input = s.nextLine();
        if (input.equalsIgnoreCase("y")) {
            randomMode = true;
        }

        System.out.println();

        FlipGrid f = new FlipGrid(gridLength, gridWidth, randomMode);
        System.out.println(f);

        // primary game loop
        while (gameRunning) {
            System.out.print("Enter your grid selection (\"e\" to end the current game, \"h\" to print the instructions again): ");
            input = s.nextLine();
            if (input.equalsIgnoreCase("e")) {
                gameRunning = false;
            } else if (input.equalsIgnoreCase("h")) {
                printHelp();
                continue;
            } else if (f.isMoveValid(input)) {
                f.makePlayerMove(input);
            } else {
                System.out.println("Move invalid! Make another move fits the format (Letter + Number), like 5B, r7, 12b, P21, etc.");
                continue;
            }

            if (gameRunning) {
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

    /**
     * Initial code that begins the game loop and manages game state outside the primary game loop.
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input;

        boolean programRunning = true;

        printHelp();

        gameLoop();

        while (programRunning) {
            System.out.println("Would you like to play another game? (Y/N): ");
            input = s.nextLine();

            if (input.equalsIgnoreCase("n")) {
                programRunning = false;
            } else if (input.equalsIgnoreCase("y")) {
                gameLoop();
            }
        }

    }
}