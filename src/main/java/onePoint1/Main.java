package onePoint1;

public class Main {
	public static void main (String [] args){
        GridWorld world = new GridWorld(true, 50, 0.7);
        world.establishValueIterationUtilities(); // use one of these
        //world.establishPolicyIterationUtilites(); // use one of these
        DrawingBoard d = new DrawingBoard(world);
        d.drawCurrentBoardState();
	}
}