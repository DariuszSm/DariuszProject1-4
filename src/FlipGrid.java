import java.util.ArrayList;

public class FlipGrid {

    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private ArrayList<ArrayList<String> > playGrid = new ArrayList<ArrayList<String>> ();
    private ArrayList<ArrayList<String> > selectGrid = new ArrayList<ArrayList<String>> ();

    private int gridLength;
    private int gridWidth;

    private boolean randomMode;

    private int turnsLeft;

    FlipGrid(int length, int width, boolean random) {

        int randomSel;

        if (length > 26) {
            length = 26;
        } else if (length < 2) {
            length = 2;
        }
        if (width > 26) {
            width = 26;
        } else if (width < 2) {
            width = 2;
        }

        gridLength = length;
        gridWidth = width;
        randomMode = random;
        turnsLeft = (int)Math.sqrt(Math.pow(length * width, 1.3));
        if ((turnsLeft % 2) == 1) {
            turnsLeft++;
        }

        if (random) {

            // generate one random tile to be selected when random mode is selected.
            randomSel = (int)(Math.random() * 8);
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

            // set the middle of the selection grid to an X
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

        for (int i = 0; i < (int)(turnsLeft * .75); i++) {
            makeMove(randomMove());
        }

    }

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

        /*
         * Test for if the input is in the last or first character of the array,
         * and extract the correct character from the input accordingly
         */
        try {
            Integer.parseInt(String.valueOf(input.charAt(0)));
            firstChar = String.valueOf(input.charAt(input.length()-1));
        } catch (Exception NumberFormatException) {
            firstChar = String.valueOf(input.charAt(0));
        }
        parsedWidth = firstChar.compareTo("a");

        if (parsedWidth < 0) {
            parsedWidth = 0;
        } else if (parsedWidth > gridWidth - 1) {
            parsedWidth = gridWidth - 1;
        } else if (parsedWidth > 25) {
            parsedWidth = 25;
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

        try {
            parsedLength = Integer.parseInt(input.substring(1)) - 1;
        } catch (Exception NumberFormatException) {
            parsedLength = Integer.parseInt(input.substring(0, input.length()-1)) - 1;
        }

        if (parsedLength < 0) {
            parsedLength = 0;
        } else if (parsedLength > gridLength - 1) {
            parsedLength = gridLength - 1;
        } else if (parsedLength > 25) {
            parsedLength = 25;
        }

        return parsedLength;
    }

    public void makeMove(String playerSelection) {
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

    public void makePlayerMove(String playerSelection) {
        makeMove(playerSelection);
        turnsLeft--;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

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

    public String toString() {
        String finalString = "";

        if (randomMode) {

            // pre grid drawing

            finalString += "Random selection area:\n ";

            for (int i = 0; i < 5; i++) finalString += "-";

            finalString += "\n";

            // grid drawing
            for (int i = 0; i < 3; i++) {
                finalString += "|";
                for (int f = 0; f < 3; f++) {
                    finalString += selectGrid.get(i).get(f) + " ";
                }

                // cut the last character of the string so that the right edge of the grid border isn't offset
                finalString = finalString.substring(0, finalString.length()-1);
                finalString += "|\n";

            }

            // post grid drawing
            finalString += " ";
            for (int f = 0; f < 5; f++) finalString += "-";

            finalString += "\n";
        }

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

            if (gridLength > 9 && i < 9) {
                finalString += " ";
            }

            finalString += i + 1 + "|";

            for (int f = 0; f < gridWidth; f++) {
                finalString += playGrid.get(i).get(f) + " ";
            }
            finalString += "\n";
        }

        finalString += "\nTurns left: " + turnsLeft + "\n";

        return finalString;
    }
}
