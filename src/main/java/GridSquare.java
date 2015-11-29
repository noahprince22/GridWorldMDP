public class GridSquare {
    public boolean isTerminal() {
        return terminal;
    }

    private boolean terminal;

    public double getReward() {
        return reward;
    }

    public int getxPos() {
        return xPos;
    }

    private int xPos;

    public int getyPos() {
        return yPos;
    }

    private int yPos;

    private double reward;

    public boolean isWall() {
        return wall;
    }

    private boolean wall;

    public GridSquare(double reward, boolean terminal, boolean wall,  int x, int y) {
        this.terminal = terminal;
        this.reward = reward;
        this.xPos = x;
        this.yPos = y;

        this.wall = wall;
    }
}
