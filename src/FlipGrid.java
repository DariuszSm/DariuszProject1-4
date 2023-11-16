import java.util.ArrayList;

public class FlipGrid {

    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int maxWidth = 26;
    private final int maxLength = 26;
    private final int minWidth = 2;
    private final int minLength = 2;

    private ArrayList<ArrayList<String>> playGrid = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> previousGrid = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> selectGrid = new ArrayList<ArrayList<String>>();

    private int gridLength;
    private int gridWidth;

    private boolean randomMode;

    private int turnsLeft;

    /**
     * Generate grids based on length and width, and generates a random selection grid if random mode is enabled.
     * @param length Length of the grid for the game (amount of rows)
     * @param width Width of the grid for the game (amount of columns)
     * @param random Determines if random mode is enabled or not.
     */
    public FlipGrid(int length, int width, boolean random) {

        // alter the parameters passed in so that they never exceed the maximum or minimum values for the grid
        if (length > maxLength) {
            length = maxLength;
        } else if (length < minLength) {
            length = minLength;
        }
        if (width > maxWidth) {
            width = maxWidth;
        } else if (width < minWidth) {
            width = minWidth;
        }

        gridLength = length;
        gridWidth = width;
        randomMode = random;

        turnsLeft = (int)Math.sqrt(Math.pow(length * width, 1.3));
        // make moves even
        if ((turnsLeft % 2) == 1) {
            turnsLeft++;
        }

        // generate the random selection grid if random mode is enabled
        if (random) {
            // generate one random tile to be selected when random mode is selected.
            int randomSel = (int)(Math.random() * 8);
            if (randomSel > 4) {
                randomSel++;
            }

            // generate the selection grid, which will affect which tiles around the selected tile will be flipped.
            for (int i = 0; i < 3; i++) {
                selectGrid.add(new ArrayList<String>());;
                for (int f = 0; f < 3; f++) {
                    if ((int)(Math.random() * 2) == 0) {
                        selectGrid.get(i).add("O");
                    } else {
                        selectGrid.get(i).add(".");
                    }
                }
            }

            // set the middle of the selection grid to a .
            selectGrid.get(1).set(1, ".");
            selectGrid.get(randomSel/3).set(randomSel % 3, ".");

        }

        // generate starting layout for the grid
        for (int i = 0; i < length; i++) {
            playGrid.add(new ArrayList<String>());;
            for (int f = 0; f < width; f++) {
                playGrid.get(i).add("O");
            }
        }

        // generate starting layout for the grid
        for (int i = 0; i < length; i++) {
            previousGrid.add(new ArrayList<String>());;
            for (int f = 0; f < width; f++) {
                previousGrid.get(i).add("O");
            }
        }

        // used to track all previous moves done to the grid during generation
        ArrayList<String> moveTrack = new ArrayList<String>();

        // directly make moves onto the grid based on the amount of turns the player has to solve the grid
        for (int i = 0; i < (int)(turnsLeft * .75) + turnsLeft % 2; i++) {
            moveTrack.add(randomMove());

            // check if any of the previous moves match with the one just made
            for (int f = 0; f < moveTrack.size()-1; f++) {
                if (f != 0 && moveTrack.get(f).equals(moveTrack.get(moveTrack.size()-1))) {
                    moveTrack.set(moveTrack.size()-1, randomMove());
                    f--;
                }
            }
            makeMove(moveTrack.get(moveTrack.size()-1));
        }
        System.out.println(moveTrack);


        // set previous grid to the play grid
        for (int i = 0; i < length; i++) {
            for (int f = 0; f < width; f++) {
                previousGrid.get(i).set(f, playGrid.get(i).get(f));
            }
        }

    }

    /**
     * Generate a String that can be used as a valid move for makeMove.
     * @return A string with the format A11, where a letter represents the width, and the number as the length.
     */
    private String randomMove() {

        int randomWidth = (int)(Math.random() * gridWidth);
        int randomLength = (int)(Math.random() * gridLength) + 1;

        return "" + ALPHABET.charAt(randomWidth) + randomLength;
    }

    /**
     * Parses the player input to get the X value of their selection.
     * Player format should look like "A5", "H12", "20z", etc
     * Max allowed width is 25, or the width of the grid, whichever is lower
     *
     * @param input Raw player input
     * @return Parsed width of input
     */
    private int getXInput(String input) {
        String firstChar;

        int parsedWidth;

        input = input.toLowerCase();


        // Test for if the input is in the last or first character of the array,
        // and extract the correct character from the input accordingly
        try {
            // This first line will force the catch segment to run if the first letter of the input is not an integer.
            Integer.parseInt(String.valueOf(input.charAt(0)));
            firstChar = String.valueOf(input.charAt(input.length()-1));
        } catch (Exception NumberFormatException) {
            firstChar = String.valueOf(input.charAt(0));
        }
        parsedWidth = firstChar.compareTo("a");

        // limit the value range to only include numbers between 0 and the maximum width of the grid
        if (parsedWidth < 0) {
            parsedWidth = 0;
        } else if (parsedWidth > gridWidth - 1) {
            parsedWidth = gridWidth - 1;
        }

        return parsedWidth;
    }

    /**
     * Parses the player input to get the Y value of their selection.
     * Player format should look like "A5", "H12", "z20", etc.
     * Max allowed length is 25, or the length of the grid, whichever is lower
     *
     * @param input Raw player input
     * @return Parsed length of input
     */
    private int getYInput(String input) {
        int parsedLength;


         // Test for if the input is in the last or first character of the array, and extract the correct character from the input accordingly
        try {
            // This first line will force the catch segment to run if the first letter of the input is not an integer.
            parsedLength = Integer.parseInt(input.substring(1)) - 1;
        } catch (Exception NumberFormatException) {
            // This line will force the program to close if anything other than a number is leading the last number of the input.
            parsedLength = Integer.parseInt(input.substring(0, input.length()-1)) - 1;
        }

        // limit the value range to only include numbers between 0 and the maximum length of the grid
        if (parsedLength < 0) {
            parsedLength = 0;
        } else if (parsedLength > gridLength - 1) {
            parsedLength = gridLength - 1;
        }

        return parsedLength;
    }

    /**
     * Updates the previousGrid grid to the current play grid.
     */
    private void updatePrevGrid() {
        // set previous grid to the play grid
        for (int i = 0; i < gridLength; i++) {
            for (int f = 0; f < gridWidth; f++) {
                previousGrid.get(i).set(f, playGrid.get(i).get(f));
            }
        }
    }

    /**
     * Makes a move on the grid held within the class the function is called in.
     * @param playerSelection The input that represents where on the grid the move takes place in.
     */
    private void makeMove(String playerSelection) {

        // first order of action so that the grids are always different between moves
        updatePrevGrid();

        int selectedX = getXInput(playerSelection);
        int selectedY = getYInput(playerSelection);

        for (int i = -1; i <= 1; i++) {
            if (selectedY + i < 0 || selectedY + i >= gridLength) {
                continue;
            }
            for (int f = -1; f <= 1; f++) {
                if (selectedX + f < 0 || selectedX + f >= gridWidth) {
                    continue;
                }

                if (randomMode) {
                    if (!selectGrid.get(i+1).get(f+1).equals(".")) {
                        continue;
                    }
                }

                if (playGrid.get(selectedY + i).get(selectedX+f).equals(".")) {
                    playGrid.get(selectedY + i).set(selectedX+f, "O");
                } else if (playGrid.get(selectedY + i).get(selectedX+f).equals("O")) {
                    playGrid.get(selectedY + i).set(selectedX+f, ".");
                }

            }
        }
    }

    /**
     * Calls makeMove, then decrements the turns left.
     * @param playerSelection the String the player uses to make the move, formatted 5A, I23, 9Z, etc.
     */
    public void makePlayerMove(String playerSelection) {
        makeMove(playerSelection);
        turnsLeft--;
    }

    /**
     * @return The amount of turns left within the game that the class contains.
     */
    public int getTurnsLeft() {
        return turnsLeft;
    }

    /**
     * Checks if the game is in a completed, won state (all O tiles).
     * @return If the game is in a won state, return true, otherwise, return false.
     */
    public boolean isGameWon() {

        for (int i = 0; i < gridLength; i++) {
            for (int f = 0; f < gridWidth; f++) {
                if (playGrid.get(i).get(f).equals(".")) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Produces a String designed to display information about the current state of the game the player is playing.
     * @return A String displaying the play grid, the amount of turns left, and the random selection grid if random mode is active.
     */
    public String toString() {
        String finalString = "";

        // random selection grid drawing
        if (randomMode) {
            finalString += "Random selection area:\n ";

            // border drawing
            for (int i = 0; i < 5; i++) finalString += "-";
            finalString += "\n";

            // grid drawing
            for (int i = 0; i < 3; i++) {
                finalString += "|";

                // Add the ANSI_GREEN character code to make the following characters green
                finalString += "\u001B[36m";

                for (int f = 0; f < 3; f++) {
                    finalString += selectGrid.get(i).get(f) + " ";
                }

                // Add the reset code to return the color and format of the string back to normal
                finalString += "\u001B[0m";

                // cut the last character of the string so that the right edge of the grid border isn't offset
                finalString = finalString.substring(0, finalString.length()-1);
                finalString += "|\n";

            }

            // post grid border drawing
            finalString += " ";
            for (int f = 0; f < 5; f++) finalString += "-";

            finalString += "\n";
        }

        // letter alignment
        finalString += "  ";
        if (gridLength > 9) {
            finalString += " ";
        }

        // draw letter associated with column
        for (int i = 0; i < gridWidth; i++) {
            finalString += ALPHABET.charAt(i) + " ";
        }
        finalString += "\n";

        for (int i = 0; i < gridLength; i++) {
            // add an extra space to align numbers with single digit counts to rows with higher digit counts
            if (gridLength > 9 && i < 9) {
                finalString += " ";
            }

            // border between number and rest of grid
            finalString += i + 1 + "|";

            for (int f = 0; f < gridWidth; f++) {
                if (!playGrid.get(i).get(f).equals(previousGrid.get(i).get(f))) {
                    // ANSI_GREEN code
                    finalString += "\u001B[36m";
                } else {
                    // ANSI_RESET code
                    finalString += "\u001B[0m";
                }
                finalString += playGrid.get(i).get(f) + " ";

                finalString += "\u001B[0m";

            }
            finalString += "\n";
        }

        if (!isGameWon()) {
            finalString += "\nTurns left: " + turnsLeft + "\n";
        }

        return finalString;
    }
}
