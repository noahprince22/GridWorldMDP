import java.util.ArrayList;
import java.util.List;

public class GridWorld {
    public GridSquare[][] getGridWorld() {
        return grid;
    }


    public GridSquare getStartingSquare() {
        return start;
    }

    private GridSquare start;
    private GridSquare grid[][];

    public GridSquare get(int x, int y) {
        return grid[y][x];
    }

    // Constructs the gridworld as defined in the mp
    public GridWorld(boolean rewardsTerminal) {
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
                    square = new GridSquare(3, rewardsTerminal, true, x, y);
                }

                grid[y][x] = square;
            }
        }

        start = grid[3][1];
    }

    public static void main(String[] args) {
        GridWorld world = new GridWorld(false);
        DrawingBoard d = new DrawingBoard(world);
    }
}
