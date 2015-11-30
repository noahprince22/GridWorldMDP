public class GridSquare {

    private double reward;
    private boolean terminal;
    private boolean wall;
    private int xPos;
    private int yPos;

    public double utility;

    public GridSquare(double reward, boolean terminal, boolean wall, int x, int y) {
        this.terminal = terminal;
        this.reward = reward;
        this.xPos = x;
        this.yPos = y;
        this.wall = wall;
        
        this.utility = 0;
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
}
