public class GridSquare {

    private boolean terminal;
    private int xPos;
    private int yPos;
    private double reward;
    private boolean wall;

    public double utility;


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

    public GridSquare(double reward, boolean terminal, boolean wall,  int x, int y) {
        this.terminal = terminal;
        this.reward = reward;
        this.xPos = x;
        this.yPos = y;
        this.wall = wall;
        this.utility = 0;
    }
}
