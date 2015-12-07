package onePoint2;

import onePoint1.DrawingBoard;
import onePoint1.GridWorld;

public class Main2 {
	public static void main (String [] args){
		GridWorld world = new GridWorld(true, 50, 0.7);
        world.establishValueIterationUtilities(); 
        
        GridWorld_Q world_Q = null;
        for (int numIterations = 10; numIterations <= 10000000; numIterations *= 10){
        	world_Q = new GridWorld_Q(true, numIterations, 0.7, 500, world.grid); // numIterations doesn't matter here
			world_Q.establish_Q_Utilities();
			System.out.println(world_Q.RMSE());
        }
        
        DrawingBoard d = new DrawingBoard(world_Q);
        d.drawCurrentBoardState();
        
        
	}
}