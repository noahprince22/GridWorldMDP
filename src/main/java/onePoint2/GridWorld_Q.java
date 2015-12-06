package onePoint2;

import onePoint1.GridSquare;
import onePoint1.GridWorld;

public class GridWorld_Q extends GridWorld{
	
	int threshold;
	int t;
	double alpha; // learning rate
	int numTrials;
	
	public GridWorld_Q(boolean rewardsTerminal, int iterations, double discountFactor, int threshold) {
		super(rewardsTerminal, iterations, discountFactor);
		this.threshold = threshold;
		t = 0;
		updateAlpha();
		numTrials = 0;

		/* Piazza @476: Q values for terminal states should be reward values */
		// Are these necessary?
		grid[0][1].utility = -1;
		grid[1][4].utility = -1;
		grid[5][1].utility = -1;
		grid[5][4].utility = -1;
		grid[5][5].utility = -1;
		grid[2][5].utility = 3;
		grid[5][0].utility = 1;
	}
	
	public boolean notConverged(){
		System.out.println(getGridSquare(0,0).utility);
		return Math.abs(getGridSquare(0,0).utility) - 0.0893 < 0.1; // can make better criteria for convergence
	}
	
	/* Lecture 17 Slide 20 has pseudocode. Piazza @528 has more detailed pseudocode */
    public void establish_Q_Utilities() {
    	//while(notConverged()){ // can try this instead of the for loop line below
    	for (int i = 0; i < 50000; i++){
	    	GridSquare currentState = start;
	    	GridSquare intendedSuccessorState = null;
	    	GridSquare successorState = null;
	    	while (! currentState.isTerminal()){
		        intendedSuccessorState = selectAction(currentState);
		        Direction dir = getDirection(currentState, intendedSuccessorState);
		        successorState = getSuccessorState(currentState, intendedSuccessorState);
		       	TD_Update(currentState, successorState, dir);
		       	updateOtherVariables(currentState, dir);
		    	currentState = successorState;
	    	}
	    	numTrials++;
    	}
    }
    
    public GridSquare selectAction(GridSquare currentState){
        int x = currentState.getxPos();
        int y = currentState.getyPos();
        
        /* Get adjacent GridSquares */
		GridSquare leftSquare  = getGridSquare(x - 1, y);
		GridSquare rightSquare = getGridSquare(x + 1, y);
		GridSquare downSquare  = getGridSquare(x, y + 1);
		GridSquare upSquare  = getGridSquare(x, y - 1);
		
		if ( ! isValidLocation(leftSquare))
			leftSquare = new GridSquare(0, true, true, x - 1, y); // off of Grid
		if ( ! isValidLocation(rightSquare))
			rightSquare = new GridSquare(0, true, true, x + 1, y); // off of Grid
		if ( ! isValidLocation(downSquare))
			downSquare = new GridSquare(0, true, true, x, y + 1); // off of Grid
		if ( ! isValidLocation(upSquare))
			upSquare = new GridSquare(0, true, true, x, y - 1); // off of Grid
		
		/* Explore: will always try each direction at least "threshold" number of times. No need for R+ */
		if (N(currentState, Direction.LEFT) < threshold)
			return leftSquare;
		else if (N(currentState, Direction.RIGHT) < threshold)
			return rightSquare;
		else if (N(currentState, Direction.DOWN) < threshold)
			return downSquare;
		else if (N(currentState, Direction.UP) < threshold)
			return upSquare;
		
		/* Exploit */
		Direction dir = currentState.highestUtilityDirection();
    	if (dir == Direction.LEFT)
    		return leftSquare;
    	else if (dir == Direction.RIGHT)
    		return rightSquare;
    	else if (dir == Direction.DOWN)
    		return downSquare;
    	else if (dir == Direction.UP)
    		return upSquare;
    	return null; //should never execute
    }
    
    /* 80% chance of going in intended direction */
    public GridSquare getSuccessorState(GridSquare currentState, GridSquare successorState) {    	
        int x = currentState.getxPos();
        int y = currentState.getyPos();
        
        GridSquare ninetyDegreesLeft  = null;
        GridSquare ninetyDegreesRight = null;
    	Direction dir = getDirection(currentState, successorState);
    	
    	if (dir == Direction.LEFT){
            ninetyDegreesLeft  = getGridSquare(x, y + 1);
            ninetyDegreesRight = getGridSquare(x, y - 1); 
    	}
    	else if (dir == Direction.RIGHT){
            ninetyDegreesLeft  = getGridSquare(x, y - 1);
            ninetyDegreesRight = getGridSquare(x, y + 1);
    	}
    	else if (dir == Direction.DOWN){
            ninetyDegreesLeft  = getGridSquare(x + 1, y);
            ninetyDegreesRight = getGridSquare(x - 1, y);
    	}
    	else if (dir == Direction.UP){
            ninetyDegreesLeft  = getGridSquare(x - 1, y);
            ninetyDegreesRight = getGridSquare(x + 1, y);
    	}
        
        double num = Math.random() * 10;
        if (num < 8){
        	if (isValidLocation(successorState) && ! successorState.isWall())
        		return successorState;
        	else
        		return currentState;
        }
        else if (num < 9){
        	if (isValidLocation(ninetyDegreesLeft) && ! ninetyDegreesLeft.isWall())
        		return ninetyDegreesLeft;
        	else
        		return currentState;
        }
        else{
        	if (isValidLocation(ninetyDegreesRight) && ! ninetyDegreesRight.isWall())
        		return ninetyDegreesRight;
        	else
        		return currentState;
        }
    }
    
    /* Formula from bottom of Lecture 17, Slide 20 */
    private void TD_Update(GridSquare currentState, GridSquare successorState, Direction dir){ //also updates N(s, a')
    	if (dir == Direction.LEFT)
    		currentState.utilityLeft += alpha * (currentState.getReward() + (discountFactor * successorState.utility) - currentState.utilityLeft);
    	else if (dir == Direction.RIGHT)
    		currentState.utilityRight += alpha * (currentState.getReward() + (discountFactor * successorState.utility) - currentState.utilityRight);
    	else if (dir == Direction.DOWN)
    		currentState.utilityDown += alpha * (currentState.getReward() + (discountFactor * successorState.utility) - currentState.utilityDown);
    	else if (dir == Direction.UP)
    		currentState.utilityUp += alpha * (currentState.getReward() + (discountFactor * successorState.utility) - currentState.utilityUp);
    }
    
    private void updateOtherVariables(GridSquare currentState, Direction dir){
    	/* Update N(s, a') */
    	if (dir == Direction.LEFT)
    		currentState.triedLeft++;
    	else if (dir == Direction.RIGHT)
    		currentState.triedRight++;
    	else if (dir == Direction.DOWN)
    		currentState.triedDown++;
    	else if (dir == Direction.UP)
    		currentState.triedUp++;
    	
       	currentState.updateUtility();
    	updateAlpha();
    }
    
    public void updateAlpha(){
    	t++;
		alpha = (double) 60 / (59 + t);
    	//alpha = (double) 600 / (599 + t);
    	//alpha = (double) 6000 / (5999 + t);
    }
    
    /* Returns number of times we've gone a given direction from a certain state (Function defined in Lecture 17 Slide 15) */
    public int N(GridSquare currentState, Direction dir){
    	if (dir == Direction.LEFT)
    		return currentState.triedLeft;
    	else if (dir == Direction.RIGHT)
    		return currentState.triedRight;
    	else if (dir == Direction.DOWN)
    		return currentState.triedDown;
    	else if (dir == Direction.UP)
    		return currentState.triedUp;
    	return -1; // should never execute
 	}
  
	public Direction getDirection(GridSquare origin, GridSquare destination){
	  	if (origin.getxPos() - 1 == destination.getxPos())
	  		return Direction.LEFT;
	  	else if (origin.getxPos() + 1 == destination.getxPos())
	  		return Direction.RIGHT;
	  	else if (origin.getyPos() + 1 == destination.getyPos())
	  		return Direction.DOWN;
	  	else if (origin.getyPos() - 1 == destination.getyPos())
	  		return Direction.UP;
	  	return null; // should never execute
	}
}


