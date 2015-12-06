package onePoint1;

import onePoint2.Direction;

public class GridSquare {

    private double reward;
    private boolean terminal;
    private boolean wall;
    private int xPos;
    private int yPos;
    public double utility;

    /* Added for part 1.2 */
	public double utilityLeft;
	public double utilityRight;
	public double utilityUp;
	public double utilityDown;
	
	public int triedLeft;
	public int triedRight;
	public int triedUp;
	public int triedDown;
	
    public GridSquare(double reward, boolean terminal, boolean wall, int x, int y) {
        this.terminal = terminal;
        this.reward = reward;
        this.xPos = x;
        this.yPos = y;
        this.wall = wall;
        this.utility = 0;
        
        /* For part 1.2 */
    	this.utilityLeft = 0;
    	this.utilityRight = 0;
    	this.utilityUp = 0;
    	this.utilityDown = 0;
    	
    	this.triedLeft = 0;
    	this.triedRight = 0;
    	this.triedUp = 0;
    	this.triedDown = 0;
    	
    }
    
    public boolean isTerminal() {
        return terminal;
    }

    public double getReward() {
        return reward;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean isWall() {
        return wall;
    }
    
    /**********************/
    /* Added for part 1.2 */	
    /**********************/
    
	public void updateUtility(){
		utility = Math.max(utilityLeft, Math.max(utilityRight, Math.max(utilityUp, utilityDown)));
	}
	
	public Direction highestUtilityDirection(){
		if (utilityLeft > Math.max(utilityRight, Math.max(utilityUp, utilityDown)))
			return Direction.LEFT;
		else if (utilityRight > Math.max(utilityUp, utilityDown))
			return Direction.RIGHT;
		else if (utilityUp > utilityDown)
			return Direction.UP;
		else
			return Direction.DOWN;
	}
}
