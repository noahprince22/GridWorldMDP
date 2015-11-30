import java.util.ArrayList;
import java.util.List;

public class GridWorld {
    // Globals
    public static int NUM_ITERATIONS = 50;
    public static double DISCOUNT_FACTOR = .99;

    private GridSquare start;
    private GridSquare grid[][];

    // Note that nodes are considered actions. A node we will move to is indicative of the action we are trying
    //  to take.
    private GridSquare policy[][]; // The node we will attempt to move from from node at x, y

    public GridSquare[][] getGridWorld() {
        return grid;
    }


    public GridSquare getStartingSquare() {
        return start;
    }


    public GridSquare get(int x, int y) {
        return grid[y][x];
    }

    // Constructs the gridworld as defined in the mp
    public GridWorld(boolean rewardsTerminal, boolean valueIteration) {
        grid = new GridSquare[6][6];
        policy = new GridSquare[6][6];

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {

                // Setting up the walls, rewards, and negative rewards.
                GridSquare square = new GridSquare(-0.04, false, false, x, y); // default to -.04 square
                if (x == 1 && y == 0) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y); // - rewardSquare
                } else if (x == 3 && ((y >= 1 && y <= 3) || y == 5)) { // y 1-3 and 5, all walls
                    square = new GridSquare(0, false, true, x, y); // wall
                } else if (x == 0 && y == 5) {
                    square = new GridSquare(1, rewardsTerminal, false, x, y); // rewardSquare
                } else if (x == 1 && y == 5) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y); // - rewardSquare
                } else if (x == 4 && y == 1) {
                    square = new GridSquare(-1, rewardsTerminal, false, x, y); // - rewardSquare
                } else if ((x == 4 || x == 5) && y == 5) {
                    square = new GridSquare(0, false, true, x, y); // wall
                } else if (x == 5 && y == 2) {
                    square = new GridSquare(3, rewardsTerminal, false, x, y); // rewardSquare
                }

                grid[y][x] = square;
            }
        }

        start = grid[3][1];

        if (valueIteration) {
            establishValueIterationUtilities();
        } else {
            establishPolicyIterationUtilites();
        }
    }

    private boolean isValidLocation(int x, int y) {
        return  x > 0 && y > 0 && x < 6 && y < 6 && !get(x, y).isWall();
    }

    // A list of adjacent grids and their respective direction
    private List<GridSquare> getValidAdjacentSquares(GridSquare square) {
        int x = square.getxPos();
        int y = square.getyPos();

        ArrayList<GridSquare> validActions = new ArrayList<GridSquare>();
        if (square.isTerminal()) {
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
        if (original == null || moveTo == null) {
            return 0;
        }

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

    // Calculates argmax a \in A(s) Sum_{s'} P(s' | s, a) U_{current} (s')
    private GridSquare maxUtilityAction(GridSquare s) {
        List<GridSquare> validNextSquares = getValidAdjacentSquares(s);
        if (validNextSquares.size() != 0) {
            GridSquare maxNode = validNextSquares.get(0);
            double maxUtility = Double.NEGATIVE_INFINITY;

            // For all valid directions to move, get the node with the expected max utility
            for (GridSquare s_prime : validNextSquares) {
                double utility = getUtilityForAction(s, s_prime);

                if (utility > maxUtility) {
                    maxNode = s_prime;
                    maxUtility = utility;
                }
            }

            return maxNode;
        } else {
            return null;
        }
    }

    private double maxUtility(GridSquare s) {
        GridSquare maxAction = maxUtilityAction(s);

        if (maxAction != null) {
            return maxAction.utility;
        } else {
            return 0;
        }
    }

    private void establishValueIterationUtilities() {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            for (int y = 0; y < 6; y++) {
                for (int x = 0; x < 6; x++) {
                    GridSquare currentSquare = get(x, y);
                    currentSquare.utility = currentSquare.getReward() + DISCOUNT_FACTOR * maxUtility(currentSquare);
                }
            }
        }
    }

    private void policyEvaluation() {
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                GridSquare currentSquare = get(x, y);
                currentSquare.utility = currentSquare.getReward() +
                        DISCOUNT_FACTOR * getUtilityForAction(currentSquare, policy[y][x]);
            }
        }
    }

    private void policyImprovement() {
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                policy[y][x] = maxUtilityAction(get(x, y));
            }
        }
    }

    private void establishPolicyIterationUtilites() {
        // Setup initial policy (always go to first in adjacent list)
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                GridSquare currentSquare = get(x, y);
                List<GridSquare> validAdjacentSquares = getValidAdjacentSquares(currentSquare);

                if (validAdjacentSquares.size() == 0 ) {
                    policy[y][x] = null;
                } else {
                    policy[y][x] = getValidAdjacentSquares(currentSquare).get(0);
                }
            }
        }

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            policyEvaluation();
            policyImprovement();
        }
    }

        public static void main(String[] args) {
        // Terminal rewards with value iteration
        GridWorld world = new GridWorld(false, false);
        DrawingBoard d = new DrawingBoard(world);
        d.drawCurrentBoardState();
    }
}
