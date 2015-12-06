package onePoint1;

//import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

public class DrawingBoard {
    private int numRows;
    private int numCols;

    private GridWorld gridWorld;

    private Font valueFont;

    /** Constructor */
    public DrawingBoard(GridWorld grid){
        gridWorld = grid;
        this.numRows = grid.rows;
        this.numCols = grid.columns;
        createCanvas();

        int style = Font.BOLD | Font.ITALIC;
        this.valueFont = new Font ("Garamond", style , 36);

        this.drawInitialState();
    }

    private void drawInitialState(){
        drawBlankGrid();
        drawGridAllGridValues();
        StdDraw.show(5);
    }

    private void createCanvas(){
        int canvasScale = 200;
        int maxCanvasSize = 900;
        while(this.numCols*canvasScale > maxCanvasSize || this.numRows*canvasScale > maxCanvasSize){
            canvasScale = canvasScale - 5;
            if(canvasScale <= 0){
                canvasScale = 1;
                break;
            }
        }
        StdDraw.setCanvasSize(this.numCols * canvasScale, this.numRows * canvasScale);
        StdDraw.setXscale(0, this.numCols);
        StdDraw.setYscale(0, this.numRows);
    }

    private void drawGridSpace(int row, int col){
        StdDraw.setPenRadius();

        GridSquare square = gridWorld.getGridSquare(col, row);
        if (square.isWall()) {
            StdDraw.setPenColor(StdDraw.GRAY);
        } else if (square.getReward() == -0.04) {
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        } else if (square.getReward() < 0) {
            StdDraw.setPenColor(StdDraw.ORANGE);
        } else if (square.getReward() > 0) {
            StdDraw.setPenColor(StdDraw.GREEN);
        } else if (square == gridWorld.start){
            StdDraw.setPenColor(StdDraw.RED);
        }

        StdDraw.filledSquare(col+0.5, (this.numRows-1-row)+0.5, .45 );
    }

    private void drawBlankGrid(){
        for(int row = 0; row < this.numRows; row++){
            for(int col = 0; col < this.numCols; col++){
                this.drawGridSpace(row, col);
            }
        }
    }

    private void drawSingleGridSpaceValue(int row, int col) {
        StdDraw.setFont(this.valueFont);
        StdDraw.setPenColor(StdDraw.BLACK);
        double value = this.gridWorld.getGridSquare(col, row).utility;
        DecimalFormat df = new DecimalFormat("#.####");
        String val = df.format(value);

        GridSquare square = gridWorld.getGridSquare(col, row);
        if (square.isWall()) {
            StdDraw.text(col+0.5, (this.numRows-1-row)+0.5, "WALL");
        } else if (square == gridWorld.start){
            StdDraw.text(col+0.5, (this.numRows-1-row)+0.5, "START");
        } else {
            StdDraw.text(col+0.5, (this.numRows-1-row)+0.5, ""+ val);
        }
    }

    private void drawGridAllGridValues() {
        for(int row = 0; row < this.numRows; row++){
            for(int col = 0; col < this.numCols; col++){
                this.drawSingleGridSpaceValue(row, col);
            }
        }
    }

//    private void drawColoredCircle(int row, int col, Color playerColor){
//        StdDraw.setPenRadius();
//        StdDraw.setPenColor(playerColor);
//        StdDraw.filledCircle(col+0.5, (this.numRows-1-row)+0.5, .45 );
//    }


    /**
     * will draw the current state of the game based off the
     * drawing board's instance variable of game state. To draw an
     * updated picture of the game, set the game state instance
     * variable every time the game state changes (using setGameStateNode())
     * before calling drawCurrentBoardState().
     */
    public void drawCurrentBoardState(){
        StdDraw.clear();
        this.drawBlankGrid();
        this.drawGridAllGridValues();
        StdDraw.show(5);
    }

    public void saveImage(String filename) {
        StdDraw.save(filename);
    }
}
