import java.util.ArrayList;
import java.util.List;

public class GridWorld {
    // Globals
    public static int NUM_ITERATIONS = 50;
    public static double DISCOUNT_FACTOR = .7; // Rodney: Once matching MP4's results, can change this to 0.99

    public int rows = 6;
    public int columns = 6;
    
    private GridSquare start;
    private GridSquare grid[][];

    // Nodes are considered actions. A node we will move to is indicative of the action we are trying to take
    private GridSquare policy[][];

    public GridSquare[][] getGridWorld() {
        return grid;
    }

    public GridSquare getStartingSquare() {
        return start;
    }

    public GridSquare getGridSquare(int x, int y) {
    	if (!isValidLocation(x, y))
    		return null;
        return grid[y][x];
    }

    // Constructs the gridworld as defined in the mp
    public GridWorld(boolean rewardsTerminal, boolean valueIteration) {
        grid = new GridSquare[rows][columns];
        policy = new GridSquare[rows][columns];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {

                // Setting up the walls, rewards, and negative rewards.
                if (x == 1 && y == 0) {
                	grid[y][x] = new GridSquare(-1, rewardsTerminal, false, x, y); // rewardSquare
                } else if (x == 3 && ((y >= 1 && y <= 3) || y == 5)) {
                	grid[y][x] = new GridSquare(0, false, true, x, y); // walls
                } else if (x == 0 && y == 5) {
                	grid[y][x] = new GridSquare(1, rewardsTerminal, false, x, y); // rewardSquare
                } else if (x == 1 && y == 5) {
                	grid[y][x] = new GridSquare(-1, rewardsTerminal, false, x, y); // rewardSquare
                } else if (x == 4 && y == 1) {
                	grid[y][x] = new GridSquare(-1, rewardsTerminal, false, x, y); // rewardSquare
                } else if ((x == 4 || x == 5) && y == 5) {
                	grid[y][x] = new GridSquare(-1, rewardsTerminal, false, x, y); // rewardSquares
                } else if (x == 5 && y == 2) {
                	grid[y][x] = new GridSquare(3, rewardsTerminal, false, x, y); // rewardSquare
                } else {
                	grid[y][x] = new GridSquare(-0.04, false, false, x, y); // default to -.04 square
                }
            }
        }

        start = grid[3][1];

        if (valueIteration) {
            establishValueIterationUtilities();
        } else {
            ;//establishPolicyIterationUtilites();
        }
    }

    /* Rodney: TRYING to move into walls is actually a valid move. I updated this to reflect that */
    private boolean isValidLocation(int x, int y) {
        return x >= 0 && y >= 0 && x < columns && y < rows;
    }
    
    private boolean isValidLocation(GridSquare square){
    	return (square != null) && (isValidLocation(square.getxPos(), square.getyPos()));
    }

    // Returns a list of (up to 4) adjacent GridSquares (including walls) that are on Grid.
    private List<GridSquare> getValidAdjacentSquares(GridSquare square) {
        int x = square.getxPos();
        int y = square.getyPos();

        ArrayList<GridSquare> validActions = new ArrayList<GridSquare>();
        if (square.isTerminal()) {
            return validActions;
        }

        GridSquare leftSquare  = getGridSquare(x - 1, y);
        GridSquare rightSquare = getGridSquare(x + 1, y);
        GridSquare downSquare  = getGridSquare(x, y + 1);
        GridSquare upSquare    = getGridSquare(x, y - 1);

        if (isValidLocation(leftSquare)) {
        	validActions.add(leftSquare);
        }
        else
        	validActions.add(new GridSquare(0, true, true, x - 1, y)); // dummy variable
        
        if (isValidLocation(rightSquare)) {
        	validActions.add(rightSquare);
        }
        else
        	validActions.add(new GridSquare(0, true, true, x + 1, y)); // dummy variable
        
        if (isValidLocation(downSquare)) {
        	validActions.add(downSquare);
        }
        else
        	validActions.add(new GridSquare(0, true, true, x, y + 1)); // dummy variable
        
        if (isValidLocation(upSquare)) {
        	validActions.add(upSquare);
        }
        else
        	validActions.add(new GridSquare(0, true, true, x, y - 1)); // dummy variable

        return validActions;
    }

    /* Rodney: I updated this function so that it deals with attempts to move into walls properly.
     *  "If the move would make the agent walk into a wall, the agent stays in the same place as before." */
    // Action here is defined as one gridspace to another
    public double getUtilityForAction(GridSquare original, GridSquare moveTo) {
        if (original == null || moveTo == null) {
            return 0;
        }

        double utility;
        
        int x = original.getxPos();
        int y = original.getyPos();
        
        GridSquare leftSquare  = getGridSquare(x - 1, y);
        GridSquare rightSquare = getGridSquare(x + 1, y);
        GridSquare downSquare  = getGridSquare(x, y + 1);
        GridSquare upSquare    = getGridSquare(x, y - 1);
        
        if (isValidLocation(moveTo) && ! moveTo.isWall())
        	utility = 0.8 * moveTo.utility;
        else
        	utility = 0.8 * original.utility; // agent stays in same place as before.

        if (moveTo.getxPos() == original.getxPos()) { // intended movement is vertical
            if (isValidLocation(leftSquare) && ! leftSquare.isWall()) {
                utility += 0.1 * leftSquare.utility;
            }
            else {
            	utility += 0.1 * original.utility; // agent stays in same place as before.
            }

            if (isValidLocation(rightSquare) && ! rightSquare.isWall()) {
                utility += 0.1 * rightSquare.utility;
            }
            else {
            	utility += 0.1 * original.utility; // agent stays in same place as before.
            }
        }
        else { // intended movement is horizontal
            if (isValidLocation(upSquare) && ! upSquare.isWall()) {
                utility += 0.1 * upSquare.utility;
            }
            else {
            	utility += 0.1 * original.utility; // agent stays in same place as before.
            }
            
            if (isValidLocation(downSquare) && ! downSquare.isWall()) {
                utility += 0.1 * downSquare.utility;
            }
            else {
            	utility += 0.1 * original.utility; // agent stays in same place as before.
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
        	return getUtilityForAction(s, maxAction);
        } else {
            return 0;
        }
    }

    /* Other version of "Value Iteration". We must not write the utilities to cells until all 36 are calculated */
    private void establishValueIterationUtilities() {
        for (int i = 1; i <= NUM_ITERATIONS; i++) {
        	double utilities[][] = new double[rows][columns];
        	/* 1st calculate all the utilities before writing to the cells */
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    GridSquare currentSquare = getGridSquare(x, y);
                    if ( ! currentSquare.isWall())
                    	utilities[y][x] = currentSquare.getReward() + DISCOUNT_FACTOR * maxUtility(currentSquare); 
                }
            }
            /* Now we can copy the utilities */
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                	grid[y][x].utility = utilities[y][x];
                }
            }
        }
    }
    
//    private void policyEvaluation() {
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < columns; x++) {
//                GridSquare currentSquare = getGridSquare(x, y);
//                currentSquare.utility = currentSquare.getReward() +
//                        DISCOUNT_FACTOR * getUtilityForAction(currentSquare, policy[y][x]);
//            }
//        }
//    }
//
//    private void policyImprovement() {
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < columns; x++) {
//                policy[y][x] = maxUtilityAction(getGridSquare(x, y));
//            }
//        }
//    }
//
//    private void establishPolicyIterationUtilites() {
//        // Setup initial policy (always go to first in adjacent list)
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < columns; x++) {
//                GridSquare currentSquare = getGridSquare(x, y);
//                List<GridSquare> validAdjacentSquares = getValidAdjacentSquares(currentSquare);
//
//                if (validAdjacentSquares.size() == 0 ) {
//                    policy[y][x] = null;
//                } else {
//                    policy[y][x] = getValidAdjacentSquares(currentSquare).get(0);
//                }
//            }
//        }
//
//        for (int i = 0; i < NUM_ITERATIONS; i++) {
//            policyEvaluation();
//            policyImprovement();
//        }
//    }

    public static void main(String[] args) {
        GridWorld world = new GridWorld(true, true);
        DrawingBoard d = new DrawingBoard(world);
        d.drawCurrentBoardState();
    }
}
