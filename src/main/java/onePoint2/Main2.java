package onePoint2;

import onePoint1.DrawingBoard;

public class Main2 {
	public static void main (String [] args){
		GridWorld_Q world = new GridWorld_Q(true, 0, 0.7, 50); // numIterations doesn't matter here
        world.establish_Q_Utilities();
        DrawingBoard d = new DrawingBoard(world);
        d.drawCurrentBoardState();
	}
}