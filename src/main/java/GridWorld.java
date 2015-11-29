import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GridWorld {
    public static int NUM_ITERATIONS = 50;
    public static double DISCOUNT_FACTOR = .99;

    public GridSquare[][] getGridWorld() {
        return grid;
    }


    public GridSquare getStartingSquare() {
        return start;
    }

    private GridSquare start;
    private GridSquare grid[][];
    private boolean rewardsTerminal;

    public GridSquare get(int x, int y) {
        return grid[y][x];
    }

    // Constructs the gridworld as defined in the mp
    public GridWorld(boolean rewardsTerminal) {
        this.rewardsTerminal = rewardsTerminal;
        grid = new GridSquare[6][6];
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                GridSquare square = new GridSquare(-0.04, false, false, x, y);;
                if (x == 1 && y == 0) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y);
                } else if (x == 3 && ((y >= 1 && y <= 3) || y == 5)) { // y 1-3 and 5
                    square = new GridSquare(0, rewardsTerminal, true, x, y);
                } else if (x == 0 && y == 5) {
                    square = new GridSquare(1, rewardsTerminal, false, x, y);
                } else if (x == 1 && y == 5) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y);
                } else if (x == 4 && y == 1) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y);
                } else if ((x == 4 || x == 5) && y == 5) {
                    square = new GridSquare(0, rewardsTerminal, true, x, y);
                } else if (x == 5 && y == 2) {
                    square = new GridSquare(3, rewardsTerminal, false, x, y);
                }

                grid[y][x] = square;
            }
        }

        start = grid[3][1];

        establishUtilities();
    }

    private boolean isValidLocation(int x, int y) {
        return  x > 0 && y > 0 && x < 6 && y < 6 && !get(x, y).isWall();
    }

    // A list of adjacent grids and their respective direction
    private List<GridSquare> getValidAdjacentSquares(int x, int y) {
        ArrayList<GridSquare> validActions = new ArrayList<GridSquare>();
        if (get(x, y).isTerminal()) {
            return validActions;
        }

        if (isValidLocation(x+1, y)) {
            validActions.add(get(x+1, y));
        }

        if (isValidLocation(x-1, y)) {
            validActions.add(get(x-1,y));
        }


        if (isValidLocation(x, y+1)) {
            validActions.add(get(x,y+1));
        }

        if (isValidLocation(x, y-1)) {
            validActions.add(get(x,y-1));
        }

        return validActions;
    }

    // Action here is defined as one gridspace to another
    public double getUtilityForAction(GridSquare original, GridSquare moveTo) {
        double utility = 0.8 * moveTo.utility;
        int x = original.getxPos();
        int y = original.getyPos();

        // Must include left/right
        if (moveTo.getxPos() == get(x,y).getxPos()) {
            if (isValidLocation(x - 1 , y)) {
                utility+= 0.1*get(x - 1, y).utility;
            }

            if (isValidLocation(x + 1 , y)) {
                utility+= 0.1*get(x + 1, y).utility;
            }
        }

        // Must include up/down
        if (moveTo.getyPos() == get(x,y).getyPos()) {
            if (isValidLocation(x , y - 1)) {
                utility+= 0.1*get(x, y - 1).utility;
            }

            if (isValidLocation(x , y + 1)) {
                utility+= 0.1*get(x, y + 1).utility;
            }
        }

        return utility;
    }

    private void establishUtilities() {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            for (int y = 0; y < 6; y++) {
                for (int x = 0; x < 6; x++) {
                    double maxUtility;
                    List<GridSquare> validNextSquares = getValidAdjacentSquares(x, y);
                    if (validNextSquares.size() == 0) {
                        maxUtility = 0;
                    } else {
                        maxUtility = Double.NEGATIVE_INFINITY;
                    }

                    for (GridSquare square : validNextSquares) {
                        double utility = getUtilityForAction(get(x,y), square);
                        maxUtility = utility > maxUtility ? utility : maxUtility;
                    }

                    get(x, y).utility = get(x, y).getReward() + DISCOUNT_FACTOR*maxUtility;
                }
            }
        }
    }

    public static void main(String[] args) {
        GridWorld world = new GridWorld(true);
        DrawingBoard d = new DrawingBoard(world);
        d.drawCurrentBoardState();
    }
}
