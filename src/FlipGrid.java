import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class FlipGrid {
    private ArrayList<ArrayList<String> > playGrid = new ArrayList<ArrayList<String>> ();
    private ArrayList<ArrayList<String> > selectGrid = new ArrayList<ArrayList<String>> ();

    int gridLength;
    int gridWidth;

    boolean randomMode;

    int turnsLeft;

    FlipGrid(int length, int width, boolean random) {

        int randomSel;

        gridLength = length;
        gridWidth = width;
        randomMode = random;
        turnsLeft = (int)Math.sqrt(length * width);

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
                        selectGrid.get(i).add("X");
                    }
                }
            }

            // set the middle of the selection grid to an X
            selectGrid.get(1).set(1, "X");

            selectGrid.get(randomSel/3).set(randomSel % 3, "X");

        }

        // generate starting layout for the grid
        for (int i = 0; i < length; i++) {
            playGrid.add(new ArrayList<String>());;
            for (int f = 0; f < width; f++) {
                playGrid.get(i).add("O");
            }
        }
    }

    /**
     * Parses the player input to get the X value of their selection.
     * Player format should look like "A5", "H12", "z20", etc
     * Max allowed width is 25, or the width of the grid, whichever is lower
     *
     * @param input Raw player input
     * @return Parsed width of input
     */
    private int getXInput(String input) {
        String firstChar;

        int parsedWidth;

        input = input.toLowerCase();
        firstChar = String.valueOf(input.charAt(0));
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

        parsedLength = Integer.parseInt(input.substring(1));

        if (parsedLength < 0) {
            parsedLength = 0;
        } else if (parsedLength > gridLength - 1) {
            parsedLength = gridLength - 1;
        } else if (parsedLength > 25) {
            parsedLength = 25;
        }

        return parsedLength;
    }

    public String toString() {
        String finalString = "";

        for (int i = 0; i < gridLength; i++) {
            for (int f = 0; f < gridWidth; f++) {
                finalString += playGrid.get(i).get(f);
            }
            finalString += "\n";
        }

        if (randomMode) {
            for (int i = 0; i < 3; i++) {
                for (int f = 0; f < 3; f++) {
                    finalString += selectGrid.get(i).get(f);
                }
                finalString += "\n";
            }
        }

        return finalString;
    }
}
