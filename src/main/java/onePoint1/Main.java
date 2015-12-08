package onePoint1;

public class Main {
	
	/* Parameters to tune: 
	 * Number of Iterations (1st parameter to constructor): 50 is enough for TERMINAL states. Use ~5000 for non-terminal states
	 * Discount factor      (2nd parameter to constructor): use 0.7 to compare with instructor results. Use 0.99 for report.
	 */
	public static void main (String [] args){
		GridWorld world = new GridWorld(false, 50, 0.99); // Use one of these (Terminal States)
        //GridWorld world = new GridWorld(false, 5000, 0.7); // Use one of these (Non-Terminal States)
        
        //world.establishValueIterationUtilities(); // Use one of these (Value Iteration)
        world.establishPolicyIterationUtilites(); // Use one of these (Policy Iteration)

            world.setVisible(true);
        
        DrawingBoard d = new DrawingBoard(world);
        
        d.draw(false);   // Use one of these (draws Utilities)
        //d.draw(true);    // Use one of these (draws Policy)
	}
}